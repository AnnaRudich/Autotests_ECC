package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.extjs.ExtCheckboxTypeDiv;
import com.scalepoint.automation.pageobjects.extjs.ExtComboBoxBoundView;
import com.scalepoint.automation.pageobjects.extjs.ExtText;
import com.scalepoint.automation.pageobjects.pages.BaseClaimPage;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.utils.OperationalUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompleted;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;
import static org.assertj.core.api.Assertions.assertThat;

public class SettlementGroupDialog extends BaseDialog {

    @Override
    protected void ensureWeAreAt() {

        waitForAjaxCompletedAndJsRecalculation();
        saveGroup.should(Condition.visible);
    }

    @FindBy(name = "groupName")
    private SelenideElement groupName;
    @FindBy(name = "customerDemand")
    private SelenideElement customerDemand;
    @FindBy(name = "newPrice")
    private SelenideElement newPrice;
    @FindBy(name = "valuation")
    private SelenideElement valuation;
    @FindBy(xpath = "//*[contains(@id, 'average-age-field-inputEl')]")
    private SelenideElement averageAge;
    @FindBy(xpath = "//*[contains(@id, 'group-save-button-btnEl')]")
    private SelenideElement saveGroup;
    @FindBy(xpath = "//*[contains(@id, 'group-close-button-btnInnerEl')]")
    private SelenideElement closeGroup;

    private ExtCheckboxTypeDiv getShowLineAmountsInMail(){

        return new ExtCheckboxTypeDiv($(By.xpath("//*[contains(@id, 'show-line-amounts-in-mails-checkbox-inputEl')]")));
    }

    private ExtComboBoxBoundView getReason(){

        return new ExtComboBoxBoundView($(By.name("reason")));
    }

    private ExtText getNote(){

        return new ExtText($(By.name("note")));
    }

    private ExtCheckboxTypeDiv getIncludeInClaim(){

        return new ExtCheckboxTypeDiv($(By.xpath("//*[contains(@id, 'include-in-claim-checkbox-inputEl')]")));
    }

    public SettlementGroupDialog enterGroupName(String name) {

        groupName.setValue(name);
        return this;
    }

    public SettlementGroupDialog chooseType(GroupTypes type) {

        type.getRadioButton().click();
        return this;
    }

    public SettlementGroupDialog enterNewPrice(Double newPrice) {

        this.newPrice.setValue(OperationalUtils.toStringWithComma(newPrice));
        return this;
    }

    public SettlementGroupDialog enterCustomerDemand(Double customerDemand) {

        this.customerDemand.setValue(OperationalUtils.toStringWithComma(customerDemand));
        return this;
    }

    public SettlementGroupDialog enterValuation(Double valuation) {

        this.valuation.setValue(OperationalUtils.toStringWithComma(valuation));
        return this;
    }

    public SettlementGroupDialog enterNote(String note) {

        getNote().enter(note);
        return this;
    }

    public SettlementGroupDialog selectFirstReason() {

        getReason().click();
        List<WebElement> options = driver.findElements(By.xpath("//li[@class='x-boundlist-item']"));
        options.get(1).click();
        return this;
    }

    public SettlementGroupDialog clickSave() {

        saveGroup.click();
        return this;
    }

    public SettlementPage saveGroup() {

        saveGroup.click();
        waitForAjaxCompleted();
        return BaseClaimPage.at(SettlementPage.class);
    }

    public SettlementGroupDialog clearNewPriceField() {

        newPrice.clear();
        return this;
    }

    public SettlementGroupDialog clearCustomerDemand() {

        customerDemand.clear();
        return this;
    }

    public enum GroupTypes {

        VALUATION($(By.xpath("//*[contains(@id, 'valuation-group-type-radio-displayEl')]"))),
        OVERVIEW($(By.xpath("//*[contains(@id, 'overview-group-type-radio-displayEl')]")));

        private WebElement element;

        GroupTypes(WebElement element) {
            this.element = element;
        }

        public WebElement getRadioButton() {
            return element;
        }
    }

    public SettlementGroupDialog doAssert(Consumer<SettlementGroupDialog.Asserts> func) {

        func.accept(new Asserts());
        return SettlementGroupDialog.this;
    }

    public class Asserts {

        public Asserts assertAverageAgeIs(String age) {

            assertThat(averageAge.getText()).isEqualToIgnoringCase(age);
            return this;
        }

        public Asserts assertIsOverviewChecked() {

            assertThat(GroupTypes.OVERVIEW.getRadioButton().findElement(By.xpath("./../input")).getAttribute("aria-checked")).isEqualToIgnoringCase("true");
            return this;
        }

        public Asserts assertIsValuationChecked() {

            assertThat(GroupTypes.VALUATION.getRadioButton().findElement(By.xpath("./../input")).getAttribute("aria-checked")).isEqualToIgnoringCase("true");
            return this;
        }

        public Asserts assertIsNewPriceFiledDisabled() {

            assertThat(newPrice.attr("aria-disabled")).isEqualToIgnoringCase("true");
            return this;
        }

        public Asserts assertIsCustomerDemandFiledDisabled() {

            assertThat(customerDemand.attr("aria-disabled")).isEqualToIgnoringCase("true");
            return this;
        }

        public Asserts assertIsReasonFiledDisabled() {

            assertThat(getReason().getAttribute("aria-disabled")).isEqualToIgnoringCase("true");
            return this;
        }

        public Asserts assertIsValuationFiledDisabled() {

            assertThat(valuation.attr("aria-disabled")).isEqualToIgnoringCase("true");
            return this;
        }

        public Asserts assertIsNewPriceFiledEnabled() {

            assertThat(newPrice.attr("aria-disabled")).isEqualToIgnoringCase("false");
            return this;
        }

        public Asserts assertIsCustomerDemandFiledEnabled() {

            assertThat(customerDemand.attr("aria-disabled")).isEqualToIgnoringCase("false");
            return this;
        }

        public Asserts assertIsReasonFiledEnabled() {

            assertThat(getReason().getAttribute("aria-disabled")).isEqualToIgnoringCase("false");
            return this;
        }

        public Asserts assertIsValuationFiledEnabled() {

            assertThat(valuation.attr("aria-disabled")).isEqualToIgnoringCase("false");
            return this;
        }

        public Asserts assertIsIncludeInClaimChecked() {

            assertThat(getIncludeInClaim().isChecked());
            return this;
        }

        public Asserts assertIsShowLineAmountInMailChecked() {

            assertThat(getShowLineAmountsInMail().isChecked());
            return this;
        }

        public Asserts assertIsValuationRequired() {

            assertThat(valuation.attr("aria-invalid")).isEqualToIgnoringCase("true");
            return this;
        }

        public Asserts assertIsReasonRequired() {

            assertThat(getReason().getAttribute("aria-invalid")).isEqualToIgnoringCase("true");
            return this;
        }

        public Asserts assertReasonIsNotVisible() {

            assertThat(isDisplayed(getReason())).isFalse();
            return this;
        }

        public Asserts assertNewPriceIsRequired() {

            assertThat(newPrice.attr("aria-invalid")).isEqualToIgnoringCase("true");
            return this;
        }

        public Asserts assertCustomerDemandIsRequired() {

            assertThat(customerDemand.attr("aria-invalid")).isEqualToIgnoringCase("true");
            return this;
        }
    }
}
