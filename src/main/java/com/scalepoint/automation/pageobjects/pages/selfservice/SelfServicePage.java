package com.scalepoint.automation.pageobjects.pages.selfservice;

import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.RandomUtils;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

import static com.scalepoint.automation.utils.OperationalUtils.unifyStr;
import static com.scalepoint.automation.utils.Wait.*;

public class SelfServicePage extends Page {

    @FindBy(xpath = "//a[contains(@onclick, 'javascript:logout()')]")
    private WebElement logoutOption;

    @FindBy(xpath = "//a[contains(@onclick, 'submitSelfServiceLines()')]")
    private WebElement submitButton;

    @FindBy(xpath = "//a[contains(@onclick, 'closeSelfService')]")
    private WebElement closeButton;

    @FindBy(id = "cell1_1")
    private WebElement descriptionFirstField;

    @FindBy(xpath = "(//div[contains(@class, 'categoryColumn')])[3]/div")
    private WebElement categoryFirstField;

    @FindBy(xpath = "//input[@id='cs_category_group']")
    private WebElement groupInputField;

    @FindBy(xpath = "//div[contains(@id,'cg_item')]")
    private List<WebElement> allGroups;

    @FindBy(xpath = "//input[@id='cs_pseudo_category']")
    private WebElement categoriesInputField;

    @FindBy(xpath = "//div[contains(@id,'cs_cat_item')]")
    private List<WebElement> allCategories;

    @FindBy(xpath = "//div[4]/div/div/table/tbody/tr/td/div/img")
    private WebElement chooseCategoryField;

    @FindBy(xpath = "//div[@id='cell1_4']")
    private WebElement purDateFirstField;

    @FindBy(xpath = "//div[@id='cell1_6']")
    private WebElement purPriceFirstField;

    @FindBy(xpath = "//div[@id='cell1_7']")
    private WebElement purPriceFirstSeField;

    @FindBy(xpath = "//input[contains(@class, 'x-form-num-field')]")
    private List<WebElement> priceInputs;

    @FindBy(xpath = "//div[@id='cell1_7']")
    private WebElement newPriceField;

    @FindBy(xpath = "//div[@id='cell1_8']")
    private WebElement newPriceSeField;

    @FindBy(xpath = "//*[contains(@id,'cg_item')]")
    private List<WebElement> allCategoriesList;

    @FindBy(xpath = "//img[contains(@class, 'x-form-date-trigger')]")
    private WebElement calendarImage;

    @FindBy(xpath = "//button[@class='x-date-mp-ok']")
    private WebElement calendarOKOption;

    @FindBy(xpath = "//*[contains(@class,'date-mp-year')]")
    private List<WebElement> allVisibleYears;

    @FindBy(xpath = "//*[contains(@class,'date-mp-month')]")
    private List<WebElement> allVisibleMonths;

    @FindBy(xpath = "//*[contains(@class,'date-mp-prev')]")
    private WebElement previousYearButton;

    @FindBy(xpath = "//input[contains(@class, 'x-form-focus')]")
    private WebElement documentInput;

    @FindBy(xpath = "//div[@class='x-combo-list-inner']/div[contains(@class, 'x-combo-list-item')]")
    private List<WebElement> documentSelects;

    @FindBy(id = "uploadokbutton")
    private WebElement uploadOKButton;

    @FindBy(xpath = "//input[@class='x-form-file']")
    private WebElement file;

    @FindBy(xpath = "//*[contains(@class, 'x-combo-list-item')]")
    private List<WebElement> allDescriptionSuggestions;

    @FindBy(id = "customer_comment")
    private WebElement commonCommentsField;

    @FindBy(xpath = "//a[contains(@href, 'removeRecord')]")
    private WebElement deleteLineIcon;

    @FindBy(xpath = "//div[@id='cell1_9']")
    private WebElement documentationFirstField;

    @FindBy(id = "ext-comp-1009")
    private WebElement purPriceField;

    @FindBy(id = "ext-comp-1010")
    private WebElement newPriceValueField;

