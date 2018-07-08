package com.github.pgcomb.ddf.merge;

import com.github.pgcomb.ddf.common.Stopable;

import java.util.function.Consumer;

public interface MergeStrategy<D> extends Consumer<TierDataStream>,Stopable {

    D toObj(String readLine);

}