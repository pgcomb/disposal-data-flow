package com.github.pgcomb.ddf.map;

public class StringPrincipal implements Principal<String> {

    public StringPrincipal(String value) {
        this.value = value;
    }

    private String value;

    @Override
    public boolean same(String stringPrincipal) {
        return this.getPrincipal().equals(stringPrincipal);
    }

    @Override
    public String stringValue() {
        return getPrincipal();
    }

    @Override
    public Principal<String> revivification(String string) {
        return new StringPrincipal(string);
    }

    @Override
    public int compareTo(String o) {
        return this.getPrincipal().compareTo(o);
    }

    @Override
    public String getPrincipal() {
        return value;
    }
}
