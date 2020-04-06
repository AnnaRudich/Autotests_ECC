package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

@EccPage
public class OpenSettlementOptionDialog extends BaseDialog{

    @FindBy(id = "btn_reopen")
    private Button reopen;

    @FindBy(id = "btn_cancel")
    private Button cancel;

    @Override
    protected void ensureWeAreAt() {
        waitForAjaxCompletedAndJsRecalculation();
        $(reopen).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
        $(cancel).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
    }
}
