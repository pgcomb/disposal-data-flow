package com.github.pgcomb.ddf.common;

import java.util.ArrayList;
import java.util.List;

public abstract class PipelineAdapter<I,O> implements Pipeline<I,O>,Stoppable {

    private List<Pipeline<O,?>> pipelines = new ArrayList<>();

    private boolean stop;

    public final void doPipe(I in){
        if (!isStop()) {
            O o = handlePipe(in);
            pipelines.forEach(pipeline -> pipeline.doPipe(o));
        }
    }

    public void addPipeline(Pipeline<O, ?> pipeline) {
        this.pipelines .add(pipeline);
    }

    public List<Pipeline<O, ?>> getPipelines() {
        return pipelines;
    }

    @Override
    public void stop() {
        stop = true;
    }

    @Override
    public boolean isStop() {
        return stop;
    }
}
