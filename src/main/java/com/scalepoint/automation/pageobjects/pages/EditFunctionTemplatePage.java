package com.scalepoint.automation.pageobjects.pages;

import com.scalepoint.automation.utils.annotations.EccPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Select;
import ru.yandex.qatools.htmlelements.element.TextInput;

import static com.scalepoint.automation.utils.Wait.waitForVisible;

@EccPage
public class EditFunctionTemplatePage extends Page {

    private static final String URL = "webshop/jsp/Admin/func_template_edit.jsp?ftrfnbr=";

    @FindBy(xpath = "//input[@value='Save values']")
    private Button saveValues;

    @FindBy(xpath = "//button[(text() = 'show hidden')]")
    private Button showHidden;

    @Override
    protected String geRelativeUrl() {
        return URL;
    }

    @Override
    public EditFunctionTemplatePage ensureWeAreOnPage() {
        waitForUrl(URL);
        waitForVisible(saveValues);
        return this;
    }

    public AdminPage saveTemplate() {
        saveValues.click();
        return at(AdminPage.class);
    }

    public EditFunctionTemplatePage enableFeature(String _function) {
        WebElement checkBox = browser.findElement(By.xpath("(//td[text()='" + _function + "']/following::td/input)[1]"));
        if (!checkBox.isSelected()) {
            checkBox.click();
        }
        return this;
    }

    public EditFunctionTemplatePage disableFeature(String _function) {
        WebElement checkBox = browser.findElement(By.xpath("(//td[text()='" + _function + "']/following::td/input)[1]"));
        if (checkBox.isSelected()) {
            checkBox.click();
        }
        return this;
    }

    public EditFunctionTemplatePage selectComboboxValue(String _function, String option) {
        Select combobox = new Select(browser.findElement(By.xpath("(//td[text()='" + _function + "']/following::td/select)[1]")));
        combobox.selectByVisibleText(option);
        return this;
    }

    public EditFunctionTemplatePage updateValue(String _function, String _value) {
        TextInput textInput = new TextInput(browser.findElement(By.xpath("(//td[text()='" + _function + "']/following::td/input)[1]")));
        textInput.clear();
        textInput.sendKeys(_value);
        return this;
    }

    public EditFunctionTemplatePage ShowHidden() {
        showHidden.click();
        return this;
    }
}
