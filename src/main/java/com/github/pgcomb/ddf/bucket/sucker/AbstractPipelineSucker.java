package com.github.pgcomb.ddf.bucket.sucker;

import com.github.pgcomb.ddf.common.PipelineAdapter;

/**
 * @author 王东旭
 * @date 2018-07-06
 */
public abstract class AbstractPipelineSucker<I,O> extends PipelineAdapter<I,O> implements PipeSucker<I> {

    @Override
    public void suck(I data) {
        doPipe(data);
    }
}

