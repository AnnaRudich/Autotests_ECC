package com.scalepoint.automation.pageobjects.pages.admin;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.google.common.collect.Lists;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.OperationalUtils;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.data.entity.input.SystemUser;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForPageLoaded;
import static org.assertj.core.api.Assertions.assertThat;

@EccPage
public class UserAddEditPage extends AdminBasePage {

    @FindBy(name = "userCompanyId")
    private WebElement companySelect;

    @FindBy(tagName = "option")
    private List<WebElement> options;

    @FindBy(id = "btnOk")
    private WebElement saveButton;

    @FindBy(id = "btnCancel")
    private WebElement cancelButton;

    @FindBy(name = "userDepartmentToken")
    private WebElement departmentSelect;

    @FindBy(name = "userLogin")
    private WebElement loginField;

    @FindBy(name = "userPassword1")
    private WebElement passwordField;

    @FindBy(name = "userPassword2")
    private WebElement passwordRetField;

    @FindBy(name = "userFirstName")
    private WebElement firstNameField;

    @FindBy(name = "userLastName")
    private WebElement lastNameField;

    @FindBy(name = "userEmail")
    private WebElement emailField;

    @FindBy(id = "userType4")
    private WebElement adminCheckBox;

    @FindBy(id = "userType2")
    private WebElement chCheckBox;

    @FindBy(id = "userType8")
    private WebElement smCheckBox;

    @FindBy(name = "ROLE_ID_1")
    private WebElement itManCheckBox;

    @FindBy(xpath = "//input[@id='userCanCreateNewCases']")
    private WebElement createNewCaseManually;

    @FindBy(name = "userCulture")
    private WebElement cultureSelect;

    @FindBy(xpath = "//*[@id='rolesDiv']/table/tbody/tr/td[1] | //div[@id='rolesDiv']/table/tbody/tr/td[2]/input")
    private List<WebElement> existingRoles;

    @FindBy(xpath = "//div[@id='rolesDiv']/table/tbody/tr/td[2]/input")
    private List<WebElement> existingRolesBox;

    @FindBy(id = "btnGenerate")
    private WebElement generatePasswordButton;

    private String byCompanyPath = "select[name=userCompanyId]";
    private String byDepartmentPath = "select[name=userDepartmentToken]";
    private String byRolesXpath = "//*[@id='rolesDiv']/table/tbody/tr/td[1][contains(.,'%s')]";

