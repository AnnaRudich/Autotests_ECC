package com.scalepoint.automation.pageobjects.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.modules.ClaimMenu;
import com.scalepoint.automation.pageobjects.modules.MainMenu;
import com.scalepoint.automation.shared.ClaimStatus;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Link;
import ru.yandex.qatools.htmlelements.element.Table;

import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

@EccPage
public class MyPage extends Page {

    private MainMenu mainMenu = new MainMenu();
    private ClaimMenu claimMenu = new ClaimMenu();

    @FindBy(id = "RecentState0")
    private SelenideElement latestCustomerStatus;

    private Button getEditPreferences(){

        return new Button($(By.id("EditPreferences")));
    }

    private Button getCreateNewCase(){

        return new Button($(By.id("newCustomerButton-btnEl")));
    }

    private Table getLastClaims(){

        return new Table($(By.id("cases-table")));
    }

    private Link getActiveCustomerLink(){

        return new Link($(By.id("ActiveCustomer")));
    }

    private Link getRecentCustomer(){

        return new Link($(By.id("RecentCustomer0")));
    }

    @Override
    protected String getRelativeUrl() {


        return "webshop/jsp/matching_engine/my_page.jsp";
    }

    @Override
    protected void ensureWeAreOnPage() {

        waitForUrl(getRelativeUrl());
        waitForAjaxCompletedAndJsRecalculation();
        $(getEditPreferences()).should(Condition.visible);
        $(getLastClaims()).should(Condition.visible);
    }

    public CustomerDetailsPage openRecentClaim() {

        $("#RecentCustomer0").click();
        return at(CustomerDetailsPage.class);
    }

    public SettlementPage openActiveRecentClaim() {

        getActiveCustomerLink().click();
        return at(SettlementPage.class);
    }

    public NewCustomerPage clickCreateNewCase() {

        getCreateNewCase().click();
        return Page.at(NewCustomerPage.class);
    }

    public EditPreferencesPage openEditPreferences() {

        getEditPreferences().click();
        return Page.at(EditPreferencesPage.class);
    }

    public MainMenu getMainMenu() {

        return mainMenu;
    }

    public ClaimMenu getClaimMenu() {

        return claimMenu;
    }

    public MyPage doAssert(Consumer<Asserts> assertsFunc) {

        assertsFunc.accept(new Asserts());
        return MyPage.this;
    }

    public class Asserts {

        public Asserts assertClaimCompleted() {

            Assert.assertTrue(latestCustomerStatus.getText().contains(ClaimStatus.COMPLETED.getText()), "Claim must be completed");
            return this;
        }

        public Asserts assertRecentClaimCancelled() {

            Assert.assertTrue(latestCustomerStatus.getText().contains(ClaimStatus.CANCELLED.getText()), "Claim must be cancelled");
            return this;
        }

        public Asserts assertClaimHasStatus(String status) {

            Assert.assertTrue(latestCustomerStatus.getText().contains(status), errorMessage("Claim should have [%s] status", status));
            return this;
        }

        public Asserts assertAdminLinkDisplayed() {

            Assert.assertTrue(getClaimMenu().isAdminLinkDisplayed());
            return this;
        }
    }
}
