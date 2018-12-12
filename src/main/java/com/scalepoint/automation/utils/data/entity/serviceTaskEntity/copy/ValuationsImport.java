package com.scalepoint.automation.utils.data.entity.serviceTaskEntity.copy;

public class ValuationsImport extends Valuations{
    public ValuationsImport(){

    }
//    public ItemImport(ServiceLineExport serviceLineExport){
//
//        ItemImport itemImport = new ItemImport();
//        ItemExport itemExport = serviceLineExport.getItem();
//
//        itemImport.setAge(itemExport.getAge());
//        itemImport.setDescription(itemExport.getDescription());
//        itemImport.setOriginalDescription(itemExport.getOriginalDescription());
//        itemImport.setQuantity(itemExport.getQuantity());
//        this.servicePartnerNote = "sePaNote";
//        this.totalDamage = true;
//    }

    public ValuationsImport(ServiceLineExport serviceLineExport){
        ValuationsImport valuationsImport = new ValuationsImport();
        ValuationsExport valuationsExport = serviceLineExport.getValuations();

        valuationsImport.setCustomerDemand(valuationsExport.getCustomerDemand());
        valuationsImport.setNewPrice(valuationsExport.getNewPrice());
        valuationsImport.setUsedPrice(valuationsExport.getUsedPrice());

    }
}
