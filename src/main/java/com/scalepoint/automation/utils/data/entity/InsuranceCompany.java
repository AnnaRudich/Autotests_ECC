package com.scalepoint.automation.utils.data.entity;

import com.scalepoint.automation.utils.RandomUtils;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * NewSystemUser: kke
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class InsuranceCompany {

    private String icID = Integer.toString(RandomUtils.randomInt());
    private String icName = RandomUtils.randomName("Autotest-IC");
    private String icCode = icName.toUpperCase();
    private String address = RandomUtils.randomName("addr");
    @XmlElement
    private String zipCode;
    private String icCity = RandomUtils.randomName("City");
    @XmlElement
    private String companyCommonMail;
    @XmlElement
    private String functionTemplate;
    @XmlElement
    private String icCulture;
    @XmlElement
    private String guiTemplate;
    @XmlElement
    private String contactNumber;
    @XmlElement
    private String officeHours;
    @XmlElement
    private String ftTrygHolding;
    @XmlElement
    private String sendTimeFrom;
    @XmlElement
    private String sendTimeTo;
}
