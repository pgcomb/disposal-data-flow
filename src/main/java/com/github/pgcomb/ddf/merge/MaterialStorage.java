package com.github.pgcomb.ddf.merge;

import java.util.function.Consumer;

/**
 * 合并库
 * @param <T> 数据格式
 */
public interface MaterialStorage<S,T> {

    Consumer<T> overflow();

    Consumer<S> end();

    void stop();

    void destroy();

    void back(T materials);

    void add(S t);
}
