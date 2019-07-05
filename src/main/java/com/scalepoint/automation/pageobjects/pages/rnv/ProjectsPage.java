package com.scalepoint.automation.pageobjects.pages.rnv;

import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.rnv.tabs.CommunicationTab;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.data.entity.ServiceAgreement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import static com.codeborne.selenide.Selenide.$;
import static org.assertj.core.api.Assertions.assertThat;

@EccPage
public class ProjectsPage extends Page {
    @FindBy(xpath = "//span[contains(text(),'Kommunikation')]")
    private WebElement communicationTab;

    @FindBy(xpath = "//span[contains(text(),'Factura')]")
    private WebElement invoiceTab;

    @FindBy(xpath = "//span[contains(text(),'Status')]")
    private WebElement statusTab;

    @FindBy(xpath = "//span[contains(text(),'Oversigt')]")
    private WebElement overviewTab;

    //Overwiev tab
    private String byTaskStatusAgrXpath = "//div[contains(@id,'project_view_id')]//tr[1]//tr[1]/td/div[contains(text(), '$1')]/ancestor::tr[1]/td[5]/div";
    private String byClaimLineXpath = "//span[contains(text(),'$1')]";

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

    @FindBy(css = ".x-panel-header span span")
    private WebElement auditInfoPanelHeader;

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

    public CommunicationTab toCommunicationTab() {
        clickAndWaitForDisplaying(communicationTab, By.cssSelector("td#combo-communication-task-inputCell"));
        return at(CommunicationTab.class);
    }

    public void navigateToInvoiceTab() {
       $(By.xpath("//div[contains(@id, 'tabbar')]//span[contains(text(),'Faktura')]")).click();
    }

    public EvaluateTaskDialog openEvaluateTaskDialog(){
        $(By.xpath("//span[contains(text(), 'Evaluer opgave')]/following-sibling::span")).click();
        return BaseDialog.at(EvaluateTaskDialog.class);
    }

    public ProjectsPage expandTopTaskDetails() {
        clickAndWaitForDisplaying(firstTaskExpander, By.cssSelector("div#taskNestedGrid-body"));
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

    private void selectCLByName(String cLName) {
        String clCheckboxXpath = byClaimLineXpath.replace("$1", cLName) + "/ancestor::tr[1]/td//div[contains(@class, 'x-grid-row-checker')]";
        find(By.xpath(clCheckboxXpath)).click();
    }

    private String getAuditInfoPanelText() {
        return auditInfoPanelHeader.getText();
    }

    public Assertion getAssertion() {
        return new Assertion();
    }

    public class Assertion {

        public Assertion assertAuditResponseText(AuditResultEvaluationStatus auditResponse) {
            String actualAuditResponse = getAuditInfoPanelText();
            assertThat(actualAuditResponse)
                    .as("Expected Audit response is:" + auditResponse.getResponse() + "but actual was:" + actualAuditResponse)
                    .isEqualTo(auditResponse.getResponse());
            return this;
        }

        public Assertion assertTaskHasFeedbackReceivedStatus(ServiceAgreement agreement) {
            String taskStatus = getTaskStatus(agreement.getTestAgreementForRnV());
            Assert.assertEquals(taskStatus, agreement.getFeedbackReceivedStatusName(), "Task has " + taskStatus + " status. Must be feedback received");
            return this;
        }
    }

    public enum AuditResultEvaluationStatus {
        APPROVE("Godkendt"),
        REJECT("Afvist"),
        MANUAL("Manuelt"),
        ERROR("Intet svar fra Validering!");

        String response;

        AuditResultEvaluationStatus(String response) {
            this.response = response;
        }

        String getResponse() {
            return response;
        }
    }
}


