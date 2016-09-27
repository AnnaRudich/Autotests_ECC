package com.scalepoint.automation.pageobjects.modules;

import com.scalepoint.automation.utils.OperationalUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CustomerDetails extends Module {

    @FindBy(xpath = "//td[9][contains(@class,'rejected-text')]")
    private WebElement cashValue;

    @FindBy(xpath = "//td[8][contains(@class,'rejected-text')]")
    private WebElement depreciation;

    @FindBy(xpath = "//td[4][contains(@class,'rejected-text')]")
    private WebElement faceValue;

    @FindBy(xpath = "//td[contains(@class,'rejected-text')]//img")
    private WebElement iconToolTip;

    public Double getCashValue() {
        String value = cashValue.getText().replaceAll("[^0-9.,]+", "");
        return OperationalUtils.getDoubleValue(value);
    }

    public Double getFaceValue() {
        String value = faceValue.getText().replaceAll("[^0-9.,]+", "");
        return OperationalUtils.getDoubleValue(value);
    }

    public Double getFaceTooltipValue() {
        String tooltipText = (iconToolTip.getAttribute("title")).split("\\(")[0];
        String value = tooltipText.replaceAll("[^\\.,0123456789]", "");
        return OperationalUtils.getDoubleValue(value);
    }

    public static Double doubleString(String s) throws NumberFormatException {
        return Double.parseDouble(s);
    }
}