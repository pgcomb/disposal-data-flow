package com.github.pgcomb.ddf.exception;

public class WorkCallBackException extends Exception {
    public WorkCallBackException() {
    }

    public WorkCallBackException(String message) {
        super(message);
    }

    public WorkCallBackException(String message, Throwable cause) {
        super(message, cause);
    }

    public WorkCallBackException(Throwable cause) {
        super(cause);
    }

    public WorkCallBackException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
