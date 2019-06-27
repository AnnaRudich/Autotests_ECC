package com.scalepoint.automation.pageobjects.pages.rnv1;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.Wait;
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
import static com.codeborne.selenide.Selenide.$$;

@RVPage
public class RnvTaskWizardPage1 extends Page {

    @FindBy(css = "input[name='name']")
    private WebElement nameField;
    @FindBy(css = "#button-next")
    private WebElement nextBtn;


    @Override
    protected Page ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        Wait.waitForVisible(nameField);
        Wait.waitForAjaxCompleted();
        return null;
    }

    @Override
    protected String getRelativeUrl() {
        return "/?orderToken";
    }

    public RnvTaskWizardPage2 nextRnVstep() {
        clickAndWaitForDisplaying(nextBtn, By.cssSelector("span[id*='second-step_header']"));
        return at(RnvTaskWizardPage2.class);
    }

    public RnvTaskWizardPage1 selectRnvType(String lineDescription, String rnvType) {
        selectValueFromDropdown(lineDescription, "Opgavetype", rnvType);
        return this;
    }

    public RnvTaskWizardPage1 selectDamageType(String lineDescription, String damageType) {
        selectValueFromDropdown(lineDescription, "Skadetype", damageType);
        return this;
    }

    private void selectValueFromDropdown(String lineDescription, String columnName, String valueToSelect) {
        By dropDownListSelector = By.xpath("//div[contains(@class, 'x-boundlist-list-ct')]/ul/li");
        SelenideElement column = new ServiceLinesRows().getRowByDescription(lineDescription).get(columnName);
        column.click();
        column.findAll(dropDownListSelector).findBy(text(valueToSelect)).click();
    }

    class ServiceLinesHeaders {

        List<String> columnNames;
        ElementsCollection columnHeaderElements;

        ServiceLinesHeaders() {
            this.columnHeaderElements = getColumnHeaderElements();
            this.columnNames = getColumnNames(columnHeaderElements);
        }

        List<String> getColumnNames(ElementsCollection tableHeadersElements) {

            List<String> columnNames = new ArrayList<>();
            for (WebElement tableHeaderElement : tableHeadersElements) {
                columnNames.add(tableHeaderElement.getText());
            }
            return columnNames;
        }


        ElementsCollection getColumnHeaderElements() {
            By columnHeadersSelector = By.xpath("//span[following-sibling::div[contains(@class, 'x-column-header-trigger')]]");
            return $$(columnHeadersSelector).excludeWith(Condition.not(visible));
        }
    }


    class ServiceLinesRows {
        List<Map<String, SelenideElement>> serviceLinesList;

        ServiceLinesRows() {
            List<String> columnHeadersTexts = new ServiceLinesHeaders().columnNames;
            this.serviceLinesList = collectLinesData(columnHeadersTexts);
        }

        private List<Map<String, SelenideElement>> collectLinesData(List<String> columnNames) {
            List<Map<String, SelenideElement>> serviceLines = new ArrayList<>();

            ElementsCollection rows = $$(By.xpath("//div[contains(@id, 'serviceLineListId-body')]//tr"));
            By oneRowCellsSelector = By.xpath("(//div[contains(@class, 'x-grid-cell-inner')][not(contains(@class, 'x-grid-cell-inner-action-col'))])[position()>1]");

            for (SelenideElement row : rows) {
                ElementsCollection rowCells =
                        row.findAll(oneRowCellsSelector);
                serviceLines.add(mapCellsToHeaders(columnNames, rowCells));
            }
            return serviceLines;
        }

        private Map<String, SelenideElement> mapCellsToHeaders(List<String> columnNames, ElementsCollection rowCells) {
            Map<String, SelenideElement> lines = new HashMap<>();
            for (int i = 0; i < columnNames.size(); i++) {
                lines.put(columnNames.get(i), rowCells.get(i));
            }
            return lines;
        }

        private Map<String, SelenideElement> getRowByDescription(String lineDescription) {

            Map<String, SelenideElement> row = new HashMap<>();

            for (int i = 0; i < serviceLinesList.size(); i++) {
                if (serviceLinesList.get(i).get("Beskrivelse").getText().equals(lineDescription)) {
                    row = serviceLinesList.get(i);
                }
            }
            return row;
        }
    }
}
