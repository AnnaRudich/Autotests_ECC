
package com.scalepoint.automation.utils.data.entity.payments;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for dankort complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="dankort">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="number" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="expMonth" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="expYear" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="cvc" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dankort", propOrder = {
        "number",
        "expMonth",
        "expYear",
        "cvc"
})
public class Dankort {

    @XmlElement(required = true)
    protected String number;
    @XmlElement(required = true)
    protected String expMonth;
    @XmlElement(required = true)
    protected String expYear;
    @XmlElement(required = true)
    protected String cvc;

}
