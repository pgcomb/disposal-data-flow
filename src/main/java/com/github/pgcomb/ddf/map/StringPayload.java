package com.github.pgcomb.ddf.map;

public class StringPayload implements Payload<String> {

    String value;

    public StringPayload(String value) {
        this.value = value;
    }

    @Override
    public String stringValue() {
        return value;
    }

    @Override
    public Payload<String> revivification(String string) {
        return new StringPayload(string);
    }
}
