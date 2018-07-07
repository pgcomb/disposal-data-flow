package com.github.pgcomb.ddf.common;

/**
 * 传送带
 * @author 王东旭
 * @date 2018-07-07
 */
public interface Conveyor<T> extends Stoppable {

    /**
     * 传送
     * @param data 数据
     */
    void transfer(T data);

    /**
     * 停止传送带
     */
    @Override
    default void stop(){}

    /**
     *
     * @return 是否停止
     */
    @Override
    default boolean isStop(){return false;}
}
