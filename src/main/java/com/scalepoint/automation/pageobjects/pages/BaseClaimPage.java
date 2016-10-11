package com.scalepoint.automation.pageobjects.pages;

import com.scalepoint.automation.pageobjects.modules.ClaimNavigationMenu;
import com.scalepoint.automation.pageobjects.modules.MainMenu;
import com.scalepoint.automation.pageobjects.pages.admin.AdminPage;

public abstract class BaseClaimPage extends Page {

    private ClaimNavigationMenu claimNavigationMenu = new ClaimNavigationMenu();
    private MainMenu mainMenu = new MainMenu();

    public NotesPage toNotesPage() {
        return claimNavigationMenu.clickOnNotes();
    }

    public MailsPage toMailsPage() {
        return claimNavigationMenu.clickMails();
    }

    public CustomerDetailsPage toCustomerDetails() {
        return claimNavigationMenu.clickCustomerDetails();
    }

    public AdminPage toAdminPage() {
        mainMenu.toAdminPage();
        return at(AdminPage.class);
    }

    public void toSuppliersPage() {
        mainMenu.suppliers();
    }
}
