package com.scalepoint.automation.pageobjects.pages;

import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.extjs.ExtCheckbox;
import com.scalepoint.automation.pageobjects.extjs.ExtComboBox;
import com.scalepoint.automation.pageobjects.extjs.ExtInput;
import com.scalepoint.automation.pageobjects.extjs.ExtText;
import com.scalepoint.automation.utils.OperationalUtils;
import com.scalepoint.automation.utils.Wait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;

import java.util.List;
import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static org.assertj.core.api.Assertions.assertThat;

public class SettlementGroupDialog extends BaseDialog {

    @Override
    protected BaseDialog ensureWeAreAt() {
        Wait.waitForAjaxCompleted();
        return this;
    }

    @FindBy(name = "groupName")
    private ExtInput groupName;

    @FindBy(name = "customerDemand")
    private ExtInput customerDemand;

    @FindBy(name = "newPrice")
    private ExtInput newPrice;

    @FindBy(name = "valuation")
    private ExtInput valuation;

    @FindBy(xpath = "//*[contains(@id, 'show-line-amounts-in-mails-checkbox-inputEl')]")
    private ExtCheckbox showLineAmountsInMail;

    @FindBy(name = "reason")
    private ExtComboBox reason;

    @FindBy(name = "note")
    private ExtText note;

    @FindBy(xpath = "//*[contains(@id, 'include-in-claim-checkbox-inputEl')]")
    private ExtCheckbox includeInClaim;

    @FindBy(xpath = "//*[contains(@id, 'group-save-button-btnEl')]")
    private Button saveGroup;

    @FindBy(xpath = "//*[contains(@id, 'group-close-button-btnInnerEl')]")
    private Button closeGroup;

    @FindBy(xpath = "//*[contains(@id, 'average-age-field-inputEl')]")
    private WebElement averageAge;

    public SettlementGroupDialog enterGroupName(String name){
        groupName.setValue(name);
        return this;
    }

    public SettlementGroupDialog chooseType(GroupTypes type){
        type.getRadioButton().click();
        return this;
    }

    public SettlementGroupDialog enterNewPrice(Double newPrice){
        this.newPrice.setValue(OperationalUtils.toStringWithComma(newPrice));
        return this;
    }

    public SettlementGroupDialog enterCustomerDemand(Double customerDemand){
        this.customerDemand.setValue(OperationalUtils.toStringWithComma(customerDemand));
        return this;
    }

    public SettlementGroupDialog enterValuation(Double valuation){
        this.valuation.setValue(OperationalUtils.toStringWithComma(valuation));
        return this;
    }

    public SettlementGroupDialog enterNote(String note){
        this.note.sendKeys(note);
        return this;
    }

    public SettlementGroupDialog selectFirstReason(){
        reason.click();
        List<WebElement> options = driver.findElements(By.xpath("//li[@class='x-boundlist-item']"));
        options.get(1).click();
        return this;
    }

    public SettlementGroupDialog clickSave(){
        $(saveGroup).click();
        return this;
    }

    public SettlementPage saveGroup(){
        $(saveGroup).click();
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

    public enum GroupTypes{
        VALUATION($(By.xpath("//*[contains(@id, 'valuation-group-type-radio-displayEl')]"))),
        OVERVIEW($(By.xpath("//*[contains(@id, 'overview-group-type-radio-displayEl')]")));

        private WebElement element;

        GroupTypes(WebElement element){
            this.element = element;
        }

        public WebElement getRadioButton(){
            return element;
        }
    }

    public SettlementGroupDialog doAssert(Consumer<SettlementGroupDialog.Asserts> func) {
        func.accept(new Asserts());
        return SettlementGroupDialog.this;
    }

    public class Asserts {

        public Asserts assertAverageAgeIs(String age){
            assertThat(averageAge.getText()).isEqualToIgnoringCase(age);
            return this;
        }

        public Asserts assertIsOverviewChecked(){
            assertThat(GroupTypes.OVERVIEW.getRadioButton().findElement(By.xpath("./../input")).getAttribute("aria-checked")).isEqualToIgnoringCase("true");
            return this;
        }

        public Asserts assertIsValuationChecked(){
            assertThat(GroupTypes.VALUATION.getRadioButton().findElement(By.xpath("./../input")).getAttribute("aria-checked")).isEqualToIgnoringCase("true");
            return this;
        }

        public Asserts assertIsNewPriceFiledDisabled(){
            assertThat(newPrice.getAttribute("aria-disabled")).isEqualToIgnoringCase("true");
            return this;
        }

        public Asserts assertIsCustomerDemandFiledDisabled(){
            assertThat(customerDemand.getAttribute("aria-disabled")).isEqualToIgnoringCase("true");
            return this;
        }

        public Asserts assertIsReasonFiledDisabled(){
            assertThat(reason.getAttribute("aria-disabled")).isEqualToIgnoringCase("true");
            return this;
        }

        public Asserts assertIsValuationFiledDisabled(){
            assertThat(valuation.getAttribute("aria-disabled")).isEqualToIgnoringCase("true");
            return this;
        }

        public Asserts assertIsNewPriceFiledEnabled(){
            assertThat(newPrice.getAttribute("aria-disabled")).isEqualToIgnoringCase("false");
            return this;
        }

        public Asserts assertIsCustomerDemandFiledEnabled(){
            assertThat(customerDemand.getAttribute("aria-disabled")).isEqualToIgnoringCase("false");
            return this;
        }

        public Asserts assertIsReasonFiledEnabled(){
            assertThat(reason.getAttribute("aria-disabled")).isEqualToIgnoringCase("false");
            return this;
        }

        public Asserts assertIsValuationFiledEnabled(){
            assertThat(valuation.getAttribute("aria-disabled")).isEqualToIgnoringCase("false");
            return this;
        }

        public Asserts assertIsIncludeInClaimChecked(){
            assertThat(includeInClaim.getAttribute("aria-checked")).isEqualToIgnoringCase("true");
            return this;
        }

        public Asserts assertIsShowLineAmountInMailChecked(){
            assertThat(showLineAmountsInMail.getAttribute("aria-checked")).isEqualToIgnoringCase("true");
            return this;
        }

        public Asserts assertIsValuationRequired() {
            assertThat(valuation.getAttribute("aria-invalid")).isEqualToIgnoringCase("true");
            return this;
        }

        public Asserts assertIsReasonRequired() {
            assertThat(reason.getAttribute("aria-invalid")).isEqualToIgnoringCase("true");
            return this;
        }

        public Asserts assertReasonIsNotVisible() {
            assertThat(isDisplayed(reason)).isFalse();
            return this;
        }

        public Asserts assertNewPriceIsRequired() {
            assertThat(newPrice.getAttribute("aria-invalid")).isEqualToIgnoringCase("true");
            return this;
        }

        public Asserts assertCustomerDemandIsRequired() {
            assertThat(customerDemand.getAttribute("aria-invalid")).isEqualToIgnoringCase("true");
            return this;
        }
    }
}
