package com.scalepoint.automation.pageobjects.pages;

import com.scalepoint.automation.utils.annotations.EccPage;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Link;

import java.util.List;

import static com.scalepoint.automation.utils.Wait.waitForVisible;

@EccPage
public class AdminPage extends Page {

    private static final String URL = "webshop/jsp/Admin";

    public static String FUNCTION_TEMPLATES = "Function Templates";
    public static String PSEUDO_CATEGORY_GROUP = "Pseudo Category Group";
    public static String SETTLEMENT_COLUMN_MODEL_CONFIG = "Settlement column model config";

    @FindBy(id = "signOutButton")
    private Link signOut;

    @FindBy(xpath = "//a[contains(@href, 'matching_engine/start.jsp')]")
    private Link matchingEngine;

    @FindBy(xpath = "//a")
    private List<Link> adminLinks;

    @FindBy(xpath = "//a[text()='Function Templates']")
    private Link functionalTemplateLink;

    @FindBy(name = "ftform")
    private FunctionalTemplatesPage functionalTemplatesPage;

    @FindBy(xpath = "//form[contains(@name,'settingsform')]")
    private EditFunctionTemplatePage editFunctionTemplatePage;

    @Override
    protected String geRelativeUrl() {
        return URL;
    }

    @Override
    public AdminPage ensureWeAreOnPage() {
        waitForUrl(URL);
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
}
