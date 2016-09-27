package com.scalepoint.automation.pageobjects.pages;

import com.scalepoint.automation.pageobjects.modules.MainMenu;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.data.entity.Claim;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Link;
import ru.yandex.qatools.htmlelements.element.Table;

import static com.scalepoint.automation.utils.Wait.waitForVisible;

@EccPage
public class MyPage extends Page {

    private MainMenu mainMenu = new MainMenu();

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
        waitForUrl(getRelativeUrl());
        waitForVisible(editPreferences);
        waitForVisible(lastClaims);
        return this;
    }

    public void selectActiveClient() {
        activeCustomerLink.click();
    }

    public CustomerDetailsPage openRecentClaim() {
        recentCustomer.click();
        return at(CustomerDetailsPage.class);
    }

    public boolean isRecentClaimCompleted(Claim claim) {
        return latestCustomerStatus.getText().contains(claim.getStatusCompleted());
    }

    public boolean isRecentClaimSaved(Claim claim) {
        return latestCustomerStatus.getText().contains(claim.getStatusSaved());
    }

    public NewCustomerPage clickCreateNewCase() {
        createNewCase.click();
        return Page.at(NewCustomerPage.class);
    }

    public MainMenu getMainMenu() {
        return mainMenu;
    }
}
