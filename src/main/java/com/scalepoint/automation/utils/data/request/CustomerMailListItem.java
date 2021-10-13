package com.scalepoint.automation.utils.data.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerMailListItem {

    private String token;
    private String subject;
    private String type;
    private int status;
    private String date;
    @JsonProperty("resendAllowed")
    private boolean resendAllowed;
}
