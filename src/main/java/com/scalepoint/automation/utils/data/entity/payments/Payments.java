
package com.scalepoint.automation.utils.data.entity.payments;

import lombok.Data;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for payments complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="payments">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="masterCard" type="{}masterCard"/>
 *         &lt;element name="dankort" type="{}dankort"/>
 *         &lt;element name="rabobank" type="{}rabobank"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@XmlType(name = "payments", propOrder = {
        "masterCard",
        "dankort",
        "rabobank"
})
public class Payments {

    @XmlElement(required = true)
    protected MasterCard masterCard;
    @XmlElement(required = true)
    protected Dankort dankort;
    @XmlElement(required = true)
    protected Rabobank rabobank;

}
