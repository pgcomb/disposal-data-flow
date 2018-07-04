package com.github.pgcomb.ddf.map;

public class SplitMapper implements Mapper<StringPrincipal,StringPayload> {

    private String separator;

    public SplitMapper(String separator) {
        this.separator = separator;
    }

    @Override
    public MapPair<StringPrincipal, StringPayload> map(String in) {
        String[] split = in.split(separator);
        return null;
    }
}
