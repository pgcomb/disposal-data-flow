package com.github.pgcomb.ddf.map;

import com.github.pgcomb.ddf.common.Stringable;
import com.github.pgcomb.ddf.map.api.Payload;
import org.springframework.util.Assert;

/**
 * @author 王东旭
 * @date 2018-07-07
 */
public abstract class AbstractPayload<T> implements Payload<T> {

    private T payload;

    public AbstractPayload( T value) {
        Assert.notNull(value,"payload can not null");
        this.payload = value;
    }

    public AbstractPayload( String strValue, Stringable<T> stringable){
        Assert.notNull(strValue,"strValue can not null");
        Assert.notNull(stringable,"stringable can not null");
        this.payload = stringable.str2obj(strValue);
    }
    @Override
    public final T objValue() {
        return payload;
    }

    @Override
    public final String strValue() {
        return obj2str(payload);
    }

    @Override
    public String toString() {
        return "AbstractPayload{" +
                "payload=" + payload +
                '}';
    }
}
