package com.scalepoint.automation.pageobjects.dialogs.eccadmin;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.eccadmin.voucheagreementtab.VoucherAgreementCategoriesTab;
import com.scalepoint.automation.utils.data.entity.input.PseudoCategory;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

public class EditCategoryMappingsDialog extends BaseDialog {

    @FindBy(xpath = ".//a[contains(@class,'supplier-voucher-save-mappings')]")
    private SelenideElement saveMappings;

    @Override
    protected void ensureWeAreAt() {

        waitForAjaxCompletedAndJsRecalculation();
        saveMappings.should(Condition.visible);
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
        $(saveMappings).doubleClick().shouldNot(Condition.visible);
        return BaseDialog.at(VoucherAgreementCategoriesTab.class);
    }

    public static String formatCategoryOption(String categoryName, String subcategoryName) {

        return categoryName + " - " + subcategoryName;
    }

}
