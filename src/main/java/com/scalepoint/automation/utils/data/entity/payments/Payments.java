
package com.scalepoint.automation.utils.data.entity.payments;

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
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "payments", propOrder = {
        "masterCard",
        "dankort",
        "rabobank"
})
@XmlRootElement
public class Payments {

    @XmlElement(required = true)
    protected MasterCard masterCard;
    @XmlElement(required = true)
    protected Dankort dankort;
    @XmlElement(required = true)
    protected Rabobank rabobank;

    /**
     * Gets the value of the masterCard property.
     *
     * @return possible object is
     * {@link MasterCard }
     */
    public MasterCard getMasterCard() {
        return masterCard;
    }

    /**
     * Sets the value of the masterCard property.
     *
     * @param value allowed object is
     *              {@link MasterCard }
     */
    public void setMasterCard(MasterCard value) {
        this.masterCard = value;
    }

    /**
     * Gets the value of the dankort property.
     *
     * @return possible object is
     * {@link Dankort }
     */
    public Dankort getDankort() {
        return dankort;
    }

    /**
     * Sets the value of the dankort property.
     *
     * @param value allowed object is
     *              {@link Dankort }
     */
    public void setDankort(Dankort value) {
        this.dankort = value;
    }

    /**
     * Gets the value of the rabobank property.
     *
     * @return possible object is
     * {@link Rabobank }
     */
    public Rabobank getRabobank() {
        return rabobank;
    }

    /**
     * Sets the value of the rabobank property.
     *
     * @param value allowed object is
     *              {@link Rabobank }
     */
    public void setRabobank(Rabobank value) {
        this.rabobank = value;
    }

}
