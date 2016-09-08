package com.scalepoint.automation.utils.data.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * NewSystemUser: kke
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Integration {

    @XmlElement
    private String simpleIntegrationFile;
    @XmlElement
    private String scalepointDomainServer;
    @XmlElement
    private String companyNameIntegration;
    @XmlElement
    private String productIntegrationFile;


    public String getSimpleIntegrationFile() {
        return simpleIntegrationFile;
    }

    public String getScalepointDomainServer() {
        return scalepointDomainServer;
    }

    public String getCompanyNameIntegration() {
        return companyNameIntegration;
    }

    public String getProductIntegrationFile() {
        return productIntegrationFile;
    }
}
