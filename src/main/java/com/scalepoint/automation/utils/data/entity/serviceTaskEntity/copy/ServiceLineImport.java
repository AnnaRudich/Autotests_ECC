package com.scalepoint.automation.utils.data.entity.serviceTaskEntity.copy;

import javax.xml.bind.annotation.XmlElement;

public class ServiceLineImport extends ServiceLine{

    private ItemImport item;
    private ValuationsImport valuations;

    public ServiceLineImport(){}

    public ServiceLineImport(ItemImport item, ValuationsImport valuations){
//        super.getLineIndex();
//        super.getLineGUID();
//        super.getCategory();
//        super.getTaskType();
        this.item = item;
        this.valuations = valuations;
    }

    @XmlElement
    public ItemImport getItem() {
        return item;
    }

    public void setItem(ItemImport item) {
        this.item = item;
    }

    @XmlElement
    public ValuationsImport getValuations() {
        return valuations;
    }

    public void setValuations(ValuationsImport valuations) {
        this.valuations = valuations;
    }
}
