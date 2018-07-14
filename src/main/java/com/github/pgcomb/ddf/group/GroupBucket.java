package com.github.pgcomb.ddf.group;


import com.github.pgcomb.ddf.bucket.AbstractSuckerBucket;
import com.github.pgcomb.ddf.bucket.sucker.Sucker;
import com.github.pgcomb.ddf.common.DataConvertor;
import com.github.pgcomb.ddf.common.StopForward;
import com.github.pgcomb.ddf.common.packagee.StreamDataPackage;
import com.github.pgcomb.ddf.map.MapPair;
import com.github.pgcomb.ddf.map.api.Payload;
import com.github.pgcomb.ddf.map.api.Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class GroupBucket<K extends Principal, V extends Payload> extends AbstractSuckerBucket<List<MapPair<K,V>>> implements Group<StreamDataPackage> {

    private static final Logger log = LoggerFactory.getLogger(GroupBucket.class);

    private StreamDataPackage streamDataPackage;

    private List<MapPair<K,V>> lastData = new ArrayList<>();

    private DataConvertor<String,MapPair<K,V>> dataConvertor;

    public GroupBucket(DataConvertor<String, MapPair<K, V>> dataConvertor) {
        this.dataConvertor = dataConvertor;
    }

    @Override
    public synchronized void inject(StreamDataPackage string) {
        streamDataPackage= string;
        this.topple();
    }

    @Override
    protected void flow(Sucker<List<MapPair<K, V>>>  shake, StopForward stopForward) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(streamDataPackage.inputStream()))) {
            String s;
            while ((s = br.readLine()) != null && !stopForward.isStop()) {
                out(s,shake);
            }
        } catch (IOException e) {
            log.error("io error", e);
            stopForward.stopForward();
        }
    }
    public void out(String string,Sucker<List<MapPair<K, V>>> sucker) {
        MapPair<K, V> kvMapPair = dataConvertor.convert(string);
        if (kvMapPair == null) {
            return;
        }
        if (!CollectionUtils.isEmpty(lastData) && !kvMapPair.getPrincipal().same(lastData.get(lastData.size()-1).getPrincipal())){
            List<MapPair<K,V>> data = lastData;
            lastData = new ArrayList<>();
            sucker.suck(data);
        }else {
            lastData.add(kvMapPair);
        }
    }
}
