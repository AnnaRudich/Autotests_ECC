package com.scalepoint.automation.pageobjects.pages.admin;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Select;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForPageLoaded;

@EccPage
public class PseudoCategoryGroupMovePage extends AdminBasePage {

    @FindBy(id = "btnOk")
    private WebElement okButton;

    @FindBy(name = "newpseudocatgroup")
    private Select groups;

    @Override
    protected void ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForPageLoaded();
        $(okButton).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
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
