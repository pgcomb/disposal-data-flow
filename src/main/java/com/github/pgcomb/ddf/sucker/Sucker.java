package com.github.pgcomb.ddf.sucker;

import com.github.pgcomb.ddf.exception.SuckerException;

public interface Sucker {

    String next() throws SuckerException;

    long getCount();

}
