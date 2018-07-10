package com.github.pgcomb.ddf.merge;

import com.github.pgcomb.ddf.common.Stopable;

import java.util.function.Consumer;

public interface MergeStrategy<C,D> extends Stopable {

    void accept(C g);

    void output(Consumer<C> o);

    D toObj(String readLine);

}