package com.github.pgcomb.ddf.common;

import com.github.pgcomb.ddf.map.MapPair;
import com.github.pgcomb.ddf.map.api.Payload;
import com.github.pgcomb.ddf.map.api.Principal;

public abstract class StringMapPirConvertor<K extends Principal,V extends Payload> implements DataConvertor<String,MapPair<K,V>> {

    private String separator;

    public StringMapPirConvertor(String separator) {
        this.separator = separator;
    }

    @Override
    public MapPair<K, V> convert(String in) {
        String[] split = in.split(separator);
        return new MapPair<>(principal(split[0]),payload(split[1]),separator);
    }
     abstract K principal(String principal);

     abstract V payload(String payload);

}
