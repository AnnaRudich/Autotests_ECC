
package com.scalepoint.automation.utils.data.entity.payments;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for masterCard complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="masterCard">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cardNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="expMonth" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="expYear" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="cardHolderName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="cvc" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "masterCard", propOrder = {
        "cardNumber",
        "expMonth",
        "expYear",
        "cardHolderName",
        "cvc"
})
public class MasterCard {

    @XmlElement(required = true)
    protected String cardNumber;
    @XmlElement(required = true)
    protected String expMonth;
    @XmlElement(required = true)
    protected String expYear;
    @XmlElement(required = true)
    protected String cardHolderName;
    @XmlElement(required = true)
    protected String cvc;

}
