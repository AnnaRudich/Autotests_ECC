package com.scalepoint.automation.pageobjects.pages.selfservice;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.RandomUtils;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import java.util.List;
import java.util.function.Consumer;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.scalepoint.automation.utils.OperationalUtils.unifyStr;
import static com.scalepoint.automation.utils.Wait.*;
import static org.testng.Assert.assertTrue;

public class SelfServicePage extends Page {

    @FindBy(xpath = ".//*[@id='selfService_grid']//div[contains(@class,'x-grid3-body')]//div[contains(@class,'x-grid3-row')]")
    private List<WebElement> selfServiceLines;

    @FindBy(xpath = "//a[contains(@onclick, 'javascript:logout()')]")
    private WebElement logoutOption;

    @FindBy(xpath = "//a[contains(@onclick, 'submitSelfServiceLines()')]")
    private WebElement submitButton;

    @FindBy(xpath = "//a[contains(@onclick, 'closeSelfService')]")
    private WebElement closeButton;

    @FindBy(xpath = "//div[contains(@class,'descriptionColumn')]/div")//use after selfServiceLines.get()
    private WebElement descriptionField;

    @FindBy(id = "cell1_2")
    private WebElement categoryField;

    @FindBy(xpath = "//input[@id='cs_category_group']")
    private WebElement groupInputField;

    @FindBy(xpath = "//div[contains(@id,'cg_item')]")
    private List<WebElement> allGroups;

    @FindBy(xpath = "//input[@id='cs_pseudo_category']")
    private WebElement categoriesInputField;

    @FindBy(xpath = "//div[contains(@id,'cs_cat_item')]")
    private List<WebElement> allCategories;


    @FindBy(id = "//div[contains(@class,'purchaseDate')]/div")
    private WebElement purchaseDateFirstField;

    @FindBy(xpath = "//div[contains(@class,'acquired')]/div")
    private WebElement acquiredField;

    @FindBy(xpath = "//div[contains(@class,'purchasePrice')]/div")
    private WebElement purchasePriceFirstField;

    @FindBy(xpath = "//td[contains(@class,'newPrice')]/div")
    private WebElement newPriceFirstField;

    @FindBy(xpath = "//div[contains(@class,'customerDemand')]/div")
    private WebElement customerDemandFirstField;

    @FindBy(xpath = "//div[contains(@class,'documentation')]/div")
    private WebElement documentationFirstField;

    @FindBy(xpath = ".//input[contains(@class, 'num-field')]")
    private List<WebElement> priceInputs;

    @FindBy(id = "cell1_7")
    private WebElement newPriceFirstSeField;

    @FindBy(xpath = "//*[contains(@id,'cg_item')]")
    private List<WebElement> allCategoriesList;

    @FindBy(xpath = "//img[contains(@class, 'x-form-date-trigger')]")
    private WebElement calendarImage;

    @FindBy(xpath = ".//*[@id='ext-comp-1005']")
    private WebElement calendarPopUp;

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

    @FindBy(xpath = "//div[contains(text(),'[Choose category]')]")
    private WebElement chooseCategoryButton;


    @FindBy(xpath = "//input[@id='agreement']")
    private WebElement shopAgreementCheckBox;

    @FindBy(xpath = "(//div[contains(@class, 'categoryColumn')])[3]/div")
    private WebElement categoryFirstField;


