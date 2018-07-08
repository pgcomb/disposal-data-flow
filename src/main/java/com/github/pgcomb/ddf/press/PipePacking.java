package com.github.pgcomb.ddf.press;

import com.github.pgcomb.ddf.common.AbstractPipeline;
import com.github.pgcomb.ddf.common.Conveyor;
import com.github.pgcomb.ddf.common.packagee.InMemoryDataPackage;

/**
 * 打包管道
 * @author 王东旭
 * @date 2018-07-07
 */
public class PipePacking<I> extends AbstractPipeline<I,Void> implements PackingPress<I> {

    /**
     * 打包大小
     */
    private int packageSize = 200000;

    /**
     * 包
     */
    private InMemoryDataPackage<I> bag = new InMemoryDataPackage<>(0, 0);

    /**
     * 数据计数(0开始)
     */
    private long eleCount = 0;

    /**
     * 数据包计数(0开始)
     */
    private int packageCount = 0;
    /**
     * 输出包的履带
     */
    private Conveyor<InMemoryDataPackage<I>> conveyor;

    private static final String NAME = "pipe_packing";

    public PipePacking(int packageSize, Conveyor<InMemoryDataPackage<I>> conveyor) {
        //将自己的开关设置到传送带的另一端，遇到异常可以反馈
        conveyor.preStop(this);
        this.packageSize = packageSize;
        this.conveyor = conveyor;
    }

    public PipePacking(Conveyor<InMemoryDataPackage<I>> conveyor) {
        conveyor.preStop(this);
        this.conveyor = conveyor;
    }

    @Override
    public Void handlePipe(I in) {
        inject(in);
        return null;
    }

    @Override
    public final void inject(I data) {
        eleCount++;
        bag.add(data);
        if (bag.size() == packageSize){
            out();
        }
    }

    @Override
    public void stopEvent() {
        if (!bag.isEmpty()){
            out();
            conveyor.stopForward();
        }
    }

    private void out(){
        packageCount++;
        conveyor().transfer(bag);
        bag = new InMemoryDataPackage<>(eleCount,packageCount);
    }

    @Override
    public final Conveyor<InMemoryDataPackage<I>> conveyor() {
        return conveyor;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
