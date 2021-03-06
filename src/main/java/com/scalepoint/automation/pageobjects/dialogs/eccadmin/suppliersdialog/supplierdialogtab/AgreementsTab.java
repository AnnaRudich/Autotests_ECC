package com.scalepoint.automation.pageobjects.dialogs.eccadmin.suppliersdialog.supplierdialogtab;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.eccadmin.CreateVoucherAgreementDialog;
import com.scalepoint.automation.pageobjects.dialogs.eccadmin.suppliersdialog.SupplierDialog;
import com.scalepoint.automation.pageobjects.dialogs.eccadmin.voucheagreementtab.VoucherAgreementGeneralTab;
import com.scalepoint.automation.utils.Wait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import java.util.NoSuchElementException;
import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;
import static org.testng.Assert.assertEquals;

public class AgreementsTab extends SupplierDialog {

    public static final String DIV_ID_SUPPLIER_VOUCHERS_GRID_ID_DIV_TEXT = "//div[@id='supplierVouchersGridId']//div[text()='";

    @FindBy(xpath = "//td[contains(@class, 'agreementsPanelExclusiveId')]")
    private SelenideElement exclusiveGridCell;
    @FindBy(className = "supplier-new-voucher-agreement-btn")
    private SelenideElement createNewVoucherAgreementBtn;
    @FindBy(xpath = "//td[contains(@class, 'voucherNameId')]/div")
    private ElementsCollection voucherNameGridCell;

    @Override
    protected void ensureWeAreAt() {

        waitForAjaxCompletedAndJsRecalculation();
        createNewVoucherAgreementBtn.should(Condition.visible);
    }

    public CreateVoucherAgreementDialog openCreateVoucherAgreementDialog() {

        createNewVoucherAgreementBtn.click();
        return BaseDialog.at(CreateVoucherAgreementDialog.class);
    }

    public VoucherAgreementGeneralTab editVoucherAgreement(String agreementName) {

        Wait.waitForAjaxCompleted();
        $(By.xpath("id('supplierVouchersGridId-body')//div[contains(text(),'" + agreementName + "')]")).doubleClick();
        isOn(VoucherAgreementGeneralTab.class);
        Wait.waitForAjaxCompleted();
        return at(VoucherAgreementGeneralTab.class);
    }

    public enum ActionType {

        LEAVE,
        JOIN
    }

    public boolean isExclusiveTickForFirstVoucherAvailable() {

        return exclusiveGridCell.getAttribute("class").contains("tick");
    }

    private WebElement findVoucher(String voucherName) {

        return voucherNameGridCell.stream()
                .filter(element -> element.getText().equals(voucherName))
                .findAny().orElseThrow(() -> new NoSuchElementException("Can't find voucher with name " + voucherName));
    }

    public boolean isExclusiveTickForVoucherAvailable(String voucherName) {

        return findVoucher(voucherName).findElement(By.xpath("./ancestor::tr//td[contains(@class, 'agreementsPanelExclusiveId')]"))
                .getAttribute("class").contains("tick");
    }

    public AgreementsTab doWithAgreement(String voucherAgreementName, ActionType actionType) {

        SelenideElement voucherRow = $(By.xpath(DIV_ID_SUPPLIER_VOUCHERS_GRID_ID_DIV_TEXT + voucherAgreementName + "']/ancestor::tr"));
        voucherRow.click();

        SelenideElement actionButton = $(By.className("supplier-join-leave-voucher-agreement-btn")).should(Condition.visible);
        Assert.assertEquals(actionButton.getText(), actionType == ActionType.JOIN ? "Join" : "Leave");
        actionButton.click();

        $(By.xpath(".//div[contains(@id, 'messagebox')]//span[text()='Yes']//ancestor::a")).should(Condition.visible)
                .click();

        Wait.waitForAjaxCompleted();
        $(voucherRow).click();

        Assert.assertEquals(actionButton.getText(), actionType == ActionType.JOIN ? "Leave" : "Join");
        return this;
    }

    public AgreementsTab doAssert(Consumer<Asserts> assertFunc) {

        assertFunc.accept(new Asserts());
        return AgreementsTab.this;
    }

    public class Asserts {
        public Asserts assertVoucherStatus(String voucherName, boolean active) {

            By voucherRowActive = By.xpath(DIV_ID_SUPPLIER_VOUCHERS_GRID_ID_DIV_TEXT + voucherName + "']/ancestor::tr//td[4]");
            assertEquals($(voucherRowActive).getText(), active ? "Yes" : "No");
            return this;
        }

        public Asserts assertVoucherAbsent(String voucherName) {

            Assert.assertFalse($(By.xpath(DIV_ID_SUPPLIER_VOUCHERS_GRID_ID_DIV_TEXT + voucherName + "']")).exists());
            return this;
        }

        public Asserts assertShopOnlyVoucherIsPresent(String voucherName){

            Assert.assertTrue($(By.xpath(DIV_ID_SUPPLIER_VOUCHERS_GRID_ID_DIV_TEXT + voucherName + "_SHOP"+"']")).exists());
            return this;
        }

        public Asserts assertIsExclusiveTickForVoucherVisible() {

            Assert.assertTrue(isExclusiveTickForFirstVoucherAvailable());
            return this;
        }

        public Asserts assertIsExclusiveTickForVoucherNotVisible(String voucherName) {

            Assert.assertFalse(isExclusiveTickForVoucherAvailable(voucherName));
            return this;
        }
    }
}
