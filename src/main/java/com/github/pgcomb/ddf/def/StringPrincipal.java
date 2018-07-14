package com.github.pgcomb.ddf.def;

import com.github.pgcomb.ddf.map.AbstractPrincipal;
import com.github.pgcomb.ddf.map.api.Principal;

public class StringPrincipal extends AbstractPrincipal<String> {

    public StringPrincipal( String value) {
        super(value);
    }

    @Override
    public boolean same(Principal<String> oneself, Principal<String> other) {
        return oneself.objValue().equals(other.objValue());
    }

    @Override
    public String obj2str(String obj) {
        return obj;
    }

    @Override
    public String str2obj(String string) {
        return string;
    }

    @Override
    public int compare(String oneself, String other) {
        return oneself.compareTo(other);
    }
}
