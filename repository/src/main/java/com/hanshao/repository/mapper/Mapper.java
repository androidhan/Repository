package com.hanshao.repository.mapper;

/**
 * AUTHOR: hanshao
 * DATE: 17/6/9.
 * ACTION:Mapper
 */


import rx.functions.Func1;

public interface Mapper<E,T> extends Func1<E,T> {
}