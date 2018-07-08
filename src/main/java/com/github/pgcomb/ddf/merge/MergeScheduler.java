package com.github.pgcomb.ddf.merge;

import com.github.pgcomb.ddf.common.Inform;
import com.github.pgcomb.ddf.common.StopFeedBack;
import com.github.pgcomb.ddf.common.packagee.StreamDataPackage;

public class MergeScheduler<T extends StreamDataPackage> implements Inform<T> {

    private MaterialStorage materialStorage;

    private MergeStrategy mergeStrategy;

    private boolean stop;

    private StopFeedBack stopFeedBack;

    public void submitTask(){

    }

    @Override
    public void stopBack() {
        this.stop = true;
        if (stopFeedBack != null && !stopFeedBack.isStop()){
            stopFeedBack.stopBack();
        }
    }

    @Override
    public void preStop(StopFeedBack stopFeedBack) {
        this.stopFeedBack = stopFeedBack;
    }

    @Override
    public void stopForward() {
        this.stop = true;
        mergeStrategy.stopForward();
    }

    @Override
    public boolean isStop() {
        return stop;
    }

    @Override
    public void accept(T t) {

    }
}
