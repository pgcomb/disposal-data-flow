package com.github.pgcomb.ddf.merge;

import com.github.pgcomb.ddf.common.Tierable;
import com.github.pgcomb.ddf.common.packagee.StreamDataPackage;

import java.util.List;

public class TierDataStream<T extends StreamDataPackage> implements Tierable {

    private int tier;

    private List<T> streamDataPackage;

    public TierDataStream(int tier, List<T> streamDataPackage) {
        this.tier = tier;
        this.streamDataPackage = streamDataPackage;
    }

    public List<T> getStreamDataPackage() {
        return streamDataPackage;
    }

    @Override
    public int getTier() {
        return 0;
    }
}
