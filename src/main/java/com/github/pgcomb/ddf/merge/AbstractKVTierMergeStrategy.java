package com.github.pgcomb.ddf.merge;

import com.github.pgcomb.ddf.common.StopFeedBack;
import com.github.pgcomb.ddf.map.MapPair;
import com.github.pgcomb.ddf.map.api.Payload;
import com.github.pgcomb.ddf.map.api.Principal;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public abstract class AbstractKVTierMergeStrategy<K extends Principal,V extends Payload> implements MergeStrategy<TierDataStream,MapPair<K,V>> {

    private static final Logger log = LoggerFactory.getLogger(AbstractKVTierMergeStrategy.class);

    private static final int CORE_POOL_SIZE = 5;

    private static final int MAXIMUM_POOL_SIZE = 10;

    private static final int KEEP_ACTIVE_TIME = 2;

    private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

    private ThreadPoolExecutor executor;

    private boolean stop = false;

    private Consumer<Void> stopSortManagerThread;

    private StopFeedBack stopFeedBack;

    private Consumer<TierDataStream> exporter;

    public AbstractKVTierMergeStrategy() {
        init();
    }

    private void init() {
        //初始化设置停止排序器的线程
        stopSortManagerThread = t -> new Thread(AbstractKVTierMergeStrategy.this::stop, "merge stop thread").start();
    }
    private void initPool() {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("sorter-pool-%d").build();
        executor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ACTIVE_TIME, TIME_UNIT,
                new LinkedBlockingQueue<>(1), namedThreadFactory, (r, executor1) -> {
            if (!CollectionUtils.isEmpty(executor1.getQueue())) {
                try {
                    Thread.sleep(50L);
                } catch (InterruptedException e) {
                    log.error("sleep is false", e);
                    Thread.currentThread().interrupt();
                }
            } else {
                executor1.execute(r);
            }
        });
    }

    @Override
    public void stopBack() {
        stopFeedBack.stopBack();
        this.stop = true;
    }

    @Override
    public void preStop(StopFeedBack stopFeedBack) {
        this.stopFeedBack = stopFeedBack;
    }

    @Override
    public void stopForward() {

    }

    @Override
    public boolean isStop() {
        return stop;
    }

    @Override
    public void accept(TierDataStream tierDataStream) {
        if (executor == null || executor.isShutdown()){
            initPool();
        }
        executor.submit(new Work(stopSortManagerThread,tierDataStream,exporter));
    }

    @Override
    public void output(Consumer<TierDataStream> o) {
        exporter = o;
    }

    static class Work implements Runnable{

        private static final Logger log = LoggerFactory.getLogger(Work.class);

        private Consumer<Void> stopThread;

        private TierDataStream tierDataStream;

        private Consumer<TierDataStream> output;

        private Work(Consumer<Void> stopThread, TierDataStream tierDataStream, Consumer<TierDataStream> output) {
            this.stopThread = stopThread;
            this.tierDataStream = tierDataStream;
            this.output = output;
        }

        @Override
        public void run() {
            try {

                //TODO

            }catch (Exception e){
                log.error("merge error",e);
                stopThread.accept(null);
            }
        }
    }
}
