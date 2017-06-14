package com.hanshao.repository.cache;

import java.io.File;

import rx.Observable;

/**
 * AUTHOR: hanshao
 * DATE: 17/6/8.
 * ACTION: 缓存接口
 */
public interface Cache {

    /**
     * 读取
     *
     * @param url    网址
     * @param params 参数标识
     * @param <T>    泛型
     * @param clazz  字节码
     * @return
     */
    <T> Observable<T> get(String url, String params, Class<?> clazz);

    /**
     * 写入数据
     * @param object 写入的对象
     * @param url 路径
     * @param params 参数标识
     */
    void put(Object object, String url, String params);

    /**
     * 是否缓存
     * @param url
     * @return
     */
    boolean isCached(String url);


    /**
     * 过时时间动态设置
     * @param url 网址
     * @param offlineExpiredTime 离线缓存时间
     * @param params 参数标识
     * @return
     */
    boolean isOfflineExpired(String url, String params, long offlineExpiredTime);


    /**
     * 过时时间动态设置
     * @param url 网址
     * @param onlineEpiredTime 在线缓存时间
     * @param offlineExpiredTime 离线缓存时间
     * @param params 参数标识
     * @return
     */
    boolean isOnlineExpired(String url, String params, long onlineEpiredTime, long offlineExpiredTime);


    /**
     * 清除文件
     */
    void evictFile(File file);

    /**
     * 获取默认在线缓存时间
     * @return
     */
    long onlineExpiredTime();

    /**
     * 获取默认离线缓存时间
     */
    long offlineExpiredTime();
}
