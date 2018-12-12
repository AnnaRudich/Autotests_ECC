package com.scalepoint.automation.utils.data.entity.serviceTaskEntity.copy;

import javax.xml.bind.annotation.XmlAttribute;

public class ItemImport extends Item{

    private String servicePartnerNote;
    private boolean totalDamage;

    public ItemImport(){}

    public ItemImport(ServiceLineExport serviceLineExport){

        ItemImport itemImport = new ItemImport();
        ItemExport itemExport = serviceLineExport.getItem();

        itemImport.setAge(itemExport.getAge());
        itemImport.setDescription(itemExport.getDescription());
        itemImport.setOriginalDescription(itemExport.getOriginalDescription());
        itemImport.setQuantity(itemExport.getQuantity());
        this.servicePartnerNote = "sePaNote";
        this.totalDamage = true;
    }

    @XmlAttribute
    public String getServicePartnerNote() {
        return servicePartnerNote;
    }

    public void setServicePartnerNote(String servicePartnerNote) {
        this.servicePartnerNote = servicePartnerNote;
    }

    @XmlAttribute
    public boolean isTotalDamage() {
        return totalDamage;
    }

    public void setTotalDamage(boolean totalDamage) {
        this.totalDamage = totalDamage;
    }
}
