package com.scalepoint.automation.pageobjects.dialogs.eccadmin;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.eccadmin.voucheagreementtab.VoucherAgreementCategoriesTab;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.data.entity.PseudoCategory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

public class EditCategoryMappingsDialog extends BaseDialog {

    @FindBy(xpath = ".//a[contains(@class,'supplier-voucher-save-mappings')]")
    private WebElement saveMappings;

    @FindBy(xpath = ".//*[@id='from-list']//li")
    private List<WebElement> unmappedCategories;

    @FindBy(xpath = ".//*[@id='to-list']//li")
    private List<WebElement> mappedCategories;

    @Override
    protected void ensureWeAreAt() {
        waitForAjaxCompletedAndJsRecalculation();
        $(saveMappings).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
    }

    public VoucherAgreementCategoriesTab mapCategory(PseudoCategory pseudoCategory) {
        return clickOnCategoryAndSave(pseudoCategory, $$("#from-list li"));
    }

    public VoucherAgreementCategoriesTab removeMapping(PseudoCategory pseudoCategory) {
        return clickOnCategoryAndSave(pseudoCategory, $$("#to-list li"));
    }

    private VoucherAgreementCategoriesTab clickOnCategoryAndSave(PseudoCategory pseudoCategory, ElementsCollection categories) {
        String optionToFind = formatCategoryOption(pseudoCategory.getGroupName(), pseudoCategory.getCategoryName());
        SelenideElement element = categories
                .stream()
                .filter(e -> e.text().equals(optionToFind))
                .findFirst()
                .get();
        element.click();
        element.doubleClick();
        doubleClick(saveMappings);
        Wait.waitElementDisappeared(By.xpath("//a[contains(@class,'supplier-voucher-save-mappings')]"));
        Wait.waitForAjaxCompleted();
        return at(VoucherAgreementCategoriesTab.class);
    }

    public static String formatCategoryOption(String categoryName, String subcategoryName) {
        return categoryName + " - " + subcategoryName;
    }

}
