package com.scalepoint.automation.utils.data.entity.rnv.serviceTask;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class Line{

    private String description;
    private String category;
    private String task;
}
