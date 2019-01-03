package com.scalepoint.automation.utils.data.entity.rnv.serviceTask;

class ConvertItem {
    static ItemImport convertItem(ItemExport itemExport){
        ItemImport itemImport = new ItemImport();
        itemImport.setAge(itemExport.getAge());
        itemImport.setDescription("Description");
        itemImport.setQuantity(itemExport.getQuantity());
        itemImport.setServicePartnerNote("note");
        itemImport.setTotalDamage(false);
        return itemImport;
    }
}
