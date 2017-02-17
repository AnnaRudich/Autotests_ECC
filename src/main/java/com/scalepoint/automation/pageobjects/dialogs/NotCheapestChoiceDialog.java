package com.scalepoint.automation.pageobjects.dialogs;

import com.scalepoint.automation.pageobjects.extjs.ExtComboBox;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.utils.OperationalUtils;
import com.scalepoint.automation.utils.Wait;
import org.junit.Assert;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.TextBlock;

import java.util.function.Consumer;

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

    public NotCheapestChoiceDialog doAssert(Consumer<Asserts> assertFunc) {
        assertFunc.accept(new Asserts());
        return NotCheapestChoiceDialog.this;
    }

    public class Asserts {
        public Asserts assertMinimalValuationIsSuggested(Double expectedAmount) {
            OperationalUtils.assertEqualsDouble(OperationalUtils.getDoubleValue(getAmount()), expectedAmount, "Must be suggested expectedAmount: "+expectedAmount);
            return this;
        }

        public Asserts assertNotPossibleToCloseDialog() {
            ok.click();
            try {
                at(NotCheapestChoiceDialog.class);
            } catch (Exception e) {
                Assert.fail("We were able to close NotCheapestChoiceDialog");
            }
            return this;
        }
    }

}
