package com.scalepoint.automation.pageobjects.pages.admin;

import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.pages.MyPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.data.entity.SystemUser;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompleted;
import static com.scalepoint.automation.utils.Wait.waitForStaleElements;

@EccPage
public class UsersPage extends AdminBasePage {

    @FindBy(xpath = "//button[contains(@class,'icon-create')]")
    private WebElement createUserButton;

    @FindBy(xpath = "//input[contains(@class,'x-form-field')]")
    private WebElement quickSearchField;

    @FindBy(linkText = "Users")
    private WebElement usersLink;

    @FindBy(id = "btnRefresh")
    private WebElement refreshButton;

    private String byUserLoginXpath = "id('user-grid')//table[@class='x-grid3-row-table']//tr[contains(.,'$1')]";

    private String filterByIcXpath = "//select[contains(@name, 'company')]/option[contains(text(), '$1')]";

    @Override
    protected Page ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        Wait.waitForVisible(createUserButton);
        return this;
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/Admin/users.jsp";
    }

    public UserAddEditPage toUserCreatePage() {
        scrollTo(createUserButton);
        clickAndWaitForDisplaying(createUserButton, By.id("userLoginId"));
        return at(UserAddEditPage.class);
    }

    public void refreshUsersList() {
        refreshButton.click();
        waitForAjaxCompleted();
    }

    public void makeUserSearchByName(String query) {
        quickSearchField.clear();
        quickSearchField.sendKeys(query);
        quickSearchField.sendKeys(Keys.ENTER);
        waitForAjaxCompleted();
    }

    public boolean isUserExists(SystemUser user) {
        filterByIC(user.getCompany());
        makeUserSearchByName(user.getLogin());
        WebElement item = find(byUserLoginXpath, user.getLogin());
        return item.getText().contains(user.getLogin());
    }

    public boolean isUserDisplayed(SystemUser user) {
        filterByIC(user.getCompany());
        makeUserSearchByName(user.getLogin());
        waitForStaleElements((By.xpath("id('user-grid')//table[@class='x-grid3-row-table']//tr")));
        WebElement item = find(byUserLoginXpath, user.getLogin());
        return item.getText().contains(user.getLogin());
    }

    public boolean isDisplayed(SystemUser user) {
        makeUserSearchByName(user.getLogin());
        waitForAjaxCompleted();
        waitForStaleElements((By.xpath("id('user-grid')//table[@class='x-grid3-row-table']//tr")));
        WebElement item = find(byUserLoginXpath, user.getLogin());
        return item.getText().contains(user.getLogin()) &&
                item.getText().contains((user.getFirstName())) &&
                item.getText().contains(user.getLastName());
    }

    public UserAddEditPage openUserForEditing(String userName) {
        makeUserSearchByName(userName);
        waitForAjaxCompleted();
        SelenideElement element = $(By.xpath("id('user-grid')//table[@class='x-grid3-row-table']//tr[1]/td[2]"));
        WebElement item = find(byUserLoginXpath, userName);
        if (item.getText().contains(userName)) {
            scrollTo(item);
            element.doubleClick();
        }
        return at(UserAddEditPage.class);
    }

    public UsersPage filterByIC(String companyName) {
        String xpath = filterByIcXpath.replace("$1", companyName);
        $(By.xpath(xpath)).click();
        refreshUsersList();
        return this;
    }

    public MyPage toMatchingEngine() {
        return to(AdminPage.class).toMatchingEngine();
    }

    public UsersPage assertUserExists(SystemUser newUser) {
        Assert.assertTrue(isDisplayed(newUser), "User is not found");
        return this;
    }
}
