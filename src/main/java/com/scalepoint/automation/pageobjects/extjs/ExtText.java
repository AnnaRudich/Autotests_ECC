package com.scalepoint.automation.pageobjects.extjs;

import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.Actions;
import com.scalepoint.automation.utils.JavascriptHelper;
import com.scalepoint.automation.utils.Wait;
import org.openqa.selenium.WebElement;

import static com.codeborne.selenide.Selenide.$;

public class ExtText extends ExtElement implements Actions {

    public ExtText(WebElement wrappedElement) {
        super(wrappedElement);
    }

    public void enter(String value) {
        SelenideElement element = $(getWrappedElement());
        hoverAndClickNoWait(element);
        sendKeys(value);
        JavascriptHelper.blur();
        Wait.waitForAjaxCompletedAndJsRecalculation();
    }

    public void clear() {
        getWrappedElement().clear();
    }

    public String getText() {
        return getWrappedElement().getAttribute("value");
    }
}
