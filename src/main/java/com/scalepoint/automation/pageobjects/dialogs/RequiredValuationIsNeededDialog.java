package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;

import static com.codeborne.selenide.Selenide.$;

/**
 * @author : igu
 */
public class RequiredValuationIsNeededDialog extends BaseDialog {

    @Override
    protected BaseDialog ensureWeAreAt() {
        $(".required-valuation-is-needed-dialog").waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
        return this;
    }

}
