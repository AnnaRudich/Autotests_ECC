package com.scalepoint.automation.pageobjects.pages.rnv1;

import com.google.common.base.Function;
import com.scalepoint.automation.pageobjects.pages.BaseClaimPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.services.externalapi.EccFileApi;
import com.scalepoint.automation.utils.ExcelDocUtil;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.data.entity.ServiceAgreement;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import javax.annotation.Nullable;

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

    @FindBy(id = "button-rely")
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

    public RnvCommunicationPage assertLatestMessageContains(String textMessage) {
        if (!latestMessageText.getText().contains(textMessage)) {
            throw new AssertionError("Latest message doesn't contain: " + textMessage);
        }
        return this;
    }

    private void downloadTemplate(User user, String fileLocation) {
        firstAttachIcon.click();
        String href = find(By.cssSelector("div[id*='attachmentsDownloadList'] a")).getAttribute("href");
        EccFileApi eccFileApi = new EccFileApi(user);
        eccFileApi.downloadFile(href, fileLocation);
        find(By.cssSelector("span#button-close-chat-attachment-btnInnerEl")).click();
    }

    public RnvCommunicationPage doFeedback(User user, ServiceAgreement serviceAgreement, ExcelDocUtil.FeedbackActionType actionType) {
        try {
            downloadTemplate(user, serviceAgreement.getSaveTemplateTo());
            ExcelDocUtil doc = new ExcelDocUtil();
            doc.doFeedback(actionType, serviceAgreement);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return this;
    }


    public RnvCommunicationPage sendTextMailToSePa(String textMsg) {
        sendKeys(mailTextField, textMsg);
        sendBtn.click();
        Wait.forCondition(new Function<WebDriver, Object>() {
            @Nullable
            @Override
            public Object apply(@Nullable WebDriver webDriver) {
                return latestMessageText.getText().contains(textMsg);
            }
        });
        return this;
    }
}
