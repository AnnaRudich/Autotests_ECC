package com.scalepoint.automation.pageobjects.pages.rnv;

import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.utils.annotations.page.RVPage;
import com.scalepoint.automation.utils.data.entity.ServiceAgreement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;
import static com.scalepoint.automation.utils.Wait.waitForPageLoaded;

@RVPage
public class TaskWizardPage2 extends Page {


    @FindBy(css = "#button-send")
    private WebElement sendBtn;

    @Override
    protected void ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForAjaxCompletedAndJsRecalculation();
        waitForPageLoaded();
        $(sendBtn).waitUntil(visible, TIME_OUT_IN_MILISECONDS);
    }

    @Override
    protected String getRelativeUrl() {
        return "/?orderToken";
    }

    public SettlementPage sendRnvIsSuccess(ServiceAgreement serviceAgreement) {
        String sent = serviceAgreement.getSent();
        sendTaskAndWaitForStatus(sent);
        return at(SettlementPage.class);
    }

    public SettlementPage sendRnvIsFailOnServicePartnerSide(ServiceAgreement serviceAgreement) {
        String error = serviceAgreement.getError();
        sendTaskAndWaitForStatus(error);
        return at(SettlementPage.class);
    }

    private void sendTaskAndWaitForStatus(String status){
        clickAndWaitForDisplaying(sendBtn, By.xpath("//div[contains(text(), '" + status + "')]"));
        $("a.tasks-statuses-close-button").click();
    }
}
