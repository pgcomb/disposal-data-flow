package com.github.pgcomb.ddf.map.api;

import com.github.pgcomb.ddf.map.MapPair;

/**
 * 映射管道
 * @param <I> 输入
 * @param <K> 输出键
 * @param <V> 输出负载
 */
public interface Mapper<I,K extends Principal,V extends Payload> {

    /**
     * 映射方法
     * @param in 输入
     * @return 输出
     */
    MapPair<K,V> map(I in);
}
