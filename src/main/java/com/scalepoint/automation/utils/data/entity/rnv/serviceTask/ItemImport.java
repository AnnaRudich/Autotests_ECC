package com.scalepoint.automation.utils.data.entity.rnv.serviceTask;

import javax.xml.bind.annotation.XmlAttribute;

class ItemImport extends Item{

    private String servicePartnerNote;
    private boolean totalDamage;

    ItemImport(){}

    public ItemImport(ServiceLineExport serviceLineExport){

        ItemImport itemImport = new ItemImport();
        ItemExport itemExport = serviceLineExport.getItem();

        itemImport.setAge(itemExport.getAge());
        //itemImport.setDescription(itemExport.getDescription());
        itemImport.setDescription("Iphone");
        //itemImport.setOriginalDescription(itemExport.getOriginalDescription());
        itemImport.setOriginalDescription("originalDescription");
        itemImport.setQuantity(itemExport.getQuantity());
        this.servicePartnerNote = "sePaNote";
        this.totalDamage = true;
    }

    @XmlAttribute
    public String getServicePartnerNote() {
        return servicePartnerNote;
    }

    void setServicePartnerNote(String servicePartnerNote) {
        this.servicePartnerNote = servicePartnerNote;
    }

    @XmlAttribute
    public boolean isTotalDamage() {
        return totalDamage;
    }

    void setTotalDamage(boolean totalDamage) {
        this.totalDamage = totalDamage;
    }
}
