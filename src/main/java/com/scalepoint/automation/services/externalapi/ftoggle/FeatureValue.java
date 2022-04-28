package com.scalepoint.automation.services.externalapi.ftoggle;

import lombok.Getter;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum FeatureValue {

    ENABLED("enabled", true),
    DISABLED("disabled", false);

    @Getter
    private String status;
    @Getter
    public boolean enabled;

    FeatureValue(String status, boolean enabled) {

        this.status = status;
        this.enabled = enabled;
    }

    public static FeatureValue getByStatus(String name) {

        return Arrays.stream(FeatureValue.values())
                .filter(featureValue -> featureValue.getStatus().equals(name))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(name));
    }
}
