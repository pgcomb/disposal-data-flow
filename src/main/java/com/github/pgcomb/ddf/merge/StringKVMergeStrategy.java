package com.github.pgcomb.ddf.merge;

import com.github.pgcomb.ddf.def.StringPayload;
import com.github.pgcomb.ddf.def.StringPrincipal;
import com.github.pgcomb.ddf.map.MapPair;

/**
 * @deprecated {@link KVMergeStrategy}
 */
@Deprecated
public class StringKVMergeStrategy extends AbstractKVTierMergeStrategy<StringPrincipal,StringPayload> {

    private String separator;

    public StringKVMergeStrategy(String separator) {
        this.separator = separator;
    }

    @Override
    public MapPair<StringPrincipal, StringPayload> toObj(String readLine) {
        String[] split = readLine.split(separator);
        return new MapPair<>(new StringPrincipal(split[0]),new StringPayload(split[1]),separator);
    }
}
