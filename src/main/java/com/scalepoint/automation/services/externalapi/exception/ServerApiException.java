package com.scalepoint.automation.services.externalapi.exception;

public class ServerApiException extends RuntimeException {

    public ServerApiException() {
    }

    public ServerApiException(String message) {
        super(message);
    }

    public ServerApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerApiException(Throwable cause) {
        super(cause);
    }

    public ServerApiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String toString() {
        if (getCause() != null) {
            return getCause().toString();
        }
        return super.toString();
    }
}
