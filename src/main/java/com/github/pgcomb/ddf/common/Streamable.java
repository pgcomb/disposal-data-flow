package com.github.pgcomb.ddf.common;

import java.io.FileNotFoundException;
import java.io.InputStream;

public interface Streamable {

    InputStream inputStream() throws FileNotFoundException;
}
