package com.scalepoint.automation.pageobjects.pages;

import com.scalepoint.automation.pageobjects.modules.ClaimNavigationMenu;
import com.scalepoint.automation.pageobjects.modules.MainMenu;
import com.scalepoint.automation.pageobjects.pages.admin.AdminPage;
import com.scalepoint.automation.pageobjects.pages.rnv.ProjectsPage;
import com.scalepoint.automation.stubs.MailserviceMock;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.threadlocal.CurrentUser;

public abstract class BaseClaimPage extends Page {

    private ClaimNavigationMenu claimNavigationMenu = new ClaimNavigationMenu();

    private MainMenu mainMenu = new MainMenu();

    public NotesPage toNotesPage() {
        return claimNavigationMenu.toNotesPage();
    }

    public MailsPage toMailsPage(MailserviceMock.MailserviceStub mailserviceStub) {

        mailserviceStub.findSentEmails(CurrentUser.getClaimId());

        Wait.waitForAjaxCompletedAndJsRecalculation();
        return claimNavigationMenu.toMailsPage();
    }

    public MailsPage toEmptyMailsPage() {
        return claimNavigationMenu.toEmptyMailsPage();
    }

    public OrderDetailsPage toOrdersDetailsPage() {
        return claimNavigationMenu.toOrderDetailsPage();
    }

    public CustomerDetailsPage toCustomerDetails() {
        return claimNavigationMenu.toCustomerDetailsPage();
    }

    public AdminPage toAdminPage() {
        mainMenu.toAdminPage();
        return at(AdminPage.class);
    }

    public ClaimSearchPage toClaimSearchPage() {
        mainMenu.openClaimSearch();
        return at(ClaimSearchPage.class);
    }

    public ProjectsPage toRepairValuationProjectsPage() {
        return claimNavigationMenu.toRepairValuationProjectsPage();
    }

    public void toSuppliersPage() {
        mainMenu.toEccAdminPage();
    }

    public SettlementPage toSettlementPageUsingNavigationMenu() {
        claimNavigationMenu.toSettlementPage();
        return at(SettlementPage.class);
    }

    public SettlementPage toSettlementPage() {
        return claimNavigationMenu.toSettlementPage();
    }
}
