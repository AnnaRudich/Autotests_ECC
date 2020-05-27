package com.scalepoint.automation.pageobjects.dialogs.eccadmin.voucheagreementtab;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

public class VoucherAgreementInfoTab extends BaseDialog implements VoucherAgreementTabs {

    @FindBy(xpath = ".//textarea[contains(@name, 'informations')]")
    private WebElement information;
    @FindBy(xpath = ".//textarea[contains(@name, 'terms')]")
    private WebElement terms;
    @FindBy(xpath = ".//textarea[contains(@name, 'deliveryInformations')]")
    private WebElement deliveryInformation;
    @FindBy(xpath = ".//textarea[contains(@name, 'issuedTexts')]")
    private WebElement issued;

    @Override
    protected void ensureWeAreAt() {
        waitForAjaxCompletedAndJsRecalculation();
        $(information).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
    }

    public VoucherAgreementInfoTab setInformation(String informationText) {
        information.sendKeys(informationText);
        return this;
    }

    public VoucherAgreementInfoTab setTags(String termsText) {
        terms.sendKeys(termsText);
        return this;
    }

    public VoucherAgreementInfoTab setDeliveryInformation(String deliveryInformationText) {
        deliveryInformation.sendKeys(deliveryInformationText);
        return this;
    }

    public VoucherAgreementInfoTab setIssuedText(String issuedText) {
        issued.sendKeys(issuedText);
        return this;
    }
}