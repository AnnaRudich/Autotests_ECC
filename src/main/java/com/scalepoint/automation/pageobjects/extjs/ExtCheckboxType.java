package com.scalepoint.automation.pageobjects.extjs;

import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.Actions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.yandex.qatools.htmlelements.element.TypifiedElement;

import static com.codeborne.selenide.Selenide.$;

public abstract class ExtCheckboxType extends TypifiedElement implements Actions {

    final SelenideElement element;
    final By locator;

    protected ExtCheckboxType(WebElement wrappedElement, By locator) {
        super(wrappedElement);
        element = $(wrappedElement);
        this.locator = locator;
    }

    public boolean isChecked() {

        return element.attr("class").contains("x-form-cb-checked");
    }

    public void set(boolean state) {
        if (state != isChecked()) {

            hoverAndClick(element.find(locator));
        }
    }
}
