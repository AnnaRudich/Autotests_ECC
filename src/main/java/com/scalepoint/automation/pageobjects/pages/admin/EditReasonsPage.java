package com.scalepoint.automation.pageobjects.pages.admin;

import com.scalepoint.automation.pageobjects.extjs.ExtInput;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.CheckBox;
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
    @FindBy(xpath = "//button[contains(@id,'changeStatusBtn')]")
    private Button changeStatus;
    @FindBy(id = "reasons_table")
    private Table reasons;
    @FindBy(id = "showDisabled")
    private CheckBox showDisabled;


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

    public EditReasonsPage showDisabled(boolean state) {
        showDisabled.set(state);
        return this;
    }

    public boolean isValueReason(String expectedValue) {
        WebElement webElement = reasonsFields.get(reasonsFields.size() - 2);
        String val = webElement.getAttribute("value");
        return expectedValue.equals(val);
    }

    public boolean isReasonEditable(String visibleValue) {
        int numberOfRow = findRowNumber(visibleValue) + 1;
        WebElement element = driver.findElement(By.xpath("//tr[" + numberOfRow + "]/td/div/input"));
        String readonly = element.getAttribute("readonly");
        return readonly == null;
    }

    public boolean isReasonEnabled(String visibleValue) {
        int numberOfRow = findRowNumber(visibleValue) + 1;
        WebElement element = driver.findElement(By.xpath("//tr[" + numberOfRow + "]/td/div/input"));
        String disabled = element.getAttribute("disabled");
        return disabled == null;
    }


    private int findRowNumber(String visibleValue) {
        List<List<WebElement>> rowsNames = reasons.getRows();
        for (int i = 0; i < rowsNames.size(); i++) {
            WebElement input = rowsNames.get(i).get(0).findElement(By.tagName("input"));
            if (visibleValue.equals(input.getAttribute("value"))) {
                return i;
            }
        }
        return -1;
    }

    public EditReasonsPage deleteReason(String visibleValue) {
        int numberOfRow = findRowNumber(visibleValue) + 1;
        driver.findElement(By.xpath("//tr[" + numberOfRow + "]//button[contains(@id,'deleteBtn')]")).click();
        if (isAlertPresent()) {
            acceptAlert();
        }
        return this;
    }

    public boolean isDeleteEnable(String visibleValue) {
        int numberOfRow = findRowNumber(visibleValue) + 1;
        return driver.findElement(By.xpath("//tr[" + numberOfRow + "]//button[contains(@id,'deleteBtn')]")).isEnabled();
    }

    public boolean isReasonVisible(String visibleValue) {
        List<WebElement> reasons = reasonsFields;
        return reasons.stream().anyMatch(reason -> reason.getAttribute("value").equals(visibleValue));
    }

    public EditReasonsPage disableEnableReason(String visibleValue) {
        int numberOfRow = findRowNumber(visibleValue) + 1;
        driver.findElement(By.xpath("//tr[" + numberOfRow + "]//button[contains(@onclick,'disableEnableReason')]")).click();
        if (isAlertPresent()) {
            acceptAlert();
        }
        return this;
    }

}


