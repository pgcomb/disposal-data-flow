package com.github.pgcomb.ddf;

import com.github.pgcomb.ddf.bucket.sucker.PipeSucker;
import com.github.pgcomb.ddf.def.DefaultPipelineSucker;
import com.github.pgcomb.ddf.def.FileBucket;
import com.github.pgcomb.ddf.def.SplitMapper;
import com.github.pgcomb.ddf.exception.SuckerException;
import com.github.pgcomb.ddf.map.MapPair;
import com.github.pgcomb.ddf.map.api.Payload;
import com.github.pgcomb.ddf.map.api.Principal;
import com.github.pgcomb.ddf.press.PipePacking;
import com.github.pgcomb.ddf.sort.PassiveSorterManager;

import java.io.File;

public class Main {
    public static void main(String[] args) throws SuckerException {

        test2();
    }
    public static void test1(){
        {
            FileBucket fileBucket = new FileBucket(new File("E:\\test"));

            fileBucket.addPipe(new PipeSucker<String>() {

                private int count = 0;
                private static final int end = 4;
                private boolean stop;
                @Override
                public void stop() {
                    stop = true;
                    System.out.println("sucker stop");
                }

                @Override
                public boolean isStop() {
                    return stop;
                }

                @Override
                public void suck(String data) {
                    System.out.println(count +":"+data);
                    if (count > end){
                        fileBucket.stop();
                    }
                    count++;
                }
            });
            System.out.println("fileBucket.isStop()"+fileBucket.isStop());
            System.out.println("fileBucket.getPipes().get(0).isStop()"+fileBucket.getPipes().get(0).isStop());
            fileBucket.topple();
            System.out.println("fileBucket.isStop()"+fileBucket.isStop());
            System.out.println("fileBucket.getPipes().get(0).isStop()"+fileBucket.getPipes().get(0).isStop());
        }
    }
    public static void test2(){
        {
            FileBucket fileBucket = new FileBucket(new File("D:\\test"));
            DefaultPipelineSucker defaultPipelineSucker3 = new DefaultPipelineSucker("3");
            defaultPipelineSucker3.addPipeline(new DefaultPipelineSucker("4"));

            SplitMapper splitMapper = new SplitMapper("-");
            PipePacking<MapPair<Principal,Payload>> mapPairPipePacking = new PipePacking<>(new PassiveSorterManager<>());

            splitMapper.addPipeline(mapPairPipePacking);
            defaultPipelineSucker3.addPipeline(splitMapper);

            fileBucket.addPipe(new DefaultPipelineSucker("1"));
            fileBucket.addPipe(new DefaultPipelineSucker("2"));
            fileBucket.addPipe(defaultPipelineSucker3);
            System.out.println("fileBucket.isStop()"+fileBucket.isStop());
            System.out.println("fileBucket.getPipes().get(0).isStop()"+fileBucket.getPipes().get(0).isStop());
            fileBucket.topple();
            System.out.println("fileBucket.isStop()"+fileBucket.isStop());
            System.out.println("fileBucket.getPipes().get(0).isStop()"+fileBucket.getPipes().get(0).isStop());
        }
    }
}
