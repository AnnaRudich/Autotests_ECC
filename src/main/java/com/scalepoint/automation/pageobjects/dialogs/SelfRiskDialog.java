package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

public class SelfRiskDialog extends BaseDialogSelenide {

    ElementsCollection buttons = $(DIALOG_PATH).findAll("[id^=toolbar] a");

    private static final By DIALOG_PATH = By.cssSelector("[role=dialog][aria-hidden=false]");
    private static final By HEADER_PATH = By.cssSelector("[id$=header]");
    private static final By TEXTBOX_PATH = By.cssSelector("input");

    @Override
    protected void ensureWeAreAt() {

        waitForAjaxCompletedAndJsRecalculation();
        $(DIALOG_PATH).find(HEADER_PATH)
                .should(Condition.exactText("Rediger selvrisiko"));
    }

    public SelfRiskDialog setSelfRisk(String value){

        $(DIALOG_PATH).find(TEXTBOX_PATH)
                .setValue(value);
        return this;
    }

    public SettlementPage clickOkButton(){

        hoverAndClick(buttons.find(Condition.exactText("OK")));
        return Page.at(SettlementPage.class);
    }
}
