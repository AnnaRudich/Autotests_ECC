package com.scalepoint.automation.pageobjects.modules;

import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.pages.ClaimSearchPage;
import com.scalepoint.automation.pageobjects.pages.MyPage;
import com.scalepoint.automation.pageobjects.pages.NewCustomerPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.admin.AdminPage;
import com.scalepoint.automation.pageobjects.pages.suppliers.SuppliersPage;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Link;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.pageobjects.pages.Page.at;

public class MainMenu extends Module {

    @FindBy(id = "signOutButton")
    private SelenideElement signOut;

    private Link getSuppliers(){

        return new Link($(By.id("topMenuSuppliersButton")));
    }

    private Link getAdmin(){

        return new Link($(By.id("topMenuAdminButton")));
    }

    private Link getMyPage(){

        return new Link($(By.id("myPageButton")));
    }

    private Link getNewCustomer(){

        return new Link($(By.id("newCustomerButton")));
    }

    private Link getSearch(){

        return new Link($(By.id("searchTextButton")));
    }

    private Button getCustomerDetails(){

        return new Button($(By.id("secondaryCaseButton")));
    }

    private Button getClaimInfo(){

        return new Button($(By.id("primaryCaseButton")));
    }


    public SuppliersPage toEccAdminPage() {

        $(getSuppliers()).click();
        return at(SuppliersPage.class);
    }

    public AdminPage toAdminPage() {

        $(getAdmin()).click();
        return Page.at(AdminPage.class);
    }

    public void logOut() {

        hoverAndClick(signOut);
        acceptAlert();
    }

    public MyPage myPage() {

        $(getMyPage()).click();
        return at(MyPage.class);
    }

    public NewCustomerPage newCustomer() {

        $(getNewCustomer()).click();
        return at(NewCustomerPage.class);
    }

    public ClaimSearchPage openClaimSearch() {

        $(getSearch()).click();
        return at(ClaimSearchPage.class);
    }

    public boolean isClaimInfoBlockPresent() {

        return $(getClaimInfo()).isDisplayed();
    }

    public void customerDetailsOpen() {

        $(getCustomerDetails()).click();
    }

    public void claimInfoOpen() {

        $(getClaimInfo()).click();
    }
}

