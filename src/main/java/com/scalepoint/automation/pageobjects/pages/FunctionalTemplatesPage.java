package com.scalepoint.automation.pageobjects.pages;

import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Select;

import static com.scalepoint.automation.utils.Wait.waitForVisible;

@EccPage
public class FunctionalTemplatesPage extends Page {

    private static final String URL = "webshop/jsp/Admin/func_templates.jsp";

    @FindBy(name = "ftList")
    private Select templates;

    @FindBy(id = "btnCopy")
    private Button copy;

    @FindBy(id = "btnDelete")
    private Button delete;

    @FindBy(id = "btnEdit")
    private Button edit;

    @Override
    protected String geRelativeUrl() {
        return URL;
    }

    @Override
    public FunctionalTemplatesPage ensureWeAreOnPage() {
        waitForUrl(URL);
        waitForVisible(edit);
        return this;
    }

    public EditFunctionTemplatePage editTemplate(String templateName) {
        templates.selectByVisibleText(templateName);
        edit.click();
        return at(EditFunctionTemplatePage.class);
    }
}

