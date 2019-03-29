package com.scalepoint.automation.exceptions;

public class ServerApiException extends RuntimeException {

    public ServerApiException(Throwable cause) {
        super(cause);
    }

    @Override
    public String toString() {
        if (getCause() != null) {
            return getCause().toString();
        }
        return super.toString();
    }
}
