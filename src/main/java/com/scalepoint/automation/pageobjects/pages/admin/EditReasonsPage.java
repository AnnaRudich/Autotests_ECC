package com.scalepoint.automation.pageobjects.pages.admin;

import com.scalepoint.automation.pageobjects.extjs.ExtCheckbox;
import com.scalepoint.automation.pageobjects.extjs.ExtInput;
import com.scalepoint.automation.pageobjects.pages.LoginPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.CheckBox;
import ru.yandex.qatools.htmlelements.element.Select;
import ru.yandex.qatools.htmlelements.element.Table;

import java.util.List;

import static com.scalepoint.automation.utils.Wait.waitForVisible;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

/**
 * Created by asa on 11/14/2016.
 */
@EccPage
public class EditReasonsPage extends AdminBasePage {

    public enum ReasonType {
        DISCRETIONARY("Discretionary choice"),
        NOTLOWEST("Not lowest choice"),
        REJECT("Reject choice");

        private String text;

        ReasonType(String text) {
            this.text = text;
        }
    }

    @FindBy(id = "reason_type_dropdown")
    private Select reasonTypes;
    @FindBy(id = "company_list_dropdown")
    private Select companies;
    @FindBy(xpath = "//button[contains(text(), 'Refresh')]")
    private Button refresh;
    @FindBy(id = "editNotLowestChoiceReasonsForm")
    private WebElement editReasonsForm;
    @FindBy(xpath = "//table[@id='reasons_table']//tbody//td//input")
    private List<WebElement> reasonsFields;
    @FindBy(id = "addLine")
    private Button addReason;
    @FindBy(xpath = "//input[contains(@id,'reasonNameAdd')]")
    private ExtInput addReasonField;
    @FindBy(xpath = "//button[contains(text(),'Save')]")
    private Button save;
    @FindBy(xpath = "//button[contains(@id,'changeStatusBtn')]")
    private Button changeStatus;
    @FindBy(id = "reasons_table")
    private Table reasons;
    @FindBy(id = "showDisabled")
    private CheckBox showDisabledCheckbox;

    @Override
    protected Page ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForVisible(reasonTypes);
        return this;
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/Admin/edit_reasons.jsp";
    }

    public EditReasonsPage applyFilters(String insuranceCompany, ReasonType reasonType, boolean showDisabled) {
        companies.selectByVisibleText(insuranceCompany);
        reasonTypes.selectByVisibleText(reasonType.text);
        showDisabledCheckbox.set(showDisabled);
        refresh.click();
        return at(EditReasonsPage.class);
    }

    public EditReasonsPage addReason(String reason) {
        addReasonField.enter(reason);
        save.click();
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
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return this;
    }

    public EditReasonsPage assertReasonDisabled(String reason) {
        Assert.assertTrue(new ReasonRow(reason).isReasonDisabled(), "Reason is active: " + reason);
        return this;
    }

    public ReasonRow findReason(String reason) {
        return new ReasonRow(reason);
    }

    public class ReasonRow {
        private int rowNumber;
        private boolean readonly;
        private boolean disabled;
        private WebElement disableButton;
        private WebElement deleteButton;

        public ReasonRow(String reasonName) {
            WebElement reasonInput = driver.findElement(By.xpath("//input[contains(@value, '" + reasonName + "')]"));
            String prefix = "reasonNameEdit_";
            String id = reasonInput.getAttribute("id");
            this.rowNumber = Integer.valueOf(id.substring(id.indexOf(prefix) + prefix.length()));
            this.readonly = reasonInput.getAttribute("readOnly") != null;
            WebElement reasonRow = driver.findElement(By.id(rowNumber + "_reasonRow"));
            this.disabled = !reasonRow.findElement(By.tagName("input")).isEnabled();

            this.disableButton = driver.findElement(By.id(rowNumber + "_changeStatusBtn"));
            this.deleteButton = driver.findElement(By.id(rowNumber + "_deleteBtn"));
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

        public ReasonRow assertDeleteIsDisabled() {
            Assert.assertFalse(deleteButton.isEnabled(), "Delete button must be disabled!");
            return this;
        }

        public ReasonRow assertReasonIsEditable() {
            Assert.assertFalse(readonly, "The reason field should be enabled!");
            return this;
        }

        public ReasonRow assertReasonIsNotEditable() {
            Assert.assertTrue(readonly, "The reason field should be disabled!");
            return this;
        }
    }

}


