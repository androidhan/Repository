package com.hanshao.repositorysample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hanshao.repository.DataRepository;
import com.hanshao.repositorysample.bean.News;
import com.hanshao.repositorysample.bean.NewsMapper;
import com.hanshao.repositorysample.repository.MainApi;
import com.hanshao.repositorysample.repository.MainCloudDataStore;
import com.hanshao.repositorysample.repository.MainDataStoreFacotry;
import com.hanshao.repositorysample.repository.MainMapper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;


/**
 * AUTHOR: hanshao
 * DATE: 17/6/11.
 * ACTION: sample
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = "NEWS";
    private final static long ONLINE = 10*1000;
    private final static long OFFLINE = 60*1000;

    private DataRepository mDataRepository;
    private DataRepository mMapperRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.bt_mapper).setOnClickListener(this);
        findViewById(R.id.bt_normal).setOnClickListener(this);
    }


    private void toSubscribe(Observable o,  Subscriber s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.bt_normal){
            normal();
        }else if(id == R.id.bt_mapper){
            mapper();
        }
    }

    /**
     * 未经Mapper转化
     */
    private void normal() {

        if(mDataRepository == null){
            mDataRepository = new DataRepository(new MainDataStoreFacotry(MainActivity.this,new MainApi(MainActivity.this)),ONLINE,OFFLINE);
        }
        //参数Map
        Map<String,String> map  = new HashMap();
        map.put("key","0bd8ccf22273c0547289f5ae7eb5e39b");
        map.put("num","5");
        //采用默认初始化的在线与离线缓存时间，MainCloudDataStore.NEWS就是一个标识符，用于在MainCloudDataStore的realEntityDetails()方法中获取相应的Observable。
        //这里需要提供字节码对象用于Gson解析，若你返回的Observable<List<News>>时，还是传News.class即可
        Observable<News> o = mDataRepository.data(MainCloudDataStore.NEWS, map,News.class);
//        //设置离线缓存时间60s，不会进行在线缓存
//        Observable<News> o = mDataRepository.data(MainCloudDataStore.NEWS, map,News.class,60*1000);
//        //设置在线缓存时间60s,离线缓存时间120s
//        Observable<News> o = mDataRepository.data(MainCloudDataStore.NEWS, map,News.class,60*1000,120*1000);
//        //设置在线缓存时间60s,不会进行离线缓存
//        Observable<News> o = mDataRepository.onlyOnlineData(MainCloudDataStore.NEWS, map,News.class,60*1000);
        toSubscribe(o, new Subscriber<News>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG,"error:"+e.toString());
            }

            @Override
            public void onNext(News news) {
                for (News.NewslistEntity newslistEntity : news.getNewslist()) {
                    Log.e(TAG,newslistEntity.toString());
                }
            }
        });
    }

    /**
     * Mapper转化
     */
    private void mapper() {

        if(mMapperRepository == null){
            mMapperRepository = new DataRepository(new MainDataStoreFacotry(MainActivity.this,
                    new MainApi(MainActivity.this)),ONLINE,OFFLINE,new MainMapper());
        }

        //参数Map
        Map<String,String> map  = new HashMap();
        map.put("key","0bd8ccf22273c0547289f5ae7eb5e39b");
        map.put("num","5");
        //采用默认初始化的在线与离线缓存时间
        Observable<List<NewsMapper>> o = mMapperRepository.data(MainCloudDataStore.NEWSMAPPER, map,NewsMapper.class);
//          //设置离线缓存时间60s，不会进行在线缓存
//        Observable<News> o = mNormalRepository.data(MainCloudDataStore.NEWSMAPPER, map,News.class,60*1000);
//        //设置在线缓存时间60s,离线缓存时间120s
//        Observable<News> o = mNormalRepository.data(MainCloudDataStore.NEWSMAPPER, map,News.class,60*1000,120*1000);
//        //设置在线缓存时间60s,不会进行离线缓存
//        Observable<News> o = mNormalRepository.onlyOnlineData(MainCloudDataStore.NEWSMAPPER, map,News.class,60*1000);
        toSubscribe(o, new Subscriber<List<NewsMapper>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG,"error:"+e.toString());
            }

            @Override
            public void onNext(List<NewsMapper> news) {
                for (NewsMapper aNew : news) {
                    Log.e(TAG,aNew.toString());
                }
            }
        });
    }
}
