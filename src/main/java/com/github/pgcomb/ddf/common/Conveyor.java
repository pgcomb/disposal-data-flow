package com.github.pgcomb.ddf.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 传送带
 * @author 王东旭
 * @date 2018-07-07
 */
public abstract class Conveyor<T> implements Stopable {

    private static final Logger log = LoggerFactory.getLogger(Conveyor.class);

    private boolean stop = false;

    private StopFeedBack stopFeedBack;

    /**
     * 传送
     * @param data 数据
     */
    public abstract void transfer(T data);

    @Override
    public final void stopForward(){
        this.stop = true;
        stopEvent();
    }

    /**
     * 停止事件
     */
    public abstract void stopEvent();

    @Override
    public final boolean isStop(){
        return stop;
    }

    @Override
    public final void stopBack() {
        stop = true;
       if ( stopFeedBack != null && !stopFeedBack.isStop()){
           stopFeedBack.stopBack();
       }
    }

    @Override
    public final void preStop(StopFeedBack stopFeedBack) {
        this.stopFeedBack = stopFeedBack;
    }
}
