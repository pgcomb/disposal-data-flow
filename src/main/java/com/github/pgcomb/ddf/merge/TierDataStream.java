package com.github.pgcomb.ddf.merge;

import com.github.pgcomb.ddf.common.Tierable;
import com.github.pgcomb.ddf.common.packagee.FileDataPackage;

import java.util.List;

public class TierDataStream implements Tierable {

    private int tier;

    private List<FileDataPackage> streamDataPackage;

    private FileDataPackage mergePackage;

    public TierDataStream(int tier, List<FileDataPackage> streamDataPackage, FileDataPackage fileDataPackage) {
        this.tier = tier;
        this.streamDataPackage = streamDataPackage;
        this.mergePackage = fileDataPackage;
    }

    public List<FileDataPackage> getStreamDataPackage() {
        return streamDataPackage;
    }

    public FileDataPackage getMergePackage() {
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
