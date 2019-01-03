package com.scalepoint.automation.utils.data.entity.rnv.serviceTask;

class ValuationsImport extends Valuations{
    private ValuationsImport(){}

    ValuationsImport(ServiceLineExport serviceLineExport){
        ValuationsImport valuationsImport = new ValuationsImport();
        ValuationsExport valuationsExport = serviceLineExport.getValuations();

        valuationsImport.setCustomerDemand(valuationsExport.getCustomerDemand());
        valuationsImport.setNewPrice(valuationsExport.getNewPrice());
        valuationsImport.setUsedPrice(valuationsExport.getUsedPrice());
    }
}
