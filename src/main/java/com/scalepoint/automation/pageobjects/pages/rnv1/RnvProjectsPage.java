package com.scalepoint.automation.pageobjects.pages.rnv1;

import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.data.entity.ServiceAgreement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

@EccPage
public class RnvProjectsPage extends Page {
    @FindBy(xpath = "//span[contains(text(),'Kommunikation')]")
    private WebElement communicationTab;

    @FindBy(xpath = "//span[contains(text(),'Factura')]")
    private WebElement invoiceTab;

    @FindBy(xpath = "//span[contains(text(),'Status')]")
    private WebElement statusTab;

    @FindBy(xpath = "//span[contains(text(),'Oversigt')]")
    private WebElement overviewTab;

    //Overwiev tab
    private String byAgrXpath = "//div[contains(@id,'project_view_id')]//tr[1]//tr[1]/td/div[contains(text(), '$1')]";
    private String byTaskIdAgrXpath = "//div[contains(@id,'project_view_id')]//tr[1]//tr[1]/td/div[contains(text(), '$1')]/ancestor::tr[1]/td[2]/div";
    private String byTaskStatusAgrXpath = "//div[contains(@id,'project_view_id')]//tr[1]//tr[1]/td/div[contains(text(), '$1')]/ancestor::tr[1]/td[5]/div";
    private String byClaimLineXpath = "//span[contains(text(),'$1')]";

    @FindBy(css = "div#taskNestedGrid-body tr td:nth-of-type(2) div")
    private WebElement taskTypeFirstLine;

    @FindBy(css = "table[class*='x-grid-with-row-lines'] tr:first-of-type tr div.x-grid-row-expander")
    private WebElement firstTaskExpander;

    @FindBy(css = "div[id*='messagebox'] a:nth-of-type(2) span span span:first-of-type")
    private WebElement yesBtnConfirmationDialog;

    @FindBy(css = "div[id*='messagebox'] a:nth-of-type(1) span span span:first-of-type")
    private WebElement okBtnConfirmationDialog;

    @FindBy(css = "div[id*='messagebox'] a:nth-of-type(3) span span span:first-of-type")
    private WebElement noBtnConfirmationDialog;

    @FindBy(css = "div[id*='messagebox'] a:nth-of-type(4) span span span:first-of-type")
    private WebElement cancelBtnConfirmationDialog;

    @FindBy(id = "button-overview-cancel")
    private WebElement cancelTaskBtn;

    @FindBy(css = "a#button-overview-accept span span span:first-of-type")
    private WebElement acceptTaskBtn;

    @FindBy(css = "a#button-overview-reject span span span:first-of-type")
    private WebElement rejectTaskBtn;

    @FindBy(css = "div.x-column-header-checkbox span")
    private WebElement selectAllLinesCheckbox;

