
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
