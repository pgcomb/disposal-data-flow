package com.github.pgcomb.ddf.press;

import com.github.pgcomb.ddf.common.Conveyor;

import java.util.List;

/**
 * 打包器
 * @author 王东旭
 * @date 2018-07-07
 */
public interface PackingPress<T>  {

    /**
     * 内容注入
     * @param data 注入的内容
     */
    void inject(T data);

    /**
     * 打包输出
     * @return 输出接收
     */
    Conveyor<List<T>> conveyor();
}
