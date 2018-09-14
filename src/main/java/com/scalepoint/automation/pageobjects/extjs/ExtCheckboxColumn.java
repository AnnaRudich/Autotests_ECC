package com.scalepoint.automation.pageobjects.extjs;

import com.scalepoint.automation.utils.threadlocal.Browser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

public class ExtCheckboxColumn extends ExtElement {

    protected Logger log = LogManager.getLogger(ExtCheckboxColumn.class);

    private String checkboxFieldName;
    private String textFieldName;
    private int columnIndex;

    public ExtCheckboxColumn(WebElement wrappedElement, String textFieldName, String checkboxFieldName, int columnIndex) {
        super(wrappedElement);
        this.checkboxFieldName = checkboxFieldName;
        this.textFieldName = textFieldName;
        this.columnIndex = columnIndex;
    }

    public void enable(String visibleText) {
        select(visibleText, true);
    }

    public void disable(String visibleText) {
        select(visibleText, false);
    }

    private void select(String visibleText, boolean enable) {
        Object[] args = {getRootElement(), visibleText, textFieldName, checkboxFieldName};
        String js =
                "var id = arguments[0].id," +
                        "cmp = Ext.getCmp(id)," +
                        "store = cmp.getStore()," +
                        "text = arguments[1]," +
                        "textFieldName = arguments[2]," +
                        "checkboxFieldName = arguments[3]," +
                        "record = store.findRecord(textFieldName, text);" +
                        "record.set(checkboxFieldName, "+enable+");" +
                        "cmp.fireEvent('checkchangedEvent', cmp.getColumns()["+columnIndex+"], store.indexOf(record), true);";
        try {
            ((JavascriptExecutor) Browser.driver()).executeScript(js, args);
        } catch (Exception e) {
            log.info(e.getMessage());
            throw e;
        }
    }

    public boolean isSelected(String visibleText) {
        Object[] args = {getRootElement(), visibleText, textFieldName, checkboxFieldName};
        String js =
                "var id = arguments[0].id," +
                        "cmp = Ext.getCmp(id)," +
                        "store = cmp.getStore()," +
                        "text = arguments[1]," +
                        "textFieldName = arguments[2]," +
                        "checkboxFieldName = arguments[3]," +
                        "record = store.findRecord(textFieldName, text);" +
                        "return record.get(checkboxFieldName);";
        Object result = ((JavascriptExecutor) Browser.driver()).executeScript(js, args);
        return Boolean.valueOf(result.toString());
    }
}
