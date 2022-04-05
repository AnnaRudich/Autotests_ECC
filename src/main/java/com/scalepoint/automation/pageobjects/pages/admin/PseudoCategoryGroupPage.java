package com.scalepoint.automation.pageobjects.pages.admin;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Select;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

@EccPage
public class PseudoCategoryGroupPage extends AdminBasePage {

    private Select getPseudoCategoryGroupList(){

        return new Select($(By.name("pseudoCategoryGroupList")));
    }

    private Button getEdit(){

        return new Button($(By.id("btnEdit")));
    }

    private Button getAdd(){

        return new Button($(By.id("btnAdd")));
    }

    private Select getGroups(){

        return new Select($(By.name("pseudoCategoryGroupList")));
    }

    @Override
    protected String getRelativeUrl() {

        return "webshop/jsp/Admin/pseudocategory_group.jsp";
    }

    @Override
    protected void ensureWeAreOnPage() {

        waitForUrl(getRelativeUrl());
        waitForAjaxCompletedAndJsRecalculation();
        $(getEdit()).should(Condition.visible);
    }

    private boolean isGroupDisplayed(String groupName) {

        try {

            getGroups().selectByVisibleText(groupName);
        } catch (NoSuchElementException e) {

            return false;
        }
        return true;
    }

    public PseudoCategoryGroupAddEditPage editGroup(String pseudoCategory) {

        getPseudoCategoryGroupList().selectByVisibleText(pseudoCategory);
        getEdit().click();
        return at(PseudoCategoryGroupAddEditPage.class);
    }

    public PseudoCategoryGroupAddEditPage toAddGroupPage() {

        getAdd().click();
        return at(PseudoCategoryGroupAddEditPage.class);
    }

    public PseudoCategoryGroupPage assertGroupDisplayed(String groupName) {

        Assert.assertTrue(isGroupDisplayed(groupName));
        return this;
    }
}
