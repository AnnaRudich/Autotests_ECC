package com.scalepoint.automation.utils.data.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Notifications {

    @XmlElement
    private String mappingsImport;
    @XmlElement
    private String externalPricesImport;
    @XmlElement
    private String extendedDataSummaryText;

    public String getMappingsImport() {
        return mappingsImport;
    }

    public String getExternalPricesImport() {
        return externalPricesImport;
    }

    public String getExtendedDataSummaryText() {
        return extendedDataSummaryText;
    }
}
