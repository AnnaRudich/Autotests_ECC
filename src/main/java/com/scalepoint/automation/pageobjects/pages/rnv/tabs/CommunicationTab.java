package com.scalepoint.automation.pageobjects.pages.rnv.tabs;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.pageobjects.pages.BaseClaimPage;
import com.scalepoint.automation.services.externalapi.EccFileApi;
import com.scalepoint.automation.utils.ExcelDocUtil;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.data.entity.ServiceAgreement;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Condition.exactTextCaseSensitive;
import static com.codeborne.selenide.Selenide.$;
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
    protected CommunicationTab ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForJavascriptRecalculation();
        waitForPageLoaded();
        $(sendBtn).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
        return this;
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/matching_engine/projects.jsp";
    }

    public Boolean isMessageSent() {
        return isDisplayed(By.cssSelector("div[class='bubble sent']"));
    }

    public CommunicationTab assertLatestMessageContains(String textMessage) {
        $(latestMessageText).shouldHave(exactTextCaseSensitive(textMessage));
        return this;
    }

    private void downloadTemplate(User user, String fileLocation) {
        firstAttachIcon.click();
        String href = find(By.cssSelector("div[id*='attachmentsDownloadList'] a")).getAttribute("href");
        EccFileApi eccFileApi = new EccFileApi(user);
        eccFileApi.downloadFile(href, fileLocation);
        find(By.cssSelector("span#button-close-chat-attachment-btnInnerEl")).click();
    }

    public CommunicationTab doFeedback(User user, ServiceAgreement serviceAgreement, ExcelDocUtil.FeedbackActionType actionType) {
        try {
            downloadTemplate(user, serviceAgreement.getSaveTemplateTo());
            ExcelDocUtil doc = new ExcelDocUtil();
            doc.doFeedback(actionType, serviceAgreement);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return this;
    }


    public CommunicationTab sendTextMailToSePa(String textMsg) {
        $(mailTextField).sendKeys(textMsg);
        $(sendBtn).click();
        $(latestMessageText).waitUntil(exactTextCaseSensitive(textMsg), 60000);
        return this;
    }
}
