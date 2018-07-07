package com.github.pgcomb.ddf.def;

import com.github.pgcomb.ddf.map.AbstractPipelineMapper;
import com.github.pgcomb.ddf.map.MapPair;
import com.github.pgcomb.ddf.map.api.Payload;
import com.github.pgcomb.ddf.map.api.Principal;

public class SplitMapper extends AbstractPipelineMapper<String,Principal,Payload> {

    private String separator;

    private static final String NAME = "mapper";
    public SplitMapper(String separator) {
        this.separator = separator;
    }

    @Override
    public MapPair<Principal, Payload> map(String in) {
        String[] split = in.split(separator, 2);
        StringPrincipal stringPrincipal;
        StringPayload stringPayload;
        if (split.length == 0) {
            stringPrincipal = new StringPrincipal("");
            stringPayload = new StringPayload("");
        } else if (split.length == 1) {
            stringPrincipal = new StringPrincipal(split[0]);
            stringPayload = new StringPayload("");
        } else {
            stringPrincipal = new StringPrincipal(split[0]);
            stringPayload = new StringPayload(split[1]);
        }
        System.out.println(new MapPair<>(stringPrincipal, stringPayload));
        return new MapPair<>(stringPrincipal, stringPayload);
    }

    @Override
    public String getName() {
        return NAME;
    }
}
