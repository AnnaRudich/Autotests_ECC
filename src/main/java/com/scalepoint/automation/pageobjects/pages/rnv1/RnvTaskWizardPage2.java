package com.scalepoint.automation.pageobjects.pages.rnv1;

import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.RVPage;
import com.scalepoint.automation.utils.data.entity.ServiceAgreement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompleted;

@RVPage
public class RnvTaskWizardPage2 extends Page {

    @FindBy(css = "span[id*='button-next'] span span")
    private WebElement nextBtn;
    @FindBy(css = "#button-send")
    private WebElement sendBtn;
    @FindBy(css = "span[id*='button-back'] span span")
    private WebElement backBtn;
    @FindBy(css = "span[id*='button-close'] span span")
    private WebElement closeBtn;

    private String byCLNameInTaskDetailsXpath = "//div[contains(@id, 'expandedLinesGridId']//td/div[contains(., '$1')]";
    private String taskTypeByCLNameInTaskDetailsXpath = "//div[contains(., '$1')]/ancestor::tr/td[3]/div";

    @FindBy(css = "input[name = 'file']")
    private WebElement attachmentFileField;

    @FindBy(xpath = "//tr/td[3]/div")
    private WebElement agrInTopTask;

    @FindBy(css = "textarea#noteToServicePartnerId-inputEl")
    private WebElement noteToSePaField;

    @FindBy(css = "input#sendEmailToClaimantCheckboxId-inputEl")
    private WebElement addNoteToClaimantCheckbox;

    @FindBy(css = "textarea#noteToClaimantId-inputEl")
    private WebElement noteToClaimantField;

    @FindBy(css = "div.x-grid-row-expander")
    private WebElement expanderIcon;

    @FindBy(css = "span#yesToFirstWizardStep-btnInnerEl")
    private WebElement okBtn;

    private String byAgrNameXpath = "//div[contains(@id, 'prepearedTaskGridId-body')]//td[4]/div[contains(., '$1')]";
    private String agrInfoIconByAgrNameXpath = "//div[contains(@id, 'prepearedTaskGridId-body')]//td[4]/div[contains(., '$1')]/ancestor::tr/td[3]";
    private String locByAgrNameXpath = "//div[contains(@id, 'prepearedTaskGridId-body')]//td[4]/div[contains(., '$1')]/ancestor::tr/td[5]";
    private String emailByAgrNameXpath = "//div[contains(@id, 'prepearedTaskGridId-body')]//td[4]/div[contains(., '$1')]/ancestor::tr/td[6]";
    private String agrInfoIconByAgrName = "//div[contains(., '$1')]/ancestor::tr/td[3]/div/img";

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

    public SettlementPage sendRnV(ServiceAgreement serviceAgreement) {
        String sendText = serviceAgreement.getSentText();
        clickAndWaitForDisplaying(sendBtn, By.xpath("//div[contains(text(), '" + sendText + "')]"));
        $("a.tasks-statuses-close-button").click();
        return at(SettlementPage.class);
    }

    public void closeRnV() {
        clickAndWaitForDisplaying(closeBtn, By.cssSelector("div[id*='messagebox'] a:nth-of-type(2) span span span"));
        //click Yes btn
        find(By.cssSelector("div[id*='messagebox'] a:nth-of-type(2) span span span")).click();
    }

    public String getTopTaskAgr() {
        return getText(agrInTopTask);
    }

    public void uploadAttach(ServiceAgreement serviceAgreement) {
        enterToHiddenUploadFileField(attachmentFileField, serviceAgreement.getTemplateFileLocation());
    }


    public boolean isNotesToSePaFieldDisplays() {
        return isDisplayed(noteToSePaField);
    }

    public void addNoteToSePa(String noteToSepaText) {
        clear(noteToSePaField);
        sendKeys(noteToSePaField, noteToSepaText);
    }

    public void collapseTopTask() {
        expanderIcon.click();
        waitForAjaxCompleted();
    }

    public void expandTopTask() {
        expanderIcon.click();
        waitForAjaxCompleted();
    }

    public String getNotesToSePaText() {
        return noteToSePaField.getText();
    }

    public void backTo1stStep() {
        backBtn.click();
        clickAndWaitForDisplaying(okBtn, By.cssSelector("input[name='name']"));
    }

    public boolean isLinePresentInTaskDetails(String claimLineName) {
        return isElementPresent(By.xpath(byCLNameInTaskDetailsXpath.replace("$1", claimLineName)));
    }

    public String getTaskType(String clName) {
        return getText(By.xpath(taskTypeByCLNameInTaskDetailsXpath.replace("$1", clName)));
    }

    public String getTaskLoc(String agrName) {
        return getText(By.xpath(locByAgrNameXpath.replace("$1", agrName)));
    }

    public boolean isAgrInfoIconDisplays(String agrName) {
        WebElement item = find(By.xpath(agrInfoIconByAgrName.replace("$1", agrName)));
        return item.isDisplayed();
    }

    public String getAgrInfoText(String agrName) {
        WebElement item = find(By.xpath(agrInfoIconByAgrName.replace("$1", agrName)));
        String text = item.getAttribute("data-qtip");
        text = text.replace("<b>", "");
        text = text.replace("</b>", " ");
        text = text.replace("<br>", " ");
        return text;
    }
}