    @Override
    protected void ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForPageLoaded();
        $(loginField).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/Admin/user_edit.jsp";
    }

    /**
     * This method fills all required text fields for new user
     */
    public void fillUserGeneralData(SystemUser user) {

        $(loginField).setValue(user.getLogin());
        $(passwordField).setValue(user.getPassword());
        $(passwordRetField).setValue(user.getPassword());
        $(firstNameField).setValue(user.getFirstName());
        $(lastNameField).setValue(user.getLastName());
        $(emailField).setValue(user.getEmail());
    }

    /**
     * This method clears all required fields and type settings for existing SP Admin and It manager
     */
    public UserAddEditPage clearFields() {
        loginField.clear();
        passwordField.clear();
        passwordRetField.clear();
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        enableSMType();
        selectITManagerRole();
        return this;
    }

    /**
     * This method clicks IT Manager Role not depending is option enabled or not
     */
    public void selectITManagerRole() {
        itManCheckBox.click();
    }

    public void selectCreateNewCaseManually() {
        createNewCaseManually.click();
    }

    public UserAddEditPage enableSMType() {
        if (!smCheckBox.isSelected()) {
            smCheckBox.click();
        }
        return this;
    }

    public UserAddEditPage disableSMType() {
        if (smCheckBox.isSelected()) {
            smCheckBox.click();
        }
        return this;
    }

    public UserAddEditPage disableCHType() {
        if (chCheckBox.isSelected()) {
            chCheckBox.click();
        }
        return this;
    }

    public UserAddEditPage enableCHType() {
        if (!chCheckBox.isSelected()) {
            chCheckBox.click();
        }
        return this;
    }

    public UserAddEditPage disableAdminType() {
        if (adminCheckBox.isSelected()) {
            adminCheckBox.click();
        }
        return this;
    }

    public UserAddEditPage enableAdminType() {
        if (!adminCheckBox.isSelected()) {
            adminCheckBox.click();
        }
        return this;
    }

    public UsersPage selectSaveOption() {
        try {
            $(saveButton).click();
            acceptAlert();
            $(By.xpath("//button[contains(@class,'icon-create')]")).shouldBe(Condition.visible);
        } catch (TimeoutException e) {
            logger.error(e.getMessage());
            $(saveButton).click();
            acceptAlert();
            $(By.xpath("//button[contains(@class,'icon-create')]")).shouldBe(Condition.visible);
        }
        return at(UsersPage.class);
    }

    public <T extends Page> T selectSaveOption(Class<T> page) {
        clickUsingJavaScriptIfClickDoesNotWork(saveButton);
        return at(page);
    }

    public UsersPage update(SystemUser user) {
        $(byDepartmentPath).selectOption(user.getDepartment());
        fillUserGeneralData(user);
        enableSMType();
        selectITManagerRole();
        selectSaveOption();
        return at(UsersPage.class);
    }

    /**
     * This method creates new SP Admin and IT manager user
     */
    public UsersPage createUser(SystemUser user, UserType... userTypesArr) {
        createUserWithoutSaving(user, userTypesArr);

        selectSaveOption();
        return at(UsersPage.class);
    }

    public UserAddEditPage createUserWithoutSaving(SystemUser user, UserType[] userTypesArr) {
        ArrayList<UserType> userTypes = Lists.newArrayList(userTypesArr);
        $(byCompanyPath).selectOption(user.getCompany());
        $(byDepartmentPath).selectOption(user.getDepartment());
        fillUserGeneralData(user);
        if (userTypes.contains(UserType.ADMIN)) {
            enableAdminType();
            selectITManagerRole();
            enableCHType();
        } else {
            disableAdminType();
        }
        if (userTypes.contains(UserType.CLAIMSHANDLER)) {
            enableCHType();
        } else {
            disableCHType();
        }
        if (userTypes.contains(UserType.SUPPLYMANAGER)) {
            enableSMType();
        } else {
            disableSMType();
        }

        if (userTypes.contains(UserType.ADMIN) || userTypes.contains(UserType.CLAIMSHANDLER)) {
            selectCreateNewCaseManually();
        }
        return this;
    }

    public enum UserType {
        ADMIN,
        CLAIMSHANDLER,
        SUPPLYMANAGER
    }

    /**
     * This method disable  It manager role and clicks new Role
     */
    public void selectNewRoleSPUser(String roleName) {
        if (itManCheckBox.isSelected()) {
            itManCheckBox.click();
        }

        SelenideElement option = $(By.xpath(String.format(byRolesXpath, roleName)));

        if (option.getText().equals(roleName)) {
            option.scrollTo();
            existingRolesBox.get(existingRolesBox.size() - 1).click();
        }
    }

    public boolean checkGeneratePasswordButton() {
        return isDisplayed(generatePasswordButton);
    }

    public String generateAndGetNewPassword() {
        clickUsingJavaScriptIfClickDoesNotWork(generatePasswordButton);
        return getAlertTextAndAccept().split(" ")[1];
    }

    public UsersPage createNewSPAdminNewRole(SystemUser user, String roleName) {

        $(byCompanyPath).selectOption(user.getCompany());
        $(byDepartmentPath).selectOption(user.getDepartment());

        fillUserGeneralData(user);
        selectNewRoleSPUser(roleName);
        return selectSaveOption();
    }

    public UserAddEditPage doAssert(Consumer<Asserts> assertFunc) {
        assertFunc.accept(new Asserts());
        return this;
    }

    public class Asserts {

        public Asserts assertIsGenerateButtonVisible() {
            assertThat(checkGeneratePasswordButton()).isTrue();
            return this;
        }

        public Asserts assertIsGeneratedPasswordCorrect(String generatedPassword) {
            OperationalUtils.assertStringMatchingPattern("(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?!.*[&%$]#).{6,}", generatedPassword);
            return this;
        }

        public Asserts assertIsAlertPresent() {
            assertThat($(By.tagName("i")).isDisplayed()).isTrue();
            return this;
        }

    }
}

