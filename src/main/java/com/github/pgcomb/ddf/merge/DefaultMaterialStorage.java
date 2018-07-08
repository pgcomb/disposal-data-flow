package com.github.pgcomb.ddf.merge;

import com.github.pgcomb.ddf.common.packagee.StreamDataPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DefaultMaterialStorage<S extends StreamDataPackage, T extends TierDataStream<S>> implements MaterialStorage<S, T> {

    private static final Logger log = LoggerFactory.getLogger(DefaultMaterialStorage.class);

    private Map<Integer, List<S>> preparePool = new ConcurrentHashMap<>();

    private Map<Integer, List<S>> executingPool = new ConcurrentHashMap<>();

    private boolean stop = false;

    @Override
    public Consumer<T> overflow() {
        return null;
    }

    @Override
    public Consumer<S> end() {
        return null;
    }

    @Override
    public void stop() {
        stop = true;
        examine(-1);
    }

    @Override
    public void destroy() {
        preparePool.clear();
        executingPool.clear();
    }

    @Override
    public void back(T materials) {

    }

    @Override
    public void add(S t) {

    }

    private synchronized void examine(int tier) {
        if (tier == -1) {
            List<Map.Entry<Integer, List<S>>> collect =
                    preparePool.entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toList());
            for (int i = 0; i < collect.size(); i++) {
                List<S> integerListEntry = collect.get(i).getValue();
                if (CollectionUtils.isEmpty(integerListEntry)){
                    if (i == collect.size()-1 && collect.size() ==1){
                        end().accept(integerListEntry.get(0));
                    } else {

                    }
                }
            }
        }
    }
}
