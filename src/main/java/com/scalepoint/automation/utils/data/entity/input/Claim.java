package com.scalepoint.automation.utils.data.entity.input;

import com.scalepoint.automation.utils.RandomUtils;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * SystemUser: kke
 */
@Data
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Claim {

    private String title = RandomUtils.randomName("mr");
    private String lastName = RandomUtils.randomName("lname");
    private String firstName = RandomUtils.randomName("fname");
    private String fullName = firstName + " " + lastName;
    private String fullNameWithTitle = title + " " + firstName + " " + lastName;
    private String policyNumber = Integer.toString(RandomUtils.randomInt());
    private String policyType = "ECC";
    private String claimNumber = UUID.randomUUID().toString();
    private String phoneNumber = Integer.toString(RandomUtils.randomInt());
    private String damageDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    private String cellNumber;
    private String address = RandomUtils.randomName("addr");
    private String address2 = RandomUtils.randomName("addr2");
    private String city = RandomUtils.randomName("city");
    private String zipCode;
    private String email;
    //private String fullNameWithTitle = getTitle()+" " +getFirstName() + " " +getLastName();
    private String statusSaved;
    @XmlElement(name = "statusClosedEx")
    private String statusClosedExternally;
    private String statusCompleted;
    private String policyTypeTrygUser;
    private String policyTypeAF;
    private String policyTypeFF;
    private transient String claimId;

    @Override
    public String toString() {
        return "Client{" +
                "name='" + fullNameWithTitle + '\'' +
                ", claimNumber='" + claimNumber + '\'' +
                '}';
    }

}
