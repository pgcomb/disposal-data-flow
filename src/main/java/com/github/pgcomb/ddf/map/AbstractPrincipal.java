package com.github.pgcomb.ddf.map;

import com.github.pgcomb.ddf.common.Stringable;
import com.github.pgcomb.ddf.map.api.Principal;
import org.springframework.util.Assert;

/**
 * @author 王东旭
 * @date 2018-07-07
 */
public abstract class AbstractPrincipal<T> implements Principal<T> {

    private T principal;


    public AbstractPrincipal(T principal) {
        Assert.notNull(principal,"principal can not null");
        this.principal = principal;
    }

    public AbstractPrincipal(String principal, Stringable<T> stringable) {
        Assert.notNull(principal,"principal can not null");
        Assert.notNull(principal,"stringable can not null");
        stringable.str2obj(principal);
    }

    @Override
    public final T objValue() {
        return principal;
    }

    @Override
    public final String strValue() {
        return this.obj2str(principal);
    }

    @Override
    public final boolean same(T t) {
        return same(principal,t);
    }

    /**
     * 相同比较
     * @param oneself 自己
     * @param other 其他
     * @return 是否相等
     */
    public abstract boolean same(T oneself,T other);

    @Override
    public int compareTo(Principal<T> o) {
        return compare(principal,o.objValue());
    }

    /**
     * 大小比较
     * @param oneself 自己
     * @param other 对比的对象
     * @return 1 自己大
     *         0 相等
     *        -1 自己小
     */
    public abstract int compare(T oneself,T other);

    @Override
    public String toString() {
        return "AbstractPrincipal{" +
                "principal=" + principal +
                '}';
    }
}
