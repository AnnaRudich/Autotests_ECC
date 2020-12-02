package com.scalepoint.automation.pageobjects.pages.rnv.tabs;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.pageobjects.pages.BaseClaimPage;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Condition.exactTextCaseSensitive;
import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;
import static com.scalepoint.automation.utils.Wait.waitForPageLoaded;

@EccPage
public class CommunicationTab extends BaseClaimPage {

    @FindBy(css = "input[name='taskSelect']")
    private WebElement taskDropdownField;

    @FindBy(css = "a.attachmentsIcon")
    private WebElement firstAttachIcon;

    @FindBy(css = "div[class='bubble sent']:first-of-type span:nth-of-type(2)")
    private WebElement latestMessageText;

    @FindBy(css = "textarea[name = 'replyMsgTextArea']")
    private WebElement mailTextField;

    @FindBy(id = "button-rely")
    private WebElement sendBtn;

    @Override
    protected void ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForAjaxCompletedAndJsRecalculation();
        waitForPageLoaded();
        $(sendBtn).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/matching_engine/projects.jsp";
    }

    public CommunicationTab assertLatestMessageContains(String textMessage) {
        $(latestMessageText).shouldHave(exactTextCaseSensitive(textMessage));
        return this;
    }

    public CommunicationTab sendTextMailToSePa(String textMsg) {
        $(mailTextField).sendKeys(textMsg);
        $(sendBtn).click();
        $(latestMessageText).waitUntil(exactTextCaseSensitive(textMsg), 60000);
        return this;
    }
}
