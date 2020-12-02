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
    private WebElement settelment;

    @FindBy(id = "mailsButton")
    private WebElement mails;

    @FindBy(id = "importButton")
    private Button imports;

    @FindBy(id = "repairValuationButton")
    private WebElement repairValuation;

    @FindBy(id = "settlementSummaryButton")
    private Link settlementSummary;

    @FindBy(id = "orderButton")
    private WebElement order;

    @FindBy(id = "detailsButton")
    private WebElement details;

    @FindBy(id = "notesButton")
    private WebElement notes;

    @FindBy(id = "historyButton")
    private Link history;

    public SettlementPage toSettlementPage() {
        hoverAndClick($(settelment));
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
        hoverAndClick($(details));
        return at(CustomerDetailsPage.class);
    }

    public ProjectsPage toRepairValuationProjectsPage() {
        hoverAndClick($(repairValuation));
        return at(ProjectsPage.class);
    }

    public OrderDetailsPage toOrderDetailsPage() {
        hoverAndClick($(order));
        return at(OrderDetailsPage.class);
    }

    public NotesPage toNotesPage() {
        hoverAndClick($(notes));
        return at(NotesPage.class);
    }

}
