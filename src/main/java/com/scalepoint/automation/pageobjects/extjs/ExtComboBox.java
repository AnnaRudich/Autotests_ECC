package com.scalepoint.automation.pageobjects.extjs;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.Actions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Condition.*;
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
        hoverAndClickNoWait(picker);
        SelenideElement option = getOptions()
                .stream()
                .filter(item -> item.getText().contains(visibleText))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
        hoverAndClickNoWait(option);
    }

    /**
     * FtSelect option by index
     *
     * @param index place number of option text which should be selected from list of combo box
     */
    public void select(int index) {
        hoverAndClickNoWait(picker);
        SelenideElement option = getOptions()
                .get(index);
        hoverAndClickNoWait(option);
    }

    private boolean isPickerFieldOpen(){

        return $(getWrappedElement())
                .find("[data-ref='bodyEl']")
                .attr("class")
                .contains("x-pickerfield-open");
    }

    @SuppressWarnings("unchecked")
    public List<String> getComboBoxOptions() {
        if(!isPickerFieldOpen()) {
            hoverAndClickNoWait(picker);
        }
        return getOptions().stream()
                .parallel()
                .map(element -> element.getText())
                .collect(Collectors.toList());
    }

    private ElementsCollection getOptions(){
        ElementsCollection collection = $$(optionSelector)
                .filter(Condition.visible);
        return collection;
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
