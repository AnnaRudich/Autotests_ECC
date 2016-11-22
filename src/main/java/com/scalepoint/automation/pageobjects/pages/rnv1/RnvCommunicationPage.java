package com.scalepoint.automation.pageobjects.pages.rnv1;

import com.scalepoint.automation.pageobjects.pages.BaseClaimPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.services.externalapi.EccFileApi;
import com.scalepoint.automation.utils.ExcelDocUtil;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.data.entity.ServiceAgreement;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@EccPage
public class RnvCommunicationPage extends BaseClaimPage {

    @FindBy(css = "input[name='taskSelect']")
    private WebElement taskDropdownField;

    @FindBy(css = "a.attachmentsIcon")
    private WebElement firstAttachIcon;

    @FindBy(css = "div[class='bubble sent']:first-of-type span:nth-of-type(2)")
    private WebElement latestMessageText;

    @FindBy(css = "textarea[name = 'replyMsgTextArea']")
    private WebElement mailTextField;

    @FindBy(css = "span#button-rely-btnInnerEl")
    private WebElement sendBtn;

    @Override
    protected Page ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        Wait.waitForVisible(sendBtn);
        return null;
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/matching_engine/projects.jsp";
    }

    public Boolean isMessageSent() {
        return isDisplayed(By.cssSelector("div[class='bubble sent']"));
    }

    public Boolean isLatestMessageContains(String textMessage) {
        return latestMessageText.getText().contains(textMessage);
    }

    private void downloadTemplate(User user, String fileLocation) throws Exception {
        firstAttachIcon.click();
        String href = find(By.cssSelector("div[id*='attachmentsDownloadList'] a")).getAttribute("href");
        EccFileApi eccFileApi = new EccFileApi(user);
        eccFileApi.downloadFile(href, fileLocation);
        find(By.cssSelector("span#button-close-chat-attachment-btnInnerEl")).click();
    }

    public RnvCommunicationPage doFeedback(User user, ServiceAgreement serviceAgreement, ExcelDocUtil.FeedbackActionType actionType) throws Exception {
        downloadTemplate(user, serviceAgreement.getSaveTemplateTo());
        ExcelDocUtil doc = new ExcelDocUtil();
        doc.doFeedback(actionType, serviceAgreement);
        return this;
    }


    public void sendTextMailToSePa(String textMsg) {
        sendKeys(mailTextField, textMsg);
        sendBtn.click();
    }
}
