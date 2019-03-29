package com.scalepoint.automation.utils.data.entity.rnv.serviceTask;

import javax.xml.bind.annotation.XmlAttribute;

public class BankExport extends Bank {
    private String GIRO;

    @XmlAttribute
    public String getGIRO() {
        return GIRO;
    }

    public void setGIRO(String GIRO) {
        this.GIRO = GIRO;
    }
}
