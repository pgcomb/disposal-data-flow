package com.github.pgcomb.ddf.map;

public interface Mapper<K extends Principal,V extends Payload> {

    MapPair<K,V> map(String in);
}
