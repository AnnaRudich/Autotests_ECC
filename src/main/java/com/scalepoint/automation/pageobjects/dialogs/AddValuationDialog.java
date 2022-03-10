package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.extjs.AddManualValuationTypeComboBox;
import com.scalepoint.automation.pageobjects.extjs.ExtText;
import com.scalepoint.automation.utils.OperationalUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

public class AddValuationDialog extends BaseDialogSelenide {

    @Override
    protected void ensureWeAreAt() {

        waitForAjaxCompletedAndJsRecalculation();
        getAddValuationType().should(Condition.visible);
    }

    @FindBy(id = "add-manual-valuation-ok-button")
    private SelenideElement ok;

    private AddManualValuationTypeComboBox getAddValuationType(){

        return new AddManualValuationTypeComboBox($(By.id("add-manual-valuation-type-combo")));
    }

    private ExtText getAddValuationPrice(){

        return new ExtText($(By.id("add-manual-valuation-price-input")));
    }

    public SettlementDialog addValuation(String valuationName, Double priceAmount) {
        return addValuationType(valuationName)
                .addValuationPrice(priceAmount)
                .closeValuationDialogWithOk();
    }

    public AddValuationDialog addValuationType(String valuationType) {
        getAddValuationType().select(valuationType);
        return this;
    }

    public AddValuationDialog addValuationPrice(Double valuationPrice) {
        getAddValuationPrice().enter(OperationalUtils.format(valuationPrice));
        return this;
    }

    public SettlementDialog closeValuationDialogWithOk() {
        ok.click();
        return BaseDialog.at(SettlementDialog.class);
    }
}
