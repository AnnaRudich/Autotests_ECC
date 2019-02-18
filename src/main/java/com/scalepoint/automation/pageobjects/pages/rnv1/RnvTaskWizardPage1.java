package com.scalepoint.automation.pageobjects.pages.rnv1;

import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.RVPage;
import com.scalepoint.automation.utils.data.entity.Claim;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@RVPage
public class RnvTaskWizardPage1 extends Page {

    @FindBy(css = "input[name='name']")
    private WebElement nameField;
    @FindBy(css = "input[name='phone']")
    private WebElement phoneField;
    @FindBy(css = "input[name='mobilePhone']")
    private WebElement mobilePhoneField;
    @FindBy(css = "input[name='email']")
    private WebElement emailField;
    @FindBy(css = "input[name='address1']")
    private WebElement address1Field;
    @FindBy(css = "input[name='address2']")
    private WebElement address2Field;
    @FindBy(css = "input[name='postalCode']")
    private WebElement postalCodeField;
    @FindBy(css = "input[name='city']")
    private WebElement cityField;

    @FindBy(css = "span[id*='button-next'] span span")
    private WebElement nextBtn;
    @FindBy(css = "span[id*='button-send'] span span")
    private WebElement sendBtn;
    @FindBy(css = "span[id*='button-back'] span span")
    private WebElement backBtn;
    @FindBy(css = "span[id*='button-close'] span span")
    private WebElement closeBtn;
    @FindBy(css = "img[id^='close-tool']")
    private WebElement crossImg;

    @FindBy(css = "input[name = 'file']")
    private WebElement attachmentFileField;
    @FindBy(css = "div[id^='attachmentsDownloadList'] td div a")
    private WebElement topAttachment;

    @FindBy(css = "div#serviceLineListId div:nth-of-type(2) div div div div span")
    private WebElement allLinesCheckbox;

    @FindBy(css = "a#bulk-actions-menu")
    private WebElement actionMenu;
    @FindBy(css = "span#bulk-delete-textEl")
    private WebElement deleteAction;
    @FindBy(css = "span#bulk-set-agreement-and-task-type-textEl")
    private WebElement bulkReassingAction;
    @FindBy(css = "tr#agreementsCombo-inputRow")
    private WebElement agrForBulkUpdateField;
    @FindBy(css = "input#taskTypeCombo-inputEl")
    private WebElement taskForBulkUpdateField;
    @FindBy(css = "input[id^='updateTypeAndAgreementCheckBox']")
    private WebElement agrCheckboxForBulkUpdate;
    @FindBy(css = "input[id^='locationsCombo']")
    private WebElement locForBulkUpdateField;
    @FindBy(id = "bulk-update")
    private WebElement bulkUpdateBtn;

    private String byCLNameXpath = "//tr/td[3]/div[contains(., '$1')]";
    private String categoryByCLNameXpath = "//tr/td[3]/div[contains(., '$1')]/ancestor::tr/td[3]";
    private String taskTypeFieldByCLNameXpath = "//tr/td[3]/div[contains(., '$1')]/ancestor::tr/td[5]";
    private String agrByCLNameXpath = "//tr/td[3]/div[contains(., '$1')]/ancestor::tr/td[7]";
    private String locByCLXpath = "//tr/td[3]/div[contains(., '$1')]/ancestor::tr/td[9]/div";
    private String agrInfoIconByCLName = "//div[contains(., '$1')]/ancestor::tr/td[10]/div/img";
    private String tasksXpath = "//ul/li[contains(., '$1')]";
    private String agrXpath = "//ul/li[contains(., '$1')]";
    private String attachIconByCLNameXpath = "//div[contains(., '$1')]/ancestor::tr/td[2]/div/img";
    private String checkboxByCLNameXpath = "//div[contains(., '$1')]/ancestor::tr/td[1]/div/div";

