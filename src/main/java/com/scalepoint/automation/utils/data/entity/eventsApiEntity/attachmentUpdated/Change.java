package com.scalepoint.automation.utils.data.entity.eventsApiEntity.attachmentUpdated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Change {

    Property property;
    String attachmentGUID;
    String attachmentLink;
    String fileType;
    String fileName;

    public enum Property {

        ATTACHMENT_ADDED_TO_CLAIM_LEVEL("attachmentAddedToClaimLevel"),
        ATTACHMENT_ADDED_TO_CLAIM_LINE_LEVEL("attachmentAddedToClaimLineLevel"),
        ATTACHMENT_ADDED_FROM_CLAIM_LEVEL_TO_CLAIM_LINE_LEVEL("attachmentAddedFromClaimLevelToClaimLineLevel"),
        ATTACHMENT_ADDED_FROM_CLAIM_LINE_LEVEL_TO_CLAIM_LINE_LEVEL("attachmentAddedFromClaimLineLevelToClaimLineLevel"),
        ATTACHMENT_DELETED_FROM_CLAIM_LEVEL("attachmentDeletedFromClaimLevel"),
        ATTACHMENT_UNLINKED_FROM_CLAIM_LINE_LEVEL("attachmentUnlinkedFromClaimLineLevel"),
        ATTACHMENT_IMPORTED_FROM_FNOL("attachmentImportedFromFNOL"),
        ATTACHMENT_IMPORTED_FROM_SELFSERVICE("attachmentImportedFromSelfService");

        private final String value;
        private final static Map<String, Change.Property> CONSTANTS = new HashMap<>();

        static {
            for (Change.Property c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        Property(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static Change.Property fromValue(String value) {
            Change.Property constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }
    }
}
