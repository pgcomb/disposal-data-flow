package com.github.pgcomb.ddf.common;

/**
 * 向前停止
 *
 * @author 王东旭
 * @date 2018-07-06
 */
public interface StopForward {

    /**
     * 停止
     */
    void stopForward();

    /**
     * 停止状态
     *
     * @return 是否停止
     */
    boolean isStop();
}
