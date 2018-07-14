package com.github.pgcomb.ddf.bucket.sucker;

import jdk.nashorn.internal.objects.annotations.Function;

/**
 * @author 王东旭
 * @date 2018-07-06
 */
public interface Sucker<T> {
    /**
     * 接收数据
     * @param data 一条数据
     */
    @Function
    void suck(T data);
}
