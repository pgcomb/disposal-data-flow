package com.github.pgcomb.ddf.def;

import com.github.pgcomb.ddf.bucket.sucker.AbstractAbstractPipelineSucker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 简单的一个吸取管道
 * @author 王东旭
 */
public class DefaultPipelineSucker extends AbstractAbstractPipelineSucker<String,String> {

    private static final Logger log = LoggerFactory.getLogger(DefaultPipelineSucker.class);

    private String name;

    public DefaultPipelineSucker(String name) {
        this.name = name;
    }

    @Override
    public String handlePipe(String in) {
        if (getName().equals("2")){
            stop();
        }
        log.debug("{}:{}",getName(),in);
        return in;
    }

    @Override
    public String getName() {
        return name;
    }
}
