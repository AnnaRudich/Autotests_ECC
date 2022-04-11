package com.scalepoint.automation.pageobjects.dialogs.eccadmin;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.suppliers.LossSheetTemplatesPage;
import com.scalepoint.automation.utils.Wait;

import java.io.File;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

public class NewTemplateDialog extends BaseDialog {

    ElementsCollection buttons = $$(".addUploadTemplateWindow [id^=toolbar] a");

    @Override
    protected void ensureWeAreAt() {

        waitForAjaxCompletedAndJsRecalculation();
        $(".addUploadTemplateWindow [id$=header]")
                .should(Condition.exactText("Upload new template"));
    }

    public NewTemplateDialog setTemplateName(String templateName){

        $("input[name=name]").setValue(templateName);
        return this;
    }

    public NewTemplateDialog uploadTemplate(File file){

        $("input[name=templateFileName]").uploadFile(file);
        return this;
    }

    public LossSheetTemplatesPage clickUploadButton(){

        buttons.find(Condition.exactText("Upload")).click();
        Wait.waitForSpinnerToDisappear();
        Wait.waitForAjaxCompletedAndJsRecalculation();
        return Page.at(LossSheetTemplatesPage.class);
    }
}
