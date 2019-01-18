package com.scalepoint.automation.utils.data.entity.rnv.serviceTask.dataBuilders;

import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.data.entity.rnv.serviceTask.Valuations;

import java.math.BigDecimal;

public class ValuationsBuilder {
    private Valuations valuations;

    public Valuations setDefault(){
        valuations = new Valuations();
        valuations.setRepairPrice(BigDecimal.valueOf(Constants.PRICE_50));
        valuations.setCustomerDemand(BigDecimal.valueOf(Constants.PRICE_500));
        return valuations;
    }
}
