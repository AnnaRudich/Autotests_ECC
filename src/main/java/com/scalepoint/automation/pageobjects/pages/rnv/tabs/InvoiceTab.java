package com.scalepoint.automation.pageobjects.pages.rnv.tabs;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.pages.BaseClaimPage;
import com.scalepoint.automation.pageobjects.pages.rnv.dialogs.InvoiceDialog;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;
import static org.assertj.core.api.Java6Assertions.assertThat;

public class InvoiceTab extends BaseClaimPage {

    @Override
    protected void ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForAjaxCompletedAndJsRecalculation();
        $("#grid-invoice-body").waitUntil(visible, TIME_OUT_IN_MILISECONDS);
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/matching_engine/projects.jsp";
    }

    public InvoiceDialog openInvoiceDialogForLineWithIndex(int index){
        return toInvoiceGrid()
                .getGridLine(index)
                .clickViewInvoiceActive();
    }

    public InvoiceTab doAssert(Consumer<InvoiceTab.Asserts> assertFunc) {
        assertFunc.accept(new InvoiceTab.Asserts());
        return InvoiceTab.this;
    }

    public class Asserts {
        public InvoiceTab.Asserts assertThereIsNoInvoiceGrid() {
            assertThat(toInvoiceGrid().gridSize())
                    .as("There should be no invoice lines")
                    .isEqualTo(0);
            return this;
        }

        public InvoiceTab.Asserts assertInvoiceGridSize(int expectedSize) {
            assertThat(toInvoiceGrid().gridSize())
                    .as(String .format("Invoice size should be %d", expectedSize))
                    .isEqualTo(expectedSize);
            return this;
        }
    }

    public InvoiceGrid toInvoiceGrid(){
        return new InvoiceGrid();
    }

    public class InvoiceGrid{
        private SelenideElement invoiceGrid;
        private List<InvoiceGridLine> invoiceGridLines;

        InvoiceGrid(){
            invoiceGrid = $("#panel-main-body");
            invoiceGridLines = invoiceGrid
                    .findAll("#grid-invoice-body [role=row]")
                    .stream()
                    .map(InvoiceGridLine::new)
                    .collect(Collectors.toList());
        }

        public InvoiceGridLine getGridLine(int index){
            return invoiceGridLines.get(index);
        }

        public int gridSize(){
            return invoiceGridLines.size();
        }

        public class InvoiceGridLine{

            private String taskNo;
            private String servicePartner;
            private String invoiceDate;
            private String paymentDate;
            private String subtotal;
            private String vat;
            @Getter
            private BigDecimal total;
            private String status;
            private String invoiceNumber;
            private String approvalDate;
            private SelenideElement viewInvoiceActive;

            InvoiceGridLine(SelenideElement invoiceGridLine){
                ElementsCollection invoiceGridLineCells= invoiceGridLine.findAll("td");
                taskNo = invoiceGridLineCells.get(0).getText();
                servicePartner = invoiceGridLineCells.get(1).getText();
                invoiceDate = invoiceGridLineCells.get(2).getText();
                paymentDate = invoiceGridLineCells.get(3).getText();
                subtotal = invoiceGridLineCells.get(4).getText();
                vat =invoiceGridLineCells.get(5).getText();
                total = new BigDecimal(invoiceGridLineCells.get(6).getText());
                status = invoiceGridLineCells.get(7).getText();
                invoiceNumber = invoiceGridLineCells.get(8).getText();
                approvalDate = invoiceGridLineCells.get(9).getText();
                viewInvoiceActive = invoiceGridLineCells.get(10).find("img[src$='view_invoice_active.png']");
            }

            public InvoiceDialog clickViewInvoiceActive(){

                viewInvoiceActive.click();
                return BaseDialog.at(InvoiceDialog.class);
            }

            public InvoiceGridLine doAssert(Consumer<Asserts> assertFunc) {
                assertFunc.accept(new InvoiceGridLine.Asserts());
                return InvoiceGridLine.this;
            }

            public class Asserts {
                public InvoiceGridLine.Asserts assertTotal(BigDecimal total) {
                    assertThat(getTotal())
                            .as(String.format("Total value should be %s", total))
                            .isEqualTo(total);
                    return this;
                }
            }
        }
    }
}
