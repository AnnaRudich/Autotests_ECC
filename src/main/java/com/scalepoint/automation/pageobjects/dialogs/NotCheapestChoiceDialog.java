package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.extjs.NotCheapestReasonExtComboBox;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.utils.OperationalUtils;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.TextBlock;

import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

/**
 * @author : igu
 */
public class NotCheapestChoiceDialog extends BaseDialogSelenide {

    @Override
    protected void ensureWeAreAt() {

        waitForAjaxCompletedAndJsRecalculation();
        getReason().should(Condition.visible);
        ok.should(Condition.visible);
    }

    @FindBy(id = "not-cheapest-reason-ok-button")
    private SelenideElement ok;

    private NotCheapestReasonExtComboBox getReason(){

        return new NotCheapestReasonExtComboBox($(By.id("not-cheapest-reason-combo")));
    }

    private TextBlock getAmountTextBlock(){

        return new TextBlock($(By.id("not-cheapest-reason-valuation-amount")));
    }

    //TODO the price is not i18n, 1.00 instead pf 1,00
    public Double getAmount() {

        return Double.valueOf(getAmountTextBlock().getText());
    }

    public String selectAndGetFirstReasonValue() {

        getReason().select(1);
        return getReason().getValue();
    }

    public String selectSecondReason() {

        getReason().select(2);
        return getReason().getValue();
    }

    public SettlementDialog ok() {

        ok.click();
        return BaseDialog.at(SettlementDialog.class);
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

            OperationalUtils.assertEqualsDouble(getAmount(), expectedAmount, "Must be suggested expectedAmount: " + expectedAmount);
            return this;
        }

        public Asserts assertNotPossibleToCloseDialog() {

            try {

                ok.click();
            } catch (Error e) {

                return this;
            }

            Assert.fail("We were able to close NotCheapestChoiceDialog");
            return this;
        }
    }

}
