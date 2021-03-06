package com.scalepoint.automation.grid;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ex.ElementNotFound;
import com.scalepoint.automation.Actions;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.utils.OperationalUtils;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.testng.Assert;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.scalepoint.automation.utils.OperationalUtils.assertEqualsDoubleWithTolerance;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;
import static com.scalepoint.automation.utils.Wait.waitForJavascriptRecalculation;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class ValuationGrid implements Actions {

    protected Logger logger = LogManager.getLogger(ValuationGrid.class);

    public static final String TR_CONTAINS_CLASS = ".//tr[contains(@class, '";
    public static final String CLASS = "class";

    public enum ValuationGridColumn {

        CHECK_COLUMN("active-valuation-checkcolumn"),
        TYPE("description"),
        CASH_COMPENSATION("cashCompensation"),
        DEPRECIATION_COLUMN("depreciation"),
        TOTAL_AMOUNT_OF_VALUATION("totalPrice"),
        EDIT_VALUATION("editValuation"),
        NULL(null);

        @Getter
        private String dataColumnId;

        ValuationGridColumn(String dataColumnId) {

            this.dataColumnId = dataColumnId;
        }

        public static ValuationGridColumn getColumn(String dataColumnId) {

            return Arrays.stream(ValuationGridColumn.values())
                    .filter(valuationGridColumn -> valuationGridColumn.getDataColumnId().equals(dataColumnId))
                    .findFirst()
                    .orElse(NULL);
        }
    }

    public enum Valuation {

        NOT_SELECTED("valuation-type-NOT_SELECTED"),
        CUSTOMER_DEMAND("valuation-type-CUSTOMER_DEMAND"),
        VOUCHER("valuation-type-VOUCHER"),
        NEW_PRICE("valuation-type-NEW_PRICE"),
        MARKET_PRICE("valuation-type-MARKET_PRICE"),
        DISCRETIONARY("valuation-type-DISCRETIONARY_VALUATION"),
        CATALOG_PRICE("valuation-type-CATALOG_PRICE"),
        USED_PRICE("valuation-type-USED_PRICE"),
        PURCHASE_PRICE("valuation-type-PURCHASE_PRICE");

        @Getter
        private String className;

        Valuation(String className) {
            this.className = className;
        }

        public static Valuation findByClassName(String className){

            return Arrays.stream(Valuation.values())
                    .filter(valuation -> className.contains(valuation.getClassName()))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException(className));
        }
    }

    public class ValuationRow {

        private Valuation valuation;
        private Double cashCompensation;
        private Integer depreciationPercentage;
        private Double totalPrice;
        private String description;

        public ValuationRow(SelenideElement element){

            valuation = Valuation.findByClassName(element.attr("class"));

            for (SelenideElement column : element.findAll("td")) {

                String attribute = column.attr("data-columnid");

                switch (ValuationGrid.ValuationGridColumn.getColumn(attribute)) {

                    case CASH_COMPENSATION:

                        cashCompensation = StringUtils.isBlank(column.getText()) ? null : OperationalUtils.toNumber(column.getText());
                        break;
                    case DEPRECIATION_COLUMN:

                        depreciationPercentage = StringUtils.isBlank(column.getText()) ? null : Integer.valueOf(column.getText());
                        break;
                    case TOTAL_AMOUNT_OF_VALUATION:

                        totalPrice = StringUtils.isBlank(column.getText()) ? null : OperationalUtils.toNumber(column.getText());
                        break;
                    case TYPE:

                        description = column.getText();
                        break;
                    default:

                        logger.warn("Valuation not supported: " + ValuationGrid.ValuationGridColumn.getColumn(attribute));
                        break;
                }
            }
        }

        Boolean isValuationChecked() {
            return $(By.xpath("//tr[contains(@class, '" + valuation.className + "')]//div[@role='button']"))
                    .getAttribute(CLASS).contains("x-grid-checkcolumn-checked");
        }

        public ValuationRow makeActive() {
            while(!isValuationChecked()){
                $(By.xpath("//tr[contains(@class, '" + valuation.className + "')]//div[@role='button']")).click();
            }
            waitForJavascriptRecalculation();
            return this;
        }

        private void confirmAlert(){
            $("div[role='alertdialog']").find(By.xpath("//span[contains(text(),'Ja')]")).click();
        }

        private ValuationRow(Valuation valuation) {
            this.valuation = valuation;
        }

        public String getDescription() {
            return this.description;
        }

        public Double getCashCompensation() {
            return cashCompensation;
        }

        public Double getTotalPrice() {
            return totalPrice;
        }

        public ValuationGrid backToGrid() {

            return ValuationGrid.this;
        }

        public ValuationGrid doAssert(Consumer<ValuationRow.Asserts> func) {
            func.accept(new ValuationRow.Asserts());
            return ValuationGrid.this;

        }

        public class Asserts {

            public Asserts assertCashCompensationIs(Double amount) {
                OperationalUtils.assertEqualsDouble(cashCompensation, amount);
                return this;
            }

            public Asserts assertTotalAmountIs(Double amount) {
                OperationalUtils.assertEqualsDouble(totalPrice, amount);
                return this;
            }

            public Asserts assertDepreciationPercentageIs(Integer expectedDepreciationPercentage) {
                assertEquals(depreciationPercentage, expectedDepreciationPercentage);
                return this;
            }

            public Asserts assertValuationIsSelected(){
                assertTrue(isValuationChecked());
                return this;
            }
        }
    }

    public List<ValuationRow> getValuationRows(){

        waitForAjaxCompletedAndJsRecalculation();

        List<ValuationRow> valuationRows = $$("#valuations-grid table [role=row]")
                .stream()
                .map(ValuationRow::new)
                .collect(Collectors.toList());

        return valuationRows;
    }

    public ValuationRow getValuationRow(Valuation valuation){

        List<ValuationRow> valuationRows = null;

        LocalDateTime start = LocalDateTime.now();

        do{

            try {

                valuationRows = getValuationRows();
            } catch (ElementNotFound e) {

                logger.info("getValuationRows stale element check");
            }
        }while(LocalDateTime.now().isBefore(start.plusSeconds(5)));

            return valuationRows.stream()
                    .filter(valuationRow -> valuationRow.valuation.equals(valuation))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException(valuation.getClassName()));
    }

    private boolean isValuationDisabled(ValuationGrid.Valuation valuation) {
        SelenideElement unselectable = $(By.xpath(TR_CONTAINS_CLASS + valuation.className + "')]/td[2]/div[contains(@style, 'silver')]"))
                .shouldHave(Condition.attribute("unselectable"));
        if (unselectable == null)
            return false;
        return true;
    }

    public SettlementDialog toSettlementDialog(){

        return BaseDialog.at(SettlementDialog.class);
    }

    public ValuationGrid doAssert(Consumer<Asserts> func) {
        func.accept(new Asserts());
        return ValuationGrid.this;

    }

    public class Asserts {

        private Asserts checkInvisibilityOfValuationRow(String message, ValuationGrid.Valuation valuation) {
            try {
                if (getValuationRow(valuation).getDescription() != null) {
                    Assert.fail(message);
                }
            } catch (Exception e) {
                Assert.fail(message);
            }
            return this;
        }


        public Asserts assertCashCompensationIsDepreciated(int percentage, ValuationGrid.Valuation valuation) {
            ValuationGrid.ValuationRow valuationRow = getValuationRow(valuation);
            assertEqualsDoubleWithTolerance(valuationRow.getCashCompensation(), valuationRow.getTotalPrice() * (1 - ((double) percentage / 100)));
            return this;
        }

        public Asserts assertCashCompensationIsNotDepreciated(ValuationGrid.Valuation valuation, Double expectedNotDepreciatedValue) {
            ValuationGrid.ValuationRow valuationRow = getValuationRow(valuation);
            assertEqualsDoubleWithTolerance(valuationRow.getCashCompensation(), expectedNotDepreciatedValue);
            return this;
        }

        public Asserts assertIsLowestPriceValuationSelected(ValuationGrid.Valuation... valuations) {
            List<ValuationGrid.ValuationRow> valuationRows = new ArrayList<>();
            Arrays.stream(valuations).forEach(v -> valuationRows.add(getValuationRow(v)));
            assertTrue(valuationRows.stream().min(Comparator.comparing(ValuationGrid.ValuationRow::getCashCompensation))
                    .map(ValuationGrid.ValuationRow::isValuationChecked).orElse(false));
            return this;
        }

        public Asserts assertPriceIsSameInTwoColumns(ValuationGrid.Valuation valuation) {
            ValuationGrid.ValuationRow valuationRow = getValuationRow(valuation);
            assertEquals(valuationRow.cashCompensation, valuationRow.totalPrice);
            return this;
        }

        public Asserts assertTotalPriceIsSameInRows(ValuationGrid.Valuation... valuations) {
            List<ValuationGrid.ValuationRow> valuationRows = new ArrayList<>();
            Arrays.stream(valuations).forEach(valuation -> valuationRows.add(getValuationRow(valuation)));
            assertTrue(valuationRows.stream()
                            .map(price -> price.getTotalPrice()).collect(Collectors.toList()).stream()
                            .distinct().count() <= 1,
                    "Total prices are not equal");
            return this;
        }

        public Asserts assertValuationIsDisabled(ValuationGrid.Valuation valuation) {
            assertTrue(isValuationDisabled(valuation));
            return this;
        }


        public Asserts assertMarketPriceVisible() {
            String failMessage = "Market price must be visible";
            return checkVisibilityOfValuationRow(failMessage, Valuation.MARKET_PRICE);
        }

        public Asserts assertCatalogPriceVisible() {
            String failMessage = "Catalog price must be visible";
            return checkVisibilityOfValuationRow(failMessage, Valuation.CATALOG_PRICE);
        }

        public Asserts assertCatalogPriceInvisible() {
            String failMessage = "Catalog price must be invisible";
            return checkInvisibilityOfValuationRow(failMessage, Valuation.CATALOG_PRICE);
        }

        private Asserts checkVisibilityOfValuationRow(String message, Valuation valuation) {
            try {
                if (getValuationRow(valuation).getDescription() == null) {
                    Assert.fail(message);
                }
            } catch (Exception e) {
                Assert.fail(message);
            }
            return this;
        }
    }
}
