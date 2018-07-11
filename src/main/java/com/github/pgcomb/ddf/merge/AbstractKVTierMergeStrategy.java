package com.github.pgcomb.ddf.merge;

import com.github.pgcomb.ddf.common.StopFeedBack;
import com.github.pgcomb.ddf.common.packagee.FileDatePackage;
import com.github.pgcomb.ddf.map.MapPair;
import com.github.pgcomb.ddf.map.api.Payload;
import com.github.pgcomb.ddf.map.api.Principal;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public abstract class AbstractKVTierMergeStrategy<K extends Principal, V extends Payload> implements MergeStrategy<TierDataStream, MapPair<K, V>> {

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
        if (executor == null || executor.isShutdown()) {
            initPool();
        }
        executor.submit(new Work(stopSortManagerThread, tierDataStream, exporter));
    }

    @Override
    public void output(Consumer<TierDataStream> o) {
        exporter = o;
    }

    class Work implements Runnable {

        private final Logger log = LoggerFactory.getLogger(Work.class);

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
            List<FileDatePackage> streamDataPackage = tierDataStream.getStreamDataPackage();

            List<BufferedReader> brs = new ArrayList<>();
            try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(tierDataStream.getMergePackage().outputStream()))
            ) {
                for (FileDatePackage fp : streamDataPackage) {
                    brs.add(new BufferedReader(new InputStreamReader(fp.inputStream())));
                }
                ReadMore readMore = new ReadMore(brs);

                readMore.read(d -> {
                    try {
                        bw.write(d.strValue());
                        bw.newLine();
                    } catch (IOException e) {
                        log.error("write file error", e);
                    }
                });
            } catch (Exception e) {
                log.error("merge error", e);
                stopThread.accept(null);
            } finally {
                brs.forEach(bufferedReader -> {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        log.error("close file error", e);
                    }
                });
            }
        }
    }

    private class ReadMore {

        private BufferedReader[] brs;
        private Map<Integer, MapPair<K, V>> values;


        private ReadMore(List<BufferedReader> brList) {
            brs = new BufferedReader[brList.size()];
            for (int i = 0; i < brList.size(); i++) {
                brs[i] = brList.get(i);
            }
            values = new HashMap<>();
        }

        private void read(Consumer<MapPair<K, V>> consumer) throws IOException {

            final boolean[] returnV = {true};
            while (returnV[0]) {
                for (int i = 0; i < brs.length; i++) {
                    if (brs[i] != null && values.get(i) == null) {
                        String s = brs[i].readLine();
                        if (s != null) {
                            MapPair<K, V> kvMapPair = AbstractKVTierMergeStrategy.this.toObj(s);
                            values.put(i, kvMapPair);
                        } else {
                            brs[i].close();
                            brs[i] = null;
                        }
                    }
                }
                Optional<Map.Entry<Integer, MapPair<K, V>>> min = values.entrySet().stream().min(Map.Entry.comparingByValue());
                if (min.isPresent()) {
                    consumer.accept(min.get().getValue());
                    values.remove(min.get().getKey());
                } else {
                    returnV[0] = false;
                }
            }

        }
    }
}
