package com.scalepoint.automation.pageobjects.dialogs.eccadmin.voucheagreementtab;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import org.openqa.selenium.support.FindBy;

import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

public class VoucherAgreementInfoTab extends BaseDialog implements VoucherAgreementTabs {

    @FindBy(xpath = ".//textarea[contains(@name, 'informations')]")
    private SelenideElement information;
    @FindBy(xpath = ".//textarea[contains(@name, 'terms')]")
    private SelenideElement terms;
    @FindBy(xpath = ".//textarea[contains(@name, 'deliveryInformations')]")
    private SelenideElement deliveryInformation;
    @FindBy(xpath = ".//textarea[contains(@name, 'issuedTexts')]")
    private SelenideElement issued;

    @Override
    protected void ensureWeAreAt() {

        waitForAjaxCompletedAndJsRecalculation();
        information.should(Condition.visible);
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
