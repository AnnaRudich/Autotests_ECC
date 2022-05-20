package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.extjs.ExtComboBoxBoundView;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompleted;


public class UpdateCategoriesDialog extends BaseDialog {

    @Override
    protected void ensureWeAreAt() {

        waitForAjaxCompleted();
        getCategory().should(Condition.visible);
    }

    @FindBy(id = "bulk-ok-button")
    private SelenideElement okButton;

    private ExtComboBoxBoundView getCategory(){

        return new ExtComboBoxBoundView($(By.id("bulk-group-combobox")));
    }

    private ExtComboBoxBoundView getSubcategory(){

        return new ExtComboBoxBoundView($(By.id("bulk-pseudocategory-combobox")));
    }

    public UpdateCategoriesDialog selectCategory(String categoryText) {

        getCategory().select(categoryText);
        return this;
    }

    public UpdateCategoriesDialog selectSubcategory(String subcategoryText) {

        getSubcategory().select(subcategoryText);
        loadVouchersList();
        return this;
    }
    /*
    voucher list would be loaded only when we trigger open list, voucher data loaded at this point is needed for the voucher handling in SID later
     */
    private void loadVouchersList(){

        $("#bulk-vouchers-combobox-trigger-picker").click();
        $("#bulk-vouchers-combobox-picker-listEl")
                .$$(By.tagName("li")).shouldHave(CollectionCondition.sizeGreaterThan(0));
    }

    public SettlementPage closeUpdateCategoriesDialog() {

        okButton.click();
        SelenideElement loadingIndicator = $("#loadmask-1104-msgTextEl");

        try {

            loadingIndicator.should(Condition.appear, Duration.ofSeconds(3));
        }catch (Throwable t){
        }finally {

            loadingIndicator.should(Condition.disappear);
        }
        return Page.at(SettlementPage.class);
    }
}