package com.github.pgcomb.ddf.sort;

import com.github.pgcomb.ddf.common.Conveyor;
import com.github.pgcomb.ddf.common.Inform;
import com.github.pgcomb.ddf.common.packagee.InMemoryDataPackage;
import com.github.pgcomb.ddf.common.packagee.PackageMetadata;
import com.github.pgcomb.ddf.common.packagee.StreamDataPackage;
import com.github.pgcomb.ddf.sort.strategy.SortOutStrategy;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author 王东旭
 * @date 2018-07-07
 */
@SuppressWarnings("unchecked")
public class PassiveSorterManager<T extends Comparable<?>> extends Conveyor<InMemoryDataPackage<T>> implements Sorter<T> {

    private static final Logger log = LoggerFactory.getLogger(PassiveSorterManager.class);

    private static final int CORE_POOL_SIZE = 2;

    private static final int MAXIMUM_POOL_SIZE = 2;

    private static final int KEEP_ACTIVE_TIME = 2;

    private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

    private ThreadPoolExecutor executor;

    private Consumer<Void> stopSortManagerThread;

    private void init() {
        //初始化设置停止排序器的线程
        StopSortManager stopSortManager = new StopSortManager(this);
        stopSortManagerThread = t -> new Thread(stopSortManager,stopSortManager.getName()).start();
    }
    /**
     * 排序输出策略
     */
    private SortOutStrategy<InMemoryDataPackage<T>, PackageMetadata> sortOutStrategy;

    /**
     * 排序完成通知
     */
    private Inform<StreamDataPackage> inform;

    public PassiveSorterManager(SortOutStrategy<InMemoryDataPackage<T>, PackageMetadata> sortOutStrategy, Inform<StreamDataPackage> inform) {
        this.sortOutStrategy = sortOutStrategy;
        this.inform = inform;
        init();
    }
    private void initPool() {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("sorter-pool-%d").build();
        executor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ACTIVE_TIME, TIME_UNIT,
                new LinkedBlockingQueue<>(1), namedThreadFactory, (r, executor1) -> {
            if (!CollectionUtils.isEmpty(executor1.getQueue())) {
                try {
                    Thread.sleep(500L);
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
    public InMemoryDataPackage<T> sort(InMemoryDataPackage<T> bag) {
        if (!isStop() && executor == null){
            initPool();
        }
        executor.execute(new SortWorker(bag,new SortCallBack(sortOutStrategy,inform),stopSortManagerThread));
        return bag;
    }

    @Override
    public void transfer(InMemoryDataPackage<T> data) {
        sort(data);
    }

    @Override
    public void stopEvent() {
        //停止线程池
        poolStop();
        //结束通知
        log.debug("PassiveSorterManager#stopEvent#inform.stopForward()");
        log.info("[sort manager] stop");
        inform.stopForward();
    }

    private void poolStop(){
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
            try {
                while (!executor.awaitTermination(2, TimeUnit.SECONDS)) {
                    log.info("wait for the thread pool task to end.");
                }
            } catch (InterruptedException e) {
                log.error("close pool is error!", e);
                Thread.currentThread().interrupt();
            }
        }
    }
}
