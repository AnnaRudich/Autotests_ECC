package com.scalepoint.automation.pageobjects.pages.admin;

import com.scalepoint.automation.pageobjects.pages.MyPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Link;

import java.util.List;

import static com.scalepoint.automation.utils.Wait.waitForVisible;

@EccPage
public class AdminPage extends Page {

    @FindBy(xpath = "//a[contains(@href, 'matching_engine/start.jsp')]")
    private Link matchingEngine;

    @FindBy(xpath = "//a")
    private List<Link> adminLinks;

    @FindBy(xpath = "//a[text()='Generic Items']")
    private Link genericItems;

    @FindBy(xpath = "//a[text()='Function Templates']")
    private Link functionalTemplateLink;

    @FindBy(xpath = "//a[contains(@href, 'matching_engine')]")
    private Link matchingEngineLink;

    @FindBy(xpath = "//a[text()='Users']")
    private Link usersLink;

    @FindBy(xpath = "//a[contains(@href, 'pseudocategory_model')]")
    private Link pseudoCategoryModels;

    @FindBy(xpath = "//a[contains(@href, 'pseudocategory_group')]")
    private Link pseudoCategoryGroup;

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/Admin";
    }

    @Override
    public AdminPage ensureWeAreOnPage() {
        waitForVisible(matchingEngine);
        return this;
    }

    public SettlementPage toSettlement() {
        matchingEngine.click();
        return at(SettlementPage.class);
    }

    public FunctionalTemplatesPage toFunctionalTemplatesPage() {
        functionalTemplateLink.click();
        return at(FunctionalTemplatesPage.class);
    }

    public GenericItemsAdminPage toGenericItemsPage() {
        genericItems.click();
        return at(GenericItemsAdminPage.class);
    }

    public MyPage toMatchingEngine() {
        matchingEngineLink.click();
        return at(MyPage.class);
    }

    public UsersPage toUsersPage() {
        usersLink.click();
        return at(UsersPage.class);
    }

    public PseudoCategoryModelPage toPseudoCategoryModelPage() {
        pseudoCategoryModels.click();
        return at(PseudoCategoryModelPage.class);
    }

    public PseudoCategoryGroupPage toPseudoCategoryGroupPage() {
        pseudoCategoryGroup.click();
        return at(PseudoCategoryGroupPage.class);
    }
}
