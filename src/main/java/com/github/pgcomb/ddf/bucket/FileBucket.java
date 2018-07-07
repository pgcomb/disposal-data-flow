package com.github.pgcomb.ddf.bucket;

import com.github.pgcomb.ddf.bucket.sucker.Sucker;
import com.github.pgcomb.ddf.common.Stoppable;
import com.github.pgcomb.ddf.sucker.FileSucker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author 王东旭
 * @date 2018-07-06
 */
public class FileBucket extends AbstractSuckerBucket<String> {

    private static final Logger log = LoggerFactory.getLogger(FileSucker.class);

    private File file;

    private String charsetName;

    public FileBucket(File file,String charsetName) {
        this.file = file;
        this.charsetName = charsetName;
    }

    public FileBucket(File file){
        this(file,"gbk");
    }
    @Override
    protected void flow(Sucker<String> consumer, Stoppable stop) {
        Optional.ofNullable(file.listFiles()).ifPresent(f ->
                Arrays.stream(f).filter(File::isFile).forEach(fi -> {
                    if (stop.isStop()) {
                        return;
                    }
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fi),charsetName ))) {
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
