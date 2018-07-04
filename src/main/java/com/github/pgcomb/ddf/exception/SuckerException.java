package com.github.pgcomb.ddf.exception;

public class SuckerException extends Exception {

    public SuckerException() {
    }

    public SuckerException(String message) {
        super(message);
    }

    public SuckerException(String message, Throwable cause) {
        super(message, cause);
    }

    public SuckerException(Throwable cause) {
        super(cause);
    }

    public SuckerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
