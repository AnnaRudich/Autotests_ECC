package com.scalepoint.automation.utils.listeners;

import com.scalepoint.automation.services.externalapi.ftemplates.operations.FtOperation;
import com.scalepoint.automation.utils.data.entity.credentials.User;

import java.util.List;

public class RollbackContext {

    private User user;
    private List<FtOperation> operations;

    public RollbackContext(User user, List<FtOperation> operations) {
        this.user = user;
        this.operations = operations;
    }

    public User getUser() {
        return user;
    }

    public RollbackContext setUser(User user) {
        this.user = user;
        return this;
    }

    public List<FtOperation> getOperations() {
        return operations;
    }

    public RollbackContext setOperations(List<FtOperation> operations) {
        this.operations = operations;
        return this;
    }
}
