package com.scalepoint.automation.pageobjects.modules.textSearch;

public interface SearchAttributesActions {

    /**
     * Method to choose action basing on attribute type
     * @param attributes
     * @see com.scalepoint.automation.pageobjects.modules.textSearch.TextSearchAttributesMenu.Attributes
     */
    void selectAttribute(TextSearchAttributesMenu.Attributes attributes);
}
