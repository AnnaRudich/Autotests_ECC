package com.scalepoint.automation.utils.data.entity.serviceTaskEntity.copy;

import javax.xml.bind.annotation.XmlAttribute;

public class BankExport {
    private String GIRO;

    @XmlAttribute
    public String getGIRO() {
        return GIRO;
    }

    public void setGIRO(String GIRO) {
        this.GIRO = GIRO;
    }
}
