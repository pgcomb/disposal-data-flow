package com.github.pgcomb.ddf.common;

import java.util.function.Consumer;

/**
 * 消息通知
 * @param <T> 消息体
 */
public interface Inform<T> extends Stopable,Consumer<T> {
}
