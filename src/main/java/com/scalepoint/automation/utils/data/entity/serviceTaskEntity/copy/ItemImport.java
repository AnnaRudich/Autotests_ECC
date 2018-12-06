package com.scalepoint.automation.utils.data.entity.serviceTaskEntity.copy;

import javax.xml.bind.annotation.XmlAttribute;

public class ItemImport extends Item{

    private String servicePartnerNote;
    private boolean totalDamage;

    public ItemImport(){}

    public ItemImport(String sePaNote, boolean totalDamage){
        this.servicePartnerNote = sePaNote;
        this.totalDamage = totalDamage;
    }

    public ItemImport(boolean totalDamage){
        this.totalDamage = totalDamage;
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
