package com.hanshao.repositorysample.repository;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hanshao.repository.cache.Cache;
import com.hanshao.repository.datasource.BaseDataStoreFactory;
import com.hanshao.repository.datasource.DataStore;
import com.hanshao.repository.mapper.Mapper;


/**
 * AUTHOR: hanshao
 * DATE: 17/6/9.
 * ACTION:主DataStoreFactory
 */

//泛型为BaseApi的子类
public class MainDataStoreFacotry extends BaseDataStoreFactory<MainApi> {

    public MainDataStoreFacotry(@NonNull Context context, @NonNull MainApi baseApi) {
        super(context, baseApi);
    }

    /**
     *
     * @param api 提供的云数据的BaseApi的子类
     * @param cache 缓存策略
     * @param mapper 映射
     * @return
     */
    @Override
    public DataStore createCloudDataStore(MainApi api, Cache cache, Mapper mapper) {
        return new MainCloudDataStore(api,cache,mapper);
    }

}
