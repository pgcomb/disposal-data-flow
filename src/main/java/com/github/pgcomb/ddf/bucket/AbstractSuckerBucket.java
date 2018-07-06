package com.github.pgcomb.ddf.bucket;

import com.github.pgcomb.ddf.Stoppable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 王东旭
 * @date 2018-07-06
 */
public abstract class AbstractSuckerBucket<T> implements Bucket<T> {

    private boolean stop = false;

    private List<PipeSucker<T>> pipes = new ArrayList<>();

    @Override
    public final void shake(T data) {
        pipes.forEach(c -> c.suck(data));
    }

    @Override
    public void topple() {
        flow((data) -> pipes.forEach(c -> c.suck(data)),this);
        this.stop();
        pipes.forEach(PipeSucker::stop);
    }

    @Override
    public void stop() {
        stop = true;
    }

    @Override
    public boolean isStop() {
        return stop;
    }

    /**
     * 数据持续流
     * @param shake 每次的数据消费者
     * @param stoppable 是否停止
     */
    protected abstract void flow(Sucker<T> shake, Stoppable stoppable);

    public List<PipeSucker<T>> getPipes() {
        return pipes;
    }

    public void addPipe(PipeSucker<T> pipeSucker){
        this.pipes.add(pipeSucker);
    }
}
