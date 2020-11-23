package com.scalepoint.automation.pageobjects.pages.rnv.dialogs;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.rnv.ProjectsPage;
import com.scalepoint.automation.utils.Wait;
import org.openqa.selenium.By;

import java.util.function.Consumer;

import static ch.lambdaj.Lambda.on;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompleted;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class EvaluateTaskDialog extends BaseDialog {
    @Override
    protected void ensureWeAreAt() {
        waitForAjaxCompleted();
        $(By.xpath("//span[contains(@id, 'evaluateTaskWindow')]")).shouldBe(Condition.visible);
    }

    public ProjectsPage closeDialog(){
        $(By.xpath("//img[contains(@class, 'x-tool-close')]")).click();
        return on(ProjectsPage.class);
    }

    public ProjectsPage acceptFeedback(){
        SelenideElement element = $$(".x-window a[role=button] span[role=img]")
                .get(0);
        hoverAndClick(element);
        hoverAndClick($(By.xpath("//span[contains(text(),'Ja')]/ancestor::a")));
        Wait.waitForAjaxCompletedAndJsRecalculation();
        return Page.at(ProjectsPage.class);
    }

    public EvaluateTaskDialog doAssert(Consumer<EvaluateTaskDialog.Asserts> assertFunc) {
        assertFunc.accept(new EvaluateTaskDialog.Asserts());
        return EvaluateTaskDialog.this;
    }

    public class Asserts {

        public EvaluateTaskDialog.Asserts assertRepairPriceForTheTaskWithIndexIs(int taskIndex, Double expectedRepairPrice){
            Double actualRepairPrice = new Double($(By.xpath("//td[@id = 'repairPrice0']/div/span[1]")).getText());
            assertThat(actualRepairPrice).as("Repair price should be: " + expectedRepairPrice + "but was: " + actualRepairPrice)
                    .isEqualTo(expectedRepairPrice);

            return this;
        }

        public EvaluateTaskDialog.Asserts assertTotalIs(Double totalExpectedValue){
            Double totalActualValue = new Double($(By.xpath("//label[contains(., 'Total:')]/../../td[2]/div")).getText());
            assertThat(totalActualValue).as("Task total should be " + totalExpectedValue + "but was: " + totalActualValue)
                    .isEqualTo(totalExpectedValue);
            return this;
        }
    }
}
