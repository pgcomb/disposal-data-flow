package com.github.pgcomb.ddf.common;

/**
 * 管道连接
 *
 * @author 王东旭
 * @date 2018-07-06
 */
public interface Pipeline<I,O> {

    /**
     * 管道方法
     * @param in 输入
     * @return 处理后数据
     */
    O handlePipe(I in);

    /**
     * 执行下一个管道
     * @param data 数据
     */
    void doPipe(I data);
}
