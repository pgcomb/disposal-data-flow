package com.github.pgcomb.ddf.map;

public class MapPair<K extends Principal,P extends Payload> {

    private K principal;

    private P payload;

    public MapPair(K principal, P payload) {
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
                "principal=" + principal +
                ", payload=" + payload +
                '}';
    }
}
