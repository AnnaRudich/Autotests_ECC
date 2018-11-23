package com.scalepoint.automation.utils.data.entity.serviceTaskEntity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class ServicePartner {
    @XmlAttribute
    private String serviceAgreementName;

    @XmlAttribute
    private String address1;

    @XmlAttribute
    private String address2;

    @XmlAttribute
    private String city;

    @XmlAttribute
    private String cvrNumber;

    @XmlAttribute
    private String email;

    @XmlAttribute
    private String name;

    @XmlAttribute
    private String phone;

    @XmlAttribute
    private String postalCode;

    @XmlElement
    private Bank bank;

    @XmlElement
    private Location location;



    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getServiceAgreementName() {
        return serviceAgreementName;
    }

    public void setServiceAgreementName(String serviceAgreementName) {
        this.serviceAgreementName = serviceAgreementName;
    }

    public String getCvrNumber() {
        return cvrNumber;
    }

    public void setCvrNumber(String cvrNumber) {
        this.cvrNumber = cvrNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "ClassPojo [phone = " + phone + ", postalCode = " + postalCode + ", serviceAgreementName = " + serviceAgreementName + ", cvrNumber = " + cvrNumber + ", email = " + email + ", location = " + location + ", name = " + name + ", address1 = " + address1 + ", address2 = " + address2 + ", bank = " + bank + ", city = " + city + "]";
    }
}
