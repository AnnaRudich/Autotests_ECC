package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class LossLineImportDialog extends BaseDialog {

    private SelenideElement lossLineImportDialog =
            $(By.xpath("//div[@data-componentid='loss-line-import-window']"));

    private SelenideElement lossImportList = $("#importList_main");

    private SelenideElement okButton =
            $("#loss-line-import-button-btnInnerEl");

    private SelenideElement cancelButton =
            $("#loss-line-close-button-btnInnerEl");

    public void pickCategory() {
    }

    public void pickVoucher() {
    }

    public void selectUpdateLineOption() {
    }

    public void selectNoChangesToLineOption() {
    }


    @Override
    protected BaseDialog ensureWeAreAt() {
        lossImportList.shouldBe(Condition.visible);
        return this;
    }

    class LossImportLines {
        List<ErrorLineWrapper> errorLines;

        LossImportLines() {
            errorLines = collectErrorLines();
        }

       private Map<String, String> mapHeadersToLineData(List<String> columnNames, ElementsCollection errorLineCells){
            Map<String, String> errorLines = new HashMap<>();
            for (int i = 0; i< columnNames.size(); i++){
                errorLines.put(columnNames.get(i), errorLineCells.get(i).getText());
            }
            return errorLines;
        }


        private List<ErrorLineWrapper> collectErrorLines() {
            List<ErrorLineWrapper> errorLines = new ArrayList<>();

            ElementsCollection errorLineDataRows = $$(By.xpath("//table[@class='importList_table']//tr[contains(@class, 'errorLine')]"));

            ElementsCollection errorTextLine = $$(By.xpath("//table[@class='importList_table']//tr[contains(@class, 'errorTextLine')]"));

            for(int i =0; i< errorLineDataRows.size();i++){
                ElementsCollection errorLineDataCells = errorLineDataRows.get(i).findAll(By.xpath("//td"));
                Map<String, String> errorLine = mapHeadersToLineData(getColumnHeaders(), errorLineDataCells);

                errorLines.add(new ErrorLineWrapper(errorLine, errorTextLine.get(i)));
            }
            return errorLines;
        }

        private List<String> getColumnHeaders() {
            ElementsCollection tableHeadersElements = $$("//tr[@class='importHeaderFixed']/th[0 < position() and position() < 13]");

            List<String> columnNames = new ArrayList<>();
            for (SelenideElement tableHeaderElement : tableHeadersElements) {
                columnNames.add(tableHeaderElement.getText());
            }
            return columnNames;
        }
    }

    class ErrorLineWrapper {

        private Map<String, String> errorLine;
        private SelenideElement errorTextLine;

        ErrorLineWrapper(Map<String, String> errorLine, SelenideElement errorTextLine) {
            this.errorLine = errorLine;
            this.errorTextLine = errorTextLine;
        }

        public Map<String, String> getErrorLine() {
            return errorLine;
        }

        public SelenideElement getErrorTextLine() {
            return errorTextLine;
        }
    }
}

        /*
                        // create our map
                Map<String, Wrapper> peopleByForename = new HashMap<>();

                // populate it
                Wrapper people = new Wrapper();
                peopleByForename.put("Bob", new Wrapper(new Person("Bob Smith"),
                                                        new Person("Bob Jones"));

                // read from it
                Wrapper bobs = peopleByForename.get("Bob");
                Person bob1 = bobs.getPerson1;
                Person bob2 = bobs.getPerson2;
    }
}
