package com.scalepoint.automation.pageobjects.dialogs.eccadmin.voucheagreementtab;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

public class VoucherAgreementCoverageTab extends BaseDialog implements VoucherAgreementTabs {

    @FindBy(name = "brands")
    private WebElement brands;
    @FindBy(name = "tags")
    private WebElement tags;

    @Override
    protected void ensureWeAreAt() {
        waitForAjaxCompletedAndJsRecalculation();
        $(brands).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
    }

    public VoucherAgreementCoverageTab setBrands(String brandsText) {
        brands.sendKeys(brandsText);
        return this;
    }

    public VoucherAgreementCoverageTab setTags(String tagsText) {
        tags.sendKeys(tagsText);
        return this;
    }
}
