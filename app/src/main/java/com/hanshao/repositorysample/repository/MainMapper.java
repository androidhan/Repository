package com.hanshao.repositorysample.repository;

import com.hanshao.repository.mapper.Mapper;
import com.hanshao.repositorysample.bean.HttpResult;

/**
 * AUTHOR: hanshao
 * DATE: 17/6/12.
 * ACTION:转换
 */

public class MainMapper<T> implements Mapper<HttpResult<T>,T> {

    @Override
    public T call(HttpResult<T> tHttpResult) {

        return tHttpResult.newslist;
    }
}
