package com.github.pgcomb.ddf.sucker.reader;

import java.io.IOException;

public class NullReader implements Reader {

    @Override
    public String read() throws IOException {
        return null;
    }

    @Override
    public void close() throws IOException {

    }
}
