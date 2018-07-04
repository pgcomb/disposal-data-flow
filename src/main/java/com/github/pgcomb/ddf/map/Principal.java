package com.github.pgcomb.ddf.map;

public interface Principal<T> extends Comparable<T>,Groupable<T>,Stringable<Principal<T>> {

    T getPrincipal();
}
