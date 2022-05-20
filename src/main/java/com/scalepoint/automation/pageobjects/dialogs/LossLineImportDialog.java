package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import org.openqa.selenium.By;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

public class LossLineImportDialog extends BaseDialog {

    @Override
    protected void ensureWeAreAt() {

        waitForAjaxCompletedAndJsRecalculation();
        $("#importList_main").shouldBe(Condition.visible);
    }

    public ExcelImportSummaryDialog confirmImportAfterErrorsWereFixed() {

        SelenideElement okButton = $("#loss-line-import-button-btnInnerEl");
        okButton.shouldBe(Condition.enabled).click();
        return BaseDialog.at(ExcelImportSummaryDialog.class);
    }

    public SettlementPage cancelImport() {

        $("#loss-line-close-button-btnInnerEl").click();
        return Page.at(SettlementPage.class);
    }

    public LossLineImportDialog updateAll(){

        $("input[onclick='ConvertAllToMatchImportLine(true)']").click();
        return this;
    }

    private void pickItemFromSelect(SelenideElement select, String valueToSelect) {

        select.selectOption(valueToSelect);
    }

    public LossLineImportDialog selectCategoryAndSubcategoryForTheErrorLine(String category, String subCategory, String lineDescription) {

        SelenideElement groupSelect = new ImportErrorLines().getErrorLineByDescription(lineDescription).getCategoryGroupDropDown();
        SelenideElement categorySelect = new ImportErrorLines().getErrorLineByDescription(lineDescription).getCategoryDropDown();

        pickItemFromSelect(groupSelect, category);
        pickItemFromSelect(categorySelect, subCategory);
        return this;
    }

    public LossLineImportDialog selectValuationForTheErrorLine(ValuationType valuationType, String lineDescription) {

        SelenideElement valuationSelect = new ImportErrorLines().getErrorLineByDescription(lineDescription).getValuationDropDown();
        pickItemFromSelect(valuationSelect, valuationType.name());
        return this;
    }

    class ImportErrorLines {

        List<ErrorLineWrapper> errorLines;

        ImportErrorLines() {
            errorLines = collectErrorLines();
        }

        private Map<String, String> mapHeadersToLineData(List<String> columnNames, List<String> errorLineCells) {

            Map<String, String> errorLines = new HashMap<>();

            for (int i = 0; i < columnNames.size(); i++) {

                errorLines.put(columnNames.get(i), errorLineCells.get(i));
            }
            return errorLines;
        }

        List<String> getColumnHeaders() {

            List<String> columnNames = new ArrayList<>();

            ElementsCollection tableHeadersElements = $$(By.xpath("//tr[@class='importHeaderFixed']/th[0 < position() and position() < 14]"));
            for (SelenideElement tableHeaderElement : tableHeadersElements) {

                columnNames.add(tableHeaderElement.getText());
            }
            return columnNames;
        }

        private List<String> getErrorLineCellData(SelenideElement row) {

            List<String> errorLineCellsData = new ArrayList<>();

            ElementsCollection cells = row.findAll(By.xpath("td"));
            for (SelenideElement cell : cells) {

                errorLineCellsData.add(cell.getText());
            }

            return errorLineCellsData;
        }


        List<ErrorLineWrapper> collectErrorLines() {

            By categoryGroupSelector = By.xpath("following-sibling::tr[1]//select[contains(@id, 'groupSelect')]");
            By categorySelector = By.xpath("following-sibling::tr[2]//select[contains(@id, 'categorySelect')]");
            By valuationSelector = By.xpath("following-sibling::tr[1]//select[contains(@id, 'selectValuation')]");
            List<ErrorLineWrapper> errorLines = new ArrayList<>();

            ElementsCollection errorLineDataRows = $$(By.xpath("//table[@class='importList_table']//tr[contains(@class, 'errorLine')]"));

            for (SelenideElement errorLineDataRow : errorLineDataRows) {

                Map<String, String> errorLine = mapHeadersToLineData(getColumnHeaders(), getErrorLineCellData(errorLineDataRow));

                SelenideElement categoryGroupDropDown = getSelect(errorLineDataRow, categoryGroupSelector);
                SelenideElement categoryDropDown = getSelect(errorLineDataRow, categorySelector);
                SelenideElement valuationDropDown = getSelect(errorLineDataRow, valuationSelector);

                errorLines.add(new ErrorLineWrapper(errorLine, categoryGroupDropDown, categoryDropDown, valuationDropDown));
            }
            return errorLines;
        }

        private SelenideElement getSelect(SelenideElement parentOfSelect, By selectSelector) {

            parentOfSelect.shouldBe(Condition.visible);
            return parentOfSelect.find(selectSelector);
        }

        ErrorLineWrapper getErrorLineByDescription(String lineDescription) {

            ErrorLineWrapper line = null;
            for (ErrorLineWrapper errorLine : errorLines) {

                if (errorLine.getErrorLine().get("Beskrivelse").equals(lineDescription)) {

                    line = errorLine;
                }
            }

            return line;
        }
    }

    class ErrorLineWrapper {

        private Map<String, String> errorLine;
        private SelenideElement categoryGroupDropDown;
        private SelenideElement categoryDropDown;
        private SelenideElement valuationDropDown;

        ErrorLineWrapper(Map<String, String> errorLine, SelenideElement categoryGroupDropDown, SelenideElement categoryDropDown, SelenideElement valuationDropDown) {

            this.errorLine = errorLine;
            this.categoryGroupDropDown = categoryGroupDropDown;
            this.categoryDropDown = categoryDropDown;
            this.valuationDropDown = valuationDropDown;
        }

        Map<String, String> getErrorLine() {
            return errorLine;
        }

        SelenideElement getCategoryGroupDropDown() {
            return categoryGroupDropDown;
        }

        SelenideElement getCategoryDropDown() {
            return categoryDropDown;
        }

        SelenideElement getValuationDropDown() {
            return valuationDropDown;
        }
    }

    public enum ValuationType {

        NEW_PRICE,
        VOUCHER;
    }
}

