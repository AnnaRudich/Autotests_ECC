package com.scalepoint.automation.pageobjects.pages.suppliers;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.eccadmin.CompaniesMappingDialog;
import com.scalepoint.automation.pageobjects.dialogs.eccadmin.NewTemplateDialog;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccAdminPage;
import lombok.Data;

import java.io.File;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.scalepoint.automation.utils.Wait.waitElementVisible;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;
import static org.assertj.core.api.Assertions.assertThat;

@EccAdminPage
public class LossSheetTemplatesPage extends BaseSupplierAdminNavigation {

    ElementsCollection buttons = $$("#contentAreaPanel #lossSheetGridId [id^=toolbar] a");

    @Override
    protected void ensureWeAreOnPage() {

        waitForUrl(getRelativeUrl());
        waitForAjaxCompletedAndJsRecalculation();
        waitElementVisible($("#lossSheetGridId-body"));
    }

    @Override
    protected String getRelativeUrl() {
        return "#lossSheetTemplates";
    }

    private List<TemplateRow> getTemplatesList(){

        return $$("#lossSheetGridId-body tbody tr")
                .stream()
                .map(TemplateRow::new)
                .collect(Collectors.toList());
    }

    public TemplateRow getTemplateRowByName(String name){

        return getTemplatesList()
                .stream()
                .filter(templateRow -> templateRow.getTemplateName().equals(name))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

    public NewTemplateDialog uploadNewTemplate(){

        buttons.find(Condition.exactText("Upload new template")).click();
        return BaseDialog.at(NewTemplateDialog.class);
    }

    public LossSheetTemplatesPage uploadTemplate(String templateName, File templateFile) {

        return uploadNewTemplate()
                .setTemplateName(templateName)
                .uploadTemplate(templateFile)
                .clickUploadButton();
    }

    public CompaniesMappingDialog reassignTemplate(){

        buttons.find(Condition.exactText("Reassign template")).click();
        return BaseDialog.at(CompaniesMappingDialog.class);
    }

    public TemplateRow reasignTemplate(String companyName, String templateName){

        return reassignTemplate()
                .getCompanyByName(companyName)
                .selectCompany()
                .addItem()
                .clickSaveButton()
                .getTemplateRowByName(templateName);
    }

    public LossSheetTemplatesPage doAssert(Consumer<Asserts> assertFunc) {

        assertFunc.accept(new Asserts());
        return this;
    }

    public class Asserts {

        public Asserts assertTemplateExists(String name) {

            assertThat(getTemplatesList().stream()
                    .anyMatch(templateRow -> templateRow.getTemplateName().equals(name)))
                    .isTrue();
            return this;
        }
    }

    @Data
    public class TemplateRow{

        int id;
        String templateName;
        boolean active;
        boolean inUse;
        private SelenideElement element;

        public TemplateRow(SelenideElement element){

            this.element = element.should(Condition.exist);
            id = Integer.parseInt(element.find("#lossSheetGridId-body tbody tr [role=gridcell]:nth-of-type(1)").getText());
            templateName = element.find("#lossSheetGridId-body tbody tr [role=gridcell]:nth-of-type(2)").getText();
            active = element.find("#lossSheetGridId-body tbody tr [role=gridcell]:nth-of-type(3)").has(Condition.cssClass("tick"));
            inUse = element.find("#lossSheetGridId-body tbody tr [role=gridcell]:nth-of-type(4)").has(Condition.cssClass("tick"));
        }

        public TemplateRow selectTemplate(){

            element.click();
            Wait.waitForAjaxCompletedAndJsRecalculation();
            return TemplateRow.this;
        }

        public LossSheetTemplatesPage doAssert(Consumer<TemplateRow.Asserts> assertFunc) {

            assertFunc.accept(new TemplateRow.Asserts());
            return LossSheetTemplatesPage.this;
        }

        public class Asserts {

            public TemplateRow.Asserts assertActive() {

                assertThat(active).isTrue();
                return this;
            }

            public TemplateRow.Asserts assertInUse() {

                assertThat(inUse).isTrue();
                return this;
            }
        }
    }
}

