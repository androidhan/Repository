package com.hanshao.repositorysample.bean;

/**
 * AUTHOR: hanshao
 * DATE: 17/6/12.
 * ACTION:需经Mapper的模板
 */

public class HttpResult<T> {
    public String code;
    public String msg;
    public T  newslist;
}
