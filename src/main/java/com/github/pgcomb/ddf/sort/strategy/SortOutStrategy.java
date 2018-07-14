package com.github.pgcomb.ddf.sort.strategy;

import com.github.pgcomb.ddf.common.packagee.InMemoryDataPackage;
import com.github.pgcomb.ddf.common.packagee.PackageMetadata;
import com.github.pgcomb.ddf.exception.SortOutException;

/**
 * 排序输出策略
 *
 * @author 王东旭
 */
public interface SortOutStrategy<I extends InMemoryDataPackage<? extends Comparable<?>>, O extends PackageMetadata> {

    /**
     * 导出
     *
     * @param inMemoryDataPackage 待导出数据包
     * @return 数据包元数据
     * @throws SortOutException 导出异常
     */
    O export(I inMemoryDataPackage) throws SortOutException;

}
