package com.github.pgcomb.ddf.common.packagee;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 内存数据包
 *
 * @param <T> 数据结构
 * @author 王东旭
 */
public class InMemoryDataPackage<T> implements PackageMetadata {

    private List<T> data = new ArrayList<>();

    private long start;

    private int serialNumber;

    public InMemoryDataPackage(long start, int serialNumber) {
        this.start = start;
        this.serialNumber = serialNumber;
    }

    public List<T> getData() {
        return data;
    }

    public void add(T ele) {
        data.add(ele);
    }

    @Override
    public long start() {
        return start;
    }

    @Override
    public long end() {
        return start + data.size() - 1;
    }

    @Override
    public int serialNumber() {
        return serialNumber;
    }

    @Override
    public int size() {
        return data.size();
    }

    public boolean isEmpty() {
        return CollectionUtils.isEmpty(data);
    }

    @Override
    public String toString() {
        return "InMemoryDataPackage{" +
                "dataSize=" + size() +
                ", start=" + start +
                ", serialNumber=" + serialNumber +
                '}';
    }
}
