package com.github.pgcomb.ddf.common;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Streamable {

    InputStream inputStream() throws FileNotFoundException;

    OutputStream outputStream() throws FileNotFoundException;
}
