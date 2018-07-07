package com.github.pgcomb.ddf.sort;

import com.github.pgcomb.ddf.common.Conveyor;
import com.github.pgcomb.ddf.map.MapPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 王东旭
 * @date 2018-07-07
 */
public class PassiveSorterManager<T extends MapPair> implements Sorter<T>,Conveyor<List<T>> {

    private static final Logger log = LoggerFactory.getLogger(PassiveSorterManager.class);

    @Override
    public Collection<T> sort(List<T> bag) {
        Collections.sort(bag);
        return bag;
    }

    @Override
    public void transfer(List<T> data) {
        System.out.println(sort(data).stream().map(t -> t.getPrincipal().objValue()).collect(Collectors.toList()));
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isStop() {
        return false;
    }
}
