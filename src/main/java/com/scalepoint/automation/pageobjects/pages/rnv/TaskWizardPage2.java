package com.scalepoint.automation.pageobjects.pages.rnv;

import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.RVPage;
import com.scalepoint.automation.utils.data.entity.ServiceAgreement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Selenide.$;

@RVPage
public class TaskWizardPage2 extends Page {


    @FindBy(css = "#button-send")
    private WebElement sendBtn;

    @Override
    protected Page ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        Wait.waitForVisible(sendBtn);
        return null;
    }

    @Override
    protected String getRelativeUrl() {
        return "/?orderToken";
    }

    public SettlementPage sendRnvIsSuccess(ServiceAgreement serviceAgreement) {
        String sent = serviceAgreement.getSentText();
        sendTaskAndWaitForStatus(sent);
        return at(SettlementPage.class);
    }

    public SettlementPage sendRnvIsFailOnServicePartnerSide(ServiceAgreement serviceAgreement) {
        String error = serviceAgreement.getErrorText();
        sendTaskAndWaitForStatus(error);
        return at(SettlementPage.class);
    }

    private void sendTaskAndWaitForStatus(String status){
        clickAndWaitForDisplaying(sendBtn, By.xpath("//div[contains(text(), '" + status + "')]"));
        $("a.tasks-statuses-close-button").click();
    }
}
