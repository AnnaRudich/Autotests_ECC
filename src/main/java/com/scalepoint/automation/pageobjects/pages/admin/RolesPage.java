package com.scalepoint.automation.pageobjects.pages.admin;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import ru.yandex.qatools.htmlelements.element.Select;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

@EccPage
public class RolesPage extends AdminBasePage {

    @FindBy(id = "btnAdd")
    private SelenideElement addButton;
    @FindBy(id = "btnEdit")
    private SelenideElement editButton;
    @FindBy(id = "btnMembers")
    private SelenideElement membersButton;

    private Select getRoles(){

        return new Select($(By.name("roleList")));
    }

    @Override
    protected void ensureWeAreOnPage() {

        waitForUrl(getRelativeUrl());
        waitForAjaxCompletedAndJsRecalculation();
        addButton.should(Condition.visible);
    }

    @Override
    protected String getRelativeUrl() {

        return "webshop/jsp/Admin/role_permissions.jsp";
    }

    public RoleAddEditPage toAddRolePage() {

        hoverAndClick(addButton);
        return at(RoleAddEditPage.class);
    }

    public boolean isRoleDisplayed(String roleName) {

        try {

            getRoles().selectByVisibleText(roleName);
        } catch (NoSuchElementException e) {

            return false;
        }
        return true;
    }

    public RolesPage assertRoleDisplayed(String roleName) {

        Assert.assertTrue(isRoleDisplayed(roleName));
        return this;
    }
}