package com.github.pgcomb.ddf.sort;

import com.github.pgcomb.ddf.common.packagee.InMemoryDataPackage;

/**
 * 排序器
 * @author 王东旭
 * @date 2018-07-07
 */
public interface Sorter<T extends Comparable> {

    /**
     * 排序方法
     * @param bag 排序数据
     * @return 排序后数据
     */
    InMemoryDataPackage<T> sort(InMemoryDataPackage<T> bag);

}
