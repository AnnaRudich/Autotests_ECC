package com.scalepoint.automation.pageobjects.pages.admin;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.pageobjects.pages.MyPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.data.entity.input.Category;
import org.openqa.selenium.By;
import ru.yandex.qatools.htmlelements.element.Link;

import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

@EccPage
public class AdminPage extends AdminBasePage {

    private Link getMatchingEngine(){

        return new Link($(By.xpath("//a[contains(@href, 'matching_engine/start.jsp')]")));
    }

    private  List<Link> getAdminLinks(){

        return $$(By.xpath("//a")).stream()
                .map(Link::new)
                .collect(Collectors.toList());
    }

    private Link getGenericItems(){

        return new Link($(By.xpath("//a[text()='Generic Items']")));
    }

    private Link getFunctionalTemplateLink(){

        return new Link($(By.xpath("//a[text()='Function Templates']")));
    }

    private Link getMatchingEngineLink(){

        return new Link($(By.xpath("//a[contains(@href, 'matching_engine')]")));
    }

    private Link getUsersLink(){

        return new Link($(By.xpath("//a[text()='Users']")));
    }

    private Link getPseudoCategoryModels(){

        return new Link($(By.xpath("//a[contains(@href, 'pseudocategory_model')]")));
    }

    private Link getPseudoCategoryGroup(){

        return new Link($(By.xpath("//a[contains(@href, 'pseudocategory_group')]")));
    }

    private Link getEditReasons(){

        return new Link($(By.xpath("//a[contains(@href,'edit_reasons')]")));
    }

    @Override
    protected void ensureWeAreOnPage() {

        waitForUrl(getRelativeUrl());
        waitForAjaxCompletedAndJsRecalculation();
        $(getMatchingEngine()).should(Condition.visible);
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/Admin";
    }

    public SettlementPage toSettlement() {

        getMatchingEngine().click();
        return Page.at(SettlementPage.class);
    }

    public FunctionalTemplatesPage toFunctionalTemplatesPage() {
        getFunctionalTemplateLink().click();
        return at(FunctionalTemplatesPage.class);
    }

    public GenericItemsAdminPage toGenericItemsPage() {
        getGenericItems().click();
        return at(GenericItemsAdminPage.class);
    }

    public MyPage toMatchingEngine() {
        getMatchingEngine().click();
        return Page.at(MyPage.class);
    }

    public UsersPage toUsersPage() {
        getUsersLink().click();
        return at(UsersPage.class);
    }

    public PseudoCategoryModelPage toPseudoCategoryModelPage() {
        getPseudoCategoryModels().click();
        return at(PseudoCategoryModelPage.class);
    }

    public PseudoCategoryGroupPage toPseudoCategoryGroupPage() {
        getPseudoCategoryGroup().click();
        return at(PseudoCategoryGroupPage.class);
    }

    public EditReasonsPage toEditReasonsPage() {
        getEditReasons().click();
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
