package com.scalepoint.automation.pageobjects.pages.admin;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.utils.data.entity.Assignment;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Select;

import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForPageLoaded;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by bza on 6/20/2017.
 */
public class ReductionRuleAssignmentsPage extends AdminBasePage {

    @FindBy(xpath = "//input[contains(@onclick,'resetCurrentValue()')]")
    private WebElement cancelButton;

    @FindBy(xpath = "//input[contains(@onclick,'validateInput()')]")
    private WebElement saveButton;

    @FindBy(name = "company")
    private Select company;

    @FindBy(name = "categorygroup")
    private Select pseudoCategory;

    @FindBy(name = "category")
    private Select category;

    @FindBy(name = "policy")
    private Select policy;

    @FindBy(xpath = "//th[contains(text(),'Insurance Company')]/following::tr[1]")
    private WebElement firstAssigment;

    @Override
    protected ReductionRuleAssignmentsPage ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForPageLoaded();
        $(saveButton).waitUntil(Condition.visible, STANDARD_WAIT_UNTIL_TIMEOUT);
        return this;
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/Admin/reduction_rule_assignment_edit.jsp";
    }

    public ReductionRuleAssignmentsPage fillAssignment(Assignment assignment) {
        company.selectByVisibleText(assignment.getCompany());
        pseudoCategory.selectByVisibleText(assignment.getPseudoCategory());
        policy.selectByVisibleText(assignment.getPolicy());
        return this;
    }

    public ReductionRuleAssignmentsPage save() {
        saveButton.click();
        return this;
    }

    public Assignment parseLine(WebElement assignmentLine) {
        Assignment assignment = new Assignment();
        assignment.setCompany(assignmentLine.findElement(By.xpath("./td[1]")).getText());
        assignment.setPseudoCategory(assignmentLine.findElement(By.xpath("./td[2]")).getText());
        assignment.setPolicy(assignmentLine.findElement(By.xpath("./td[3]")).getText());
        return assignment;
    }

    public ReductionRuleAssignmentsPage doAssert(Consumer<ReductionRuleAssignmentsPage.Asserts> assertFunc) {
        assertFunc.accept(new ReductionRuleAssignmentsPage.Asserts());
        return this;
    }

    public class Asserts {
        public ReductionRuleAssignmentsPage.Asserts assertIsFirstLineAssignmentAdded(Assignment assignment) {
            Assignment parsedAssigment = parseLine(firstAssigment);
            assertThat(parsedAssigment).isEqualToComparingOnlyGivenFields(assignment, "company", "policy");
            assertThat(parsedAssigment.getPseudoCategory()).contains(assignment.getPseudoCategory());
            return this;
        }
    }
}
