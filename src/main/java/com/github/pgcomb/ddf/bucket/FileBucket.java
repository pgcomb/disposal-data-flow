package com.github.pgcomb.ddf.bucket;

import com.github.pgcomb.ddf.Stoppable;
import com.github.pgcomb.ddf.sucker.FileSucker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author 王东旭
 * @date 2018-07-06
 */
public class FileBucket extends AbstractSuckerBucket<String> {

    private static final Logger log = LoggerFactory.getLogger(FileSucker.class);

    private File file;

    public FileBucket(File file) {
        this.file = file;
    }

    @Override
    protected void flow(Sucker<String> consumer, Stoppable stop) {
        Optional.ofNullable(file.listFiles()).ifPresent(f ->
                Arrays.stream(f).filter(File::isFile).forEach(file -> {
                    if (stop.isStop()) {
                        return;
                    }
                    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                        String s;
                        while ((s = br.readLine()) != null && !stop.isStop()) {
                            consumer.suck(s);
                        }
                    } catch (IOException e) {
                        log.error("io error",e);
                        stop.isStop();
                    }
                })
        );
    }

    public File getFile() {
        return file;
    }
}
