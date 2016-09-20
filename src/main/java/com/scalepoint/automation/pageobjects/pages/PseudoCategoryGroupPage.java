package com.scalepoint.automation.pageobjects.pages;

import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Select;

import static com.scalepoint.automation.utils.Wait.waitForVisible;

@EccPage
public class PseudoCategoryGroupPage extends Page {

    private static final String URL = "webshop/jsp/Admin/pseudocategory_group.jsp";

    @FindBy(name = "pseudoCategoryGroupList")
    private Select pseudoCategoryGroupList;
    @FindBy(id = "btnEdit")
    private Button edit;

    @Override
    protected Page ensureWeAreOnPage() {
        waitForUrl(URL);
        waitForVisible(edit);
        return this;
    }

    public EditPseudoCategoryGroupPage editPseudoCategory(String pseudoCategory){
        pseudoCategoryGroupList.selectByVisibleText(pseudoCategory);
        edit.click();
        return at(EditPseudoCategoryGroupPage.class);
    }

    @Override
    protected String getRelativeUrl() {
        return URL;
    }
}
