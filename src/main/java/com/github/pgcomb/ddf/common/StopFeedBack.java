package com.github.pgcomb.ddf.common;

/**
 * 停止反馈
 *
 * @author 王东旭
 */
public interface StopFeedBack {

    /**
     * 停止反馈
     */
    void stopBack();

    /**
     * 前一个反馈对象
     *
     * @param stopFeedBack 反馈对象
     */
    void preStop(StopFeedBack stopFeedBack);

    /**
     * 是否已经停止
     *
     * @return 是否已经停止
     */
    boolean isStop();
}
