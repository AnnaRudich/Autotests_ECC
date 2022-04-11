package com.scalepoint.automation.pageobjects.pages.admin;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.data.entity.input.Assignment;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Select;

import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by bza on 6/20/2017.
 */
public class ReductionRuleAssignmentsPage extends AdminBasePage {

    @FindBy(xpath = "//input[contains(@onclick,'resetCurrentValue()')]")
    private SelenideElement cancelButton;
    @FindBy(css = "input[type='button'][value='Save']")
    private SelenideElement saveButton;
    @FindBy(xpath = "//th[contains(text(),'Insurance Company')]/following::tr[1]")
    private SelenideElement firstAssigment;

    private Select getCompany(){

        return new Select($(By.name("company")));
    }

    private Select getPseudoCategory(){

        return new Select($(By.name("categorygroup")));
    }

    private Select getCategory(){

        return new Select($(By.name("category")));
    }

    private Select getPolicy(){

        return new Select($(By.name("policy")));
    }

    @Override
    protected void ensureWeAreOnPage() {

        waitForUrl(getRelativeUrl());
        waitForAjaxCompletedAndJsRecalculation();
        saveButton.should(Condition.visible);
    }

    @Override
    protected String getRelativeUrl() {

        return "webshop/jsp/Admin/reduction_rule_assignment_edit.jsp";
    }

    public ReductionRuleAssignmentsPage fillAssignment(Assignment assignment) {

        getCompany().selectByVisibleText(assignment.getCompany());
        getPseudoCategory().selectByVisibleText(assignment.getPseudoCategory());
        getCategory().selectByVisibleText(assignment.getCategory());
        getPolicy().selectByVisibleText(assignment.getPolicy());
        return this;
    }

    public ReductionRuleAssignmentsPage save() {

        saveButton.click();
        Wait.waitMillis(2000);
        acceptAlert();
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
