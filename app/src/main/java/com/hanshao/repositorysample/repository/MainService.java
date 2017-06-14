package com.hanshao.repositorysample.repository;

import com.hanshao.repositorysample.bean.HttpResult;
import com.hanshao.repositorysample.bean.News;
import com.hanshao.repositorysample.bean.NewsMapper;

import java.util.List;
import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * AUTHOR: hanshao
 * DATE: 17/6/9.
 * ACTION:新闻接口
 */

public interface MainService {

    @GET("social")
    Observable<HttpResult<List<NewsMapper>>> getNewsMapper(@QueryMap Map<String, String> map);

    @GET("guonei")
    Observable<News> getNews(@QueryMap Map<String, String> map);
}
