package com.scalepoint.automation.pageobjects.extjs;

import com.scalepoint.automation.Actions;
import com.scalepoint.automation.utils.threadlocal.Browser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ExtRadioGroup extends ExtElement implements Actions{

    protected Logger logger = LogManager.getLogger(ExtRadioGroup.class);

    public ExtRadioGroup(WebElement wrappedElement) {
        super(wrappedElement);
    }

    public void select(int index) {
        WebElement element =  getWrappedElement().findElements(By.tagName("input")).get(index);
        try {
            element.click();
        }catch (Exception e){
            logger.error(e.getMessage());
            logger.info("Using JS click for element");
            ClickElementUsingJS(element);
        }
    }

    public void select() {
        getWrappedElement().findElement(By.tagName("input")).click();
    }

    public boolean isSelected() {
        JavascriptExecutor executor = (JavascriptExecutor) Browser.driver();
        Object[] args = {getRootElement()};
        return (Boolean) executor.executeScript(
                "var id = arguments[0].id;" +
                        "var comp = Ext.getCmp(id);" +
                        "return comp.getValue();",
                args
        );
    }

    public int getSelected() {
        List<WebElement> elements = getWrappedElement().findElements(By.tagName("table"));
        int index;
        for (index = 0; index < elements.size(); index++) {
            if (elements.get(index).getAttribute("class").contains("x-form-cb-checked"))
                return index - 1;
        }
        return -1;
    }

    private WebElement getInput() {
        return getWrappedElement().findElement(By.tagName("input"));
    }

    public String getValue() {
        return getInput().getAttribute("value");
    }
}

