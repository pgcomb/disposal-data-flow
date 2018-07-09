package com.github.pgcomb.ddf.merge;

import com.github.pgcomb.ddf.common.Tierable;
import com.github.pgcomb.ddf.common.packagee.FileDatePackage;

import java.util.List;

public class TierDataStream implements Tierable {

    private int tier;

    private List<FileDatePackage> streamDataPackage;

    private FileDatePackage mergePackage;

    public TierDataStream(int tier, List<FileDatePackage> streamDataPackage,FileDatePackage fileDatePackage) {
        this.tier = tier;
        this.streamDataPackage = streamDataPackage;
        this.mergePackage = fileDatePackage;
    }

    public List<FileDatePackage> getStreamDataPackage() {
        return streamDataPackage;
    }

    public FileDatePackage getMergePackage() {
        return mergePackage;
    }

    @Override
    public int getTier() {
        return tier;
    }

    @Override
    public String toString() {
        return "TierDataStream{" +
                "tier=" + tier +
                ", streamDataPackage=" + streamDataPackage +
                ", mergePackage=" + mergePackage +
                '}';
    }
}
