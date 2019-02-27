package com.scalepoint.automation.shared;

import com.scalepoint.automation.utils.data.TestData;

public enum ClaimStatus {

    IN_USE("W"),
    DELETED("D"),
    OPEN("P"),
    COMPLETED("S"),
    LOCKED_BY_ORDER("L"),
    CANCELLED("X"),
    CLOSED_EXTERNALLY("E");

    private String status;

    ClaimStatus(String status) {
        this.status = status;
    }

    public String getText() {
        return TestData.getClaimStatuses().get(this);
    }

    public String getStatus() {
        return status;
    }

    public static ClaimStatus findByStatus(String status) {
        for (ClaimStatus value : ClaimStatus.values()) {
            if (value.status.equalsIgnoreCase(status)) {
                return value;
            }
        }
        throw new IllegalArgumentException("Status [" + status + "] is not supported");
    }


}
