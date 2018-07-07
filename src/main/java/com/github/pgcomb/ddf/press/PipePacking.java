package com.github.pgcomb.ddf.press;

import com.github.pgcomb.ddf.common.AbstractPipeline;
import com.github.pgcomb.ddf.common.Conveyor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 打包管道
 * @author 王东旭
 * @date 2018-07-07
 */
public class PipePacking<I> extends AbstractPipeline<I,ObjectUtils.Null> implements PackingPress<I> {

    /**
     * 打包大小
     */
    private int packageSize = 1000;

    /**
     * 包
     */
    private List<I> bag = new ArrayList<>();

    /**
     * 输出包的履带
     */
    private Conveyor<List<I>> conveyor;

    private final static String NAME = "pipe_packing";

    public PipePacking(int packageSize, Conveyor<List<I>> conveyor) {
        this.packageSize = packageSize;
        this.conveyor = conveyor;
    }

    public PipePacking(Conveyor<List<I>> conveyor) {
        this.conveyor = conveyor;
    }

    @Override
    public ObjectUtils.Null handlePipe(I in) {
        inject(in);
        return ObjectUtils.NULL;
    }

    @Override
    public final void inject(I data) {
        bag.add(data);
        if (bag.size() == packageSize){
            out();
        }
    }

    @Override
    public void stopEvent() {
        if (!CollectionUtils.isEmpty(bag)){
            out();
            conveyor.stop();
        }
    }

    private void out(){
        conveyor().transfer(bag);
        bag = new ArrayList<>();
    }

    @Override
    public final Conveyor<List<I>> conveyor() {
        return conveyor;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
