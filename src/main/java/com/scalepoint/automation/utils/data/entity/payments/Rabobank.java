
package com.scalepoint.automation.utils.data.entity.payments;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for rabobank complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="rabobank">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cardNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "rabobank", propOrder = {
        "cardNumber"
})
public class Rabobank {

    @XmlElement(required = true)
    protected String cardNumber;

    /**
     * Gets the value of the cardNumber property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getCardNumber() {
        return cardNumber;
    }

    /**
     * Sets the value of the cardNumber property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCardNumber(String value) {
        this.cardNumber = value;
    }

}
