package com.scalepoint.automation.utils.data.entity.rnv.serviceTask;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;


@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemImport extends Item {

    @XmlAttribute
    private String servicePartnerNote;
    @XmlAttribute
    private boolean totalDamage;

    public ItemImport() {
    }

    public ItemImport(ServiceLineExport serviceLineExport) {

        ItemImport itemImport = new ItemImport();
        ItemExport itemExport = serviceLineExport.getItem();

        itemImport.setAge(itemExport.getAge());
        itemImport.setDescription("Description");
        itemImport.setOriginalDescription("originalDescription");
        itemImport.setQuantity(itemExport.getQuantity());
        this.servicePartnerNote = "sePaNote";
        this.totalDamage = true;
    }

}
