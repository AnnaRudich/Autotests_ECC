package com.scalepoint.automation.utils.listeners;

import com.scalepoint.automation.services.externalapi.ftemplates.operations.FtOperation;
import com.scalepoint.automation.services.externalapi.ftoggle.FeatureIds;
import com.scalepoint.automation.services.restService.FeaturesToggleAdministrationService.ActionsOnToggle;
import com.scalepoint.automation.utils.data.entity.credentials.User;

import java.util.List;
import java.util.Map;

public class RollbackContext {

    private User user;
    private List<FtOperation> operations;
    private Map<FeatureIds, ActionsOnToggle> featureToggles;

    public RollbackContext(User user, Map<FeatureIds, ActionsOnToggle> featureToggles) {
        this.user = user;
        this.featureToggles = featureToggles;
    }

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

    public Map<FeatureIds, ActionsOnToggle> getFeatureToggles(){
        return featureToggles;
    }

    public RollbackContext setFeatureToggles(Map<FeatureIds, ActionsOnToggle> featureToggles){
        this.featureToggles=featureToggles;
        return this;
    }
}
