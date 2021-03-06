package com.scalepoint.automation.pageobjects.pages.admin;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.pages.MyPage;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.data.entity.input.SystemUser;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompleted;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

@EccPage
public class UsersPage extends AdminBasePage {

    @FindBy(xpath = "//button[contains(@class,'icon-create')]")
    private SelenideElement createUserButton;
    @FindBy(xpath = "//input[contains(@class,'x-form-field')]")
    private SelenideElement quickSearchField;
    @FindBy(linkText = "Users")
    private SelenideElement usersLink;
    @FindBy(id = "btnRefresh")
    private SelenideElement refreshButton;

    private String byUserLoginXpath = "id('user-grid')//table[@class='x-grid3-row-table']//tr[contains(.,'%s')]";
    private String filterByIcXpath = "//select[contains(@name, 'company')]/option[contains(text(), '$1')]";

    @Override
    protected void ensureWeAreOnPage() {

        waitForUrl(getRelativeUrl());
        waitForAjaxCompletedAndJsRecalculation();
        createUserButton.should(Condition.visible);
    }

    @Override
    protected String getRelativeUrl() {

        return "webshop/jsp/Admin/users.jsp";
    }

    public UserAddEditPage toUserCreatePage() {

        hoverAndClick(createUserButton.scrollTo());
        return at(UserAddEditPage.class);
    }

    private void refreshUsersList() {

        refreshButton.click();
        waitForAjaxCompleted();
    }

    private void makeUserSearchByName(String query) {

        quickSearchField.clear();
        quickSearchField.sendKeys(query);
        quickSearchField.sendKeys(Keys.ENTER);
        waitForAjaxCompleted();
    }

    public boolean isDisplayed(SystemUser user) {

        refreshUsersList();
        waitForAjaxCompleted();
        makeUserSearchByName(user.getLogin());
        SelenideElement item;

        try {

            item = $(By.xpath(String.format(byUserLoginXpath, user.getLogin())));
        } catch (TimeoutException e) {

            logger.error(e.getMessage());
            makeUserSearchByName(user.getLogin());
            item = $(By.xpath(String.format(byUserLoginXpath, user.getLogin())));
        }
        return item.getText().contains(user.getLogin()) &&
                item.getText().contains((user.getFirstName())) &&
                item.getText().contains(user.getLastName());
    }

    public UserAddEditPage openUserForEditing(String userName) {

        makeUserSearchByName(userName);
        waitForAjaxCompleted();
        SelenideElement element = $(By.xpath("id('user-grid')//table[@class='x-grid3-row-table']//tr[1]/td[2]"));
        WebElement item = $(By.xpath(String.format(byUserLoginXpath, userName)));

        if (item.getText().contains(userName)) {

            $(item).scrollTo();
            element.click();
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

    public UsersPage doAssert(Consumer<Asserts> assertsFunc) {

        assertsFunc.accept(new Asserts());
        return UsersPage.this;
    }

    public class Asserts {
        public Asserts assertUserExists(SystemUser newUser) {

            Assert.assertTrue(isDisplayed(newUser), "User is not found");
            return this;
        }
    }
}