    @Override
    protected Page ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        return this;
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/shop/self_service.jsp";
    }

    public boolean isColumnRequired(String columnXpath) {
        String requiredXpath = columnXpath + "//img[@src]";
        WebElement item = find(By.xpath(requiredXpath));
        return item.getAttribute("src").contains("question_mark.png");
    }

    public void selectSubmitOption() {
        clickAndWaitForDisplaying(submitButton, By.id("menu_id_1"));
    }

    public void ssLogout() {
        logoutOption.click();
    }

    public void unfocusField() {
        driver.findElement(By.xpath("//div[@class='body_text']")).click();
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
        waitForStaleElement(By.xpath("//div[contains(@class, 'x-combo-list-item')]"));
        List<WebElement> suggestionItem = driver.findElements(By.xpath("//div[contains(@class, 'x-combo-list-item')]"));
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

    //IS SELECTED BLOCK
    private boolean isCategorySelected(int lineNumber) {
        String category = getCategoryText(lineNumber);
        if (category != null) {
            return true;
        }
        return false;
    }

    public boolean isPurchaseDateSelected(int lineNumber) {
        String purchaseDate = getPurchaseDate(lineNumber);
        if (purchaseDate != null) {
            return true;
        }
        return false;
    }

    public boolean isNewPriceSelected(int lineNumber) {
        String newPrice = getNewPrice(lineNumber);
        if (newPrice != null) {
            return true;
        }
        return false;
    }


    private boolean isPurchasePriceSelected(int lineNumber) {
        String purchasePrice = getPurchasePrice(lineNumber);
        if (purchasePrice != null) {
            return true;
        }
        return false;
    }

    private boolean isDescriptionSelected(int lineNumber) {
        String description = getDescriptionText(lineNumber);
        if (description != null) {
            return true;
        }
        return false;
    }

    private boolean isCustomerDemandSelected(int lineNumber) {
        String customerDemand = getCustomerDemandPrice(lineNumber);
        if (customerDemand != null) {
            return true;
        }
        return false;
    }

//IS SELECTED BLOCK END

    //Category&Group
    public void selectCategoryField() {
       /*categoryFirstField.click();
        find(By.xpath("(//div[contains(@class, 'categoryColumnHeaderXpath')])[3]/div")).sendKeys(Keys.ENTER); */
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
        clickAndWaitForStable(allCategories.get(n), By.xpath("(//div[contains(@class, 'categoryColumnHeaderXpath')])[3]"));
        Wait.waitForAjaxCompleted();
        unfocusField();
    }

//ADD RANDOM VALUE

    /**
     * The method adds text in the description field, waits for suggestion, selects firs suggestion by clicking DOWN and Enter
     */
    public SelfServicePage addDescriptionSelectFirstSuggestion(String text) {
        descriptionField.sendKeys(text);
        descriptionField.sendKeys(Keys.ARROW_DOWN, Keys.ENTER);
        return this;
    }

    /**
     * The method selects random year and random month for Purchase date. To avoid failure with future years selected randomly, it was decided
     * to use previous Years page
     */
    public SelfServicePage addRandomPurchaseDate() {
        waitForDisplayed(By.xpath("//td[contains(@class, 'purchaseDate')]"));
        //        if (!System.getProperty("locale").equals("DK")) {
//            clickAndWaitForStable(By.xpath("//div[contains(@class,'x-grid3-scroller')]/div/div[2]//td[contains(@class,'x-grid3-td-purchaseDate')]/div/div"), By.xpath("//*[contains(@class, 'date-trigger')]"));
//        }
        clickAndWaitForStable(calendarImage, By.xpath("//img[contains(@class, 'x-form-date-trigger')]"));
        previousYearButton.click();
        WebElement year = allVisibleYears.get(RandomUtils.randomInt(allVisibleYears.size()));
        year.click();
        WebElement month = allVisibleMonths.get(RandomUtils.randomInt(allVisibleMonths.size()));
        month.click();
        calendarOKOption.click();
        unfocusField();
        return this;
    }

    public void addRandomCategory() {
        doubleClick(categoryField);
        waitForStaleElements(By.xpath("//*[contains(@id,'cg_item')]"));
        WebElement category = allCategoriesList.get(RandomUtils.randomInt(allCategoriesList.size()));
        scrollTo(category);
        category.click();
    }

    public SelfServicePage addRandomAcquired(int lineNumber) {
        SelenideElement acquiredArrowTrigger = $$(By.xpath("//*[contains(@class, 'arrow-trigger')]")).get(1);

        findTheFieldInSsGrid("acquired", lineNumber).click();
        acquiredArrowTrigger.click();
        acquiredArrowTrigger.sendKeys(Keys.ARROW_DOWN, Keys.ENTER);
        return this;
    }

//ADD BLOCK

    public SelfServicePage addDescription(String text, int lineNumber) {
        findTheFieldInSsGrid("descriptionColumn", lineNumber);
        findField("descriptionColumn").pressEnter();
        setValueToTheInput(text);
//        sendKeys(descriptionField, text);
//        waitForStaleElement(By.xpath("(//div[contains(@class, 'categoryColumnHeaderXpath')])[3]/div"));
//        find(By.xpath("//input[contains(@class, 'focus')]")).sendKeys(Keys.TAB);
//        //Wait.waitForStaleElement(By.xpath("//div[contains(text(),'[Choose category]')]"));
//        waitForStaleElement(By.cssSelector("div#cell1_2"));
        return this;
    }
    /**
     * The method selects specified year and month for Purchase date
     */
    public SelfServicePage addPurchaseDate(String year, int month) {
        waitForDisplayed(By.xpath("//td[contains(@class, 'purchaseDate')]"));
        clickAndWaitForStable(calendarImage, By.xpath("//img[contains(@class, 'x-form-date-trigger')]"));
        find("//td[@class='x-date-mp-month']/a[@month='$']", month).click();
        find("//td[@class='x-date-mp-year']/a[contains(text(),'$1')]", year).click();
        clickAndWaitForStable(calendarOKOption, By.xpath("//*[contains(@class,'cell-selected')]"));
        unfocusField();
        return this;
    }

    public SelfServicePage addCategory(Integer group, Integer category) {
        selectCategoryField();
        selectGroupInput();
        selectGroup(group);
        selectCategoryInput();
        selectCategory(category);
        return this;
    }

    //find the field section
    public SelenideElement findTheFieldInSsGrid(String fieldName, int lineNumber) {
        ElementsCollection selfServiceLines = $$(By.xpath(".//*[@id='selfService_grid']//div[contains(@class,'x-grid3-body')]//div[contains(@class,'x-grid3-row')]"));
        return selfServiceLines.get(lineNumber).$(By.xpath(".//div[contains(@class,'" + fieldName + "')]/div"));
    }

    public SelenideElement findField(String fieldName) {
        return $(By.xpath(".//div[contains(@class,'" + fieldName + "')]/div"));
    }

    public void setValueToTheInput(String text) {
        $(By.xpath("//input[contains(@class,'form-focus')]")).setValue(text).pressEnter();
    }


    public SelfServicePage addNewPrice(String text, int lineNumber) {
        findTheFieldInSsGrid("newPrice", lineNumber).click();
        findField("newPrice").pressEnter();
        setValueToTheInput(text);
        return this;
    }

    public SelfServicePage addPurchasePrice(String text, int lineNumber) {
        findTheFieldInSsGrid("purchasePrice", lineNumber).click();
        findField("purchasePrice").pressEnter();
        setValueToTheInput(text);
        return this;
    }

    public SelfServicePage addCustomerDemandPrice(String text, int lineNumber) {
        findTheFieldInSsGrid("customerDemand", lineNumber).click();
        findField("customerDemand").pressEnter();
        setValueToTheInput(text);
        return this;
    }


    public SelfServicePage addDocumentationSetting(int lineNumber, boolean hasDocumentation) {
        SelenideElement documentationArrowTrigger = $$(By.xpath("//img[contains(@class, 'arrow-trigger')]")).get(2);

        findTheFieldInSsGrid("documentation", lineNumber).click();
        documentationArrowTrigger.click();

        if (hasDocumentation) {
            documentationArrowTrigger.sendKeys(Keys.ENTER);
        } else {
            documentationArrowTrigger.sendKeys(Keys.ARROW_DOWN, Keys.ENTER);
        }
        return this;
    }

    public SelfServicePage uploadDocument(boolean hasDocumentation, ClaimItem claimItem) {
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
     * The method waits for file upload is completed
     */
    public void waitForUploadCompleted() {
        waitForDisplayed(By.xpath("//div[contains(text(),'100 %')]"));
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


    public String getDescriptionText(int lineNumber) {
        findTheFieldInSsGrid("descriptionColumn", lineNumber).click();
        return findField("descriptionColumn").getText();
    }

    public String getPurchasePrice(int lineNumber) {
        findTheFieldInSsGrid("purchasePrice", lineNumber).click();
        return findField("purchasePrice").getText();
    }

    public String getPurchaseDate(int lineNumber) {
        findTheFieldInSsGrid("purchaseDate", lineNumber).click();
        return findField("purchaseDate").getText();
    }

    public String getNewPrice(int lineNumber) {
        findTheFieldInSsGrid("newPrice", lineNumber).click();
        return findField("newPrice").getText();
    }

    public String getCategoryText(int lineNumber) {
        findTheFieldInSsGrid("categoryColumn", lineNumber).click();
        return findField("categoryColumn").getText();
    }

    public String getCustomerDemandPrice(int lineNumber) {
        findTheFieldInSsGrid("customerDemand", lineNumber).click();
        return findField("customerDemand").getText();
    }


    //CLEAR AND DELETE
    public void clearNewPriceValue(int lineNumber) {
        clear(findTheFieldInSsGrid("newPrice", lineNumber));
    }

    public void clearPurchasePriceField(int lineNumber) {
        clear(findTheFieldInSsGrid("purchasePrice", lineNumber));
    }

    public void deleteLine(int lineNumber) {
        findTheFieldInSsGrid("delete-icon", lineNumber).click();
    }


    public SelfServicePage doAssert(Consumer<Asserts> assertFunc) {
        assertFunc.accept(new Asserts());
        return this;
    }

    public class Asserts {
        public Asserts assertRequiredFieldIconPresent(String columnXpath) {
            assertTrue(isColumnRequired(columnXpath), "Required field icon should be displayed");
            return this;
        }

        public Asserts assertDescriptionColumnIsMarkedAsRequired() {
            assertRequiredFieldIconPresent("//td[contains(@class, 'descriptionColumn')]");
            return this;
        }

        public Asserts assertCategoryColumnIsMarkedAsRequired() {
            assertRequiredFieldIconPresent("//td[contains(@class, 'categoryColumn')]");
            return this;
        }

        public Asserts assertPurchaseDateColumnIsMarkedAsRequired() {
            assertRequiredFieldIconPresent("//td[contains(@class, 'purchaseDate')]");
            return this;
        }

        public Asserts assertPurchasePriceColumnIsMarkedAsRequired() {
            assertRequiredFieldIconPresent("//td[contains(@class, 'purchasePrice')]");
            return this;
        }

        public Asserts assertNewPriceColumnIsMarkedAsRequired() {
            assertRequiredFieldIconPresent("//td[contains(@class, 'newPrice')]");
            return this;
        }

        public Asserts assertDocumentationOrNoteIsMarkedAsRequired() {
            assertRequiredFieldIconPresent("//td[contains(@class, 'documentation')]");
            return this;
        }

        public Asserts assertPurchaseDateIsNotEmpty(int lineNumber) {
            assertTrue(isPurchaseDateSelected(lineNumber), "Purchase Date should be selected");
            return this;
        }

        public Asserts assertNewPriceIsNotEmpty(int lineNumber) {
            assertTrue(isNewPriceSelected(lineNumber), "New Price should be selected");
            return this;
        }

        public Asserts assertPurchasePriceIsNotEmpty(int lineNumber) {
            assertTrue(isPurchasePriceSelected(lineNumber), "Purchase Price should be selected");
            return this;
        }

        public Asserts assertDescriptionIsNotEmpty(int lineNumber) {
            assertTrue(isDescriptionSelected(lineNumber), "Description should be selected");
            return this;
        }

        public Asserts assertCategoryIsNotEmpty(int lineNumber) {
            assertTrue(isCategorySelected(lineNumber), "Category should be selected");
            return this;
        }

        public Asserts assertCustomerDemandIsNotEmpty(int lineNumber) {
            assertTrue(isCustomerDemandSelected(lineNumber), "Customer demand should be selected");
            return this;
        }
    }
}








