package com.scalepoint.automation.pageobjects.pages.admin;

import com.google.common.collect.Lists;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.data.entity.SystemUser;
import com.scalepoint.automation.utils.data.entity.Roles;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.List;

import static com.scalepoint.automation.utils.Wait.waitForAjaxComplete;

@EccPage
public class UserAddEditPage extends Page {

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
    private List<WebElement> generatePasswordButton;

    private String byCompanyXpath = "//select/option[text() = '$1']";
    private String byDepartmentXpath = "//*[@id='DepartmentDiv']/select/option[text()='$1']";
    private String bySubDepartmentXpath = "//div[@id='SubDepartmentDiv']/select/option[contains(.,'$1')]";
    private String byRolesXpath = "//*[@id='rolesDiv']/table/tbody/tr/td[1][contains(.,'$1')]";

    @Override
    protected Page ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        Wait.waitForVisible(loginField);
        return this;
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/Admin/user_edit.jsp";
    }

    /**
     * This method fills all required text fields for new user
     */
    public void fillUserGeneralData(SystemUser user) {
        setValue(loginField, user.getLogin());
        setValue(passwordField, user.getPassword());
        setValue(passwordRetField, user.getPassword());
        setValue(firstNameField, user.getFirstName());
        setValue(lastNameField, user.getLastName());
        setValue(emailField, user.getEmail());
    }

    public void checkPasswordRule(SystemUser user, String password) {
        loginField.clear();
        loginField.sendKeys(user.getLogin());
        passwordField.clear();
        passwordField.sendKeys(password);
        passwordRetField.clear();
        passwordRetField.sendKeys(password);
        firstNameField.clear();
        firstNameField.sendKeys(user.getFirstName());
        lastNameField.clear();
        lastNameField.sendKeys(user.getLastName());
        emailField.clear();
        emailField.sendKeys(user.getEmail());
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

    public void selectCancelOption() {
        clickAndWaitForDisplaying(cancelButton, By.xpath("//button[contains(@class,'icon-create')]"));
    }

    public UsersPage selectSaveOption() {
        clickAndWaitForDisplaying(saveButton, By.xpath("//button[contains(@class,'icon-create')]"));
        return at(UsersPage.class);
    }

    /**
     * This method verifies Rule restrictions (happy flow)
     */
    public void verifyRuleRestrictions(SystemUser user, String login, String password) {
        WebElement option = find(byCompanyXpath, user.getCompany());
        waitForAjaxComplete();

        if (option.getText().equals(user.getCompany())) {
            option.click();
        }
        waitForAjaxComplete();
        WebElement option1 = find(byDepartmentXpath, user.getDepartment());
        if (option1.getText().equals(user.getDepartment())) {
            option1.click();
        }
        checkPasswordRule(user, password);
        selectSaveOption();
        waitForAjaxComplete();
    }

    /**
     * This method verifies Rule restrictions (incorrect password and check for alert)
     */
    public void verifyRuleRestrictionsWithAlert(SystemUser user, String login, String password, String message) {
        WebElement option = find(byCompanyXpath, user.getCompany());
        if (option.getText().equals(user.getCompany())) {
            option.click();
        }
        WebElement option1 = find(byDepartmentXpath, user.getDepartment());
        if (option1.getText().equals(user.getDepartment())) {
            option1.click();
        }
        checkPasswordRule(user, password);
        saveButton.click();
        String alertTextAndAccept = getAlertTextAndAccept();
        Assert.assertEquals(alertTextAndAccept, message);
        waitForAjaxComplete();

    }

    /**
     * This method verifies Rule restrictions (incorrect password and check for info box)
     */
    public void verifyRuleRestrictionsWithInfo(SystemUser user, String login, String password) {
        WebElement option = find(byCompanyXpath, user.getCompany());
        waitForAjaxComplete();
        if (option.getText().equals(user.getCompany())) {
            option.click();
        }
        WebElement option1 = find(byDepartmentXpath, user.getDepartment());
        if (option1.getText().equals(user.getDepartment())) {
            option1.click();
        }
        checkPasswordRule(user, password);
        saveButton.click();
        try {
            getAlertTextAndAccept();
        } catch (Exception e) {

        }
        find(By.xpath("//i[contains(text(),'Your password must follow the rules')]"));
        waitForAjaxComplete();

    }


    public UsersPage update(SystemUser user) {
        WebElement option1 = find(byDepartmentXpath, user.getDepartment());
        if (option1.getText().equals(user.getDepartment())) {
            option1.click();
        }
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
        ArrayList<UserType> userTypes = Lists.newArrayList(userTypesArr);
        find(byCompanyXpath, user.getCompany()).click();
        find(byDepartmentXpath, user.getDepartment()).click();
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

        selectSaveOption();
        return at(UsersPage.class);
    }

    public static enum UserType {
        ADMIN,
        CLAIMSHANDLER,
        SUPPLYMANAGER
    }


    /**
     * This method checks if new role proposed for new user or not
     */
    public boolean isNewRoleDisplayed(Roles roles) {
        WebElement option = find(byRolesXpath, roles.getRoleName());
        return option.getText().contains(roles.getRoleName());
    }

    /**
     * The method selects ScalePoint Company. The method is jest technical.
     */
    public void selectSPCompany(SystemUser user) {
        WebElement option = find(byCompanyXpath, user.getCompany());
        if (option.getText().equals(user.getCompany())) {
            scrollTo(option);
            option.click();
        }
    }

    /**
     * This method disable  It manager role and clicks new Role
     */
    public void selectNewRoleSPUser(SystemUser user, Roles roles) {
        if (itManCheckBox.isSelected()) {
            itManCheckBox.click();
        }
        WebElement option = find(byRolesXpath, roles.getRoleName());
        if (option.getText().equals(roles.getRoleName())) {
            scrollTo(option);
            existingRolesBox.get(existingRolesBox.size() - 1).click();
        }
    }

    public void checkGeneratePasswordButton() {
        find(By.id("btnGenerate"));
    }

    public void createNewSPAdminNewRole(SystemUser user, Roles roles) {
        WebElement option = find(byCompanyXpath, user.getCompany());
        if (option.getText().equals(user.getCompany())) {
            option.click();
        }

        WebElement option1 = find(byDepartmentXpath, user.getDepartment());
        if (option1.getText().equals(user.getDepartment())) {
            option1.click();
        }

        fillUserGeneralData(user);
        selectNewRoleSPUser(user, roles);
        selectSaveOption();
    }
}
