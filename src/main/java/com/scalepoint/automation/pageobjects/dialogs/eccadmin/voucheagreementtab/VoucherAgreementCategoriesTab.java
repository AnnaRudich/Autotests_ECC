package com.scalepoint.automation.pageobjects.dialogs.eccadmin.voucheagreementtab;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.eccadmin.EditCategoryMappingsDialog;
import com.scalepoint.automation.utils.data.entity.input.PseudoCategory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import java.util.List;
import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

public class VoucherAgreementCategoriesTab extends BaseDialog implements VoucherAgreementTabs {
    @FindBy(xpath = "id('categoriesVoucherTabId')//tr")
    private List<WebElement> mappedCategories;

    @FindBy(className = "supplier-voucher-edit-mappings-btn")
    private WebElement editMappingsBtn;

    @Override
    protected void ensureWeAreAt() {
        waitForAjaxCompletedAndJsRecalculation();
        $(editMappingsBtn).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
    }

    public VoucherAgreementCategoriesTab doAssert(Consumer<Asserts> assertFunc) {
        assertFunc.accept(new Asserts());
        return VoucherAgreementCategoriesTab.this;
    }

    public class Asserts {

        public Asserts assertCategoryMapped(PseudoCategory pseudoCategory) {
            Assert.assertTrue(isCategoryMapped(pseudoCategory));
            return this;
        }


        public Asserts assertCategoryNotMapped(PseudoCategory pseudoCategory) {
            Assert.assertFalse(isCategoryMapped(pseudoCategory));
            return this;
        }

        private boolean isCategoryMapped(PseudoCategory pseudoCategory) {
            String expectedCategory = EditCategoryMappingsDialog.formatCategoryOption(pseudoCategory.getGroupName(), pseudoCategory.getCategoryName());
            return $(By.xpath("//div[@id='categoriesVoucherTabId']//div[text()='" + expectedCategory + "']")).isDisplayed();
        }

    }

    public EditCategoryMappingsDialog openEditMappingsDialog() {
        hoverAndClick($(editMappingsBtn));
        return BaseDialog.at(EditCategoryMappingsDialog.class);
    }

    public VoucherAgreementCategoriesTab mapToCategory(PseudoCategory pseudoCategory) {
        return openEditMappingsDialog()
                .mapCategory(pseudoCategory);
    }

    public VoucherAgreementCategoriesTab removeMapping(PseudoCategory pseudoCategory) {
        return openEditMappingsDialog()
                .removeMapping(pseudoCategory);
    }
}
