package com.scalepoint.automation.pageobjects.modules;

import com.scalepoint.automation.pageobjects.pages.admin.AdminPage;
import com.scalepoint.automation.pageobjects.pages.NewCustomerPage;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Link;

import static com.scalepoint.automation.pageobjects.pages.Page.at;

public class MainMenu extends Module {

    @FindBy(id = "primaryCaseButton")
    private Button claimInfo;

    @FindBy(id = "topMenuSuppliersButton")
    private Link suppliers;

    @FindBy(id = "topMenuAdminButton")
    private Link admin;

    @FindBy(id = "signOutButton")
    private Link signOut;

    @FindBy(id = "myPageButton")
    private Link myPage;

    @FindBy(id = "newCustomerButton")
    private Link newCustomer;

    @FindBy(id = "searchTextButton")
    private Link search;

    @FindBy(id = "secondaryCaseButton")
    private Button customerDetails;

    public void suppliers() {
        suppliers.click();
    }

    public AdminPage toAdminPage() {
        admin.click();
        return at(AdminPage.class);
    }

    public void signOut() {
        signOut.click();
    }

    public void myPage() {
        myPage.click();
    }

    public NewCustomerPage newCustomer() {
        newCustomer.click();
        return at(NewCustomerPage.class);
    }

    public void search() {
        search.click();
    }

    public boolean isClaimInfoBlockPresent() {
        return claimInfo.isDisplayed();
    }

    public void customerDetailsOpen() {
        customerDetails.click();
    }

    public void claimInfoOpen() {
        claimInfo.click();
    }
}

