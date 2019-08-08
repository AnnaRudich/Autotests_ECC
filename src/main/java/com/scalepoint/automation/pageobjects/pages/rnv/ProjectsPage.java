package com.scalepoint.automation.pageobjects.pages.rnv;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.rnv.tabs.CommunicationTab;
import com.scalepoint.automation.pageobjects.pages.rnv.tabs.InvoiceTab;
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

    private String byTaskStatusAgrXpath = "//div[contains(@id,'project_view_id')]//tr[1]//tr[1]/td/div[contains(text(), '$1')]/ancestor::tr[1]/td[5]/div";

    @FindBy(css = "table[class*='x-grid-with-row-lines'] tr:first-of-type tr div.x-grid-row-expander")
    private WebElement firstTaskExpander;

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
        $(By.xpath("//span[contains(text(),'Kommunikation')]")).click();
        acceptAlert();
        $(By.cssSelector("td#combo-communication-task-inputCell")).shouldBe(Condition.visible);
        return at(CommunicationTab.class);
    }

    public InvoiceTab toInvoiceTab(){
        $(By.xpath("//span[contains(text(),'Faktura') and contains(@class, 'x-tab-inner')]")).click();
        return at(InvoiceTab.class);
    }

    public EvaluateTaskDialog openEvaluateTaskDialog(){
        $(By.xpath("//span[contains(text(), 'Evaluer opgave')]/following-sibling::span")).click();
        return BaseDialog.at(EvaluateTaskDialog.class);
    }

    public ProjectsPage expandTopTaskDetails() {
        clickAndWaitForDisplaying(firstTaskExpander, By.cssSelector("div#taskNestedGrid-body"));
        return this;
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

