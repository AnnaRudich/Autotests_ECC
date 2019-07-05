package com.scalepoint.automation.pageobjects.pages.rnv;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.utils.Wait;
import org.openqa.selenium.By;

import java.util.function.Consumer;

import static ch.lambdaj.Lambda.on;
import static com.codeborne.selenide.Selenide.$;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class EvaluateTaskDialog extends BaseDialog {
    @Override
    protected EvaluateTaskDialog ensureWeAreAt() {
        Wait.waitForAjaxCompleted();
        $(By.xpath("//span[contains(@id, 'evaluateTaskWindow')]")).shouldBe(Condition.visible);
        return this;
    }

    public ProjectsPage closeDialog(){
        $(By.xpath("//img[contains(@class, 'x-tool-close')]")).click();
        return on(ProjectsPage.class);
    }


    public EvaluateTaskDialog doAssert(Consumer<EvaluateTaskDialog.Asserts> assertFunc) {
        assertFunc.accept(new EvaluateTaskDialog.Asserts());
        return EvaluateTaskDialog.this;
    }

    public class Asserts {
        public EvaluateTaskDialog.Asserts assertThereIsNoInvoiceLines() {

           return this;
        }

        public EvaluateTaskDialog.Asserts assertRepairPriceForTheTaskWithIndexIs(int taskIndex, Double expectedRepairPrice){

            Double actualRepairPrice = new Double($(By.xpath("//td[@id = 'repairPrice0']/div/span[1]")).getText());

            assertThat(actualRepairPrice).isEqualTo(expectedRepairPrice)
                    .as("Repair price should be: " + expectedRepairPrice + "but was: " + actualRepairPrice);

            return this;
        }

        public EvaluateTaskDialog.Asserts assertTotalIs(Double totalExpectedValue){
           Double totalActualValue = new Double($(By.xpath("//label[contains(., 'Total:')]/../../td[2]/div")).getText());
            assertThat(totalActualValue).isEqualTo(totalExpectedValue)
                    .as("Task total should be " + totalExpectedValue + "but was: " + totalActualValue);
            return this;
        }
    }
}
