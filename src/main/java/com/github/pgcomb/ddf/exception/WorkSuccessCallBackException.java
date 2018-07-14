package com.github.pgcomb.ddf.exception;

public class WorkSuccessCallBackException extends WorkCallBackException {
    public WorkSuccessCallBackException() {
    }

    public WorkSuccessCallBackException(String message) {
        super(message);
    }

    public WorkSuccessCallBackException(String message, Throwable cause) {
        super(message, cause);
    }

    public WorkSuccessCallBackException(Throwable cause) {
        super(cause);
    }

    public WorkSuccessCallBackException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
