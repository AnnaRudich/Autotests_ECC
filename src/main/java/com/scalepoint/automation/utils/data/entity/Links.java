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
public class Links {
    @XmlElement
    private String startPage;
    @XmlElement
    private String hubLink;
    @XmlElement
    private String eccLogout;
    @XmlElement
    private String eccFastLogin;
    @XmlElement
    private String eccFastLoginAndCreateClaim;
    @XmlElement
    private String meStart;
    @XmlElement
    private String autoCancelClaimLink;
    @XmlElement
    private String exportAttributeMappingsLink;
    @XmlElement
    private String exportAttributeFilePath;
    @XmlElement
    private String importAttributeFilePath;
    @XmlElement
    private String exportSupplierMappingsLink;
    @XmlElement
    private String exportSupplierFilePath;
    @XmlElement
    private String importSupplierFilePath;
    @XmlElement
    private String exportCategoryMappingsLink;
    @XmlElement
    private String exportCategoryFilePath;
    @XmlElement
    private String importCategoryFilePath;
    @XmlElement
    private String exportExternalPricesLink;
    @XmlElement
    private String exportExternalPricesFilePath;
    @XmlElement
    private String importExternalPricesFilePath;
    @XmlElement
    private String exportExtendedData;
    @XmlElement
    private String exportExtendedDataFilePath;
    @XmlElement
    private String importExtendedDataFilePath;

    public String getMeStart() {
        return meStart;
    }

    public String getEccFastLogin() {
        return eccFastLogin;
    }

    public String getEccFastLoginAndCreateClaim() {
        return eccFastLoginAndCreateClaim;
    }

    public String getStartPage() {
        return startPage;
    }

    public String getHubLink() {
        return hubLink;
    }

    public String getEccLogout() {
        return eccLogout;
    }

    public String getAutoCancelClaimLink() {
        return autoCancelClaimLink;
    }

    public String getExportAttributeMappingsLink() {
        return exportAttributeMappingsLink;
    }

    public String getExportAttributeFilePath() {
        return exportAttributeFilePath;
    }

    public String getImportAttributeFilePath() {
        return importAttributeFilePath;
    }

    public String getExportSupplierMappingsLink() {
        return exportSupplierMappingsLink;
    }

    public String getImportSupplierFilePath() {
        return importSupplierFilePath;
    }

    public String getExportSupplierFilePath() {
        return exportSupplierFilePath;
    }

    public String getExportCategoryMappingsLink() {
        return exportCategoryMappingsLink;
    }

    public String getExportCategoryFilePath() {
        return exportCategoryFilePath;
    }

    public String getImportCategoryFilePath() {
        return importCategoryFilePath;
    }

    public String getExportExternalPricesLink() {
        return exportExternalPricesLink;
    }

    public String getExportExternalPricesFilePath() {
        return exportExternalPricesFilePath;
    }

    public String getImportExternalPricesFilePath() {
        return importExternalPricesFilePath;
    }

    public String getExportExtendedData() {
        return exportExtendedData;
    }

    public String getExportExtendedDataFilePath() {
        return exportExtendedDataFilePath;
    }

    public String getImportExtendedDataFilePath() {
        return importExtendedDataFilePath;
    }

}
