package com.github.pgcomb.ddf.common.packagee;

import com.github.pgcomb.ddf.common.Fileable;
import org.springframework.util.Assert;

import java.io.*;

public class FileDataPackage implements StreamDataPackage,Fileable {

    private File file;

    private long start;

    private long end;

    private int size;

    private int serialNumber;

    public FileDataPackage(File file, PackageMetadata packageMetadata) {
        Assert.notNull(file,"file can not null");
        this.file = file;
        this.start = packageMetadata.start();
        this.serialNumber = packageMetadata.serialNumber();
        this.end = packageMetadata.end();
        this.size = packageMetadata.size();
    }

    public FileDataPackage(File file) {
        this.file = file;
    }

    @Override
    public File getFile() {
        return file;
    }

    @Override
    public InputStream inputStream() throws FileNotFoundException {
        return new FileInputStream(getFile());
    }

    @Override
    public OutputStream outputStream() throws FileNotFoundException {
        return new FileOutputStream(getFile());
    }

    @Override
    public long start() {
        return start;
    }

    @Override
    public long end() {
        return end;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int serialNumber() {
        return serialNumber;
    }

    @Override
    public String toString() {
        return "FileDataPackage{" +
                "file=" + file +
                ", start=" + start +
                ", end=" + end +
                ", size=" + size +
                ", serialNumber=" + serialNumber +
                '}';
    }
}
