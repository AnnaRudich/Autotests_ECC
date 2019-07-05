package com.scalepoint.automation.pageobjects.pages.rnv.tabs;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.pages.rnv.InvoiceDialog;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.Selenide.$$;

public class InvoiceTab {

    public InvoiceDialog openInvoiceDialogForLineWithIndex(int index){
        new InvoiceLines().getRowByIndex(index).get("Handlinger").find(By.xpath("//img[contains(@data-qtip, 'Vis faktura')])")).click();
        return  BaseDialog.at(InvoiceDialog.class);
    }


    class InvoiceLines{
        List<Map<String, SelenideElement>> invoiceLines;

        InvoiceLines(){
            this.invoiceLines = collectLinesData();
        }

        private List<Map<String, SelenideElement>> collectLinesData(){
            List<Map<String, SelenideElement>> invoiceLines = new ArrayList<>();

            ElementsCollection rows = $$(By.xpath("//div[@id ='grid-invoice-body']//tr[@role='row']"));

            By cellsSelector = By.xpath("//td[@role = 'gridcell']");

            for(SelenideElement row: rows){
                ElementsCollection rowCells = row.findAll(cellsSelector);
                invoiceLines.add(mapCellsToHeaders(getColumnNames(), rowCells));
            }
            return invoiceLines;
        }
        //lastColumnShouldAlwaysHaveEmptyText
        List<String> getColumnNames(){
            By columnHeadersSelector = By.xpath("(//div[1][contains(@class, 'x-grid-header')]/div[contains(@id, 'headercontainer')])[2]//span[@class='x-column-header-text']");
            ElementsCollection tableHeadersElements = $$(columnHeadersSelector);
            List<String> columnNames = new ArrayList<>();
            for(WebElement tableHeaderElement: tableHeadersElements){
                columnNames.add(tableHeaderElement.getText());
            } return columnNames;
        }

        private Map<String, SelenideElement> mapCellsToHeaders(List<String> columnNames, ElementsCollection rowCells){
            Map<String, SelenideElement> lines = new HashMap<>();
            for(int i = 0; i < columnNames.size(); i++){
                lines.put(columnNames.get(i), rowCells.get(i));
            }return lines;
        }

        Map<String, SelenideElement> getRowByIndex(int index){
            return invoiceLines.get(index);
        }
    }
}
