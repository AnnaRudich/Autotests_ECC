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

import static com.codeborne.selenide.Selenide.$;

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

    @FindBy(id = "create-show-line-amounts-in-mails-checkbox-displayEl")
    private ExtCheckbox showLineAmountsInMail;

    @FindBy(xpath = "//input[@name='reason']/ancestor::div[@data-ref='bodyEl']")
    private ExtComboBox reason;

    @FindBy(name = "note")
    private ExtText note;

    @FindBy(id = "create-include-in-claim-checkbox-boxLabelEl")
    private ExtCheckbox includeInClaim;

    @FindBy(id = "create-group-save-button-btnEl")
    private Button saveGroup;

    @FindBy(id = "create-group-close-button-btnInnerEl")
    private Button closeGroup;

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

    public SettlementGroupDialog selectFirstReason(){
        reason.click();
        List<WebElement> options = driver.findElements(By.xpath("//li[@class='x-boundlist-item']"));
        options.get(1).click();
        return this;
    }

    public SettlementPage saveGroup(){
        $(saveGroup).click();
        return BaseClaimPage.at(SettlementPage.class);
    }

    public enum GroupTypes{
        VALUATION($(By.id("create-valuation-group-type-radio-displayEl"))),
        OVERVIEW($(By.id("create-overview-group-type-radio-displayEl")));

        private WebElement element;

        GroupTypes(WebElement element){
            this.element = element;
        }

        public WebElement getRadioButton(){
            return element;
        }
    }
}
