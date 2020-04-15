package com.scalepoint.automation.pageobjects.extjs;

import com.scalepoint.automation.Actions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ExtCheckbox extends ExtElement implements Actions {

    public static final String INPUT = "input";
    public static final String ARIA_CHECKED = "aria-checked";

    public ExtCheckbox(WebElement wrappedElement) {
        super(wrappedElement);
    }

    protected Logger logger = LogManager.getLogger(ExtCheckbox.class);

    public void set(boolean state) {
        if (state != isSelected()) {
            safeJavaScriptClick(getWrappedElement().findElement(By.tagName(INPUT)));
        }
    }

    public boolean isSelected() {
        return getWrappedElement().getAttribute("class").contains("x-form-cb-checked")
                || (getWrappedElement().findElement(By.tagName(INPUT)).getAttribute(ARIA_CHECKED) != null && getWrappedElement().findElement(By.tagName(INPUT)).getAttribute(ARIA_CHECKED).contains("true"));
    }
}
