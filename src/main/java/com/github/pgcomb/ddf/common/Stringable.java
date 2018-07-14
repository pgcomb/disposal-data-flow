package com.github.pgcomb.ddf.common;

public interface Stringable<T> {

    /**
     * 对象字符串转换
     * @param obj 对象
     * @return 字符串
     */
    String obj2str(T obj);

    /**
     * 字符串转对象
     * @param string 字符串
     * @return 对象
     */
    T str2obj(String string);

    /**
     * 获取排序字段
     * @return 返回数据
     */
    T objValue();

    /**
     * 获取他的字符值
     * @return 字符串
     */
    String strValue();
}
