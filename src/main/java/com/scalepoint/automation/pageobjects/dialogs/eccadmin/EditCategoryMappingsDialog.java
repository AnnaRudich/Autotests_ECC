package com.scalepoint.automation.pageobjects.dialogs.eccadmin;

import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.utils.Wait;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

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
        String optionToFind = formatCategoryOption(categoryName, subcategoryName);
        for (WebElement unmappedCategory : unmappedCategories) {
            String option = unmappedCategory.getText();
            if (option.equals(optionToFind)) {
                doubleClick(unmappedCategory);
                break;
            }
        }
        saveMappings.click();
        return at(VoucherAgreementDialog.CategoriesTab.class);
    }

    private String formatCategoryOption(String categoryName, String subcategoryName) {
        return categoryName + " - " + subcategoryName;
    }

}
