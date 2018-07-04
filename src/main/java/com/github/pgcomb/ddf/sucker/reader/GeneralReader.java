package com.github.pgcomb.ddf.sucker.reader;

import java.io.BufferedReader;
import java.io.IOException;

public class GeneralReader implements Reader {
    
    private BufferedReader bufferedReader;
    
    public GeneralReader(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
    }

    @Override
    public String read() throws IOException {
        return bufferedReader.readLine();
    }

    @Override
    public void close() throws IOException {
        bufferedReader.close();
    }
}
