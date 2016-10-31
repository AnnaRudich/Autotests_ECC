package com.scalepoint.automation.pageobjects.modules;

import com.scalepoint.automation.pageobjects.pages.CustomerDetailsPage;
import com.scalepoint.automation.pageobjects.pages.MailsPage;
import com.scalepoint.automation.pageobjects.pages.NotesPage;
import com.scalepoint.automation.pageobjects.pages.OrderDetailsPage;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Link;

import static com.scalepoint.automation.pageobjects.pages.Page.at;

public class ClaimNavigationMenu extends Module {

    @FindBy(id = "settlementButton")
    private Link settelment;

    @FindBy(id = "mailsButton")
    private Link mails;

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

    public void ClickSettlement() {
        settelment.click();
    }

    public MailsPage clickMails() {
        mails.click();
        return at(MailsPage.class);
    }

    public CustomerDetailsPage clickCustomerDetails() {
        details.click();
        return at(CustomerDetailsPage.class);
    }

    public void ClickImports() {
        imports.click();
    }

    public void ClickRepairValuation() {
        repairValuation.click();
    }

    public void ClickSettlementSummary() {
        settlementSummary.click();
    }

    public OrderDetailsPage clickOrder() {
        order.click();
        return at(OrderDetailsPage.class);
    }

    public void ClickDetails() {
        details.click();
    }

    public NotesPage clickOnNotes() {
        notes.click();
        return at(NotesPage.class);
    }

    public void ClickHistory() {
        history.click();
    }

}
