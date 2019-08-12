
package com.scalepoint.automation.utils.data.entity.credentials;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for user complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="user">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *       &lt;attribute name="login" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="password" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="companyCode" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="role" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "user", propOrder = {
        "companyCode"
})
public class User {

    @XmlAttribute(name = "login")
    protected String login;
    @XmlAttribute(name = "password")
    protected String password;
    @XmlAttribute(name = "companyCode")
    protected String companyCode;
    @XmlAttribute(name = "companyName")
    protected String companyName;
    @XmlAttribute(name = "companyId")
    protected Integer companyId;
    @XmlAttribute(name = "ftId")
    protected Integer ftId;
    @XmlAttribute(name = "basic")
    protected boolean basic;
    @XmlAttribute(name = "system")
    protected boolean system;
    @XmlAttribute(name = "fraudAlert")
    protected boolean fraudAlert;

    public User() {
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    /**
     * Gets the value of the login property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getLogin() {
        return login;
    }

    /**
     * Sets the value of the login property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setLogin(String value) {
        this.login = value;
    }

    /**
     * Gets the value of the password property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the value of the password property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPassword(String value) {
        this.password = value;
    }

    /**
     * Gets the value of the companyCode property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getCompanyCode() {
        return companyCode;
    }

    /**
     * Sets the value of the companyCode property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCompanyCode(String value) {
        this.companyCode = value;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getFtId() {
        return ftId;
    }

    public void setFtId(Integer ftId) {
        this.ftId = ftId;
    }

    /**
     * Gets the value of the basic property.
     *
     * @return possible object is
     * {@link String }
     */
    public boolean isBasic() {
        return basic;
    }

    /**
     * Sets the value of the basic property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setBasic(boolean value) {
        this.basic = value;
    }

    public boolean isSystem() {
        return system;
    }

    public boolean isFraudAlert() {
        return fraudAlert;
    }

    public void setSystem(boolean system) {
        this.system = system;
    }

    public void setFraudAlert(boolean fraudAlert) {
        this.fraudAlert = fraudAlert;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", companyCode='" + companyCode + '\'' +
                '}';
    }
}
