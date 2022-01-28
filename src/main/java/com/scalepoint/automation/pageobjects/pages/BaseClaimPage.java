package com.scalepoint.automation.pageobjects.pages;

import com.scalepoint.automation.pageobjects.modules.ClaimNavigationMenu;
import com.scalepoint.automation.pageobjects.modules.MainMenu;
import com.scalepoint.automation.pageobjects.pages.admin.AdminPage;
import com.scalepoint.automation.pageobjects.pages.rnv.ProjectsPage;
import com.scalepoint.automation.pageobjects.pages.suppliers.BaseSupplierAdminNavigation;
import com.scalepoint.automation.utils.Wait;

public abstract class BaseClaimPage extends BaseSupplierAdminNavigation {

    private ClaimNavigationMenu claimNavigationMenu = new ClaimNavigationMenu();

    private MainMenu mainMenu = new MainMenu();

    public NotesPage toNotesPage() {
        return claimNavigationMenu.toNotesPage();
    }

    public MailsPage toMailsPage() {

        Wait.waitForAjaxCompletedAndJsRecalculation();
        return claimNavigationMenu.toMailsPage();
    }

    public MailsPage toEmptyMailsPage() {

        Wait.waitForAjaxCompletedAndJsRecalculation();
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

    public SettlementPage toSettlementPageUsingNavigationMenu() {
        claimNavigationMenu.toSettlementPage();
        return at(SettlementPage.class);
    }

    public SettlementPage toSettlementPage() {
        return claimNavigationMenu.toSettlementPage();
    }
}
