package com.scalepoint.automation.pageobjects.dialogs;

import com.scalepoint.automation.pageobjects.extjs.ExtComboBox;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.utils.Wait;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.TextBlock;

import static com.scalepoint.automation.utils.Wait.waitForVisible;

/**
 * @author : igu
 */
public class NotCheapestChoiceDialog extends BaseDialog {

    @FindBy(id = "not-cheapest-reason-combo")
    private ExtComboBox reason;

    @FindBy(id = "not-cheapest-reason-ok-button")
    private Button ok;

    @FindBy(id = "not-cheapest-reason-valuation-amount")
    private TextBlock amount;

    @Override
    protected BaseDialog ensureWeAreAt() {
        Wait.waitForVisible(reason);
        Wait.waitForVisible(ok);

        return this;
    }

    public String getAmount() {
        return amount.getText();
    }

    public String selectAndGetFirstReasonValue() {
        reason.select(1);
        return reason.getValue();
    }

    public NotCheapestChoiceDialog selectFirstReason() {
        selectAndGetFirstReasonValue();
        return this;
    }

    public NotCheapestChoiceDialog selectReason(String visibleText){
        waitForVisible(reason);
        reason.select(visibleText);
        return this;
    }


    public String selectSecondReason() {
        reason.select(2);

        return reason.getValue();
    }

    public SettlementDialog ok() {
        ok.click();

        return at(SettlementDialog.class);
    }

    public SettlementPage okGoToSettlementPage() {
        ok.click();

        return Page.at(SettlementPage.class);
    }

    public boolean okButtonDoesNotCloseDialog() {
        ok.click();

        at(NotCheapestChoiceDialog.class);

        return true;
    }

}
