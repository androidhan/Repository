/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hanshao.repository.datasource;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hanshao.repository.api.BaseApi;
import com.hanshao.repository.cache.Cache;
import com.hanshao.repository.cache.CacheImpl;
import com.hanshao.repository.mapper.Mapper;
import com.hanshao.repository.utils.NetWorkUtil;
import com.hanshao.repository.utils.SecurityUtil;


/**
 * AUTHOR: hanshao
 * DATE: 17/6/8.
 * ACTION: 基础的数据源工厂
 */
public abstract class BaseDataStoreFactory<T extends BaseApi> {

  protected Context mContext;
  private Cache mCache;
  private T mApi;
  private Mapper mMapper;

  /**
   * 数据源工厂构造
   *
   * @param context
   * @param baseApi            提供的API
   */
  public BaseDataStoreFactory(@NonNull Context context, @NonNull T baseApi) {
    this.mContext = context.getApplicationContext();
    this.mApi = baseApi;
  }


  /**
   * 初始化缓存
   * @param onlineExpiredTime 在线缓存时间
   * @param offlineExpiredTime 离线缓存时间
   */
  public void initCache(long onlineExpiredTime, long offlineExpiredTime){
    this.mCache = new CacheImpl(mContext, onlineExpiredTime, offlineExpiredTime);
  }

  /**
   * 初始化映射
   *
   * @param mapper
   */
  public void initMapper(Mapper mapper) {
    this.mMapper = mapper;
  }

  /**
   * 获取数据源
   *
   * @param url          网址
   * @param paramsObject 参数对象封装
   * @return
   */
  public DataStore create(String url, Object paramsObject) {
    return this.create(url, paramsObject, mCache.onlineExpiredTime(), mCache.offlineExpiredTime());
  }


  /**
   * 获取数据源
   * @param url 网址
   * @param onlineExpiredTime 在线缓存时间
   * @param offlineExpiredTime 离线缓存时间
   * @param paramsObject 参数对象封装
   * @return
   */
  public DataStore create(String url, Object paramsObject, long onlineExpiredTime, long offlineExpiredTime) {
    DataStore dataStore = null;

    if (NetWorkUtil.isNetworkConnected(mContext)) {
      //在线
      if (onlineExpiredTime >0 &&!this.mCache.isOnlineExpired(url, paramsObject == null ? "" : paramsObject.toString(), onlineExpiredTime, offlineExpiredTime) &&
              this.mCache.isCached(SecurityUtil.getMD5(url + (paramsObject == null ? "" : paramsObject.toString())))) {
        dataStore = new DiskDataStore(this.mCache);
      }
    } else {
      //离线
      if (offlineExpiredTime >0 &&!this.mCache.isOfflineExpired(url, paramsObject == null ? "" : paramsObject.toString(), offlineExpiredTime ) &&
              this.mCache.isCached(SecurityUtil.getMD5(url + (paramsObject == null ? "" : paramsObject.toString())))) {
        dataStore = new DiskDataStore(this.mCache);
      }
    }

    if (dataStore == null) {
      dataStore = createCloudDataStore(mApi, mCache,mMapper);
    }

    return dataStore;
  }

  /**
   * 创建云访问数据源
   * @return
   */
  public abstract DataStore createCloudDataStore(T api, Cache cache, Mapper mapper);


}
