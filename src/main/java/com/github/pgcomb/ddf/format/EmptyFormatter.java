package com.github.pgcomb.ddf.format;

public class EmptyFormatter implements Formatter {
    @Override
    public String format(String in) {
        return in;
    }
}
