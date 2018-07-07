package com.github.pgcomb.ddf.bucket.sucker;

import com.github.pgcomb.ddf.common.Nameable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultPipelineSucker extends AbstractPipelineSucker<String,String> implements Nameable {

    private static final Logger log = LoggerFactory.getLogger(DefaultPipelineSucker.class);

    private String name;

    public DefaultPipelineSucker(String name) {
        this.name = name;
    }

    @Override
    public String handlePipe(String in) {
        log.info("{}:{}",getName(),in);
        if (name.equals("3")&&in.equals("file2-1")){
            stop();
        }
        return in;
    }

    @Override
    public String getName() {
        return name;
    }
}
