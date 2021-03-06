package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;

import static com.codeborne.selenide.Selenide.$;

/**
 * @author : igu
 */
public class RequiredValuationIsNeededDialog extends BaseDialog {

    @Override
    protected void ensureWeAreAt() {

        $(".required-valuation-is-needed-dialog").should(Condition.visible);
    }
}