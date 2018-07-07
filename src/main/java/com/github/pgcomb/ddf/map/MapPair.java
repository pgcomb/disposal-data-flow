package com.github.pgcomb.ddf.map;

import com.github.pgcomb.ddf.map.api.Payload;
import com.github.pgcomb.ddf.map.api.Principal;
import org.springframework.util.Assert;

/**
 * 键值映射对象
 * @param <K> 领主
 * @param <P> 负荷
 */
public class MapPair<K extends Principal,P extends Payload>
        implements Comparable<MapPair<K,P>> {

    private K principal;

    private P payload;

    public MapPair(K principal, P payload) {
        Assert.notNull(principal,"principal can not null");
        Assert.notNull(payload,"payload can not null");
        this.principal = principal;
        this.payload = payload;
    }

    public K getPrincipal() {
        return principal;
    }

    public void setPrincipal(K principal) {
        this.principal = principal;
    }

    public P getPayload() {
        return payload;
    }

    public void setPayload(P payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "MapPair{" +
                "api=" + principal +
                ", payload=" + payload +
                '}';
    }

    @Override
    public int compareTo(MapPair<K, P> o) {
        return getPrincipal().compareTo(o.getPrincipal());
    }
}
