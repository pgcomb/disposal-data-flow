package com.github.pgcomb.ddf.sort;

import java.util.Collection;
import java.util.List;

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
    Collection<T> sort(List<T> bag);

}
