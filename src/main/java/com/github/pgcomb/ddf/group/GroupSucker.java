package com.github.pgcomb.ddf.group;

import com.github.pgcomb.ddf.bucket.sucker.AbstractAbstractPipelineSucker;
import com.github.pgcomb.ddf.map.MapPair;
import com.github.pgcomb.ddf.map.api.Payload;
import com.github.pgcomb.ddf.map.api.Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;

public class GroupSucker<K extends Principal,V extends Payload> extends AbstractAbstractPipelineSucker<List<MapPair<K,V>>,Object> {

    private static final Logger log = LoggerFactory.getLogger(GroupSucker.class);

    private File file;

    private BufferedWriter bufferedWriter;

    public GroupSucker(File file) {
        this.file = file;
    }

    @Override
    public String getName() {
        return "group";
    }

    @Override
    public Object handlePipe(List<MapPair<K, V>> in) {
        if (bufferedWriter == null){
            try {
                file.getParentFile().mkdirs();
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            } catch (FileNotFoundException e) {
                log.error("out error,lose data",e);
                return null;
            }
        }
        try {
            bufferedWriter.write(in.get(0).getPrincipal().strValue()+in.get(0).getSeparator()+in.size());
            bufferedWriter.newLine();
        } catch (IOException e) {
            log.error("write result error",e);
        }
        return in;
    }

    @Override
    public void stopEvent() {
        try {
            bufferedWriter.close();
        } catch (IOException e) {
            log.error("close writer error",e);
        }
    }
}
