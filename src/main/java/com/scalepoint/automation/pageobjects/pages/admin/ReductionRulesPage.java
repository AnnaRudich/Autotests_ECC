package com.scalepoint.automation.pageobjects.pages.admin;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.data.entity.input.InsuranceCompany;
import com.scalepoint.automation.utils.data.entity.input.ReductionRule;
import com.scalepoint.automation.utils.threadlocal.Browser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

@EccPage
public class ReductionRulesPage extends AdminBasePage {

    @FindBy(id = "btnNew")
    private SelenideElement newButton;
    @FindBy(id = "btnEdit")
    private SelenideElement editButton;
    @FindBy(id = "btnDelete")
    private SelenideElement deleteButton;
    @FindBy(id = "btnRefresh")
    private SelenideElement refreshButton;
    @FindBy(xpath = "//*[@id='assignments']/tbody/tr[3]/td/input")
    private SelenideElement assignmentsButton;
    @FindBy(xpath = "//input[contains(@name, 'searchText')]")
    private SelenideElement quickSearchField;
    @FindBy(tagName = "option")
    private ElementsCollection options;

    @Override
    protected void ensureWeAreOnPage() {

        waitForUrl(getRelativeUrl(), "webshop/jsp/Admin/ReductionRuleRefresh");
        waitForAjaxCompletedAndJsRecalculation();
        newButton.should(Condition.visible);
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

        return $(By.xpath(".//*[@id='ruleItemsList']/option[text() = '" + rr.getRrName() + "']"));
    }

    public ReductionRulesPage selectRefreshOption() {

        hoverAndClick(refreshButton);
        return at(ReductionRulesPage.class);
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

    public ReductionRulesPage selectExistingRR(ReductionRule rr) {

        findRule(rr).click();
        return this;
    }

    public ReductionRulesPage deleteRule(ReductionRule rr) {

        selectExistingRR(rr);
        selectDeleteOptionAndAccept();
        return this;
    }

    public ReductionRuleAssignmentsPage selectAssignmentsOption() {

        assignmentsButton.click();
        return Page.at(ReductionRuleAssignmentsPage.class);
    }

    public void selectNewICAndRefresh(InsuranceCompany ic) {

        for (SelenideElement option : options) {

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
