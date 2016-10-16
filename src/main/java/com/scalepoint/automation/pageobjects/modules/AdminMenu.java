package com.scalepoint.automation.pageobjects.modules;

import com.scalepoint.automation.pageobjects.pages.MyPage;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.admin.FunctionalTemplatesPage;
import com.scalepoint.automation.pageobjects.pages.admin.GenericItemsAdminPage;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Link;

import java.util.List;

import static com.scalepoint.automation.pageobjects.pages.Page.at;

public class AdminMenu extends Module {

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

    public Link getMatchingEngine() {
        return matchingEngine;
    }
}
