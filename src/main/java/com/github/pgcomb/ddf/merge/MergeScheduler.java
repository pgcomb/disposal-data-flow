package com.github.pgcomb.ddf.merge;

import com.github.pgcomb.ddf.common.Inform;
import com.github.pgcomb.ddf.common.StopFeedBack;
import com.github.pgcomb.ddf.common.packagee.StreamDataPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class MergeScheduler<T extends StreamDataPackage,G> implements Inform<T> {

    private static final Logger log = LoggerFactory.getLogger(MergeScheduler.class);

    private MaterialStorage<T,G> materialStorage;

    private MergeStrategy<G,?> mergeStrategy;

    private Consumer<T> out;

    public MergeScheduler(MaterialStorage<T,G> materialStorage, MergeStrategy<G,?> mergeStrategy, Consumer<T> out) {
        this.materialStorage = materialStorage;
        this.mergeStrategy = mergeStrategy;
        this.mergeStrategy.preStop(this);
        this.mergeStrategy.output(materialStorage::back);
        this.materialStorage.end(MergeScheduler.this::materialStorageEnd);
        this.materialStorage.overflow(mergeStrategy::accept);
        this.out = out;
    }

    private boolean stop;

    private StopFeedBack stopFeedBack;

    @Override
    public void stopBack() {
        this.stop = true;
        if (stopFeedBack != null && !stopFeedBack.isStop()){
            stopFeedBack.stopBack();
        }
        this.materialStorage.stop();
    }

    private void materialStorageEnd(T consumer){
        log.info("merge out:{}",consumer);
        if (mergeStrategy != null && !mergeStrategy.isStop()){
            this.mergeStrategy.stopForward();
        }
        out.accept(consumer);
    }

    @Override
    public void preStop(StopFeedBack stopFeedBack) {
        this.stopFeedBack = stopFeedBack;
    }

    @Override
    public void stopForward() {
        log.info("merge scheduler stop forward");
        this.stop = true;
        this.materialStorage.stop();

    }

    @Override
    public boolean isStop() {
        return stop;
    }

    @Override
    public void accept(T t) {
        if (!stop) {
            materialStorage.add(t);
        }
    }
}
