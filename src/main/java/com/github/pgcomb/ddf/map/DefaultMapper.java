package com.github.pgcomb.ddf.map;

import com.github.pgcomb.ddf.common.DataConvertor;
import com.github.pgcomb.ddf.map.api.Payload;
import com.github.pgcomb.ddf.map.api.Principal;

public class DefaultMapper<K extends Principal,V extends Payload> extends AbstractPipelineMapper<String, K, V> {

    private DataConvertor<String,MapPair<K,V>> dataConvertor;

    public DefaultMapper(DataConvertor<String, MapPair<K, V>> dataConvertor) {
        this.dataConvertor = dataConvertor;
    }

    @Override
    public String getName() {
        return "mapper";
    }

    @Override
    public MapPair<K, V> map(String in) {
        return dataConvertor.convert(in);
    }
}
