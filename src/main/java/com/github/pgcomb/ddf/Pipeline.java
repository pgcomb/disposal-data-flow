package com.github.pgcomb.ddf;

/**
 * @author 王东旭
 * @date 2018-07-06
 */
public interface Pipeline<I,O> {

    O handle(I in);

    void next(O data);
}
