package com.scalepoint.automation.pageobjects.pages.admin;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.data.entity.InsuranceCompany;
import com.scalepoint.automation.utils.data.entity.ReductionRule;
import com.scalepoint.automation.utils.threadlocal.Browser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForPageLoaded;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

@EccPage
public class ReductionRulesPage extends AdminBasePage {

    @FindBy(id = "btnNew")
    private WebElement newButton;

    @FindBy(id = "btnEdit")
    private WebElement editButton;

    @FindBy(id = "btnDelete")
    private WebElement deleteButton;

    @FindBy(id = "btnRefresh")
    private WebElement refreshButton;

    @FindBy(xpath = "//*[@id='assignments']/tbody/tr[3]/td/input")
    private WebElement assignmentsButton;

    @FindBy(tagName = "option")
    private List<WebElement> options;

    @FindBy(xpath = "//input[contains(@name, 'searchText')]")
    private WebElement quickSearchField;

    @Override
    protected void ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl(), "webshop/jsp/Admin/ReductionRuleRefresh");
        waitForPageLoaded();
        $(newButton).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/Admin/reduction_rule.jsp";
    }

    public AddEditReductionRulePage selectNewOption() {
        newButton.click();
        return at(AddEditReductionRulePage.class);
    }

    public boolean isRuleDisplayed(ReductionRule rr) {
        try {
            return Wait.forCondition(webDriver -> !Browser.driver().findElements(By.xpath(".//*[@id='ruleItemsList']/option[text() = '" + rr.getRrName() + "']")).isEmpty(), 3);
        } catch (Exception e) {
            return false;
        }
    }

    private WebElement findRule(ReductionRule rr) {
        return find(By.xpath(".//*[@id='ruleItemsList']/option[text() = '" + rr.getRrName() + "']"));
    }

    public void selectRefreshOption() {
        clickAndWaitForDisplaying(refreshButton, By.id("btnNew"));
    }


    public void selectDeleteOptionAndAccept() {
        deleteButton.click();
        acceptAlert();
        selectRefreshOption();
    }

    public ReductionRulesPage searchRule(ReductionRule rr) {
        selectRefreshOption();
        quickSearchField.sendKeys(rr.getRrName());
        return at(ReductionRulesPage.class);
    }

    public void selectExistingRR(ReductionRule rr) {
        findRule(rr).click();
    }

    public ReductionRulesPage deleteRule(ReductionRule rr) {
        selectExistingRR(rr);
        selectDeleteOptionAndAccept();
        return this;
    }

    public ReductionRuleAssignmentsPage selectAssignmentsOption() {
        assignmentsButton.click();
        return new ReductionRuleAssignmentsPage();
    }

    public void selectNewICAndRefresh(InsuranceCompany ic) {
        for (WebElement option : options) {
            if (option.getText().contains(ic.getIcName())) {
                option.click();
                break;
            }
        }
    }

    public ReductionRulesPage assertRuleDisplayed(ReductionRule rule) {
        assertTrue(isRuleDisplayed(rule));
        return this;
    }

    public ReductionRulesPage assertRuleNotDisplayed(ReductionRule rule) {
        assertFalse(isRuleDisplayed(rule));
        return this;
    }
}
