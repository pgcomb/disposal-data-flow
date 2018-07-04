package com.github.pgcomb.ddf.sucker;

import com.github.pgcomb.ddf.exception.SuckerException;
import com.github.pgcomb.ddf.sucker.reader.GeneralReader;
import com.github.pgcomb.ddf.sucker.reader.NullReader;
import com.github.pgcomb.ddf.sucker.reader.Reader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FileSucker implements Sucker {

    private static final Logger log = LoggerFactory.getLogger(FileSucker.class);

    private String fileCoding;

    private List<File> inputs;

    private int currentFile = 0;

    private Reader currentIn = new NullReader();

    private long count = 0;

    public FileSucker(File f, String coding) {
        this.fileCoding = coding;
        init(f);
    }

    public FileSucker(File f) {
        this(f,"gbk");
    }

    private void init(File f)  {
       if (f.isFile()){
           inputs = Collections.singletonList(f);
       }else {
           File[] files = f.listFiles(File::isFile);
           if (files == null)
               inputs = new ArrayList<>();
           else
               inputs = Arrays.asList(files);
       }
        Assert.notEmpty(inputs,"There should be at least one file");
    }
    public String next() throws SuckerException {
        try {
            String read;
            if (currentIn != null){
                read = currentIn.read();
                while (read == null && currentIn != null){
                    nextInput();
                    if (currentIn != null)
                        read = currentIn.read();
                }
                count++;
                return read;
            }else {
                return null;
            }
        } catch (IOException e) {
            throw new SuckerException("read file error",e);
        }
    }

    public long getCount() {
        return count;
    }

    private void nextInput() throws SuckerException {

        try {
            if (currentIn != null) currentIn.close();
            if (inputs.size() <= currentFile){
                currentIn = null;
            } else {
                currentIn = new GeneralReader(new BufferedReader(new InputStreamReader(new FileInputStream(inputs.get(currentFile)),fileCoding)));
                log.info("read file[{}]",inputs.get(currentFile));
                currentFile++;
            }
        } catch (IOException e) {
            throw new SuckerException(String.format("read file [%s] error",inputs.get(currentFile).getName()),e);
        }
    }
}
