package com.github.pgcomb.ddf.merge;

import com.github.pgcomb.ddf.common.DataConvertor;
import com.github.pgcomb.ddf.map.MapPair;
import com.github.pgcomb.ddf.map.api.Payload;
import com.github.pgcomb.ddf.map.api.Principal;

public class KVMergeStrategy<K extends Principal, V extends Payload> extends AbstractKVTierMergeStrategy<K,V>{

    private DataConvertor<String,MapPair<K,V>> dataConvertor;

    public KVMergeStrategy(DataConvertor<String, MapPair<K, V>> dataConvertor) {
        this.dataConvertor = dataConvertor;
    }

    @Override
    public MapPair<K, V> toObj(String readLine) {
        return dataConvertor.convert(readLine);
    }
}
