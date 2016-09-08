package com.scalepoint.automation.utils.data.entity;

import com.scalepoint.automation.utils.RandomUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * NewSystemUser: kke
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class NewSystemUser {

    private String login = RandomUtils.randomName("jhon");
    private String password = RandomUtils.randomName("!2Qpasso");
    private String firstName = RandomUtils.randomName("fname");
    private String lastName = RandomUtils.randomName("lname");
    private String email = RandomUtils.randomName("Tmail") + "@scalepoint.com";

    @XmlElement
    private String company;
    @XmlElement
    private String companyIC;
    @XmlElement
    private String department;
    @XmlElement
    private String departmentIC;
    @XmlElement
    private String subdepartmentIC;


    public String getCompany() {
        return company;
    }

    public String getDepartment() {
        return department;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getCompanyIC() {
        return companyIC;
    }

    public void setCompanyIC(String companyIC) {
        this.companyIC = companyIC;
    }

    public void setCompanyDepartment(String department) {
        this.department = department;
    }

    public String getDepartmentIC() {
        return departmentIC;
    }

    public String getSubdepartmentIC() {
        return subdepartmentIC;
    }
}
