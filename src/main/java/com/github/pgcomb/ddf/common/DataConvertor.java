package com.github.pgcomb.ddf.common;

public interface DataConvertor<I,O> {

    O convert(I in);
}
