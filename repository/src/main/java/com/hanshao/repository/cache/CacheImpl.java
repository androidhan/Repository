package com.hanshao.repository.cache;

import android.content.Context;
import android.util.Log;


import com.hanshao.repository.cache.pool.ThreadPool;
import com.hanshao.repository.cache.serializer.Serializer;
import com.hanshao.repository.utils.SecurityUtil;

import java.io.File;
import java.util.concurrent.ThreadPoolExecutor;

import rx.Observable;
import rx.Subscriber;

/**
 * AUTHOR: hanshao
 * DATE: 17/6/8.
 * ACTION: 缓存实现类
 */
public class CacheImpl implements Cache {

    private static final String SETTINGS_FILE_NAME = "cache";
    private static final String DEFAULT_FILE_NAME = "cache_";

    //在线与离线时间
    private final long OFFLINE_EXPIRATION_TIME;
    private final long ONLINE_EXPIRATION_TIME;

    private Context mContext;
    private File mCacheDir;
    private Serializer mSerializer;
    private FileManager mFileManager;
    private ThreadPoolExecutor mThreadExecutor;



    public CacheImpl(Context context,  long onlineExpirationTime , long offlineExpirationTime) {
        if (context == null || onlineExpirationTime < 0 || offlineExpirationTime < 0) {
            throw new IllegalArgumentException("Invalid null parameter and expiration time is greater than 0");
        }
        this.mContext = context.getApplicationContext();
        this.mCacheDir = this.mContext.getCacheDir();
        this.mSerializer = Serializer.instance();
        this.mFileManager = FileManager.instance();
        this.mThreadExecutor = ThreadPool.pool();
        this.OFFLINE_EXPIRATION_TIME = offlineExpirationTime;
        this.ONLINE_EXPIRATION_TIME = onlineExpirationTime;
    }


    public CacheImpl(Context context) {
        this(context,0,0);
    }


    /**
     * 读取
     *
     * @param url    网址
     * @param <T>    泛型
     * @param params 参数标识
     * @return
     */
    @Override
    public <T> Observable<T> get(final String url, final String params, final Class<?> clazz) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {

                String newUrl = SecurityUtil.getMD5(url + params);
                File entityFile = CacheImpl.this.buildFile(newUrl);
                String fileContent = CacheImpl.this.mFileManager.readFileContent(entityFile);
                T data = null;
                if ( fileContent.charAt(0) == '[') {
                    data =
                            (T) CacheImpl.this.mSerializer.deserializeToList(fileContent, clazz);
                }else{
                    data =
                            (T) CacheImpl.this.mSerializer.deserialize(fileContent, clazz);
                }

                if (data != null) {
                    subscriber.onNext(data);
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(null);
                }
            }
        });
    }


    /**
     * 写入
     *
     * @param object  写入的对象
     * @param url     路径
     * @param params  参数标识
     */
    @Override
    public void put(Object object, String url, String params) {

        if (object != null) {
            //写入最新的缓存
            String newUrl = SecurityUtil.getMD5(url + params);
            final File entityFile = this.buildFile(newUrl);
            String jsonString = this.mSerializer.serialize(object);
            this.executeAsynchronously(new CacheWriter(this.mFileManager, entityFile, jsonString));
            setLastCacheUpdateTimeMillis(newUrl);
        }
    }


    /**
     * 判断是否有缓存
     * @param url
     * @return
     */
    @Override
    public boolean isCached(String url) {
        final File userEntityFile = this.buildFile(url);
        return this.mFileManager.exists(userEntityFile);
    }


    /**
     *
     * 提供设置过时时间
     * @param offlineExpiredTime
     * @return
     */
    @Override
    public boolean isOfflineExpired(String url,String params,long offlineExpiredTime) {
        long currentTime = System.currentTimeMillis();
        long lastUpdateTime = this.getLastCacheUpdateTimeMillis(SecurityUtil.getMD5(url+params));

        boolean expired = ((currentTime - lastUpdateTime) > offlineExpiredTime);
        if (expired) {
            deleteFile(url, params);
        }
        return expired;
    }

    /**
     * 在线缓存
     * @param url 网址
     * @param params 参数标识
     * @param onlineEpiredTime 在线缓存时间
     * @param offlineExpiredTime 离线缓存时间
     * @return
     */
    @Override
    public boolean isOnlineExpired(String url, String params, long onlineEpiredTime,long offlineExpiredTime) {

        long currentTime = System.currentTimeMillis();
        long lastUpdateTime = this.getLastCacheUpdateTimeMillis(SecurityUtil.getMD5(url+params));

        boolean expired = ((currentTime - lastUpdateTime) > onlineEpiredTime);
        if (expired) {
            if((currentTime - lastUpdateTime)> offlineExpiredTime){
                //超出了离线缓存时间
                deleteFile(url, params);
            }
        }
        return expired;
    }

    /**
     * 删除缓存文件
     * @param url 网站
     * @param params 参数对象
     */
    private void deleteFile(String url, String params) {
        String newUrl = SecurityUtil.getMD5(url + params);
        File file = this.buildFile(newUrl);
        this.evictFile(file);
    }

    /**
     * 删除对象的文件
     * @param file
     */
    @Override
    public void evictFile(File file) {
        this.executeAsynchronously(new CacheEvictor(this.mFileManager, this.mCacheDir));
    }

    @Override
    public long onlineExpiredTime() {
        return ONLINE_EXPIRATION_TIME;
    }

    @Override
    public long offlineExpiredTime() {
        return OFFLINE_EXPIRATION_TIME;
    }

    /**
     * 构建File对象
     * @param url
     * @return
     */
    private File buildFile(String url) {
        final StringBuilder fileNameBuilder = new StringBuilder();
        fileNameBuilder.append(this.mCacheDir.getPath());
        fileNameBuilder.append(File.separator);
        fileNameBuilder.append(DEFAULT_FILE_NAME);
        fileNameBuilder.append(url);

        return new File(fileNameBuilder.toString());
    }

  private void setLastCacheUpdateTimeMillis(String url) {
    final long currentMillis = System.currentTimeMillis();
    this.mFileManager.writeToPreferences(this.mContext, SETTINGS_FILE_NAME,
            url, currentMillis);
  }

  private long getLastCacheUpdateTimeMillis(String url) {
    return this.mFileManager.getFromPreferences(this.mContext, SETTINGS_FILE_NAME,
            url);
  }

  private void executeAsynchronously(Runnable runnable) {
    this.mThreadExecutor.execute(runnable);
  }

  private static class CacheWriter implements Runnable {
    private final FileManager fileManager;
    private final File fileToWrite;
    private final String fileContent;

    CacheWriter(FileManager fileManager, File fileToWrite, String fileContent) {
      this.fileManager = fileManager;
      this.fileToWrite = fileToWrite;
      this.fileContent = fileContent;
    }

    @Override public void run() {
      this.fileManager.writeToFile(fileToWrite, fileContent);
    }
  }

  private static class CacheEvictor implements Runnable {
    private final FileManager fileManager;
    private final File cacheDir;

    CacheEvictor(FileManager fileManager, File cacheDir) {
      this.fileManager = fileManager;
      this.cacheDir = cacheDir;
    }

    @Override public void run() {
      this.fileManager.clearDirectory(this.cacheDir);
    }
  }
}
