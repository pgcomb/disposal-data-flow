package com.github.pgcomb.ddf.bucket;

import com.github.pgcomb.ddf.Stoppable;

/**
 * 数据桶
 *
 * @author 王东旭
 * @date 2018-07-06
 */
public interface Bucket<T> extends Stoppable {

    /**
     * 倾倒数据
     */
    void topple();

    /**
     * 每次的倾出
     *
     * @param data 数据
     */
    void shake(T data);
}
