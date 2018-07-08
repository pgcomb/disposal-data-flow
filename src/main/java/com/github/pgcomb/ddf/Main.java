package com.github.pgcomb.ddf;

import com.github.pgcomb.ddf.bucket.sucker.PipeSucker;
import com.github.pgcomb.ddf.common.Inform;
import com.github.pgcomb.ddf.common.StopFeedBack;
import com.github.pgcomb.ddf.common.packagee.FileDatePackage;
import com.github.pgcomb.ddf.common.packagee.InMemoryDataPackage;
import com.github.pgcomb.ddf.common.packagee.StreamDataPackage;
import com.github.pgcomb.ddf.def.DefaultPipelineSucker;
import com.github.pgcomb.ddf.def.FileBucket;
import com.github.pgcomb.ddf.def.SplitMapper;
import com.github.pgcomb.ddf.map.MapPair;
import com.github.pgcomb.ddf.map.api.Payload;
import com.github.pgcomb.ddf.map.api.Principal;
import com.github.pgcomb.ddf.press.PipePacking;
import com.github.pgcomb.ddf.sort.PassiveSorterManager;
import com.github.pgcomb.ddf.sort.strategy.MapPairSortFileOutStrategy;
import com.github.pgcomb.ddf.sort.strategy.SortOutStrategy;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws Exception {

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
                public void stopForward() {
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
                        fileBucket.stopForward();
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

            DefaultPipelineSucker defaultPipelineSucker2 = new DefaultPipelineSucker("2");
            defaultPipelineSucker2.addPipeline(new DefaultPipelineSucker("5"));

            SplitMapper splitMapper = new SplitMapper("-");
            SortOutStrategy<InMemoryDataPackage<MapPair<Principal, Payload>>, FileDatePackage> mapPairSortFileOutStrategy = new MapPairSortFileOutStrategy();

            PassiveSorterManager<MapPair<Principal, Payload>> passiveSorterManager = new PassiveSorterManager(mapPairSortFileOutStrategy, new NullInform());

            defaultPipelineSucker3.addPipeline(new DefaultPipelineSucker("4"));

            PipePacking<MapPair<Principal,Payload>> mapPairPipePacking = new PipePacking<>(passiveSorterManager);

            splitMapper.addPipeline(mapPairPipePacking);
            defaultPipelineSucker3.addPipeline(splitMapper);

            fileBucket.addPipe(new DefaultPipelineSucker("1"));
            fileBucket.addPipe(defaultPipelineSucker2);

            fileBucket.addPipe(defaultPipelineSucker3);
            fileBucket.topple();

        }
    }
    public static void test3() throws IOException {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("E:\\test\\a.txt"))) {
            Random random = new Random();
            for (int i = 0; i < 5000000; i++) {
                bufferedWriter.write(random.nextInt(10000)+"-U"+random.nextInt(10000)+"U");
                bufferedWriter.newLine();
            }
        }
    }
}
 class NullInform implements Inform<StreamDataPackage>{

     @Override
     public void stopBack() {

     }

     @Override
     public void preStop(StopFeedBack stopFeedBack) {

     }

     @Override
     public void stopForward() {

     }

     @Override
     public boolean isStop() {
         return false;
     }

     @Override
     public void accept(StreamDataPackage streamDataPackage) {

     }
 }