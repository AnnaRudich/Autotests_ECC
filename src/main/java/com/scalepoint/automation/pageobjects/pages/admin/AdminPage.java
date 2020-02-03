package com.scalepoint.automation.pageobjects.pages.admin;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.pageobjects.pages.MyPage;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.data.entity.Category;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Link;

import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForPageLoaded;

@EccPage
public class AdminPage extends AdminBasePage {

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

    @FindBy(xpath = "//a[contains(@href,'edit_reasons')]")
    private Link editReasons;

    @Override
    public AdminPage ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForPageLoaded();
        $(matchingEngine).waitUntil(Condition.visible, STANDARD_WAIT_UNTIL_TIMEOUT);
        return this;
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/Admin";
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

    public EditReasonsPage toEditReasonsPage() {
        editReasons.click();
        return at(EditReasonsPage.class);
    }

    @SuppressWarnings("AccessStaticViaInstance")
    public AdminPage createPsModelWithCategoryAndEnable(Category category, String pseudoCategoryModel) {
        return toPseudoCategoryGroupPage()
                .toAddGroupPage()
                .addGroup(category.getGroupName())
                .to(PseudoCategoriesPage.class)
                .toAddCategoryPage()
                .addCategory(category)
                .to(PseudoCategoryModelPage.class)
                .toEditPage(pseudoCategoryModel)
                .selectCategory(category.getGroupName())
                .save()
                .to(AdminPage.class);
    }
}
