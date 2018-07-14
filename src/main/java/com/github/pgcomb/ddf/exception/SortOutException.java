package com.github.pgcomb.ddf.exception;

public class SortOutException extends Exception {
    public SortOutException() {
    }

    public SortOutException(String message) {
        super(message);
    }

    public SortOutException(String message, Throwable cause) {
        super(message, cause);
    }

    public SortOutException(Throwable cause) {
        super(cause);
    }

    public SortOutException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
