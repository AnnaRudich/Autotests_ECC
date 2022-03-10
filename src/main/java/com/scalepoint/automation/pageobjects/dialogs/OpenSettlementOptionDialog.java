package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.By;
import ru.yandex.qatools.htmlelements.element.Button;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

@EccPage
public class OpenSettlementOptionDialog extends BaseDialogSelenide{

    private Button getReopen(){

        return new Button($(By.id("btn_reopen")));
    }

    private Button getCancel(){

        return new Button($(By.id("btn_cancel")));
    }

    @Override
    protected void ensureWeAreAt() {

        waitForAjaxCompletedAndJsRecalculation();
        $(getReopen()).should(Condition.visible);
        $(getCancel()).should(Condition.visible);
    }
}
