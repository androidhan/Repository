package com.hanshao.repositorysample.repository;

import android.content.Context;


import com.hanshao.repository.api.BaseApi;

import java.util.Map;

import okhttp3.OkHttpClient;
import rx.Observable;

/**
 * AUTHOR: hanshao
 * DATE: 17/6/9.
 * ACTION:主API
 */

public class MainApi extends BaseApi<MainService> {


    public MainApi(Context context) {
        super(context);
    }

    /**
     * 提供Service字节码
     * @return
     */
    @Override
    protected Class<?> providerServiceClazz() {
        return MainService.class;
    }

    /**
     * 提供Retrofit的BaseUrl
     * @return
     */
    @Override
    protected String providerBaseUrl() {
        return "https://api.tianapi.com/";
    }


    /**
     * 经Mapper转换
     *
     * @param map
     * @return
     */
    public Observable getNewsMapper(Map map) {
        return getService().getNewsMapper(map);
    }


    /**
     * 未经Mapper转换
     *
     * @param map
     * @return
     */
    public Observable getNews(Map map) {
        return getService().getNews(map);
    }


}
