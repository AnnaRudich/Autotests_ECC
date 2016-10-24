package com.scalepoint.automation.pageobjects.pages;

import com.scalepoint.automation.domain.ClaimStatus;
import com.scalepoint.automation.pageobjects.modules.ClaimMenu;
import com.scalepoint.automation.pageobjects.modules.MainMenu;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Link;
import ru.yandex.qatools.htmlelements.element.Table;

import static com.scalepoint.automation.utils.Wait.waitForVisible;

@EccPage
public class MyPage extends Page {

    private MainMenu mainMenu = new MainMenu();

    private ClaimMenu claimMenu = new ClaimMenu();

    @FindBy(id = "EditPreferences")
    private Button editPreferences;

    @FindBy(id = "CreateNewClaim")
    private Button createNewCase;

    @FindBy(id = "cases-table")
    private Table lastClaims;

    @FindBy(id = "ActiveCustomer")
    private Link activeCustomerLink;

    @FindBy(id = "RecentCustomer0")
    private Link recentCustomer;

    @FindBy(id = "RecentState0")
    private WebElement latestCustomerStatus;

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/matching_engine/my_page.jsp";
    }

    @Override
    public MyPage ensureWeAreOnPage() {
        long start = System.currentTimeMillis();
        waitForUrl(getRelativeUrl());
        waitForVisible(editPreferences);
        waitForVisible(lastClaims);
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        return this;
    }

    public void selectActiveClient() {
        activeCustomerLink.click();
    }

    public CustomerDetailsPage openRecentClaim() {
        recentCustomer.click();
        return at(CustomerDetailsPage.class);
    }

    public boolean isRecentClaimCompleted() {
        return latestCustomerStatus.getText().contains(ClaimStatus.completed());
    }

    public boolean isRecentClaimSaved() {
        return latestCustomerStatus.getText().contains(ClaimStatus.saved());
    }

    public boolean isRecentClaimCancelled() {
        return latestCustomerStatus.getText().contains(ClaimStatus.cancelled());
    }

    public NewCustomerPage clickCreateNewCase() {
        createNewCase.click();
        return Page.at(NewCustomerPage.class);
    }

    public MainMenu getMainMenu() {
        return mainMenu;
    }

    public ClaimMenu getClaimMenu() {
        return claimMenu;
    }

    /*------------------------------ ASSERTS ---------------------------------------*/
    /*------------------------------ ------- ---------------------------------------*/
    public MyPage assertClaimHasStatus(String status) {
        Assert.assertTrue(latestCustomerStatus.getText().contains(status), errorMessage("Claim should have [%s] status", status));
        return this;
    }
}
