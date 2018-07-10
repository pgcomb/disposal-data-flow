package com.github.pgcomb.ddf.merge;

import com.github.pgcomb.ddf.common.packagee.FileDatePackage;
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

public class DefaultMaterialStorage implements MaterialStorage<FileDatePackage, TierDataStream> {

    private static final Logger log = LoggerFactory.getLogger(DefaultMaterialStorage.class);

    private Map<Integer, List<FileDatePackage>> preparePool = new ConcurrentHashMap<>();

    private Map<Integer, List<TierDataStream>> executingPool = new ConcurrentHashMap<>();

    private static Path basePath = Paths.get(System.getProperties().getProperty("user.home")).resolve("DDF");

    private static final String EXPANDED_NAME = "mpst";

    private boolean stop = false;

    private int overflowLine = 2;

    private final Object lock = new Object();

    private Consumer<TierDataStream> overflow;

    private Consumer<FileDatePackage> end;

    public DefaultMaterialStorage(Consumer<FileDatePackage> end) {
        this.end = end;
        init();
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
    public void end(Consumer<FileDatePackage> end) {
        this.end = end;
    }

    @Override
    public void stop() {
        synchronized (lock) {
            stop = true;
            examine(-1);
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
    public void back(TierDataStream materials) {
        synchronized (lock) {
            int tier = materials.getTier();
            if (!CollectionUtils.isEmpty(preparePool) || !CollectionUtils.isEmpty(executingPool)) {
                List<FileDatePackage> fileDatePackages = preparePool.computeIfAbsent(tier, k -> new ArrayList<>());

                List<TierDataStream> tierDataStreams = executingPool.get(tier);
                tierDataStreams.remove(materials);
                if (CollectionUtils.isEmpty(tierDataStreams)) {
                    executingPool.remove(tier);
                }
                fileDatePackages.add(materials.getMergePackage());
            }
            examine(tier);
        }
    }

    @Override
    public void add(FileDatePackage t) {
        synchronized (lock) {
            List<FileDatePackage> fileDatePackages = preparePool.computeIfAbsent(0, k -> new ArrayList<>());
            fileDatePackages.add(t);
            examine(0);
        }
    }

    private void examine(int tier) {
        List<Map.Entry<Integer, List<FileDatePackage>>> preparePoolList =
                preparePool.entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toList());
        int preSize = preparePoolList.size();
        Integer preMix = preSize == 0 ? null : preparePoolList.get(0).getKey();
        List<FileDatePackage> preMixValue = preSize == 0 ? null : preparePoolList.get(0).getValue();

        List<Map.Entry<Integer, List<TierDataStream>>> executingPoolList =
                executingPool.entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toList());
        int ingSize = executingPoolList.size();
        Integer ingMix = ingSize == 0 ? null : executingPoolList.get(0).getKey();

        if (stop && preSize == 1 && preMixValue.size() == 1 && executingPool.size() == 0) {
            FileDatePackage fileDatePackage = preMixValue.get(0);
            preparePool.remove(preMix);
            end.accept(fileDatePackage);
        } else if (tier != -1 && preparePool.get(tier).size() == overflowLine) {
            TierDataStream tierDataStream = new TierDataStream(tier + 1, preparePool.get(tier), new FileDatePackage(getFile(tier + 1)));
            preparePool.remove(preMix);
            List<TierDataStream> fileDatePackages = executingPool.computeIfAbsent(tier + 1, k -> new ArrayList<>());
            fileDatePackages.add(tierDataStream);
            overflow.accept(tierDataStream);
        } else if ((tier == -1 || stop) && preMix!=null && (ingMix == null || preMix < ingMix)) {
            TierDataStream tierDataStream = new TierDataStream(preMix + 1, preMixValue, new FileDatePackage(getFile(preMix + 1)));
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

        DefaultMaterialStorage defaultMaterialStorage = new DefaultMaterialStorage(System.out::println);
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
            executor.submit(() -> defaultMaterialStorage.add(new FileDatePackage(getFile(0))));
        }
        Thread.sleep(10000);
        defaultMaterialStorage.stop();
    }
}
