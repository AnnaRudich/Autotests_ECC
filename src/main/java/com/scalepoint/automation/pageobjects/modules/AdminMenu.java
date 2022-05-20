package com.scalepoint.automation.pageobjects.modules;

import com.scalepoint.automation.pageobjects.pages.MyPage;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.admin.FunctionalTemplatesPage;
import com.scalepoint.automation.pageobjects.pages.admin.GenericItemsAdminPage;
import com.scalepoint.automation.pageobjects.pages.admin.UsersPage;
import org.openqa.selenium.By;
import ru.yandex.qatools.htmlelements.element.Link;

import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.scalepoint.automation.pageobjects.pages.Page.at;

public class AdminMenu extends Module {


    private Link getMatchingEngine(){

        return new Link($(By.xpath("//a[contains(@href, 'matching_engine/start.jsp')]")));
    }

    private List<Link> getAdminLinks(){

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

    public SettlementPage toSettlement() {

        getMatchingEngine().click();
        return at(SettlementPage.class);
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

        getMatchingEngineLink().click();
        return at(MyPage.class);
    }

    public UsersPage toUsers() {

        getUsersLink().click();
        return at(UsersPage.class);
    }
}
