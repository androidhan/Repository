package com.hanshao.repository;


import com.hanshao.repository.datasource.BaseDataStoreFactory;
import com.hanshao.repository.mapper.Mapper;

import rx.Observable;

/**
 * AUTHOR: hanshao
 * DATE: 17/6/8.
 * ACTION: 数据仓库
 */

public class DataRepository implements Repository {

  private final BaseDataStoreFactory dataStoreFactory;

  /**
   * @param dataStoreFactory   数据源工厂
   * @param onlineExpiredTime  在线缓存时间
   * @param offlineExpiredTime 离线缓存时间
   * @param mapper             映射
   */
  public DataRepository(BaseDataStoreFactory dataStoreFactory, long onlineExpiredTime, long offlineExpiredTime, Mapper mapper) {
    this.dataStoreFactory = dataStoreFactory;
    this.dataStoreFactory.initCache(onlineExpiredTime, offlineExpiredTime);
    this.dataStoreFactory.initMapper(mapper);
  }


  /**
   * @param dataStoreFactory   数据源工厂
   * @param onlineExpiredTime  在线缓存时间
   * @param offlineExpiredTime 离线缓存时间
   */
  public DataRepository(BaseDataStoreFactory dataStoreFactory, long onlineExpiredTime, long offlineExpiredTime) {
    this(dataStoreFactory,onlineExpiredTime,offlineExpiredTime,null);
  }


  /**
   * @param dataStoreFactory 数据源工厂
   */
  public DataRepository(BaseDataStoreFactory dataStoreFactory) {
    this(dataStoreFactory, 0, 0, null);
  }

  /**
   * @param dataStoreFactory 数据源工厂
   * @param mapper           映射
   */
  public DataRepository(BaseDataStoreFactory dataStoreFactory, Mapper mapper) {
    this(dataStoreFactory, 0, 0, mapper);
  }

  @Override
  public <T> Observable<T> data(String url, Object object, Class<?> clazz) {
    return this.dataStoreFactory.create(url, object).entityDetails(url, object,clazz);
  }

  @Override
  public <T> Observable<T> data(String url, Object object, Class<?> clazz, long offLineExpired) {

    verify(offLineExpired);
    return this.dataStoreFactory.create(url, object, 0, offLineExpired).entityDetails(url, object, clazz);
  }

  @Override
  public <T> Observable<T> data(String url, Object object, Class<?> clazz, long onlineExpired, long offLineExpired) {

    verify(onlineExpired, offLineExpired);
    return this.dataStoreFactory.create(url, object, onlineExpired, offLineExpired).entityDetails(url, object, clazz);
  }

  @Override
  public <T> Observable<T> onlyOnlineData(String url, Object object, Class<?> clazz, long onlineExpired) {
    verify(onlineExpired);
    return this.dataStoreFactory.create(url,object,onlineExpired,0).entityDetails(url, object,clazz);
  }

  /**
   * 校验值
   * @param values
   */
  private void verify(long... values) {
    for (long value : values) {
      if (value < 0) {
        throw new IllegalArgumentException("offLineExpired or onlineExpired must be greater than 0 ");
      }
    }
  }


}
