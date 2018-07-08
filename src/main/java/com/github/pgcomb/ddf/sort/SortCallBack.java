package com.github.pgcomb.ddf.sort;

import com.github.pgcomb.ddf.common.WorkCallBack;
import com.github.pgcomb.ddf.common.packagee.InMemoryDataPackage;
import com.github.pgcomb.ddf.common.packagee.StreamDataPackage;
import com.github.pgcomb.ddf.exception.SortOutException;
import com.github.pgcomb.ddf.exception.WorkSuccessCallBackException;
import com.github.pgcomb.ddf.sort.strategy.SortOutStrategy;

import java.util.function.Consumer;

/**
 * 排序回调
 *
 * @param <I> 回调数据类型
 * @author 王东旭
 */
public class SortCallBack<I extends InMemoryDataPackage<? extends Comparable<?>>, O extends StreamDataPackage> implements WorkCallBack<I> {

    /**
     * 排序输出策略
     */
    private SortOutStrategy<I, O> sortOutStrategy;

    /**
     * 成功的通知
     */
    private Consumer<O> inform;

    public SortCallBack(SortOutStrategy<I, O> sortOutStrategy, Consumer<O> inform) {
        this.sortOutStrategy = sortOutStrategy;
        this.inform = inform;
    }

    @Override
    public void success(I data) throws WorkSuccessCallBackException {

        O export;
        try {
            export = sortOutStrategy.export(data);
        } catch (SortOutException e) {
            throw new WorkSuccessCallBackException("sort output error", e);
        }
        inform.accept(export);
    }

    @Override
    public void defeat(I data) {

    }
}
