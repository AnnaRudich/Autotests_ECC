package com.scalepoint.automation.pageobjects.modules;

import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.pages.*;
import com.scalepoint.automation.pageobjects.pages.rnv.ProjectsPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Link;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.pageobjects.pages.Page.at;

public class ClaimNavigationMenu extends Module {

    @FindBy(id = "settlementButton")
    private SelenideElement settelment;

    @FindBy(id = "mailsButton")
    private SelenideElement mails;

    @FindBy(id = "repairValuationButton")
    private SelenideElement repairValuation;

    @FindBy(id = "orderButton")
    private SelenideElement order;

    @FindBy(id = "detailsButton")
    private SelenideElement details;

    @FindBy(id = "notesButton")
    private SelenideElement notes;

    private Button getImports(){

        return new Button($(By.id("importButton")));
    }

    private Link getSettlementSummary(){

        return new Link($(By.id("settlementSummaryButton")));
    }

    private Link getHistory(){

        return new Link($(By.id("historyButton")));
    }

    public SettlementPage toSettlementPage() {

        hoverAndClick(settelment);
        return at(SettlementPage.class);
    }

    public MailsPage toMailsPage() {

        mails.click();
        return at(MailsPage.class);
    }

    public MailsPage toEmptyMailsPage() {

        mails.click();
        return new MailsPage();
    }

    public CustomerDetailsPage toCustomerDetailsPage() {

        hoverAndClick(details);
        return at(CustomerDetailsPage.class);
    }

    public ProjectsPage toRepairValuationProjectsPage() {

        hoverAndClick(repairValuation);
        return at(ProjectsPage.class);
    }

    public OrderDetailsPage toOrderDetailsPage() {

        hoverAndClick(order);
        return at(OrderDetailsPage.class);
    }

    public NotesPage toNotesPage() {

        hoverAndClick(notes);
        return at(NotesPage.class);
    }

}
