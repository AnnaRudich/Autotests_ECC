package com.scalepoint.automation.pageobjects.dialogs.eccadmin;

import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.utils.Wait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class EditCategoryMappingsDialog extends BaseDialog {

    @FindBy(xpath = ".//a[contains(@class,'supplier-voucher-save-mappings')]")
    private WebElement saveMappings;

    @FindBy(xpath = ".//*[@id='from-list']//li")
    private List<WebElement> unmappedCategories;

    @FindBy(xpath = ".//*[@id='to-list']//li")
    private List<WebElement> mappedCategories;

    @Override
    protected BaseDialog ensureWeAreAt() {
        Wait.waitForVisible(saveMappings);
        return this;
    }

    public VoucherAgreementDialog.CategoriesTab mapCategory(String categoryName, String subcategoryName) {
        return clickOnCategoryAndSave(categoryName, subcategoryName, unmappedCategories);
    }

    public VoucherAgreementDialog.CategoriesTab removeMapping(String categoryName, String subcategoryName) {
        return clickOnCategoryAndSave(categoryName, subcategoryName, mappedCategories);
    }

    private VoucherAgreementDialog.CategoriesTab clickOnCategoryAndSave(String categoryName, String subcategoryName, List<WebElement> categories) {
        String optionToFind = formatCategoryOption(categoryName, subcategoryName);
        for (WebElement unmappedCategory : categories) {
            String option = unmappedCategory.getText();
            if (option.equals(optionToFind)) {
                doubleClick(unmappedCategory);
                break;
            }
        }
        doubleClickUsingJsIfSeleniumClickReturnError(saveMappings);
        Wait.waitElementDisappeared(By.xpath("//a[contains(@class,'supplier-voucher-save-mappings')]"));

        return at(VoucherAgreementDialog.CategoriesTab.class);
    }

    public static String formatCategoryOption(String categoryName, String subcategoryName) {
        return categoryName + " - " + subcategoryName;
    }

}
