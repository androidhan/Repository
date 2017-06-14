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
package com.hanshao.repository;


import rx.Observable;

/**
 * AUTHOR: hanshao
 * DATE: 17/6/8.
 * ACTION: 数据仓库接口
 */
public interface Repository {

  /**
   * 采用默认的离线获取数据
   * @param url 网址
   * @param <T> 泛型
   * @param object 封装的参数对象
   * @param clazz 字节码
   * @return
   */
  <T> Observable<T> data(String url, Object object, Class<?> clazz);

  /**
   * 离线获取数据
   *
   * @param url            网址
   * @param <T>            泛型
   * @param object         封装的参数对象
   * @param offLineExpired 离线时间时间
   * @param clazz          字节码
   * @return
   */
  <T> Observable<T> data(String url, Object object, Class<?> clazz, long offLineExpired);

  /**
   * 在线与离线获取数据
   *
   * @param url            网址
   * @param object         封装的参数对象
   * @param onlineExpired  在线缓存时间
   * @param offLineExpired 离线缓存时间
   * @param <T>            泛型
   * @param clazz          字节码
   * @return
   */
  <T> Observable<T> data(String url, Object object, Class<?> clazz, long onlineExpired, long offLineExpired);


  /**
   * 只在线获取数据
   *
   * @param url           网址
   * @param object        封装的参数对象
   * @param onlineExpired 在线缓存时间
   * @param clazz         字节码
   * @param <T>           泛型
   * @return
   */
  <T> Observable<T> onlyOnlineData(String url, Object object, Class<?> clazz, long onlineExpired);

}
