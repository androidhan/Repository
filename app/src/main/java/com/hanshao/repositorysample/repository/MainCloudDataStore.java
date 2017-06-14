package com.hanshao.repositorysample.repository;



import com.hanshao.repository.cache.Cache;
import com.hanshao.repository.datasource.BaseCloudDataStore;
import com.hanshao.repository.mapper.Mapper;

import java.util.Map;

import rx.Observable;


/**
 * AUTHOR: hanshao
 * DATE: 17/6/9.
 * ACTION:主云获取云数据获取
 */

public class MainCloudDataStore extends BaseCloudDataStore<MainApi> {

    public static final String NEWS = "social";
    public static final String NEWSMAPPER = "guonei";


    public MainCloudDataStore(MainApi api, Cache cache, Mapper mapper) {
        super(api, cache, mapper);
    }

    /**
     * 自主实现提供Observable
     * @param api 泛型的API
     * @param url 实际上是个标识符
     * @param paramsObject 参数对象
     * @param <T>
     * @return
     */
    @Override
    public <T> Observable<T> realEntityDetails(MainApi api,String url, Object paramsObject) {
        Observable o = null;
        switch (url) {
            case NEWSMAPPER:
                o = api.getNewsMapper((Map) paramsObject);
                break;
            case NEWS:
                o = api.getNews((Map) paramsObject);
                break;
            default:
                break;
        }
        return o;
    }

}
