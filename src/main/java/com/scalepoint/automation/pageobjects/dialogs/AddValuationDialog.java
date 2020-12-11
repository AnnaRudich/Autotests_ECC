package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.pageobjects.extjs.ExtComboBoxBoundView;
import com.scalepoint.automation.pageobjects.extjs.ExtText;
import com.scalepoint.automation.utils.OperationalUtils;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

public class AddValuationDialog extends BaseDialog {

    @FindBy(id = "add-manual-valuation-type-combo")
    private ExtComboBoxBoundView addValuationType;

    @FindBy(id = "add-manual-valuation-price-input")
    private ExtText addValuationPrice;

    @FindBy(id = "add-manual-valuation-ok-button")
    private Button ok;

    @Override
    protected void ensureWeAreAt() {
        waitForAjaxCompletedAndJsRecalculation();
        $(addValuationType).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
    }

    public SettlementDialog addValuation(String valuationName, Double priceAmount) {
        return addValuationType(valuationName)
                .addValuationPrice(priceAmount)
                .closeValuationDialogWithOk();
    }

    public AddValuationDialog addValuationType(String valuationType) {
        addValuationType.select(valuationType);
        return this;
    }

    public AddValuationDialog addValuationPrice(Double valuationPrice) {
        addValuationPrice.enter(OperationalUtils.format(valuationPrice));
        return this;
    }

    public SettlementDialog closeValuationDialogWithOk() {
        ok.click();
        return at(SettlementDialog.class);
    }
}