    @FindBy(xpath = "//div[contains(text(),'[Choose category]')]")
    private WebElement chooseCategoryButton;

    @FindBy(xpath = "//input[@id='agreement']")
    private WebElement shopAgreementCheckBox;

    private String descriptionColumnXpath = "//td[contains(@class, 'descriptionColumn')]";
    private String categoryColumnXpath = "//td[contains(@class, 'categoryColumn')]";
    private String purchaseDateColumnXpath = "//td[contains(@class, 'purchaseDate')]";
    private String purchasePriceColumnXpath = "//td[contains(@class, 'purchasePrice')]";
    private String newPriceColumnXpath = "//td[contains(@class, 'newPrice')]";
    private String descSuggestionsXpath = "//div[contains(@class, 'x-combo-list-item')]";

    @Override
    protected Page ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        return this;
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/shop/self_service.jsp";
    }

    public boolean isDescriptionRequired() {
        return isColumnRequired(descriptionColumnXpath);
    }

    public boolean isCategoryRequired() {
        return isColumnRequired(categoryColumnXpath);
    }

    public boolean isPurchaseDateRequired() {
        return isColumnRequired(purchaseDateColumnXpath);
    }

    public boolean isPurchasePriceRequired() {
        return isColumnRequired(purchasePriceColumnXpath);
    }

    public boolean isNewPriceRequired() {
        return isColumnRequired(newPriceColumnXpath);
    }

    public boolean isColumnRequired(String columnXpath) {
        String requiredXpath = columnXpath + "//tr[2]/td/span";
        WebElement item = find(By.xpath(requiredXpath));
        return item.getAttribute("CLASS").contains("required");
    }

    public void ssLogout() {
        logoutOption.click();
    }

    public SelfServicePage addDescription(String text) {
        sendKeys(descriptionFirstField, text);
        waitForStaleElement(By.xpath("(//div[contains(@class, 'categoryColumn')])[3]/div"));
        find(By.xpath("//input[contains(@class, 'focus')]")).sendKeys(Keys.TAB);
        //Wait.waitForStaleElement(By.xpath("//div[contains(text(),'[Choose category]')]"));
        waitForStaleElement(By.cssSelector("div#cell1_2"));
        return this;
    }

    public void justAddDescription(String text) {
        sendKeys(descriptionFirstField, text);
        waitForStaleElements(By.xpath("//*[contains(@class, 'x-combo-list-item')]"));
    }

    public boolean isSuggestionsContainQuery(String query) {
        waitForAjaxCompleted();
        for (WebElement suggestion : allDescriptionSuggestions) {
            scrollTo(suggestion);
            System.out.println("Query: " + unifyStr(query) + "present in " + unifyStr(suggestion.getText()) + "?");
            if (!unifyStr(suggestion.getText()).contains(unifyStr(query))) {
                System.out.println(suggestion.getText().toUpperCase() + "- doesn't contain " + unifyStr(query).toUpperCase());
                return false;
            }
        }
        return true;
    }

    public boolean isFirst10SuggestionContainQuery(String query) {
        String[] queryList = query.split(" ");
        waitForStaleElement(By.xpath(descSuggestionsXpath));
        List<WebElement> suggestionItem = driver.findElements(By.xpath(descSuggestionsXpath));
        for (int i = 0; i < 10; i++) {
            System.out.println("Query: " + unifyStr(query).toUpperCase() + " present in " + unifyStr(suggestionItem.get(i).getText()) + "?");
            for (String aQueryList : queryList) {
                if (!unifyStr(suggestionItem.get(i).getText()).contains(unifyStr(aQueryList))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * The method waits for file upload is completed
     */
    public void waitForUploadCompleted() {
        waitForDisplayed(By.xpath("//div[contains(text(),'100 %')]"));
    }

    /**
     * The method adds text in the description field, waits for suggestion, selects firs suggestion by clicking DOWN and Enter
     */
    public void addDescriptionSelectFirstSuggestion(String text) {
        descriptionFirstField.sendKeys(text);
        descriptionFirstField.sendKeys(Keys.ARROW_DOWN, Keys.ENTER);
    }

    /**
     * The method selects random year and random month for Purchase date. To avoid failure with future years selected randomly, it was decided
     * to use previous Years page
     */
    public SelfServicePage addPurchaseDate() {
        Wait.waitForDisplayed(By.xpath("//div[@id='cell1_4']"));
        purDateFirstField.click();
        if (!System.getProperty("locale").equals("DK")) {
            clickAndWaitForStable(By.xpath("//div[contains(@class,'x-grid3-scroller')]/div/div[2]//td[contains(@class,'x-grid3-td-purchaseDate')]/div/div"), By.xpath("//*[contains(@class, 'date-trigger')]"));
        }
        waitForDisplayed(By.xpath("//img[contains(@class, 'x-form-date-trigger')]"));
        calendarImage.click();
        previousYearButton.click();
        WebElement year = allVisibleYears.get(RandomUtils.randomInt(allVisibleYears.size()));
        year.click();
        WebElement month = allVisibleMonths.get(RandomUtils.randomInt(allVisibleMonths.size()));
        month.click();
        calendarOKOption.click();
        unfocusField();
        return this;
    }

    /**
     * The method selects random year and random month for Purchase date. To avoid failure with future years selected randomly, it was decided
     * to use previous Years page
     */
    public void addPurchaseDate(String year, int month) {
        purDateFirstField.click();
        if (!System.getProperty("locale").equals("DK")) {
            clickAndWaitForStable(By.xpath("//div[contains(@class,'x-grid3-scroller')]/div/div[2]//td[contains(@class,'x-grid3-td-purchaseDate')]/div/div"), By.xpath("//*[contains(@class, 'date-trigger')]"));
        }
        clickAndWaitForStable(calendarImage, By.xpath("//*[contains(@class,'date-mp-ok')]"));
        find("//td[@class='x-date-mp-month']/a[@month='$1']", Integer.toString(month)).click();
        find("//td[@class='x-date-mp-year']/a[contains(text(), '$1')]", year).click();
        clickAndWaitForStable(calendarOKOption, By.xpath("//*[contains(@class,'cell-selected')]"));
        unfocusField();
    }

    public void unfocusField() {
        driver.findElement(By.xpath("//div[@class='body_text']")).click();
    }

    public void selectCategoryField() {
       /*categoryFirstField.click();
        find(By.xpath("(//div[contains(@class, 'categoryColumn')])[3]/div")).sendKeys(Keys.ENTER); */
        clickAndWaitForDisplaying(categoryFirstField, By.xpath("//input[@id='cs_category_group']"));
    }

    public void selectGroupInput() {
        groupInputField.sendKeys(Keys.DOWN);
        waitForStaleElements(By.xpath("//div[contains(@id,'cg_item')]"));
    }

    public void selectGroup(Integer n) {
        clickAndWaitForStable(allGroups.get(n), By.xpath("//input[@id='cs_pseudo_category']"));

    }

    public void selectCategoryInput() {
        groupInputField.sendKeys(Keys.TAB);
        waitForStaleElement(By.xpath("//input[@id='cs_pseudo_category']"));
        categoriesInputField.sendKeys(Keys.DOWN);
        waitForStaleElements(By.xpath("//div[contains(@id,'cs_cat_item')]"));
    }

    public void selectCategory(Integer n) {
        clickAndWaitForStable(allCategories.get(n), By.xpath("(//div[contains(@class, 'categoryColumn')])[3]"));
        Wait.waitForAjaxCompleted();
        unfocusField();
    }

    public SelfServicePage addCategory(Integer group, Integer category) {
        selectCategoryField();
        selectGroupInput();
        selectGroup(group);
        selectCategoryInput();
        selectCategory(category);
        return this;
    }

    public void addRandomCategory() {
        doubleClick(chooseCategoryField);
        waitForStaleElements(By.xpath("//*[contains(@id,'cg_item')]"));
        WebElement category = allCategoriesList.get(RandomUtils.randomInt(allCategoriesList.size()));
        scrollTo(category);
        category.click();
    }

    public SelfServicePage addPurchasePrice(String text) {
        purPriceFirstField.click();
        Wait.waitForDisplayed(By.xpath("//input[contains(@class, 'x-form-num-field')]"));
        setValue(priceInputs.get(0), text);
        return this;
    }

    public SelfServicePage addNewPrice(String text) {
        newPriceField.click();
        setValue(priceInputs.get(1), text);
        return this;
    }

    public SelfServicePage uploadDocument(boolean hasDocumentation, ClaimItem claimItem) {
        documentationFirstField.click();
        documentInput.click();
        String yes = claimItem.getYesOption();
        String no = claimItem.getNoOption();

        for (WebElement select : documentSelects) {
            if (select.getText().contains(hasDocumentation ? yes : no)) {
                select.click();
                break;
            }

        }
        if (hasDocumentation) {
            waitForDisplayed(By.name("isInternal"));
            file.sendKeys(claimItem.getFileLoc());
            waitForUploadCompleted();
            uploadOKButton.click();
        }
        return this;
    }

    /**
     * This method adds comment to the claim line
     */
    public void addLineNote(String text) {
        Wait.waitForAjaxCompleted();
        find(By.xpath("//*[contains(@class,'cell-selected')]")).sendKeys(Keys.TAB);
        clickAndWaitForDisplaying(By.xpath("//a[contains(@href, 'showCustomerNoteDialog')]"), By.id("cutomer_note"));
        sendKeys(By.id("cutomer_note"), text);
        clickAndWaitForDisplaying(By.id("ok"), By.xpath("//*[@id='bd']//td[2]/a"));
    }

    /**
     * This method adds common comment in the page bottom
     */
    public void addCommonComment(String text) {
        sendKeys(commonCommentsField, text);
        /*commonCommentsField.sendKeys(Keys.ENTER);
        Wait.waitForAjaxCompleted();  */
        commonCommentsField.sendKeys(Keys.TAB);
    }

    /**
     * The method selects Submit option
     */
    public void selectSubmitOption() {
        clickAndWaitForDisplaying(submitButton, By.id("menu_id_1"));
    }

    /**
     * The method return description text
     */
    public String getDescriptionText() {
        return getText(descriptionFirstField);
    }

    /**
     * The method return purchase date (age)
     */
    public String getAge() {
        return getText(purDateFirstField);
    }

    /**
     * The method return purchase price
     */
    public String getPurchasePrice() {
        return getText(purPriceFirstField);
    }

    /**
     * The method return new price(price of the new item)
     */
    public String getNewPrice() {
        return getText(newPriceField);
    }

    public void clickOnInactiveDescription() {
        descriptionFirstField.click();
    }

    public void clickOnInactiveCategory() {
        categoryFirstField.click();
    }

    public void clickOnInactivePurchaseDate() {
        purDateFirstField.click();
    }

    public void clickOnInactivePurchasePrice() {
        purPriceFirstField.click();
    }

    public void clickOnInactiveNewPrice() {
        newPriceValueField.click();
    }

    public void clickOnInactiveDocumentation() {
        documentationFirstField.click();
    }

    public void clearNewPriceValue() {
        clear(newPriceValueField);
    }

    public void clearPurchasePriceField() {
        clear(purPriceField);
    }


    public void deleteFirstLine() {
        deleteLineIcon.click();
    }

    public void addManualLineFromSuggestionsByText(String descriptionText, ClaimItem claimItem) {
        addDescriptionSelectFirstSuggestion(descriptionText);
        clickOnInactivePurchaseDate();
        addPurchaseDate();
        addPurchasePrice("10000");
        addNewPrice("20000");
        uploadDocument(false, claimItem);
    }

    public String getPageText() {
        return getText(By.cssSelector("div.body_text"));
    }

    public String getFirstLineCategoryText() {
        return getText(categoryFirstField);
    }
}
