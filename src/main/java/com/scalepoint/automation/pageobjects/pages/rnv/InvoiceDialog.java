package com.scalepoint.automation.pageobjects.pages.rnv;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
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


    public InvoiceLine findInvoiceLineByIndex(int lineIndex) {
        return new InvoiceLine(lineIndex);
    }

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
                this.invoiceLine = $(By.xpath("//div[contains(@id, 'invoiceRowList')]//table/tbody/tr[" + lineIndex + "]"));
                this.description = invoiceLine.$(By.xpath("//td[1]/div")).getText();
                this.number = Integer.valueOf(invoiceLine.$(By.xpath("//td[2]/div")).getText());
                this.unit = Integer.valueOf(invoiceLine.$(By.xpath("//td[3]/div")).getText());
                this.unitPrice = Double.valueOf(invoiceLine.$(By.xpath("//td[4]/div")).getText());
                this.price = Double.valueOf(invoiceLine.$(By.xpath("//td[5]/div")).getText());
                this.VAT = Double.valueOf(invoiceLine.$(By.xpath("//td[6]/div")).getText());
                this.lineTotal = Double.valueOf(invoiceLine.$(By.xpath("//td[1]/div")).getText());
            }


            public SelenideElement getInvoiceLine() {
                return invoiceLine;
            }

            public String getDescription() {
                return description;
            }

            public int getNumber() {
                return number;
            }

            public int getUnit() {
                return unit;
            }

            public Double getUnitPrice() {
                return unitPrice;
            }

            public Double getPrice() {
                return price;
            }

            public Double getVAT() {
                return VAT;
            }

            public Double getLineTotal() {
                return lineTotal;
            }

           public InvoiceLine assertTotalForTheLineWithIndex(int lineIndex, Double expectedLineTotal) {
                    if(findInvoiceLineByIndex(lineIndex).equals(null)) {
                        logger.error("Invoice line was not found");
                    }else {
                        Double actualLineTotal = findInvoiceLineByIndex(lineIndex).getLineTotal();
                        assertThat(actualLineTotal)
                                .as("the total for line with index: " + lineIndex + " is: " + expectedLineTotal + " but was: " + actualLineTotal + "")
                                .isEqualTo(expectedLineTotal);
                    }
                    return this;
                }
        }
    }

