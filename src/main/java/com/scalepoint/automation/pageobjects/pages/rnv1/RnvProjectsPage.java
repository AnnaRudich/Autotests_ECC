package com.scalepoint.automation.pageobjects.pages.rnv1;

import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.annotations.page.RVPage;
import com.scalepoint.automation.utils.data.entity.ServiceAgreement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@EccPage
public class RnvProjectsPage extends Page {
    @FindBy(css = "div[id*='tabbar'] a:nth-of-type(2) span span span:first-of-type")
    private WebElement comunicationTab;

    @FindBy(css = "div[id*='tabbar'] a:nth-of-type(3) span span span:first-of-type")
    private WebElement invoiceTab;

    @FindBy(css = "div[id*='tabbar'] a:last-of-type span span span:first-of-type")
    private WebElement statusTab;

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

    public RnvCommunicationPage navigateToCommunicationTab() {
        clickAndWaitForDisplaying(comunicationTab, By.cssSelector("td#combo-communication-task-inputCell"));
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
        //clickAndWaitForDisplaying(yesBtnConfirmationDialog, By.id("project_view_id"));
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

    public Boolean isAcceptBtnDisplays() {
        try {
            acceptTaskBtn.isDisplayed();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public Boolean isRejectBtnDisplays() {
        try {
            rejectTaskBtn.isDisplayed();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean isCancelBtnDisplays() {
        try {
            cancelTaskBtn.isDisplayed();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void acceptCL(String cLName) {
        selectCLByName(cLName);
        acceptTaskBtn.click();
        yesBtnConfirmationDialog.click();
        Wait.waitForAjaxCompleted();
    }

    private void selectCLByName(String cLName) {
        String clCheckboxXpath = byClaimLineXpath.replace("$1", cLName) + "/ancestor::tr[1]/td//div[contains(@class, 'x-grid-row-checker')]";
        find(By.xpath(clCheckboxXpath)).click();
    }

    public void rejectCL(String cLName) {
        selectCLByName(cLName);
        rejectTaskBtn.click();
        yesBtnConfirmationDialog.click();
        Wait.waitForAjaxCompleted();
    }

    public void acceptTaskCompletly() {
        selectAllLines();
        acceptTaskBtn.click();
        yesBtnConfirmationDialog.click();
        Wait.waitForAjaxCompleted();
    }

    private void selectAllLines() {
        selectAllLinesCheckbox.click();
    }
}
