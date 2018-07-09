package com.github.pgcomb.ddf.merge;

import com.github.pgcomb.ddf.common.packagee.FileDatePackage;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DefaultMaterialStorage implements MaterialStorage<FileDatePackage, TierDataStream> {

    private static final Logger log = LoggerFactory.getLogger(DefaultMaterialStorage.class);

    private Map<Integer, List<FileDatePackage>> preparePool = new ConcurrentHashMap<>();

    private Map<Integer, List<TierDataStream>> executingPool = new ConcurrentHashMap<>();

    private static Path basePath = Paths.get(System.getProperties().getProperty("user.home")).resolve("DDF");

    private static final String EXPANDED_NAME = "mpst";

    private boolean stop = false;

    private int overflowLine = 5;

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

    private void init(){
        basePath.toFile().mkdirs();
    }
    @Override
    public Consumer<TierDataStream> overflow() {
        return overflow;
    }

    @Override
    public Consumer<FileDatePackage> end() {
        return end;
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
            if (!CollectionUtils.isEmpty(preparePool) || !CollectionUtils.isEmpty(executingPool)){
                List<FileDatePackage> fileDatePackages = preparePool.computeIfAbsent(0, k -> new ArrayList<>());

                List<TierDataStream> tierDataStreams = executingPool.get(tier);
                tierDataStreams.remove(materials);
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
        Integer preMix = preSize == 0 ? -1 : preparePoolList.get(0).getKey();
        List<FileDatePackage> preMixValue = preMix == -1 ? null:preparePoolList.get(0).getValue();

        List<Map.Entry<Integer, List<TierDataStream>>> executingPoolList =
                executingPool.entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toList());
        int ingSize = executingPoolList.size();
        Integer ingMix = ingSize == 0 ? 0 : executingPoolList.get(0).getKey();

        if (tier != -1 || preMix <= ingMix) {
            if (tier == -1 && preMix < ingMix && ingSize == 0 && preSize == 1 && preMixValue.size() == 1){
                FileDatePackage fileDatePackage = preMixValue.get(0);
                preparePool.remove(preMix);
                end().accept(fileDatePackage);
            }else if (tier == -1){
                TierDataStream tierDataStream = new TierDataStream(preMix + 1, preMixValue, new FileDatePackage(getFile(preMix + 1)));
                preparePool.remove(preMix);
                List<TierDataStream> fileDatePackages = executingPool.computeIfAbsent(tier+1, k -> new ArrayList<>());
                fileDatePackages.add(tierDataStream);
                overflow().accept(tierDataStream);
            }else if (stop && ingSize == 0 && preSize == 1){
                FileDatePackage fileDatePackage = preMixValue.get(0);
                preparePool.remove(preMix);
                end().accept(fileDatePackage);
            } else if (stop){
                TierDataStream tierDataStream = new TierDataStream(tier + 1, preparePool.get(tier), new FileDatePackage(getFile(tier + 1)));
                preparePool.remove(preMix);
                List<TierDataStream> fileDatePackages = executingPool.computeIfAbsent(tier+1, k -> new ArrayList<>());
                fileDatePackages.add(tierDataStream);
                overflow().accept(tierDataStream);
            } else if (!stop && preparePool.get(tier) != null && preparePool.get(tier).size() == overflowLine) {
                TierDataStream tierDataStream = new TierDataStream(tier + 1, preparePool.get(tier), new FileDatePackage(getFile(tier + 1)));
                preparePool.remove(preMix);
                List<TierDataStream> fileDatePackages = executingPool.computeIfAbsent(tier+1, k -> new ArrayList<>());
                fileDatePackages.add(tierDataStream);
                overflow().accept(tierDataStream);
            }
        }
    }

    private static File getFile(int tier) {

        return basePath.resolve(String.format("merge-%d-%s.%s", tier, UUID.randomUUID().toString(), EXPANDED_NAME)).toFile();

    }

    public static void main(String[] args) {
        DefaultMaterialStorage defaultMaterialStorage = new DefaultMaterialStorage(System.out::println);
        defaultMaterialStorage.setOverflow(tierDataStream -> {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(tierDataStream);
            defaultMaterialStorage.back(tierDataStream);
        });
        for (int i = 0; i < 20; i++) {
            defaultMaterialStorage.add(new FileDatePackage(getFile(0)));
        }
        defaultMaterialStorage.stop();
    }
}
