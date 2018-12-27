package com.scalepoint.automation.utils.data.entity.serviceTaskEntity.copy;

public class ConvertItem {
    public static ItemImport convertItem(ItemExport itemExport){
        ItemImport itemImport = new ItemImport();
        itemImport.setAge(itemExport.getAge());
        itemImport.setOriginalDescription("OrDescription1");
        itemImport.setDescription("Description");
        itemImport.setQuantity(itemExport.getQuantity());
        itemImport.setServicePartnerNote("note");
        itemImport.setTotalDamage(false);
        return itemImport;
    }
}
