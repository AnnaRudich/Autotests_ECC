package com.scalepoint.automation.pageobjects.pages.admin;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ex.ElementNotFound;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.CheckBox;
import ru.yandex.qatools.htmlelements.element.Select;
import ru.yandex.qatools.htmlelements.element.Table;

import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

@EccPage
public class EditReasonsPage extends AdminBasePage {

    @FindBy(id = "editNotLowestChoiceReasonsForm")
    private SelenideElement editReasonsForm;
    @FindBy(xpath = "//input[contains(@id,'reasonNameAdd')]")
    private SelenideElement addReasonField;
    @FindBy(xpath = "//button[contains(text(),'Save')]")
    private SelenideElement save;
    @FindBy(xpath = "//table[@id='reasons_table']//tbody//td//input")
    private ElementsCollection reasonsFields;

    private Button getChangeStatus(){

        return new Button($(By.xpath("//button[contains(@id,'changeStatusBtn')]")));
    }

    private Table getReasons(){

        return new Table($(By.id("reasons_table")));
    }

    private CheckBox getShowDisabledCheckbox(){

        return new CheckBox($(By.id("showDisabled")));
    }

    private Select getReasonTypes(){

        return new Select($(By.id("reason_type_dropdown")));
    }

    private Select getCompanies(){

        return new Select($(By.id("company_list_dropdown")));
    }

    private Button getRefresh(){

        return new Button($(By.xpath("//button[contains(text(), 'Refresh')]")));
    }

    private Button getAddReason(){

        return new Button($(By.id("addLine")));
    }

    public enum ReasonType {

        DISCRETIONARY("Discretionary choice"),
        NOTLOWEST("Not lowest choice"),
        REJECT("Reject choice");

        private String text;

        ReasonType(String text) {
            this.text = text;
        }
    }

    @Override
    protected void ensureWeAreOnPage() {

        waitForUrl(getRelativeUrl());
        waitForAjaxCompletedAndJsRecalculation();
        $(getReasonTypes()).should(Condition.visible);
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/Admin/edit_reasons.jsp";
    }

    public EditReasonsPage applyFilters(String insuranceCompany, ReasonType reasonType, boolean showDisabled) {

        getCompanies().selectByVisibleText(insuranceCompany);
        getReasonTypes().selectByVisibleText(reasonType.text);
        getShowDisabledCheckbox().set(showDisabled);
        getRefresh().click();
        return at(EditReasonsPage.class);
    }

    public EditReasonsPage addReason(String reason) {

        $(addReasonField).setValue(reason);
        hoverAndClick(save);
        return at(EditReasonsPage.class);
    }

    public EditReasonsPage assertEditReasonsFormVisible() {

        assertTrue(editReasonsForm.isDisplayed(), "Edit Reasons Form should be visible");
        return this;
    }

    public EditReasonsPage assertReasonNotFound(String reason) {

        try {

            new ReasonRow(reason);
            fail("Reason is found: " + reason);
        } catch (ElementNotFound e) {

            logger.info(e.getMessage());
        }
        return this;
    }

    public EditReasonsPage assertReasonDisabled(String reason) {

        Assert.assertTrue(new ReasonRow(reason).isReasonDisabled(), "Reason must be disabled");
        return this;
    }

    public ReasonRow findReason(String reason) {
        return new ReasonRow(reason);
    }

    public class ReasonRow {

        private SelenideElement element;
        private int rowNumber;
        private boolean readonly;
        private boolean disabled;
        private SelenideElement disableButton;
        private SelenideElement deleteButton;

        public ReasonRow(String reasonName) {

            element = $(By.xpath("//input[contains(@value, '" + reasonName + "')]"));
            String prefix = "reasonNameEdit_";
            String id = element.getAttribute("id");
            this.rowNumber = Integer.valueOf(id.substring(id.indexOf(prefix) + prefix.length()));
            this.readonly = element.getAttribute("readOnly") != null;
            this.disabled = driver.findElement(By.xpath("//input[@id='" + rowNumber + "_isActive']")).getAttribute("value").equals("0");

            this.disableButton = $(By.id(rowNumber + "_changeStatusBtn"));
            this.deleteButton = $(By.id(rowNumber + "_deleteBtn"));
        }

        public boolean isReasonEditable() {
            return !readonly;
        }

        public boolean isReasonDisabled() {
            return disabled;
        }

        public boolean isDeleteEnabled() {
            return deleteButton.isEnabled();
        }

        public EditReasonsPage delete() {

            deleteButton.click();

            if (isAlertPresent()) {

                acceptAlert();
            }
            Wait.waitForAjaxCompletedAndJsRecalculation();
            element.should(Condition.not(Condition.exist));
            return at(EditReasonsPage.class);
        }

        public EditReasonsPage disable() {

            if (!disableButton.getText().equals("Disable")) {

                throw new IllegalStateException("Disable button has wrong state! Must be - Disable");
            }
            return clickOnDisableButton();
        }

        public EditReasonsPage enable() {

            if (!disableButton.getText().equals("Enable")) {

                throw new IllegalStateException("Enable button has wrong state! Must be - Enable");
            }
            return clickOnDisableButton();
        }

        private EditReasonsPage clickOnDisableButton() {

            disableButton.click();

            if (isAlertPresent()) {

                acceptAlert();
            }
            return at(EditReasonsPage.class);
        }

        public EditReasonsPage getPage() {
            return EditReasonsPage.this;
        }

        public ReasonRow doAssert(Consumer<Asserts> assertFunc) {

            assertFunc.accept(new Asserts());
            return ReasonRow.this;
        }

        public class Asserts {

            public Asserts assertDeleteIsDisabled() {

                Assert.assertFalse(deleteButton.isEnabled(), "Delete button must be disabled!");
                return this;
            }

            public Asserts assertReasonIsEditable() {

                Assert.assertFalse(readonly, "The reason field should be enabled!");
                return this;
            }

            public Asserts assertReasonIsNotEditable() {

                Assert.assertTrue(readonly, "The reason field should be disabled!");
                return this;
            }

        }
    }

}