    @Override
    protected Page ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        Wait.waitForVisible(nameField);
        Wait.waitForAjaxCompleted();
        return null;
    }

    @Override
    protected String getRelativeUrl() {
        return "/?orderToken";
    }

    public RnvTaskWizardPage1 changeTask(String claimLineDescription, String taskType) {
        Wait.waitForVisible(postalCodeField).click();
        String xpathTask = taskTypeFieldByCLNameXpath.replace("$1", claimLineDescription);
        clickAndWaitForDisplaying(By.xpath(xpathTask), By.cssSelector("ul.x-list-plain"));
        String xpathTaskType = tasksXpath.replace("$1", taskType);
        driver.findElement(By.xpath(xpathTaskType)).click();
        return this;
    }

    public RnvTaskWizardPage1 changeAgreement(String claimLineDescription, String agrName) {
      Wait.waitForDisplayed(By.xpath(agrByCLNameXpath.replace("$1", claimLineDescription))).click();
        String xpathAgrName = agrXpath.replace("$1", agrName);
        WebElement item = find(By.xpath(xpathAgrName));
        scrollTo(item);
        item.click();
        return this;
    }

    public boolean isCustomerInfoCorrect(Claim client) {
        return getInputValue(nameField).contains(client.getFullName()) &&
                getInputValue(phoneField).contains(client.getPhoneNumber()) &&
                getInputValue(mobilePhoneField).contains(client.getCellNumber()) &&
                getInputValue(emailField).contains(client.getEmail()) &&
                getInputValue(address1Field).contains(client.getAddress()) &&
                getInputValue(address2Field).contains(client.getAddress2()) &&
                getInputValue(postalCodeField).contains(client.getZipCode()) &&
                getInputValue(cityField).contains(client.getCity());
    }

    public void closeRnV() {
        clickAndWaitForDisplaying(closeBtn, By.cssSelector("div[id*='messagebox'] a:nth-of-type(2) span span span"));
        clickAndWaitForDisplaying(By.cssSelector("div[id*='messagebox'] a:nth-of-type(2) span span span"), By.xpath("//td[contains(@class,'descriptionColumn')]//a"));
    }

    public RnvTaskWizardPage2 nextRnVstep() {
        clickAndWaitForDisplaying(nextBtn, By.cssSelector("span[id*='second-step_header']"));
        return at(RnvTaskWizardPage2.class);
    }

    public boolean isAgrDisplaysInTheList(String claimLine, String agrName) {
        String xpathAgreements = agrByCLNameXpath.replace("$1", claimLine);
        driver.findElement(By.xpath(xpathAgreements)).click();
        String xpathAgrName = agrXpath.replace("$1", agrName);
        boolean result  = isElementPresent(find(By.xpath(xpathAgrName)));
        driver.findElement((By.xpath(xpathAgreements))).click();
        nameField.click();
        return result;
    }

    public boolean isAttachmentIconNotDisplays(String claimLineName) {
        String attachmentIconXpath = attachIconByCLNameXpath.replace("$1", claimLineName);
        WebElement item = find(By.xpath(attachmentIconXpath));
        return item.getAttribute("CLASS").contains("hide");
    }

    public void openAttachmentPopup(String claimLineName) {
        String attachmentIconXpath = attachIconByCLNameXpath.replace("$1", claimLineName);
        WebElement item = find(By.xpath(attachmentIconXpath));
        clickAndWaitForDisplaying(item, By.cssSelector("div#attachmentsListWindow"));
    }

    public String getTopAttachmentName() {
        return getText(topAttachment);
    }

    public boolean isActionMenuNotActive() {
        return actionMenu.getAttribute("Class").contains("disabled");
    }

    public void selectCL(String claimLineName) {
        String clCheckbox = checkboxByCLNameXpath.replace("$1", claimLineName);
        find(By.xpath(clCheckbox)).click();
    }

    public void clickActions() {
        actionMenu.click();
    }

    public boolean isDeleteOptionAvailable() {
        return deleteAction.isDisplayed();
    }

    public boolean isBulkReassignOptionAvailable() {
        return bulkReassingAction.isDisplayed();
    }

    public void deleteCL(String claimLineName) {
        selectCL(claimLineName);
        clickActions();
        deleteAction.click();
        Wait.waitForAjaxCompleted();
    }


    public boolean isNextBtnDisabled() {
        return find(By.cssSelector("a#button-next")).getAttribute("Class").contains("disabled");
    }

    public boolean isSendBtnDisabled() {
        return find(By.cssSelector("a#button-send")).getAttribute("Class").contains("disabled");
    }

    public boolean isBackBtnDisabled() {
        return find(By.cssSelector("a#button-back")).getAttribute("Class").contains("disabled");
    }

    public boolean isCloseBtnDisabled() {
        return find(By.cssSelector("a#button-close")).getAttribute("Class").contains("disabled");
    }

    public boolean isCLPresent(String claimLineName) {
        return isElementPresent(By.xpath(byCLNameXpath.replace("$1", claimLineName)));
    }

    public void changeAgrForAllLines(String agreementName) {
        selectAllCLines();
        actionMenu.click();
        selectBulkReassignOption();
        selectAgrForBulkUpdate(agreementName);
        selectUpdate();
    }

    public void selectUpdate() {
        bulkUpdateBtn.click();
    }

    public void selectBulkReassignOption() {
        clickAndWaitForDisplaying(bulkReassingAction, By.cssSelector("div#agreementAndTaskTypeSelectionDialog"));
    }

    public void selectAgrForBulkUpdate(String agreementName) {
        find(By.id("agreementsCombo-inputEl")).click();
        find(By.xpath(("//ul/li[contains(text(),'$1')]").replace("$1", agreementName))).click();
    }

    public void selectAllCLines() {
        allLinesCheckbox.click();
    }

    public void selectTaskForBulkUpdate(String taskType) {
        clickAndWaitForStable(taskForBulkUpdateField, By.xpath("//ul/li"));
        find(By.xpath(("//ul/li[contains(text(),'$1')]").replace("$1", taskType))).click();
    }


    public void enableAgrForBulkUpdateCheckbox() {
        WebElement item = find(By.cssSelector("table#updateTypeAndAgreementCheckBox"));
        if (!item.getAttribute("Class").contains("checked")) {
            agrCheckboxForBulkUpdate.click();
        }
    }

    public void selectLocForBulkUpdate(String loc) {
        clickAndWaitForStable(locForBulkUpdateField, By.cssSelector("ul li"));
        find(By.xpath(("//ul/li[contains(.,'$1')]").replace("$1", loc))).click();
    }

    public String getTaskByCL(String clName) {
        return getText(By.xpath(taskTypeFieldByCLNameXpath.replace("$1", clName)));
    }

    public String getAgrByCL(String clName) {
        return getText(By.xpath(agrByCLNameXpath.replace("$1", clName)));
    }

    public String getLocByCL(String clName) {
        return getText(By.xpath(locByCLXpath.replace("$1", clName)));
    }

    public boolean isCrossDisplays() {
        return crossImg.isDisplayed();
    }

    public boolean isAgrInfoIconDisplays(String clName) {
        WebElement item = find(By.xpath(agrInfoIconByCLName.replace("$1", clName)));
        return item.isDisplayed();
    }

    public String getAgrInfoText(String clName) {
        WebElement item = find(By.xpath(agrInfoIconByCLName.replace("$1", clName)));
        String text = item.getAttribute("data-qtip");
        text = text.replace("<b>", "");
        text = text.replace("</b>", " ");
        text = text.replace("<br>", " ");
        return text;
    }

    public void addPostalCode(String postalCode) {
        clear(postalCodeField);
        postalCodeField.sendKeys(postalCode);
        nameField.click();
        clickAndWaitForEnabling(nameField, By.id("button-next"));
    }


    public RnvTaskWizardPage1 updateTaskTypeAndAgrForAllLines(String type, String agreementName) {
        selectAllCLines();
        clickActions();
        selectBulkReassignOption();
        selectTaskForBulkUpdate(type);
        selectAgrForBulkUpdate(agreementName);
        selectUpdate();
        return this;
    }
}
