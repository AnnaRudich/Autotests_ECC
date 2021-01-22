package com.scalepoint.automation.pageobjects.pages.rnv.dialogs;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.utils.NumberFormatUtils;
import lombok.Getter;
import org.openqa.selenium.By;

import java.math.BigDecimal;
import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompleted;
import static org.assertj.core.api.Assertions.assertThat;

public class InvoiceDialog extends BaseDialog {

    @Override
    protected void ensureWeAreAt() {
        waitForAjaxCompleted();
        $("#panel-invoice-view-body").waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
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

        public InvoiceDialog.Asserts assertTotalIs(BigDecimal totalExpectedValue) {
            BigDecimal totalActualValue = NumberFormatUtils.formatBigDecimalToHaveTwoDigits($(By.xpath("//label[contains(., 'Total:')]/../../td[2]/div")).getText());
            assertThat(totalActualValue)
                    .as("Task total should be " + totalExpectedValue + "but was: " + totalActualValue)
                    .isEqualTo(totalExpectedValue);
            return this;
        }

        public InvoiceDialog.Asserts assertRepairPriceForTheFirstTaskIs(Double expectedRepairPrice) {
            Double actualRepairPrice = new Double($(By.xpath("//td[@id = 'repairPrice0']/div/span[1]")).getText());
            assertThat(actualRepairPrice).as("Repair price should be: " + expectedRepairPrice + "but was: " + actualRepairPrice)
                    .isEqualTo(expectedRepairPrice);
            return this;
        }
    }


    public InvoiceLine findInvoiceLineByIndex(int lineIndex) {
        return new InvoiceLine(lineIndex);
    }
        @Getter
        public class InvoiceLine {
            SelenideElement invoiceLine;

            String description;
            int number;

            int unit;
            Double unitPrice;
            Double price;
            Double VAT;
            Double lineTotal;

            InvoiceLine(int lineIndex) {
                this.invoiceLine = $(By.xpath("(//div[contains(@id, 'invoiceRowList')]//table/tbody/tr)[" + lineIndex + "]"));
                this.description = invoiceLine.find(By.xpath("td[1]/div")).getText();
                this.number = Integer.valueOf(invoiceLine.find(By.xpath("td[2]/div")).getText());
                this.unit = Integer.valueOf(invoiceLine.find(By.xpath("td[3]/div")).getText());
                this.unitPrice = Double.valueOf(invoiceLine.find(By.xpath("td[4]/div")).getText());
                this.price = Double.valueOf(invoiceLine.find(By.xpath("td[5]/div")).getText());
                this.VAT = Double.valueOf(invoiceLine.find(By.xpath("td[6]/div")).getText());
                this.lineTotal = Double.valueOf(invoiceLine.find(By.xpath("td[7]/div")).getText());
            }

           public InvoiceLine assertTotalForTheLineWithIndex(int lineIndex, Double expectedLineTotal) {
                Double actualLineTotal = this.getLineTotal();
                assertThat(actualLineTotal)
                                .as("the total for line with index: " + lineIndex + " is: " + expectedLineTotal + " but was: " + actualLineTotal + "")
                                .isEqualTo(expectedLineTotal);
                    return this;
            }
        }
    }

