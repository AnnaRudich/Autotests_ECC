package com.scalepoint.automation.utils.data.entity.serviceTaskEntity.copy;

public class ValuationsImport extends Valuations{
    public ValuationsImport(){

    }

    public ValuationsImport(ServiceLineExport serviceLineExport){
        ValuationsImport valuationsImport = new ValuationsImport();
        ValuationsExport valuationsExport = serviceLineExport.getValuations();

        valuationsImport.setCustomerDemand(valuationsExport.getCustomerDemand());
        valuationsImport.setNewPrice(valuationsExport.getNewPrice());
        valuationsImport.setUsedPrice(valuationsExport.getUsedPrice());

    }
}
