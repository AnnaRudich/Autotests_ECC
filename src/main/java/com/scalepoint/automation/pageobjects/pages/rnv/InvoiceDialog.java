package com.scalepoint.automation.pageobjects.pages.rnv;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.utils.Wait;
import org.assertj.core.api.AssertionsForClassTypes;
import org.openqa.selenium.By;

import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static org.assertj.core.api.Java6Assertions.assertThat;

public class InvoiceDialog extends BaseDialog {

    @Override
    public InvoiceDialog ensureWeAreAt() {
        Wait.waitForAjaxCompleted();
        $(By.xpath("//div[contains(text(), 'Faktura')]")).shouldBe(Condition.visible);
        return this;
    }

    public InvoiceDialog doAssert(Consumer<InvoiceDialog.Asserts> assertFunc) {
        assertFunc.accept(new InvoiceDialog.Asserts());
        return InvoiceDialog.this;
    }

    public class Asserts {
        public InvoiceDialog.Asserts assertThereIsInvoiceLinesList() {
            assertThat($(By.xpath("//div[contains(@id, 'invoiceRowList') and contains(@class, 'x-grid-with-row-lines')]")).is(Condition.visible))
                    .as("InvoiceLinesList should be visible on " + this.getClass().getName()).isTrue();
            return this;
        }

        public InvoiceDialog.Asserts assertTotalIs(Double totalExpectedValue) {
            Double totalActualValue = new Double($(By.xpath("//label[contains(., 'Total:')]/../../td[2]/div")).getText());
            AssertionsForClassTypes.assertThat(totalActualValue).as("Task total should be " + totalExpectedValue + "but was: " + totalActualValue)
                    .isEqualTo(totalExpectedValue);
            return this;
        }

        public InvoiceDialog.Asserts assertRepairPriceForTheFirstTaskIs(Double expectedRepairPrice) {
            Double actualRepairPrice = new Double($(By.xpath("//td[@id = 'repairPrice0']/div/span[1]")).getText());
            AssertionsForClassTypes.assertThat(actualRepairPrice).as("Repair price should be: " + expectedRepairPrice + "but was: " + actualRepairPrice)
                    .isEqualTo(expectedRepairPrice);
            return this;
        }
    }
}
