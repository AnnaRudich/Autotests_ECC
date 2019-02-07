package com.scalepoint.automation.pageobjects.extjs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.yandex.qatools.htmlelements.element.TypifiedElement;

public abstract class ExtElement extends TypifiedElement {
    public ExtElement(WebElement wrappedElement) {
        super(wrappedElement);
    }

    public WebElement getRootElement() {
        return getWrappedElement();
    }

    private WebElement getInput(boolean skipHidden) {
        return findElements(By.tagName("input")).stream()
                .filter(el -> !el.getAttribute("type").equals("hidden") || !skipHidden)
                .findFirst().orElse(null);
    }

    public boolean isInputElementEnabled() {
        WebElement inputElement = getInput(true);
        return inputElement != null && inputElement.isEnabled();
    }

}
