package com.scalepoint.automation.pageobjects.pages.admin;

import com.scalepoint.automation.pageobjects.extjs.ExtInput;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Select;
import ru.yandex.qatools.htmlelements.element.Table;

import java.util.List;

import static com.scalepoint.automation.utils.Wait.waitForVisible;

/**
 * Created by asa on 11/14/2016.
 */
@EccPage
public class EditReasonsPage extends Page {
    @FindBy(id = "reason_type_dropdown")
    private Select reasonTypes;
    @FindBy(id = "company_list_dropdown")
    private Select companies;
    @FindBy(xpath = "//button[contains(text(), 'Refresh')]")
    private Button refresh;
    @FindBy(id = "editNotLowestChoiceReasonsForm")
    private WebElement editReasonsForm;
    @FindBy(xpath = "//table[@id='reasons_table']//tbody//td//input")
    private List<WebElement> reasonsFields;
    @FindBy(id = "addLine")
    private Button addReason;
    @FindBy(xpath = "//input[contains(@id,'reasonNameAdd')]")
    private ExtInput addReasonField;
    @FindBy(xpath = "//button[contains(text(),'Save')]")
    private Button save;
    @FindBy(xpath = "//button[contains(text(),'Delete')]")
    private Button delete;
    @FindBy(xpath = "//button[contains(@id,'changeStatusBtn')]")
    private Button changeStatus;
    @FindBy(id = "reasons_table")
    private Table reasons;


    @Override
    protected Page ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForVisible(reasonTypes);
        return this;
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/Admin/edit_reasons.jsp";
    }

    public EditReasonsPage selectCompany(String insuranceCompany) {
        try {
            companies.selectByVisibleText(insuranceCompany);
        } catch (NoSuchElementException e) {

        }
        return this;
    }

    public EditReasonsPage selectReasonType(String visibleText) {
        try {
            reasonTypes.selectByVisibleText(visibleText);
        } catch (NoSuchElementException e) {
        }
        return this;
    }

    public EditReasonsPage refresh() {
        refresh.click();
        return this;
    }

    public boolean isEditReasonsFormVisible() {
        editReasonsForm.isDisplayed();
        return true;
    }

    public EditReasonsPage addReason(String text) {
        addReasonField.enter(text);
        return this;
    }

    public EditReasonsPage save() {
        save.click();
        return this;
    }

    public boolean isValueReason(String expectedValue) {
        Wait.waitForPageLoaded();
        WebElement webElement = reasonsFields.get(reasonsFields.size() - 2);
        String val = webElement.getAttribute("value");
        return expectedValue.equals(val);
    }

    private int findRowNumber(String _reason) {
        List<List<WebElement>> rowsNames = reasons.getRows();
        for (int i = 0; i < rowsNames.size(); i++) {
            String typeName = rowsNames.get(i).get(1).getText();
            if (typeName.contains(_reason)) {
                return i;
            }
        }

        return -1;
    }

    public void deleteReason(String _reason){
        int numberOfRow = findRowNumber(_reason) + 2;
        driver.findElement(By.xpath("//tr[" + numberOfRow +  "]//button[contains(text(),'Delete')]")).click();
    }

}


