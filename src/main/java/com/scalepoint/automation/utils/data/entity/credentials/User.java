
package com.scalepoint.automation.utils.data.entity.credentials;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.*;


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
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
//@XmlType(name = "user", propOrder = {
//        "companyCode"
//})
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
    @XmlAttribute(name = "type")
    protected UserType type;

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
    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", companyCode='" + companyCode + '\'' +
                '}';
    }
    @XmlEnum()
    public enum UserType{
        @XmlEnumValue("BASIC")
        BASIC,
        @XmlEnumValue("SYSTEM")
        SYSTEM,
        @XmlEnumValue("SCALEPOINT_ID")
        SCALEPOINT_ID,
        @XmlEnumValue("EXCEPTIONAL")
        EXCEPTIONAL
    }
}
