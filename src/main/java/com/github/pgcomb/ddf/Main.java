package com.github.pgcomb.ddf;

import com.github.pgcomb.ddf.bucket.FileBucket;
import com.github.pgcomb.ddf.bucket.sucker.DefaultPipelineSucker;
import com.github.pgcomb.ddf.bucket.sucker.PipeSucker;
import com.github.pgcomb.ddf.exception.SuckerException;

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
            FileBucket fileBucket = new FileBucket(new File("E:\\test"));
            DefaultPipelineSucker defaultPipelineSucker3 = new DefaultPipelineSucker("3");
            defaultPipelineSucker3.addPipeline(new DefaultPipelineSucker("4"));
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
