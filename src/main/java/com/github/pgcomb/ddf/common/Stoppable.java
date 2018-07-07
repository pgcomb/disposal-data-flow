package com.github.pgcomb.ddf.common;

/**
 * @author 王东旭
 * @date 2018-07-06
 */
public interface Stoppable {

    /**
     * 可停止的
     */
    void stop();

    /**
     * 停止状态
     * @return 是否停止
     */
    boolean isStop();
}
