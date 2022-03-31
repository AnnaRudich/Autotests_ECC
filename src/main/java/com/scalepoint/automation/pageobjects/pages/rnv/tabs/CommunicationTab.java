package com.scalepoint.automation.pageobjects.pages.rnv.tabs;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.pages.BaseClaimPage;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactTextCaseSensitive;
import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

@EccPage
public class CommunicationTab extends BaseClaimPage {

    @FindBy(css = "input[name='taskSelect']")
    private SelenideElement taskDropdownField;

    @FindBy(css = "a.attachmentsIcon")
    private SelenideElement firstAttachIcon;

    @FindBy(css = "div[class='bubble sent']:first-of-type span:nth-of-type(2)")
    private SelenideElement latestMessageText;

    @FindBy(css = "textarea[name = 'replyMsgTextArea']")
    private SelenideElement mailTextField;

    @FindBy(id = "button-rely")
    private SelenideElement sendBtn;

    @Override
    protected void ensureWeAreOnPage() {

        waitForUrl(getRelativeUrl());
        waitForAjaxCompletedAndJsRecalculation();
        sendBtn.should(Condition.visible);
    }

    @Override
    protected String getRelativeUrl() {

        return "webshop/jsp/matching_engine/projects.jsp";
    }

    public CommunicationTab assertLatestMessageContains(String textMessage) {

        latestMessageText.shouldHave(exactTextCaseSensitive(textMessage));
        return this;
    }

    public CommunicationTab sendTextMailToSePa(String textMsg) {

        mailTextField.sendKeys(textMsg);
        sendBtn.click();
        latestMessageText.should(exactTextCaseSensitive(textMsg), Duration.ofMillis(60000));
        return this;
    }
}
