package com.scalepoint.automation.pageobjects.modules;

import com.scalepoint.automation.pageobjects.pages.*;
import com.scalepoint.automation.pageobjects.pages.rnv.ProjectsPage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Link;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.pageobjects.pages.Page.at;

public class ClaimNavigationMenu extends Module {

    @FindBy(id = "settlementButton")
    private Link settelment;

    @FindBy(id = "mailsButton")
    private WebElement mails;

    @FindBy(id = "importButton")
    private Button imports;

    @FindBy(id = "repairValuationButton")
    private Link repairValuation;

    @FindBy(id = "settlementSummaryButton")
    private Link settlementSummary;

    @FindBy(id = "orderButton")
    private Link order;

    @FindBy(id = "detailsButton")
    private Link details;

    @FindBy(id = "notesButton")
    private Link notes;

    @FindBy(id = "historyButton")
    private Link history;

    public SettlementPage toSettlementPage() {
        clickUsingJsIfSeleniumClickReturnError(settelment);
        return at(SettlementPage.class);
    }

    public MailsPage toMailsPage() {
        $(mails).click();
        return at(MailsPage.class);
    }

    public MailsPage toEmptyMailsPage() {
        $(mails).click();
        return new MailsPage();
    }

    public CustomerDetailsPage toCustomerDetailsPage() {
        clickUsingJsIfSeleniumClickReturnError(details);
        return at(CustomerDetailsPage.class);
    }

    public void ClickImports() {
        clickUsingJsIfSeleniumClickReturnError(imports);
    }

    public ProjectsPage toRepairValuationProjectsPage() {
        clickUsingJsIfSeleniumClickReturnError(repairValuation);
        return at(ProjectsPage.class);
    }

    public void ClickSettlementSummary() {
        clickUsingJsIfSeleniumClickReturnError(settlementSummary);
    }

    public OrderDetailsPage toOrderDetailsPage() {
        clickUsingJsIfSeleniumClickReturnError(order);
        return at(OrderDetailsPage.class);
    }

    public void ClickDetails() {
        clickUsingJsIfSeleniumClickReturnError(details);
    }

    public NotesPage toNotesPage() {
        clickUsingJsIfSeleniumClickReturnError(notes);
        return at(NotesPage.class);
    }

    public void ClickHistory() {
        clickUsingJsIfSeleniumClickReturnError(history);
    }

}
