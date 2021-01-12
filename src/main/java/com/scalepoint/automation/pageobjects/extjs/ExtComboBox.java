package com.scalepoint.automation.pageobjects.extjs;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.Actions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ExtComboBox extends ExtElement implements Actions {

    SelenideElement picker;
    By optionSelector;

    public ExtComboBox(WebElement wrappedElement, By pickerSelector, By optionSelector) {
        super(wrappedElement);
        picker=$(wrappedElement).find(pickerSelector);
        this.optionSelector = optionSelector;
    }

    /**
     * FtSelect option by visible Text
     *
     * @param visibleText option text which should be selected from list of combo box
     */
    public void select(String visibleText) {
        LocalDateTime start = LocalDateTime.now();
        hoverAndClick(picker);
        SelenideElement option = getOptions()
                .findBy(text(visibleText));
        hoverAndClick(option);
        LocalDateTime end = LocalDateTime.now();
        long duration = Duration.between(start, end).getSeconds();
        logger.info("Select ext {} time:, {}", visibleText, duration);
    }

    /**
     * FtSelect option by index
     *
     * @param index place number of option text which should be selected from list of combo box
     */
    public void select(int index) {
        hoverAndClick(picker);
        SelenideElement option = getOptions()
                .get(index);
        hoverAndClick(option);
    }

    private boolean isPickerFieldOpen(){

        return $(getWrappedElement())
                .find("[data-ref='bodyEl']")
                .attr("class")
                .contains("x-pickerfield-open");
    }

    @SuppressWarnings("unchecked")
    public List<String> getComboBoxOptions() {
        LocalDateTime start = LocalDateTime.now();
        if(!isPickerFieldOpen()) {
            hoverAndClick(picker);
        }
        List<String> options =  getOptions().stream()
                .parallel()
                .map(element -> element.getText())
                .collect(Collectors.toList());
        LocalDateTime end = LocalDateTime.now();
        long duration = Duration.between(start, end).getSeconds();
        logger.info("ComoboBoxOptions ext time:, {}", duration);

        return options;
    }

    private ElementsCollection getOptions(){
        return  $$(optionSelector);
    }

    private WebElement getInput() {
        return getWrappedElement().findElement(By.tagName("input"));
    }

    public String getValue() {
        return getInput().getAttribute("value");
    }

    public boolean isEnabled() {
        return getInput().isEnabled();
    }
}
