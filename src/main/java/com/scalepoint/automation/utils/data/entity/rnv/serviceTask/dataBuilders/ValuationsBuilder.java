package com.scalepoint.automation.utils.data.entity.rnv.serviceTask.dataBuilders;

import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.data.entity.rnv.serviceTask.Valuations;

import java.math.BigDecimal;

public class ValuationsBuilder {
    private Valuations valuations;

    public ValuationsBuilder() {
        valuations = new Valuations();
        valuations.setRepairPrice(BigDecimal.valueOf(Constants.PRICE_50));
        valuations.setCustomerDemand(BigDecimal.valueOf(Constants.PRICE_500));
    }

    public Valuations build() {
        return valuations;
    }

    public ValuationsBuilder withRepairPrice(BigDecimal repairPrice) {
        this.valuations.setRepairPrice(repairPrice);
        return this;
    }
}
