package com.scalepoint.automation.pageobjects.pages.admin;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Select;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

@EccPage
public class FunctionalTemplatesPage extends AdminBasePage {

    @FindBy(name = "ftList")
    private Select templates;

    @FindBy(id = "btnCopy")
    private Button copy;

    @FindBy(id = "btnDelete")
    private Button delete;

    @FindBy(id = "btnEdit")
    private Button edit;

    @FindBy(xpath = "//select[@name='ftList']")
    private Select templatesList;

    @Override
    protected void ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForAjaxCompletedAndJsRecalculation();
        $(edit).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/Admin/func_templates.jsp";
    }


    public EditFunctionTemplatePage editTemplate(String templateName) {
        templates.selectByVisibleText(templateName);
        edit.click();

        return at(EditFunctionTemplatePage.class);
    }

    public EditFunctionTemplatePage copyTemplate(String templateName) {
        templates.selectByVisibleText(templateName);
        copy.click();

        return at(EditFunctionTemplatePage.class);
    }

    public boolean delete(String templateName) {
        templates.selectByVisibleText(templateName);
        delete.click();
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
            templatesList.selectByVisibleText(templateName);
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

