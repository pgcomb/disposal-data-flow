package com.github.pgcomb.ddf.bucket.sucker;

public class CountPipeSucker<T> implements PipeSucker<T> {

    @Override
    public void suck(T data) {

    }

    @Override
    public void stopForward() {

    }

    @Override
    public boolean isStop() {
        return false;
    }
}
