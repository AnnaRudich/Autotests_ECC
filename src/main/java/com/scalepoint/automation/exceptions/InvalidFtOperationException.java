package com.scalepoint.automation.exceptions;

import com.scalepoint.automation.services.externalapi.ftemplates.operations.FtOperation;

import java.util.List;
import java.util.stream.Stream;

public class InvalidFtOperationException extends RuntimeException {

    public InvalidFtOperationException(List<FtOperation> operations) {
        super("Ft is in different state: " + Stream.of(operations).map(Object::toString).reduce(" ", String::concat));
    }

    public InvalidFtOperationException(String message, List<FtOperation> operations) {
        super("Ft is in different state: " + Stream.of(operations).map(Object::toString).reduce(" ", String::concat) + "\n" + message);
    }
}
