package com.scalepoint.automation.utils.data.request;

import com.scalepoint.automation.utils.data.entity.eccIntegration.ValuationTypes;
import lombok.Data;

import javax.xml.bind.annotation.*;

/**
 * Created by bza on 6/28/2017.
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Valuation")
public class Valuation {

    @XmlAttribute
    private String id;
    @XmlAttribute
    private String hidden;
    @XmlAttribute
    private String baseValuation;
    @XmlElement(name = "Price")
    private Price[] Price;
    @XmlAttribute
    private String dirty;
    @XmlAttribute
    private String valuationType;
    @XmlAttribute
    private String active;
    @XmlAttribute
    private String priceAfterAllDeductions;
    @XmlElement(name = "VoucherReplacement")
    private VoucherReplacement VoucherReplacement;
    @XmlAttribute
    private String quantity;
    @XmlAttribute
    private String preDepreciation;
    @XmlElement( name = "ProductMatch")
    private ProductMatch ProductMatch;

    @Override
    public String toString() {
        return "ClassPojo [id = " + id + ", hidden = " + hidden + ", baseValuation = " + baseValuation + ", Price = " + Price + ", dirty = " + dirty + ", valuationType = " + valuationType + ", active = " + active + ", priceAfterAllDeductions = " + priceAfterAllDeductions + ", VoucherReplacement = " + VoucherReplacement + ", quantity = " + quantity + ", preDepreciation = " + preDepreciation + "]";
    }

}
