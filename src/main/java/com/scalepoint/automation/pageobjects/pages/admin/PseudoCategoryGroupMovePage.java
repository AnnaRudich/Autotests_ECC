package com.scalepoint.automation.pageobjects.pages.admin;

import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Select;

@EccPage
public class PseudoCategoryGroupMovePage extends AdminBasePage {

    @FindBy(id = "btnOk")
    private WebElement okButton;

    @FindBy(name = "newpseudocatgroup")
    private Select groups;

    @Override
    protected Page ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        Wait.waitForVisible(okButton);
        return this;
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/Admin/pseudocategory_group_move.jsp";
    }

    public PseudoCategoryGroupAddEditPage moveToGroup(String groupName) {
        groups.selectByVisibleText(groupName);
        okButton.click();
        return at(PseudoCategoryGroupAddEditPage.class);
    }
}
