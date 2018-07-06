package com.github.pgcomb.ddf;

import com.github.pgcomb.ddf.bucket.FileBucket;
import com.github.pgcomb.ddf.bucket.PipeSucker;
import com.github.pgcomb.ddf.exception.SuckerException;

import java.io.File;

public class Main {
    public static void main(String[] args) throws SuckerException {
        FileBucket fileBucket = new FileBucket(new File("E:\\test"));

        fileBucket.addPipe(new PipeSucker<String>() {

            private int count = 0;
            private static final int end = 10;
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
                count++;
                if (count > end){
                    fileBucket.stop();
                }
                System.out.println(data);
            }
        });
        System.out.println("fileBucket.isStop()"+fileBucket.isStop());
        System.out.println("fileBucket.getPipes().get(0).isStop()"+fileBucket.getPipes().get(0).isStop());
        fileBucket.topple();
        System.out.println("fileBucket.isStop()"+fileBucket.isStop());
        System.out.println("fileBucket.getPipes().get(0).isStop()"+fileBucket.getPipes().get(0).isStop());
    }
}
