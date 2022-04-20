package com.scalepoint.automation.pageobjects.dialogs.eccadmin.suppliersdialog.supplierdialogtab;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.dialogs.eccadmin.suppliersdialog.SupplierDialog;
import com.scalepoint.automation.utils.JavascriptHelper;
import com.scalepoint.automation.utils.Wait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import java.io.File;
import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.*;
import static org.testng.Assert.assertTrue;

public  class GeneralTab extends SupplierDialog {

    @FindBy(name = "name")
    private SelenideElement name;
    @FindBy(name = "cvr")
    private SelenideElement cvr;
    @FindBy(name = "address")
    private SelenideElement address1;
    @FindBy(name = "address2")
    private SelenideElement address2;
    @FindBy(name = "city")
    private SelenideElement city;
    @FindBy(name = "postalCode")
    private SelenideElement postalCode;
    @FindBy(name = "phone")
    private SelenideElement phone;
    @FindBy(xpath = ".//div[contains(@class,'SupplierWindow')]//span[contains(@class,'x-window-header-text')]")
    private SelenideElement windowHeader;
    @FindBy(xpath = ".//*[contains(@class,'add-supplier-create-btn')]")
    private SelenideElement createSupplierButton;
    @FindBy(name = "website")
    private SelenideElement website;
    @FindBy(name = "fileData")
    private SelenideElement supplierLogo;
    @FindBy(id = "editSupplierTabPanelId")
    private SelenideElement editableSupplierDialog;

    @Override
    protected boolean areWeAt() {

        Wait.waitForJavascriptRecalculation();
        Wait.waitForAjaxCompleted();
        try {

            return name.isDisplayed();
        } catch (Exception e) {

            logger.error(e.getMessage());
            return false;
        }
    }

    @Override
    protected void ensureWeAreAt() {

        waitForAjaxCompletedAndJsRecalculation();
        windowHeader.should(Condition.matchText("[(Edit)(View)] supplier .*"));
    }

    public GeneralTab setName(String name) {

        this.name.clear();
        this.name.sendKeys(name);
        return this;
    }

    public GeneralTab fill(Consumer<GeneralTab> fillFunc) {

        fillFunc.accept(this);
        return this;
    }

    public GeneralTab setWebsite(String webSite) {

        this.website.sendKeys(webSite);
        return this;
    }

    public GeneralTab uploadLogo(String logoPath) {

        SelenideElement elem = $(By.xpath("//input[contains(@id, 'supplierLogoFileId') and contains(@type, 'file')]"));
        elem.uploadFile(new File(logoPath));
        $(By.cssSelector(("img.imageUploadImg"))).should(Condition.visible);
        return this;
    }

    public GeneralTab doAssert(Consumer<GeneralTab.Asserts> assertFunc) {

        assertFunc.accept(new GeneralTab.Asserts());
        return GeneralTab.this;
    }

    public class Asserts {

        public static final String VALUE = "value";

        public GeneralTab.Asserts assertLogoPresent() {

            assertTrue(JavascriptHelper.isImagePresent($(By.className("imageUploadImg"))));
            return this;
        }

        public Asserts assertCvr(String cvrValue) {

            Assert.assertEquals(cvr.getAttribute(VALUE), cvrValue);
            return this;
        }

        public Asserts assertAddress(String address1Value, String address2Value) {

            Assert.assertEquals(address1.getAttribute(VALUE), address1Value);
            Assert.assertEquals(address2.getAttribute(VALUE), address2Value);
            return this;
        }

        public Asserts assertCity(String cityValue) {

            Assert.assertEquals(city.should(Condition.visible).getAttribute(VALUE), cityValue);
            return this;
        }

        public Asserts assertPostalCode(String postalCodeValue) {

            Assert.assertEquals(postalCode.getAttribute(VALUE), postalCodeValue);
            return this;
        }

        public Asserts assertWebsite(String websiteValue) {

            Assert.assertEquals(website.getAttribute(VALUE), websiteValue);
            return this;
        }

        public Asserts assertIsDialogNotEditable() {

            Assert.assertFalse($(By.id("editSupplierTabPanelId")).has(Condition.visible));
            return this;
        }
    }

    public static class FormFiller {

        private GeneralTab dialog;

        public FormFiller(GeneralTab dialog) {

            this.dialog = dialog;
        }

        public GeneralTab.FormFiller withSupplierName(String supplierName) {

            clearField(dialog.name);
            dialog.name.sendKeys(supplierName);
            JavascriptHelper.blur();
            return this;
        }

        public GeneralTab.FormFiller withCvr(String cvr) {

            clearField(dialog.cvr);
            dialog.cvr.sendKeys(cvr);
            JavascriptHelper.blur();
            return this;
        }

        public GeneralTab.FormFiller withAddress1(String address1) {

            clearField(dialog.address1);
            dialog.address1.sendKeys(address1);
            JavascriptHelper.blur();
            return this;
        }

        public GeneralTab.FormFiller withAddress2(String address2) {

            clearField(dialog.address2);
            dialog.address2.sendKeys(address2);
            JavascriptHelper.blur();
            return this;
        }

        public GeneralTab.FormFiller withCity(String city) {

            clearField(dialog.city);
            dialog.city.sendKeys(city);
            JavascriptHelper.blur();
            return this;
        }

        public GeneralTab.FormFiller withPostalCode(String postalCode) {

            clearField(dialog.postalCode);
            dialog.postalCode.sendKeys(postalCode);
            JavascriptHelper.blur();
            return this;
        }

        public GeneralTab.FormFiller withWebsite(String website) {

            clearField(dialog.website);
            dialog.website.sendKeys(website);
            JavascriptHelper.blur();
            return this;
        }

        private void clearField(WebElement element) {

            SelenideElement field = $(element);
            field.click();
            field.clear();
            Wait.waitForJavascriptRecalculation();
            Wait.waitForAjaxCompleted();
        }

    }
}
