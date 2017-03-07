package com.scalepoint.automation.pageobjects.dialogs;

import com.scalepoint.automation.pageobjects.extjs.ExtComboBox;
import com.scalepoint.automation.pageobjects.extjs.ExtText;
import com.scalepoint.automation.utils.OperationalUtils;
import com.scalepoint.automation.utils.Wait;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;

public class AddValuationDialog extends BaseDialog {

    @FindBy(id = "add-manual-valuation-type-combo")
    private ExtComboBox addValuationType;

    @FindBy(id = "add-manual-valuation-price-input")
    private ExtText addValuationPrice;

    @FindBy(id = "add-manual-valuation-ok-button")
    private Button ok;

    @Override
    protected BaseDialog ensureWeAreAt() {
        Wait.waitForVisible(addValuationType);
        Wait.waitForVisible(addValuationPrice);
        Wait.waitForVisible(ok);
        return this;
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
