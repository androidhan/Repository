package com.hanshao.repository.api;

import okhttp3.OkHttpClient;

/**
 * AUTHOR: hanshao
 * DATE: 17/6/9.
 * ACTION:Api 接口
 */

public interface Api<T> {

    /**
     * 获取Retrofit
     * @return
     */
    T getService();

    /**
     * 获得OkHttpClient
     * @return
     */
    OkHttpClient buildOkHttpClient();

}
