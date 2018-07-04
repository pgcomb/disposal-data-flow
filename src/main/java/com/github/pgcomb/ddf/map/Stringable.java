package com.github.pgcomb.ddf.map;

public interface Stringable<T> {

    String stringValue();

    T revivification(String string);
}
