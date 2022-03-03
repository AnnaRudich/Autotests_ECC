package com.scalepoint.automation.utils.data.entity.input;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import static com.scalepoint.automation.utils.SystemUtils.getResourcePath;

@Data
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class LossSheetTemplates {

    @XmlElement
    private String claimSheetDKExcel2003v1;
    @XmlElement
    private String claimSheetDKExcel2007v11;
    @XmlElement
    private String claimSheetDKExcel2007v12;
    @XmlElement
    private String excel400DK;


    public String getClaimSheetDKExcel2003v1() {
        return getResourcePath(claimSheetDKExcel2003v1);
    }

    public String getClaimSheetDKExcel2007v11() {
        return getResourcePath(claimSheetDKExcel2007v11);
    }

    public String getClaimSheetDKExcel2007v12() {
        return getResourcePath(claimSheetDKExcel2007v12);
    }

    public String getExcel400DK() {
        return getResourcePath(excel400DK);
    }
}