    @Override
    protected Page ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        return this;
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/matching_engine/projects.jsp";
    }

    public String getTaskStatus(String agrName) {
        return getText(By.xpath(byTaskStatusAgrXpath.replace("$1", agrName)));
    }

    public Boolean isTaskDisplayed(String agrName) {
        return isElementPresent(By.xpath(byAgrXpath.replace("$1", agrName)));
    }

    public RnvCommunicationPage toCommunicationTab() {
        clickAndWaitForDisplaying(communicationTab, By.cssSelector("td#combo-communication-task-inputCell"));
        return at(RnvCommunicationPage.class);
    }

    public void navigateToInvoiceTab() {
        clickAndWaitForStable(invoiceTab, By.cssSelector("div#grid-invoice-body"));
    }

    public String getFirstTaskType() {
        return getText(taskTypeFirstLine);
    }

    public RnvProjectsPage expandTopTaskDetails() {
        clickAndWaitForDisplaying(firstTaskExpander, By.cssSelector("div#taskNestedGrid-body"));
        return this;
    }

    public RnvProjectsPage cancelTopTask() {
        cancelTaskBtn.click();
        yesBtnConfirmationDialog.click();
        Wait.waitForAjaxCompleted();
        return this;
    }

    public String getActionByCLName(String cLName) {
        String actionXpath = byClaimLineXpath.replace("$1", cLName) + "/ancestor::tr[1]/td[contains(@class, 'accepAction')]/div";
        WebElement item = find(By.xpath(actionXpath));
        return item.getAttribute("innerText");
    }

    public String getTaskTypeByCLName(String cLName) {
        String taskTypeXpath = byClaimLineXpath.replace("$1", cLName) + "/ancestor::tr[1]/td[contains(@id, 'taskType')]/div";
        return getText(By.xpath(taskTypeXpath));
    }

    public boolean isButtonPresent(ButtonType buttonType) {
        try {
            switch (buttonType) {
                case ACCEPT:
                    return acceptTaskBtn.isDisplayed();
                case REJECT:
                    return rejectTaskBtn.isDisplayed();
                case CANCEL:
                    return cancelTaskBtn.isDisplayed();
                default:
                    return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public RnvProjectsPage acceptCL(String cLName) {
        selectCLByName(cLName);
        acceptTaskBtn.click();
        yesBtnConfirmationDialog.click();
        Wait.waitForAjaxCompleted();
        return this;
    }

    private void selectCLByName(String cLName) {
        String clCheckboxXpath = byClaimLineXpath.replace("$1", cLName) + "/ancestor::tr[1]/td//div[contains(@class, 'x-grid-row-checker')]";
        find(By.xpath(clCheckboxXpath)).click();
    }

    public RnvProjectsPage rejectCL(String cLName) {
        selectCLByName(cLName);
        rejectTaskBtn.click();
        yesBtnConfirmationDialog.click();
        Wait.waitForAjaxCompleted();
        return this;
    }

    public RnvProjectsPage acceptTaskCompletly() {
        selectAllLines();
        acceptTaskBtn.click();
        yesBtnConfirmationDialog.click();
        Wait.waitForAjaxCompleted();
        return this;
    }

    private void selectAllLines() {
        selectAllLinesCheckbox.click();
    }

    public RnvProjectsPage waitForFeedbackReceived(ServiceAgreement agreement) {
        Wait.waitForElementWithPageReload(By.xpath(byTaskStatusAgrXpath.replace("$1", agreement.getFeedbackReceivedStatusName())));
        return this;
    }

    public Assertion getAssertion() {
        return new Assertion();
    }

    public class Assertion {

        public Assertion assertTaskHasCompletedStatus(ServiceAgreement agreement) {
            String taskStatus = getTaskStatus(agreement.getTestAgreementForRnV());
            Assert.assertEquals(taskStatus, agreement.getCompletedStatusName(), "Task has " + taskStatus + " status. Must be completed");
            return this;
        }

        public Assertion assertTaskHasCancelledStatus(ServiceAgreement agreement) {
            String taskStatus = getTaskStatus(agreement.getTestAgreementForRnV());
            Assert.assertEquals(taskStatus, agreement.getCancelledStatus(), "Task has " + taskStatus + " status. Must be cancelled");
            return this;
        }

        public Assertion assertTaskHasWaitingStatus(ServiceAgreement agreement) {
            String taskStatus = getTaskStatus(agreement.getTestAgreementForRnV());
            Assert.assertEquals(taskStatus, agreement.getWaitingStatus(), "Task has " + taskStatus + " status. Must be waiting");
            return this;
        }

        public Assertion assertTaskHasFeedbackReceivedStatus(ServiceAgreement agreement) {
            String taskStatus = getTaskStatus(agreement.getTestAgreementForRnV());
            Assert.assertEquals(taskStatus, agreement.getFeedbackReceivedStatusName(), "Task has " + taskStatus + " status. Must be feedback received");
            return this;
        }

        public Assertion assertTaskHasPartlyCompletedStatus(ServiceAgreement agreement) {
            String taskStatus = getTaskStatus(agreement.getTestAgreementForRnV());
            Assert.assertEquals(taskStatus, agreement.getPartlyCompletedStatusName(), "Task has " + taskStatus + " status. Must be partly received");
            return this;
        }

        public Assertion assertTaskDisplayed(ServiceAgreement agreement) {
            Assert.assertTrue(isTaskDisplayed(agreement.getTestAgreementForRnV()), "Task is not displayed");
            return this;
        }

        public Assertion assertTaskHasType(String lineDescription, String type) {
            String actualType = getTaskTypeByCLName(lineDescription);
            Assert.assertEquals(actualType, type, "Task has type " + actualType + ". Must be: " + type);
            return this;
        }

        public Assertion assertClaimLineHasFeedback(String claimLineName) {
            String action = getActionByCLName(claimLineName);
            Assert.assertEquals(action, "NO_FEEDBACK_RECEIVED", "Feedback received, but must not be");
            return this;
        }

        public Assertion assertClaimLineHasNoChanges(String claimLineName) {
            String action = getActionByCLName(claimLineName);
            Assert.assertEquals(action, "NO_CHANGES", "Claim line has changes");
            return this;
        }

        public Assertion assertClaimLineHasUpdates(String claimLineName) {
            String action = getActionByCLName(claimLineName);
            Assert.assertEquals(action, "UPDATE_LINE", "Claim line has no changes");
            return this;
        }

        public Assertion assertClaimLineExcluded(String claimLineName) {
            String action = getActionByCLName(claimLineName);
            Assert.assertEquals(action, "EXCLUDE_LINE", "Claim line is not excluded");
            return this;
        }

        public Assertion assertClaimLineCreated(String claimLineName) {
            String action = getActionByCLName(claimLineName);
            Assert.assertEquals(action, "CREATE_LINE", "Claim line is not created");
            return this;
        }

        public Assertion assertClaimLineAccepted(String claimLineName) {
            String action = getActionByCLName(claimLineName);
            Assert.assertEquals(action, "ACCEPTED", "Claim line is not created");
            return this;
        }

        public Assertion assertClaimLineRejected(String claimLineName) {
            String action = getActionByCLName(claimLineName);
            Assert.assertEquals(action, "REJECTED", "Claim line is not rejected");
            return this;
        }

        public Assertion assertButtonPresence(ButtonType buttonType, ButtonPresence buttonPresence) {
            boolean buttonPresent = isButtonPresent(buttonType);
            if (!buttonPresence.isValidPresence(buttonPresent)) {
                Assert.fail(buttonType + " must be " + buttonPresence + " but it's not");
            }
            return this;
        }

        public RnvProjectsPage getPage() {
            return RnvProjectsPage.this;
        }
    }

    public enum ButtonType {
        ACCEPT,
        REJECT,
        CANCEL
    }

    public enum ButtonPresence {
        SHOWN (true),
        HIDDEN (false);

        boolean presence;

        ButtonPresence(boolean presence) {
            this.presence = presence;
        }

        public boolean isValidPresence(boolean actualPresence) {
            return presence == actualPresence;
        }
    }
}


