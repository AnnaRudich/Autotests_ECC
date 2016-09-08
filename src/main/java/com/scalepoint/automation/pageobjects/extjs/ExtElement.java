package com.scalepoint.automation.pageobjects.extjs;

import org.openqa.selenium.WebElement;
import ru.yandex.qatools.htmlelements.element.TypifiedElement;

public abstract class ExtElement extends TypifiedElement {
    public ExtElement(WebElement wrappedElement) {
        super(wrappedElement);
    }

    public WebElement getRootElement() {
        return getWrappedElement();
    }
}
