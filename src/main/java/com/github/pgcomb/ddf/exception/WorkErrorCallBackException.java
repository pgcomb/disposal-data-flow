package com.github.pgcomb.ddf.exception;

public class WorkErrorCallBackException extends WorkCallBackException {
    public WorkErrorCallBackException() {
    }

    public WorkErrorCallBackException(String message) {
        super(message);
    }

    public WorkErrorCallBackException(String message, Throwable cause) {
        super(message, cause);
    }

    public WorkErrorCallBackException(Throwable cause) {
        super(cause);
    }

    public WorkErrorCallBackException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
