package com.scalepoint.automation.utils.data.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentInfo {
    @NonNull
    @JsonProperty("Name")
    String name;
    @NonNull
    @JsonProperty("Url")
    String url;
    @JsonProperty("Content")
    String content;
}
