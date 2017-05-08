package com.scalepoint.automation.utils.data.entity;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "exisitingSuppliers", propOrder = {
        "simpleSuppliers"
})
@XmlRootElement(name="exisitingSuppliers")
public class ExistingSuppliers {

    @XmlElement(name = "simpleSupplier")
    protected List<SimpleSupplier> simpleSuppliers;

    public List<SimpleSupplier> getSuppliers() {
        if (simpleSuppliers == null) {
            simpleSuppliers = new ArrayList<>();
        }
        return this.simpleSuppliers;
    }
}
