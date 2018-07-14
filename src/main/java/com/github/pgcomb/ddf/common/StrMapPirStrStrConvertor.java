package com.github.pgcomb.ddf.common;

import com.github.pgcomb.ddf.def.StringPayload;
import com.github.pgcomb.ddf.def.StringPrincipal;

public class StrMapPirStrStrConvertor extends StringMapPirConvertor<StringPrincipal,StringPayload> {
    public StrMapPirStrStrConvertor(String separator) {
        super(separator);
    }

    @Override
    StringPrincipal principal(String principal) {
        return new StringPrincipal(principal);
    }

    @Override
    StringPayload payload(String payload) {
        return new StringPayload(payload);
    }
}
