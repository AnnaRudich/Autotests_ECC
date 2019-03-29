package com.scalepoint.automation.utils.data.entity.rnv.serviceTask;

import javax.xml.bind.annotation.XmlElement;

public class ServiceLineImport extends ServiceLine {

    private ItemImport item;
    private Valuations valuations;

    public ServiceLineImport() {
    }


    @XmlElement
    public ItemImport getItem() {
        return item;
    }

    public void setItem(ItemImport item) {
        this.item = item;
    }

    @XmlElement
    public Valuations getValuations() {
        return valuations;
    }

    public void setValuations(Valuations valuations) {
        this.valuations = valuations;
    }
}
