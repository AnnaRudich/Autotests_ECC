package com.scalepoint.automation.pageobjects.pages.rnv1;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.RVPage;
import com.scalepoint.automation.utils.data.entity.Claim;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

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

    @FindBy(css = "#button-next")
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
        $(postalCodeField).click();
        Wait.waitForAjaxCompleted();
        String xpathTask = taskTypeFieldByCLNameXpath.replace("$1", claimLineDescription);
        clickAndWaitForDisplaying(By.xpath(xpathTask), By.cssSelector("ul.x-list-plain"));
        String xpathTaskType = tasksXpath.replace("$1", taskType);
        $(By.xpath(xpathTaskType)).click();
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
        boolean result = isElementPresent(find(By.xpath(xpathAgrName)));
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

    public RnvTaskWizardPage1 selectRnvType(String lineDescription, String rnvType){
        SelenideElement column = new ServiceLinesRows().getRowByDescription(lineDescription).get("Opgavetype");
        column.click();
        column.findAll(By.xpath("//div[contains(@class, 'x-boundlist-list-ct')]/ul/li")).findBy(text(rnvType)).click();
        return this;
    }



    public RnvTaskWizardPage1 selectDamageType(String lineDescription, String damageType){
        SelenideElement column = new ServiceLinesRows().getRowByDescription(lineDescription).get("Skadetype");
        column.click();
        column.findAll(By.xpath("//div[contains(@class, 'x-boundlist-list-ct')]/ul/li")).findBy(text(damageType)).click();
        return this;
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


    class ServiceLinesHeaders {

        List<String> columnNames;
        ElementsCollection columnHeaderElements;

        public ServiceLinesHeaders(){
            this.columnHeaderElements = getColumnHeaderElements();
            this.columnNames = getColumnNames(columnHeaderElements);
        }

        public List<String> getColumnNames(ElementsCollection tableHeadersElements){

            List<String> columnNames = new ArrayList<>();
            for(WebElement tableHeaderElement: tableHeadersElements){
                columnNames.add(tableHeaderElement.getText());
            }
            return columnNames;
        }

        public ElementsCollection getColumnHeaderElements(){
            return $$(By.xpath("//span[following-sibling::div[contains(@class, 'x-column-header-trigger')]]"));
        }
    }

    class ServiceLinesRows {

        List<Map<String, SelenideElement>> serviceLinesList;

        public ServiceLinesRows(){
            List<String> columnHeadersTexts = new ServiceLinesHeaders().columnNames;
            this.serviceLinesList = collectLinesData(columnHeadersTexts);
        }

        private List<Map<String, SelenideElement>> collectLinesData(List<String> columnNames){
            List<Map<String, SelenideElement>> serviceLines = new ArrayList<>();

           ElementsCollection rows = $$(By.xpath("//div[contains(@id, 'serviceLineListId-body')]//tr"));

            for(SelenideElement row: rows){
                ElementsCollection rowCells =
                        row.findAll(By.xpath("(//div[contains(@class, 'x-grid-cell-inner')][not(contains(@class, 'x-grid-cell-inner-action-col'))])[position()>1]"));
                serviceLines.add(mapCellsToHeaders(columnNames, rowCells));
            } return serviceLines;
        }

        private Map<String,SelenideElement> mapCellsToHeaders(List<String> columnNames, ElementsCollection rowCells){
            Map<String, SelenideElement> lines = new HashMap<>();
            for (int i = 0; i< columnNames.size(); i++){
                lines.put(columnNames.get(i), rowCells.get(i));
            }return lines;
        }

        private Map<String, SelenideElement> getRowByDescription(String lineDescription) {

            Map<String, SelenideElement> row = new HashMap<>();

            for (int i = 0; i < serviceLinesList.size(); i++) {
                if (serviceLinesList.get(i).get("Beskrivelse").getText().equals(lineDescription)) {
                    row = serviceLinesList.get(i);
                }
            } return row;
        }


       public String getCellValue(int lineIndex, String columnName){
            return serviceLinesList.get(lineIndex).get(columnName).getText();
        }

        public String getCellValue(String lineDescription, String columnName) {
            Map<String,SelenideElement> row = getRowByDescription(lineDescription);
            return row.get(columnName).getText();
        }


        public void selectCell(String columnName, String lineDescription){
            getRowByDescription(lineDescription).get(columnName).click();
        }

        //method to select a value from drop-down. Should it be here? SET VALUE?
    }
}
