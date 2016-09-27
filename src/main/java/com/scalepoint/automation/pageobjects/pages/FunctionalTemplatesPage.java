package com.scalepoint.automation.pageobjects.pages;

import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Select;

import static com.scalepoint.automation.utils.Wait.waitForVisible;

@EccPage
public class FunctionalTemplatesPage extends Page {

    @FindBy(name = "ftList")
    private Select templates;

    @FindBy(id = "btnCopy")
    private Button copy;

    @FindBy(id = "btnDelete")
    private Button delete;

    @FindBy(id = "btnEdit")
    private Button edit;

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/Admin/func_templates.jsp";
    }

    @Override
    public FunctionalTemplatesPage ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForVisible(edit);
        return this;
    }

    public EditFunctionTemplatePage editTemplate(String templateName) {
        templates.selectByVisibleText(templateName);
        edit.click();

        return at(EditFunctionTemplatePage.class);
    }
}

