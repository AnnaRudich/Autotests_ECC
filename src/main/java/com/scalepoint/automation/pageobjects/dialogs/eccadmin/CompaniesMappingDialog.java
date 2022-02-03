package com.scalepoint.automation.pageobjects.dialogs.eccadmin;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.suppliers.LossSheetTemplatesPage;
import com.scalepoint.automation.utils.Wait;
import lombok.Data;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

public class CompaniesMappingDialog extends BaseDialog {

    ElementsCollection buttons = $$(".editServiceAgreementCompaniesMappingsWindow [id^=toolbar] a");

    @Override
    protected void ensureWeAreAt() {

        waitForAjaxCompletedAndJsRecalculation();
        $(".editServiceAgreementCompaniesMappingsWindow [id$=header]")
                .should(Condition.exactText("Edit mappings"));
    }

    private List<Company> getAvailableCompaniesList(){

        return $$("#from-list ul li")
                .stream()
                .map(Company::new)
                .collect(Collectors.toList());
    }

    public CompaniesMappingDialog addItem(){

        $(".editServiceAgreementCompaniesMappingsWindow .x-form-itemselector-add").click();
        Wait.waitForAjaxCompletedAndJsRecalculation();
        return BaseDialog.at(CompaniesMappingDialog.class);
    }

    public CompaniesMappingDialog removeItem(){

        $(".editServiceAgreementCompaniesMappingsWindow .x-form-itemselector-remove").click();
        Wait.waitForAjaxCompletedAndJsRecalculation();
        return BaseDialog.at(CompaniesMappingDialog.class);
    }

    public Company getCompanyByName(String name){


        List<Company> test = getAvailableCompaniesList();
        return test
                .stream()
                .filter(company -> company.getName().toLowerCase().equals(name.toLowerCase()))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

    @Data
    public class Company{

        private SelenideElement element;
        private String name;

        public Company(SelenideElement element){

            this.element = element;
            name = element.getText();
        }

        public CompaniesMappingDialog selectCompany(){

            element
                    .scrollTo()
                    .click();
            return CompaniesMappingDialog.this;
        }
    }

    public LossSheetTemplatesPage clickSaveButton(){

        buttons.find(Condition.exactText("Save")).click();
        Wait.waitForSpinnerToDisappear();
        Wait.waitForAjaxCompletedAndJsRecalculation();
        return Page.at(LossSheetTemplatesPage.class);
    }
}
