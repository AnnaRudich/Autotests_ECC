package com.scalepoint.automation.pageobjects.pages.admin;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.externalapi.ftemplates.operations.FtOperation;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.annotations.page.RequiredParameters;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.CheckBox;
import ru.yandex.qatools.htmlelements.element.Select;
import ru.yandex.qatools.htmlelements.element.TextInput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForPageLoaded;

@EccPage
@RequiredParameters("ftrfnbr=%s&showHidden=true")
public class EditFunctionTemplatePage extends AdminBasePage {

    @FindBy(xpath = "//input[@value='Save values']")
    private Button saveValues;

    @FindBy(xpath = "//button[(text() = 'show hidden')]")
    private Button showHidden;

    @FindBy(xpath = "//input[@name='fttmplname']")
    private WebElement name;

    @Override
   protected void ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForPageLoaded();
        $(saveValues).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
    }

    public FunctionalTemplatesPage saveTemplate() {
        clickUsingJavaScriptIfClickDoesNotWork(saveValues);
        return at(FunctionalTemplatesPage.class);
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/Admin/func_template_edit.jsp";
    }

    public List<FtOperation> findDifferences(FtOperation... operations) {
        List<FtOperation> notAppliedOperations = new ArrayList<>();
        for (FtOperation operation : operations) {
            if (!operation.isOperationApplied(this)) {
                notAppliedOperations.add(operation);
            }
        }
        return notAppliedOperations;
    }

    public boolean isSettingEnabled(FTSetting ftSetting) {
        return driver.findElement(By.cssSelector(ftSetting.getLocator())).isSelected();
    }

    public boolean isSettingHasSameValue(FTSetting ftSetting, String text) {
        TextInput textInput = new TextInput(driver.findElement(By.cssSelector(ftSetting.getLocator())));
        return textInput.getText().equalsIgnoreCase(text);
    }

    public boolean isSettingHasSameOptionSelected(FTSetting ftSetting, String text) {
        Select comboBox = new Select(driver.findElement(By.cssSelector(ftSetting.getLocator())));
        WebElement firstSelectedOption = comboBox.getFirstSelectedOption();
        return clearTextFromNewLines(firstSelectedOption.getText()).equalsIgnoreCase(text);
    }

    private String clearTextFromNewLines(String text) {
        List<String> lines = Arrays.stream(text.split("\\r?\\n")).map(t -> t.trim()).collect(Collectors.toList());
        return StringUtils.join(lines, " ").trim();
    }

    public EditFunctionTemplatePage enableFeature(FTSetting ftSetting) {
        updateCheckBox(ftSetting, true);
        return this;
    }

    public EditFunctionTemplatePage disableFeature(FTSetting ftSetting) {
        updateCheckBox(ftSetting, false);
        return this;
    }

    private void updateCheckBox(FTSetting ftSetting, boolean enable) {
        WebElement element = driver.findElement(By.cssSelector(ftSetting.getLocator()));
        CheckBox checkBox = new CheckBox(element);
        String description = ftSetting.getDescription();

        if (enable && !checkBox.isSelected()) {
            logger.info("Enabling: " + description);
            clickUsingJS(element);
            checkBox.select();
        }

        if (!enable && checkBox.isSelected()) {
            logger.info("Disabling: " + description);
            clickUsingJS(element);
        }
        logger.debug("CheckBox state is {} for {} ", checkBox.isSelected(), ftSetting);
    }

    public EditFunctionTemplatePage selectComboBoxValue(FTSetting ftSetting, String option) {
        Select comboBox = new Select(driver.findElement(By.cssSelector(ftSetting.getLocator())));
        comboBox.selectByVisibleText(option);
        logger.info("Selecting {} with option {}", ftSetting.getDescription(), option);
        return this;
    }

    public EditFunctionTemplatePage updateValue(FTSetting ftSetting, String value) {
        TextInput textInput = new TextInput(driver.findElement(By.cssSelector(ftSetting.getLocator())));
        textInput.clear();
        textInput.sendKeys(value);
        logger.info("Update value {} with {}", ftSetting.getDescription(), value);
        return this;
    }

    public EditFunctionTemplatePage setName(String ftName) {
        name.clear();
        name.sendKeys(ftName);
        return this;
    }

    public EditFunctionTemplatePage showHidden() {
        showHidden.click();
        return this;
    }
}
