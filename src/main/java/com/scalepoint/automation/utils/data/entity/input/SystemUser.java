package com.scalepoint.automation.utils.data.entity.input;

import com.scalepoint.automation.utils.RandomUtils;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * SystemUser: kke
 */
@Data
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SystemUser {

    private String login = RandomUtils.randomName("Autotest");
    private String password = "!Q@W#E$R5t6t!" + RandomUtils.randomInt(99);
    private String firstName = RandomUtils.randomName("AutotestFirst");
    private String lastName = RandomUtils.randomName("AutotestSecond");
    private String email = RandomUtils.randomName("autotest") + "@scalepoint.com";
    @XmlElement
    private String company;
    @XmlElement
    private String department;

}
