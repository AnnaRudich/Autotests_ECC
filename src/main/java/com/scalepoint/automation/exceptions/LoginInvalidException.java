package com.scalepoint.automation.exceptions;

public class LoginInvalidException extends RuntimeException {
    public LoginInvalidException() {
        super();
    }

    public LoginInvalidException(String message) {
        super(message);
    }

    public LoginInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginInvalidException(Throwable cause) {
        super(cause);
    }

    protected LoginInvalidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
