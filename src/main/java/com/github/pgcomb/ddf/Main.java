package com.github.pgcomb.ddf;

import com.github.pgcomb.ddf.exception.SuckerException;
import com.github.pgcomb.ddf.sucker.FileSucker;

import java.io.File;

public class Main {
    public static void main(String[] args) throws SuckerException {
        FileSucker fileSucker = new FileSucker(new File("E:\\test"));
        String read;
        while ((read = fileSucker.next()) != null){
            System.out.println(fileSucker.getCount());
            System.out.println(read);
        }
    }
}
