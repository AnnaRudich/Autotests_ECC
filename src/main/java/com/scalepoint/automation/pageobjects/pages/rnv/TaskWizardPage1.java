package com.scalepoint.automation.pageobjects.pages.rnv;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.annotations.page.RVPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;
import static com.scalepoint.automation.utils.Wait.waitForPageLoaded;

@RVPage
public class TaskWizardPage1 extends Page {

    @FindBy(css = "input[name='name']")
    private WebElement nameField;
    @FindBy(css = "#button-next")
    private WebElement nextBtn;


    @Override
    protected Page ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForAjaxCompletedAndJsRecalculation();
        waitForPageLoaded();
        $(nameField).waitUntil(visible, TIME_OUT_IN_MILISECONDS);
        return null;
    }

    @Override
    protected String getRelativeUrl() {
        return "/?orderToken";
    }

    public TaskWizardPage2 nextRnVstep() {
        clickAndWaitForDisplaying(nextBtn, By.cssSelector("span[id*='second-step_header']"));
        return at(TaskWizardPage2.class);
    }

    public TaskWizardPage1 selectRnvType(String lineDescription, String rnvType) {
        selectValueFromDropdown(lineDescription, "Opgavetype", rnvType);
        return this;
    }

    public TaskWizardPage1 selectDamageType(String lineDescription, String damageType) {
        selectValueFromDropdown(lineDescription, "Skadetype", damageType);
        return this;
    }

    private void selectValueFromDropdown(String lineDescription, String columnName, String valueToSelect) {
        By dropDownListSelector = By.xpath("//div[contains(@class, 'x-boundlist-list-ct')]/ul/li");
        SelenideElement column = new ServiceLines().getRowByDescription(lineDescription).get(columnName);
        column.click();
        column.findAll(dropDownListSelector).findBy(text(valueToSelect)).click();
    }

    class ServiceLines {
        List<Map<String, SelenideElement>> serviceLines;

        ServiceLines() {
            this.serviceLines = collectLinesData();
        }

        private List<Map<String, SelenideElement>> collectLinesData() {
            List<Map<String, SelenideElement>> serviceLines = new ArrayList<>();

            ElementsCollection rows = $$(By.xpath("//div[contains(@id, 'serviceLineListId-body')]//tr"));
            By cellsSelector = By.xpath("(//div[contains(@class, 'x-grid-cell-inner')][not(contains(@class, 'x-grid-cell-inner-action-col'))])[position()>1]");

            for (SelenideElement row : rows) {
                ElementsCollection rowCells =
                        row.findAll(cellsSelector);
                serviceLines.add(mapCellsToHeaders(getColumnNames(), rowCells));
            }
            return serviceLines;
        }

        List<String> getColumnNames() {
            By columnHeadersSelector = By.xpath("//span[following-sibling::div[contains(@class, 'x-column-header-trigger')]]");
            ElementsCollection tableHeadersElements = $$(columnHeadersSelector).excludeWith(Condition.not(visible));

            List<String> columnNames = new ArrayList<>();
            for (WebElement tableHeaderElement : tableHeadersElements) {
                columnNames.add(tableHeaderElement.getText());
            }
            return columnNames;
        }

        private Map<String, SelenideElement> mapCellsToHeaders(List<String> columnNames, ElementsCollection rowCells) {
            Map<String, SelenideElement> lines = new HashMap<>();
            for (int i = 0; i < columnNames.size(); i++) {
                lines.put(columnNames.get(i), rowCells.get(i));
            }
            return lines;
        }

        Map<String, SelenideElement> getRowByDescription(String lineDescription) {

            Map<String, SelenideElement> row = new HashMap<>();

            for (Map<String, SelenideElement> serviceLine : serviceLines) {
                if (serviceLine.get("Beskrivelse").getText().equals(lineDescription)) {
                    row = serviceLine;
                }
            }
            return row;
        }
    }
}

