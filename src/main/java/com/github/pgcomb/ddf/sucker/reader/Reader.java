package com.github.pgcomb.ddf.sucker.reader;

import java.io.IOException;

public interface Reader {

    String read() throws IOException;

    void close() throws IOException;
}
