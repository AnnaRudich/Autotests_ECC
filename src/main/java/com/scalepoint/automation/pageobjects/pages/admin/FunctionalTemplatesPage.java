package com.scalepoint.automation.pageobjects.pages.admin;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Select;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

@EccPage
public class FunctionalTemplatesPage extends AdminBasePage {

    private Select getTemplates(){

        return new Select($(By.name("ftList")));
    }

    private Button getCopy(){

        return new Button($(By.id("btnCopy")));
    }

    private Button getDelete(){

        return new Button($(By.id("btnDelete")));
    }

    private Button getEdit(){

        return new Button($(By.id("btnEdit")));
    }

    private Select getTemplatesList(){

        return new Select($(By.xpath("//select[@name='ftList']")));
    }

    @Override
    protected void ensureWeAreOnPage() {

        waitForUrl(getRelativeUrl());
        waitForAjaxCompletedAndJsRecalculation();
        $(getEdit()).should(Condition.visible);
    }

    @Override
    protected String getRelativeUrl() {

        return "webshop/jsp/Admin/func_templates.jsp";
    }


    public EditFunctionTemplatePage editTemplate(String templateName) {

        getTemplates().selectByVisibleText(templateName);
        getEdit().click();

        return at(EditFunctionTemplatePage.class);
    }

    public EditFunctionTemplatePage copyTemplate(String templateName) {

        getTemplates().selectByVisibleText(templateName);
        getCopy().click();

        return at(EditFunctionTemplatePage.class);
    }

    public boolean delete(String templateName) {

        getTemplates().selectByVisibleText(templateName);
        getDelete().click();

        if (isAlertPresent()) {

            String alertText = getAlertTextAndAccept();

            if (alertText.contains("This function template is used by one or more companies and therefore can not be deleted!")) {

                return false;
            } else if (alertText.contains("Are you sure, that you want to delete the\nfunction template named:")) {

                return true;
            }
        }
        return false;
    }

    public boolean containsTemplate(String templateName) {

        try {

            getTemplatesList().selectByVisibleText(templateName);
            return true;
        } catch (NoSuchElementException e) {

            return false;
        }
    }

    public FunctionalTemplatesPage assertTemplateExists(String ftName) {

        Assert.assertTrue(containsTemplate(ftName), "Template is not found");
        return this;
    }
}

