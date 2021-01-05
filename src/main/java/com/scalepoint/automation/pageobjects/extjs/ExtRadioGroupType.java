package com.scalepoint.automation.pageobjects.extjs;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.Actions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.yandex.qatools.htmlelements.element.TypifiedElement;

import static com.codeborne.selenide.Selenide.$;

public class ExtRadioGroupType extends TypifiedElement implements Actions {

    SelenideElement element;
    By locator;

    public ExtRadioGroupType(WebElement wrappedElement, By locator) {
        super(wrappedElement);
        element = $(wrappedElement);
        this.locator = locator;
    }

    public void select(int index) {
        hoverAndClick(element.findAll(locator).get(index));
    }

    public int getSelected() {
        ElementsCollection elements = element.findAll(By.tagName("table"));
        int index;
        for (index = 0; index < elements.size(); index++) {
            if (elements.get(index).attr("class").contains("x-form-cb-checked"))
                return index - 1;
        }
        return -1;
    }
}

