package com.scalepoint.automation.pageobjects.pages.admin;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import ru.yandex.qatools.htmlelements.element.Select;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForPageLoaded;

@EccPage
public class RolesPage extends AdminBasePage {

    @FindBy(id = "btnAdd")
    private WebElement addButton;

    @FindBy(id = "btnEdit")
    private WebElement editButton;

    @FindBy(id = "btnMembers")
    private WebElement membersButton;

    @FindBy(name = "roleList")
    private Select roles;

    @Override
    protected void ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForPageLoaded();
        $(addButton).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/Admin/role_permissions.jsp";
    }

    public RoleAddEditPage toAddRolePage() {
        clickAndWaitForDisplaying(addButton, By.name("roleName"));
        return at(RoleAddEditPage.class);
    }

    public boolean isRoleDisplayed(String roleName) {
        try {
            roles.selectByVisibleText(roleName);
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;
    }

    public void selectEditOption() {
        clickAndWaitForDisplaying(editButton, By.id("btnOk"));
    }

    public RoleAddEditPage editRole(String roleName) {
        roles.selectByVisibleText(roleName);
        selectEditOption();
        return at(RoleAddEditPage.class);
    }

    public RolesPage assertRoleDisplayed(String roleName) {
        Assert.assertTrue(isRoleDisplayed(roleName));
        return this;
    }
}