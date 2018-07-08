package com.github.pgcomb.ddf.common;

import com.github.pgcomb.ddf.exception.WorkErrorCallBackException;
import com.github.pgcomb.ddf.exception.WorkSuccessCallBackException;

/**
 * 任务回调
 *
 * @param <T> 回调数据
 * @author 王东旭
 */
public interface WorkCallBack<T> {

    /**
     * 成功回调
     *
     * @param data 回调数据
     * @throws WorkSuccessCallBackException 回调成功异常
     */
    void success(T data) throws WorkSuccessCallBackException;

    /**
     * 失败回调
     *
     * @param data 回调数据
     * @throws WorkErrorCallBackException 回调成功异常
     */
    void defeat(T data) throws WorkErrorCallBackException;
}
