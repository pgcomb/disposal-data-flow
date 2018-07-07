package com.github.pgcomb.ddf.def;

import com.github.pgcomb.ddf.map.AbstractPrincipal;

public class StringPrincipal extends AbstractPrincipal<String> {

    public StringPrincipal( String value) {
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

    @Override
    public boolean same(String oneself, String other) {
        return oneself.equals(other);
    }

    @Override
    public int compare(String oneself, String other) {
        return oneself.compareTo(other);
    }

    public static void main(String[] args) {
        StringPrincipal stringPrincipal = new StringPrincipal(null);
    }
}
