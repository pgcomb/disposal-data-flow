package com.github.pgcomb.ddf.def;

import com.github.pgcomb.ddf.map.AbstractPayload;

public class StringPayload extends AbstractPayload<String> {

    public StringPayload(String value) {
        super(value);
    }

    @Override
    public String obj2str(String obj) {
        return obj;
    }

    @Override
    public String str2obj(String string) {
        return string;
    }
}
