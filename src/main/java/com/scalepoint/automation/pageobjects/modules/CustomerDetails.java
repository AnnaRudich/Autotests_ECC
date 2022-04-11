package com.scalepoint.automation.pageobjects.modules;

import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.utils.OperationalUtils;
import org.openqa.selenium.support.FindBy;

public class CustomerDetails extends Module {

    @FindBy(xpath = "//td[9][contains(@class,'rejected-text')]")
    private SelenideElement cashValue;
    @FindBy(xpath = "//td[8][contains(@class,'rejected-text')]")
    private SelenideElement depreciation;
    @FindBy(xpath = "//td[4][contains(@class,'rejected-text')]")
    private SelenideElement voucherValue;
    @FindBy(xpath = "//td[contains(@class,'rejected-text')]//img")
    private SelenideElement iconToolTip;

    public Double getCashValue() {

        String value = cashValue.getText().replaceAll("[^0-9.,]+", "");
        return OperationalUtils.getDoubleValue(value);
    }

    public Double getVoucherValue() {

        String value = voucherValue.getText().replaceAll("[^0-9.,]+", "");
        return OperationalUtils.getDoubleValue(value);
    }

    public Double getFaceTooltipValue() {

        String tooltipText = (iconToolTip.getAttribute("title")).split("\\(")[0];
        String value = tooltipText.replaceAll("[^\\.,0123456789]", "");
        return OperationalUtils.getDoubleValue(value);
    }
}