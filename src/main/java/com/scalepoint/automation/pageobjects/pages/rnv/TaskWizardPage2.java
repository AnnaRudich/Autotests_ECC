package com.scalepoint.automation.pageobjects.pages.rnv;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.utils.annotations.page.RVPage;
import com.scalepoint.automation.utils.data.entity.input.ServiceAgreement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

@RVPage
public class TaskWizardPage2 extends Page {

    @FindBy(css = "#button-send")
    private SelenideElement sendBtn;

    @Override
    protected void ensureWeAreOnPage() {

        waitForUrl(getRelativeUrl());
        waitForAjaxCompletedAndJsRecalculation();
        $(sendBtn).should(visible);
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

        hoverAndClick($(sendBtn));
        $(By.xpath("//div[contains(text(), '" + status + "')]")).should(Condition.visible, Duration.ofSeconds(30));
        $("a.tasks-statuses-close-button").click();
    }
}
