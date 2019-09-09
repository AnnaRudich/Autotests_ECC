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

public class LossLineImportDialog extends BaseDialog {

    @Override
    protected LossLineImportDialog ensureWeAreAt() {
        $("#importList_main").shouldBe(Condition.visible);
        return this;
    }

    public SettlementPage confirmImport(){
        SelenideElement okButton = $("#loss-line-import-button-btnInnerEl");
        okButton.click();
        return Page.at(SettlementPage.class);
    }

    public SettlementPage cancelImport(){
        SelenideElement cancelButton =
                $("#loss-line-close-button-btnInnerEl");
        cancelButton.click();
        return Page.at(SettlementPage.class);
    }



    private void pickItemFromSelect(SelenideElement select, String valueToSelect) {
        select.selectOptionByValue(valueToSelect);
    }

    public LossLineImportDialog selectCategoryAndSubcategoryForTheErrorLine(String lineDescription, String category, String subCategory){
        SelenideElement groupSelect = new ImportErrorLines().getErrorLineByDescription(lineDescription).getCategoryGroupDropDown();
        SelenideElement categorySelect = new ImportErrorLines().getErrorLineByDescription(lineDescription).getCategoryDropDown();

        pickItemFromSelect(groupSelect, category);
        pickItemFromSelect(categorySelect, subCategory);
        return this;
    }

    public LossLineImportDialog selectValuationForTheErrorLine(String lineDescription, ValuationType valuationType){
        SelenideElement valuationSelect = new ImportErrorLines().getErrorLineByDescription(lineDescription).getValuationDropDown();
        pickItemFromSelect(valuationSelect, valuationType.name());
        return this;
    }

    public LossLineImportDialog selectFirst(String cutGr, String cut){
        SelenideElement errorLineDataRow = $$(By.xpath("//table[@class='importList_table']//tr[contains(@class, 'errorLine')]")).get(0);
        SelenideElement categoryGroup = errorLineDataRow.find(By.xpath("//following-sibling::tr[1]//select[contains(@id, 'groupSelect')]"));
        categoryGroup.selectOption(cutGr);

        SelenideElement category = errorLineDataRow.find(By.xpath("//following-sibling::tr[2]//select[contains(@id, 'categorySelect')]"));
        category.selectOption(cut);
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

        public List<String> getColumnHeaders() {
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

            return errorLineCellsData;      }



        public List<ErrorLineWrapper> collectErrorLines() {
            By categoryGroupSelector = By.xpath("//following-sibling::tr[1]//select[contains(@id, 'groupSelect')]");
            By categorySelector = By.xpath("//following-sibling::tr[2]//select[contains(@id, 'categorySelect')]");
            By valuationSelector = By.xpath("//following-sibling::tr[1]//select[contains(@id, 'selectValuation')]");
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

        private SelenideElement getSelect(SelenideElement parentOfSelect, By selectSelector){

            return parentOfSelect.find(selectSelector);
        }

        public ErrorLineWrapper getErrorLineByDescription(String lineDescription){
            ErrorLineWrapper line = null;
            for(ErrorLineWrapper errorLine: errorLines){
                if(errorLine.getErrorLine().get("Beskrivelse").equals(lineDescription)){
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

        public Map<String, String> getErrorLine() {
            return errorLine;
        }

        public SelenideElement getCategoryGroupDropDown() {
            return categoryGroupDropDown;
        }

        public SelenideElement getCategoryDropDown() {
            return categoryDropDown;
        }

        public SelenideElement getValuationDropDown() {
            return valuationDropDown;
        }
    }
    public enum ValuationType{
        NEW_PRICE,
        VOUCHER;
    }
}

