package com.github.pgcomb.ddf.merge;

import com.github.pgcomb.ddf.common.packagee.FileDataPackage;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DefaultMaterialStorage implements MaterialStorage<FileDataPackage, TierDataStream> {

    private static final Logger log = LoggerFactory.getLogger(DefaultMaterialStorage.class);

    private Map<Integer, List<FileDataPackage>> preparePool = new ConcurrentHashMap<>();

    private Map<Integer, List<TierDataStream>> executingPool = new ConcurrentHashMap<>();

    private static Path basePath = Paths.get(System.getProperties().getProperty("user.home")).resolve("DDF");

    private static final String EXPANDED_NAME = "mpst";

    private boolean stop = false;

    private int overflowLine = 5;

    private final Object lock = new Object();

    private Consumer<TierDataStream> overflow;

    private Consumer<FileDataPackage> end;

    private static final int CORE_POOL_SIZE = 5;

    private static final int MAXIMUM_POOL_SIZE = 10;

    private static final int KEEP_ACTIVE_TIME = 2;

    private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

    private ThreadPoolExecutor executor;

    private Consumer<Void> stopSortManagerThread;

    public DefaultMaterialStorage() {
        init();
    }
    private void initPool() {
        stopSortManagerThread = t -> new Thread(this::poolStop, "merge stop thread").start();
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("material storage-pool-%d").build();
        executor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ACTIVE_TIME, TIME_UNIT,
                new LinkedBlockingQueue<>(1), namedThreadFactory, (r, executor1) -> {
            if (!CollectionUtils.isEmpty(executor1.getQueue())) {
                try {
                    Thread.sleep(50L);
                } catch (InterruptedException e) {
                    log.error("sleep is false", e);
                    Thread.currentThread().interrupt();
                }
            }
            executor1.submit(r);
        });
    }
    public void setOverflow(Consumer<TierDataStream> overflow) {
        this.overflow = overflow;
    }

    private void init() {
        basePath.toFile().mkdirs();
    }

    @Override
    public void overflow(Consumer<TierDataStream> overflow) {
        this.overflow = overflow;
    }

    @Override
    public void end(Consumer<FileDataPackage> end) {
        this.end = end;
    }

    @Override
    public void stop() {
        log.info("material storage stop");
        synchronized (lock) {
            if (!stop){
                stop = true;
                examine(-1);
            }

        }
    }

    @Override
    public void destroy() {
        synchronized (lock) {
            stop = false;
            preparePool.clear();
            executingPool.clear();
        }
    }

    @Override
    public void back(TierDataStream materials){
        executor.submit(() ->backb( materials));
    }
    public void backb(TierDataStream materials) {
        log.info("MaterialStorage back :{}",materials.getMergePackage().getFile());
        synchronized (lock) {
            int tier = materials.getTier();
            if (!CollectionUtils.isEmpty(preparePool) || !CollectionUtils.isEmpty(executingPool)) {
                List<FileDataPackage> fileDataPackages = preparePool.computeIfAbsent(tier, k -> new ArrayList<>());

                List<TierDataStream> tierDataStreams = executingPool.get(tier);
                tierDataStreams.remove(materials);
                if (CollectionUtils.isEmpty(tierDataStreams)) {
                    executingPool.remove(tier);
                }
                fileDataPackages.add(materials.getMergePackage());
            }
            examine(tier);
        }
    }
    @Override
    public void add(FileDataPackage t) {
        if (executor == null){
            initPool();
        }
        executor.submit(() -> addb(t));
    }
    public void addb(FileDataPackage t) {
        log.info("MaterialStorage add :{}",t.getFile());
        synchronized (lock) {
            List<FileDataPackage> fileDataPackages = preparePool.computeIfAbsent(0, k -> new ArrayList<>());
            fileDataPackages.add(t);
            examine(0);
        }
    }

    private void examine(int tier) {
        List<Map.Entry<Integer, List<FileDataPackage>>> preparePoolList =
                preparePool.entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toList());
        int preSize = preparePoolList.size();
        Integer preMix = preSize == 0 ? null : preparePoolList.get(0).getKey();
        List<FileDataPackage> preMixValue = preSize == 0 ? null : preparePoolList.get(0).getValue();

        List<Map.Entry<Integer, List<TierDataStream>>> executingPoolList =
                executingPool.entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toList());
        int ingSize = executingPoolList.size();
        Integer ingMix = ingSize == 0 ? null : executingPoolList.get(0).getKey();

        if (stop && preSize == 1 && preMixValue.size() == 1 && executingPool.size() == 0) {
            FileDataPackage fileDataPackage = preMixValue.get(0);
            preparePool.remove(preMix);
            stopSortManagerThread.accept(null);
            new Thread(() ->end.accept(fileDataPackage), "merge stop thread").start();

        } else if (tier != -1 && preparePool.get(tier).size() == overflowLine) {
            TierDataStream tierDataStream = new TierDataStream(tier + 1, preparePool.get(tier), new FileDataPackage(getFile(tier + 1)));
            preparePool.remove(tier);
            List<TierDataStream> fileDatePackages = executingPool.computeIfAbsent(tier + 1, k -> new ArrayList<>());
            fileDatePackages.add(tierDataStream);
            overflow.accept(tierDataStream);
        } else if ((tier == -1 || stop) && preMix!=null && (ingMix == null || preMix < ingMix)) {
            TierDataStream tierDataStream = new TierDataStream(preMix + 1, preMixValue, new FileDataPackage(getFile(preMix + 1)));
            preparePool.remove(preMix);
            List<TierDataStream> fileDatePackages = executingPool.computeIfAbsent(preMix + 1, k -> new ArrayList<>());
            fileDatePackages.add(tierDataStream);
            overflow.accept(tierDataStream);
        }
    }

    private static File getFile(int tier) {

        return basePath.resolve(String.format("merge-%d-%s.%s", tier, UUID.randomUUID().toString(), EXPANDED_NAME)).toFile();

    }

    public static void main(String[] args) throws InterruptedException {

        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("sorter-pool-%d").build();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 20, 2, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(100), namedThreadFactory, (r, executor1) -> {
            if (!CollectionUtils.isEmpty(executor1.getQueue())) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    log.error("sleep is false", e);
                    Thread.currentThread().interrupt();
                }
            } else {
                executor1.execute(r);
            }
        });

        DefaultMaterialStorage defaultMaterialStorage = new DefaultMaterialStorage();
        defaultMaterialStorage.end(System.out::println);
        defaultMaterialStorage.setOverflow(tierDataStream -> {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("overflow-" + tierDataStream);
            defaultMaterialStorage.back(tierDataStream);
        });
        for (int i = 0; i < 10; i++) {
            executor.submit(() -> defaultMaterialStorage.add(new FileDataPackage(getFile(0))));
        }
        Thread.sleep(10000);
        defaultMaterialStorage.stop();
    }
    private void poolStop() {
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
