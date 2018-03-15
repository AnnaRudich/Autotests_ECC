package com.scalepoint.automation.utils.data.entity;

import com.scalepoint.automation.utils.RandomUtils;

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
    @XmlElement
    private String cellNumber;
    private String address = RandomUtils.randomName("addr");
    private String address2 = RandomUtils.randomName("addr2");
    private String city = RandomUtils.randomName("city");
    @XmlElement
    private String zipCode;

    @XmlElement
    private String email;
    //private String fullNameWithTitle = getTitle()+" " +getFirstName() + " " +getLastName();
    @XmlElement
    private String statusSaved;
    @XmlElement
    private String statusCancelled;
    @XmlElement
    private String statusClosedEx;
    @XmlElement
    private String accessMailSSText;
    @XmlElement
    private String statusCompleted;
    @XmlElement
    private String oldClaimDate;
    @XmlElement
    private String companyName;
    @XmlElement
    private String policyTypeTrygUser;
    @XmlElement
    private String policyTypeAF;
    @XmlElement
    private String policyTypeFF;

    public String getStatusCompleted() {
        return statusCompleted;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getClaimNumber() {
        return claimNumber;
    }

    public void setClaimNumber(String claimNumber) {
        this.claimNumber = claimNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCellNumber() {
        return cellNumber;
    }

    public void setCellPhoneNumber(String cell_number) {
        this.cellNumber = cell_number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress2() {
        return address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFullNameWithTitle() {
        return fullNameWithTitle;
    }

    public void setFullNameWithTitle(String fullNameWithTitle) {
        this.fullNameWithTitle = fullNameWithTitle;
    }

    public String getStatusSaved() {
        return statusSaved;
    }

    public String getStatusCancelled() {
        return statusCancelled;
    }

    public String getStatusClosedExternally() {
        return statusClosedEx;
    }

    public String getAccessMailSSText() {
        return accessMailSSText;
    }

    public String getComplexClaimNumber() {
        return RandomUtils.randomName("AutotestClaim") + "-" + RandomUtils.randomInt();
    }

    public String getOldClaimDate() {
        return oldClaimDate;
    }

    public String getPolicyType() {
        return policyType;
    }

    public void setPolicyType(String policyType) {
        this.policyType = policyType;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getPolicyTypeTrygUser() {
        return policyTypeTrygUser;
    }

    public String getPolicyTypeAF() {
        return policyTypeAF;
    }

    public String getPolicyTypeFF() {
        return policyTypeFF;
    }

    public String getDamageDate() {
        return damageDate;
    }

    public Claim setDamageDate(String damageDate) {
        this.damageDate = damageDate;
        return this;
    }

    @Override
    public String toString() {
        return "Client{" +
                "name='" + fullNameWithTitle + '\'' +
                ", claimNumber='" + claimNumber + '\'' +
                '}';
    }
}
