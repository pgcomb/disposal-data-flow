package com.github.pgcomb.ddf.common;

public interface Stopable extends StopFeedBack,StopForward {

    default void stop(){
        stopBack();
        stopForward();
    }

}
