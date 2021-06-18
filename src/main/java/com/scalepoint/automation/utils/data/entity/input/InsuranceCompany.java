package com.scalepoint.automation.utils.data.entity.input;

import com.scalepoint.automation.utils.RandomUtils;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * NewSystemUser: kke
 */
@Data
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class InsuranceCompany {

    private String icID = Integer.toString(RandomUtils.randomInt());
    private String icName = RandomUtils.randomName("Autotest-IC ÆæØøÅåß");
    private String icCode = icName.toUpperCase();
    private String address = RandomUtils.randomName("addr ÆæØøÅåß");
    @XmlElement
    private String zipCode;
    private String icCity = RandomUtils.randomName("City ÆæØøÅåß");
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
