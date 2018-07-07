package com.github.pgcomb.ddf.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理流适配
 * @param <I> 输入
 * @param <O> 输出
 * @author 王东旭
 */
public abstract class AbstractPipeline<I,O> implements Pipeline<I,O>,Nameable {

    private static final Logger log = LoggerFactory.getLogger(AbstractPipeline.class);

    private List<Pipeline<O,?>> pipelines = new ArrayList<>();

    private boolean stop;

    @Override
    public final void doPipe(I in){
        if (!isStop()) {
            O o = handlePipe(in);
            pipelines.forEach(pipeline -> pipeline.doPipe(o));
        }
    }

    public final void addPipeline(Pipeline<O, ?> pipeline) {
        this.pipelines .add(pipeline);
    }

    public final List<Pipeline<O, ?>> getPipelines() {
        return pipelines;
    }

    @Override
    public final void stop() {
        stop = true;
        stopEvent();
        log.info("pipe[{}] stop.",getName());
        pipelines.forEach(Pipeline::stop);
    }

    /**
     * stop会调用的方法
     */
    public void stopEvent(){}

    @Override
    public final boolean isStop() {
        return stop;
    }
}
