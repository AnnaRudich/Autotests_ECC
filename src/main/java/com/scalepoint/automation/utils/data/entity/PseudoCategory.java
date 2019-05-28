package com.scalepoint.automation.utils.data.entity;

import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.*;

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
