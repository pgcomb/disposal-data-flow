package com.github.pgcomb.ddf.map;

import com.github.pgcomb.ddf.common.AbstractPipeline;
import com.github.pgcomb.ddf.map.api.Mapper;
import com.github.pgcomb.ddf.map.api.Payload;
import com.github.pgcomb.ddf.map.api.Principal;

/**
 * @author 王东旭
 * @date 2018-07-07
 */
public abstract class AbstractPipelineMapper<I,K extends Principal,V extends Payload>
        extends AbstractPipeline<I,MapPair<K,V>> implements Mapper<I,K,V> {

    @Override
    public MapPair<K, V> handlePipe(I in) {
        return map(in);
    }
}
