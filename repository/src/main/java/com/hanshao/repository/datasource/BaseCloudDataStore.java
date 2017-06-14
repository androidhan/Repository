package com.hanshao.repository.datasource;

import com.hanshao.repository.api.BaseApi;
import com.hanshao.repository.cache.Cache;
import com.hanshao.repository.mapper.Mapper;
import rx.Observable;
import rx.functions.Action1;


/**
 * AUTHOR: hanshao
 * DATE: 17/6/8.
 * ACTION: 基础的云数据源
 */
public abstract class BaseCloudDataStore<E extends BaseApi> implements DataStore {

  private final E mApi;
  protected final Cache mCache;
  private Mapper mMapper; //提供mapper

  public BaseCloudDataStore(E api, Cache cache, Mapper mapper) {
    this.mApi = api;
    this.mCache = cache;
    this.mMapper = mapper;
  }

  @Override
  public <T> Observable<T> entityDetails(final String url, final Object paramsObject, Class<?> clazz) {

    Observable o = realEntityDetails(mApi,url, paramsObject);
    if (mMapper != null) {
      o = o.map(mMapper);
    }
    return o.doOnNext(new Action1<T>() {
      @Override
      public void call(T object) {
        mCache.put(object, url, paramsObject.toString());
      }
    });
  }

  public abstract <T> Observable<T> realEntityDetails(E api,String url, Object paramsObject);

}
