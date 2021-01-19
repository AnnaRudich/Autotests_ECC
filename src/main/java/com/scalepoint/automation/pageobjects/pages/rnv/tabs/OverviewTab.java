package com.scalepoint.automation.pageobjects.pages.rnv.tabs;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.pages.BaseClaimPage;
import com.scalepoint.automation.pageobjects.pages.rnv.dialogs.InvoiceDialog;
import com.scalepoint.automation.utils.NumberFormatUtils;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;
import static com.scalepoint.automation.utils.Wait.waitForPageLoaded;
import static org.assertj.core.api.Java6Assertions.assertThat;

public class OverviewTab extends BaseClaimPage {

    @Override
    protected void ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForAjaxCompletedAndJsRecalculation();
        waitForPageLoaded();
        $("#panel-overview-list-body").waitUntil(visible, TIME_OUT_IN_MILISECONDS);
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/matching_engine/projects.jsp";
    }

//    public InvoiceDialog openInvoiceDialogForLineWithIndex(int index){
//        return toInvoiceGrid()
//                .getGridLine(index)
//                .clickViewInvoiceActive();
//    }

    public OverviewTab doAssert(Consumer<OverviewTab.Asserts> assertFunc) {
        assertFunc.accept(new OverviewTab.Asserts());
        return OverviewTab.this;
    }

    public class Asserts {
//        public OverviewTab.Asserts assertThereIsNoInvoiceGrid() {
//            assertThat(toInvoiceGrid().gridSize())
//                    .as("There should be no invoice lines")
//                    .isEqualTo(0);
//            return this;
//        }
//
//        public OverviewTab.Asserts assertInvoiceGridSize(int expectedSize) {
//            assertThat(toInvoiceGrid().gridSize())
//                    .as(String .format("Invoice size should be %d", expectedSize))
//                    .isEqualTo(expectedSize);
//            return this;
//        }
    }

    public PanelViewGrid toPanelViewGrid(){
        return new PanelViewGrid();
    }

    public class PanelViewGrid{
        private List<PanelViewGridLine> panelViewGridLines;

        PanelViewGrid(){
            panelViewGridLines = $$("#panel-overview-list-body > div > table")
                    .stream()
                    .map(PanelViewGridLine::new)
                    .collect(Collectors.toList());
        }

        public PanelViewGridLine getPanelViewGridLine(int index){
            return panelViewGridLines.get(index);
        }

        public int gridSize(){
            return panelViewGridLines.size();
        }

        public class PanelViewGridLine{

            private SelenideElement panelViewGridLine;
            private SelenideElement expander;
            private String taskNo;
            private String serviceAgreement;
            private String store;
            private String taskStatus;
            private String unknown;
            private String selfriskByServicePartner;
            @Getter
            private BigDecimal invoicePrice;
            @Getter
            private BigDecimal totalRepairPrice;
            private SelenideElement evaluateAssignment;

            PanelViewGridLine(SelenideElement panelViewGridLine){
                this.panelViewGridLine = panelViewGridLine;
                ElementsCollection panelViewGridLineCells= panelViewGridLine.findAll("[role=gridcell]");
                expander = panelViewGridLineCells.get(0).find(".x-grid-row-expander");
                taskNo = panelViewGridLineCells.get(1).getText();
                serviceAgreement = panelViewGridLineCells.get(2).getText();
                store = panelViewGridLineCells.get(3).getText();
                taskStatus = panelViewGridLineCells.get(4).getText();
                unknown = panelViewGridLineCells.get(5).getText();
                selfriskByServicePartner = panelViewGridLineCells.get(6).getText();
                invoicePrice = new BigDecimal(panelViewGridLineCells.get(7).getText());
                totalRepairPrice = new BigDecimal(panelViewGridLineCells.get(8).getText());
                evaluateAssignment = panelViewGridLineCells.get(9).find("a[role=button]");
            }

            public PanelViewGridLine clickExpander(){

                expander.click();
                $("div#taskNestedGrid-body").waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
                return new PanelViewGridLine(panelViewGridLine);
            }

            public PanelViewGridLine doAssert(Consumer<Asserts> assertFunc) {
                assertFunc.accept(new PanelViewGridLine.Asserts());
                return PanelViewGridLine.this;
            }

            public class Asserts {
                public PanelViewGridLine.Asserts assertInvoicePrice(BigDecimal invoicePrice) {
                    assertThat(getInvoicePrice())
                            .as(String.format("Invoice price value should be %s", invoicePrice))
                            .isEqualTo(invoicePrice);
                    return this;
                }
            }
        }
    }
}
