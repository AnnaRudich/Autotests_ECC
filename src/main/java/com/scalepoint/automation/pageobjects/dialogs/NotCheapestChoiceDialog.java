package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.pageobjects.extjs.NotCheapestReasonExtComboBox;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.utils.OperationalUtils;
import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.TextBlock;

import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

/**
 * @author : igu
 */
public class NotCheapestChoiceDialog extends BaseDialog {

    @FindBy(id = "not-cheapest-reason-combo")
    private NotCheapestReasonExtComboBox reason;

    @FindBy(id = "not-cheapest-reason-ok-button")
    private WebElement ok;

    @FindBy(id = "not-cheapest-reason-valuation-amount")
    private TextBlock amount;

    @Override
    protected void ensureWeAreAt() {
        waitForAjaxCompletedAndJsRecalculation();
        $(reason).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
        $(ok).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
    }

    //TODO the price is not i18n, 1.00 instead pf 1,00
    public Double getAmount() {
        return Double.valueOf(amount.getText());
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
        hoverAndClick($(ok));
        return at(SettlementDialog.class);
    }

    public SettlementPage okGoToSettlementPage() {
        hoverAndClick($(ok));
        return Page.at(SettlementPage.class);
    }

    public NotCheapestChoiceDialog doAssert(Consumer<Asserts> assertFunc) {
        assertFunc.accept(new Asserts());
        return NotCheapestChoiceDialog.this;
    }

    public class Asserts {
        public Asserts assertMinimalValuationIsSuggested(Double expectedAmount) {
            OperationalUtils.assertEqualsDouble(getAmount(), expectedAmount, "Must be suggested expectedAmount: " + expectedAmount);
            return this;
        }

        public Asserts assertNotPossibleToCloseDialog() {
            try {
                hoverAndClick($(ok));
            } catch (Error e) {
                return this;
            }
            Assert.fail("We were able to close NotCheapestChoiceDialog");
            return this;
        }
    }

}
