package com.scalepoint.automation.utils.data.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.xml.bind.annotation.*;
import java.util.List;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PseudoCategory {

    @XmlElement(required = true)
    @NonNull
    private String groupName;
    @XmlElement(required = true)
    @NonNull
    private String categoryName;
    @XmlElementWrapper
    @XmlElement(name = "damageType")
    private List<String> damageTypes;

}
