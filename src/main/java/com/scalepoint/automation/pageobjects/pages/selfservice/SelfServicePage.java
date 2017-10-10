package com.scalepoint.automation.pageobjects.pages.selfservice;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.data.TestData;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.refresh;
import static com.scalepoint.automation.utils.OperationalUtils.unifyStr;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompleted;
import static com.scalepoint.automation.utils.Wait.waitForDisplayed;
import static com.scalepoint.automation.utils.Wait.waitForStaleElement;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class SelfServicePage extends Page {

    @FindBy(xpath = "//a[contains(@onclick, 'javascript:logout()')]")
    private WebElement logoutOption;

    @FindBy(xpath = "//img[contains(@class, 'x-form-date-trigger')]")
    private WebElement calendarImage;

    @FindBy(xpath = "//button[@class='x-date-mp-ok']")
    private WebElement calendarOKOption;

    @FindBy(xpath = "//*[contains(@class, 'x-combo-list-item')]")
    private List<WebElement> allDescriptionSuggestions;

    protected Page ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        return this;
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/shop/self_service.jsp";
    }


    public SelenideElement findTheFieldInSsGrid(String fieldName, int lineNumber) {
        ElementsCollection selfServiceLines = $$(By.xpath(".//*[@id='selfService_grid']//div[contains(@class,'x-grid3-body')]//div[contains(@class,'x-grid3-row')]"));
        return selfServiceLines.get(lineNumber).$(By.xpath(".//div[contains(@class,'" + fieldName + "')]/div"));
    }

    public SelenideElement findField(String fieldName) {
        return $(By.xpath(".//div[contains(@class,'" + fieldName + "')and not(contains(@class, 'sample-text'))]/div"));
    }

    public void setValueToTheInput(String text) {
        $(By.xpath("//input[contains(@class,'form-focus')]")).setValue(text).pressEnter();
    }

    public SelfServicePage reloadPage() {
        refresh();
        return this;
    }


    public void selectSubmitOption() {
        $(By.xpath("//a[contains(@onclick, 'submitSelfServiceLines()')]")).click();
    }

    //TODO
    public void selectCloseOption() {
        $(By.xpath("//a[contains(@onclick, 'closeSelfService()')]")).shouldBe(visible).click();
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

    /**
     * The method adds text in the description field, waits for suggestion, selects firs suggestion by clicking DOWN and Enter
     */
    public SelfServicePage addDescriptionSelectFirstSuggestion(String text) {
        findField("descriptionColumn").sendKeys(text);
        findField("descriptionColumn").sendKeys(Keys.ARROW_DOWN, Keys.ENTER);
        return this;
    }

    public SelfServicePage addRandomCategory() {
        findField("categoryColumn").click();
        findField("categoryColumn").sendKeys(Keys.ARROW_DOWN);

        List<SelenideElement> categoryGroupList = $$(By.xpath("//div[contains(@id, 'cg_item')]"));
        getRandomElement(categoryGroupList).click();
        pressKeys(Keys.ENTER);
        return this;
    }

    /**
     * The method selects random year and random month for Purchase date. The date is not shown, but it presents in DOM.
     */
    public SelfServicePage addRandomPurchaseDate(int lineNumber) {
        findTheFieldInSsGrid("purchaseDate", lineNumber).click();
        $(By.xpath("//img[contains(@class, 'x-form-date-trigger')]")).click();

        List<SelenideElement> monthList = $$(By.xpath("//*[contains(@class,'date-mp-month')]/a"));
        getRandomElement(monthList).click();
        List<SelenideElement> yearList = $$(By.xpath("//*[contains(@class,'date-mp-year')]/a"));
        getRandomElement(yearList).click();

        $(By.xpath("//button[@class='x-date-mp-ok']")).click();
        return this;
    }

    //looks like has to be moved to other class
    public SelenideElement getRandomElement(List<SelenideElement> list) {
        int index = new Random().nextInt(list.size() - 1);
        return list.get(index);
    }

    public SelfServicePage addRandomAcquired(int lineNumber) {
        SelenideElement acquiredArrowTrigger = $$(By.xpath("//*[contains(@class, 'arrow-trigger')]")).get(1);

        findTheFieldInSsGrid("acquired", lineNumber).click();
        acquiredArrowTrigger.click();
        acquiredArrowTrigger.sendKeys(Keys.ARROW_DOWN, Keys.ENTER);
        return this;
    }

    /**
     * The method adds Description. The field should be in focus
     */
    public SelfServicePage addDescription(String text, int lineNumber) {
        $(By.xpath("//input[contains(@class,'form-focus')]")).setValue(text);
        unfocusField();
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

    public SelfServicePage addCustomerComment(String commentText) {
        $(By.id("customer_comment")).click();
        $(By.id("customer_comment")).sendKeys(commentText);
        unfocusField();
        return this;
    }

    public SelfServicePage addCustomerNote(String noteText) {
        unfocusField();
        clickJS($(By.xpath(".//a[contains(@href, 'showCustomerNoteDialog')]")));
        $("#cutomer_note").sendKeys(noteText);
        $(By.xpath(".//button[.='OK']")).click();
        return this;
    }

    public SelfServicePage uploadDocumentation(int lineNumber, boolean hasDocumentation) {
        SelenideElement documentationArrowTrigger = $$(By.xpath("//img[contains(@class, 'arrow-trigger')]")).get(2);

        findTheFieldInSsGrid("documentation", lineNumber).click();
        documentationArrowTrigger.click();

        if (hasDocumentation) {
            $(By.xpath("//div[contains(@class, 'list-inner')]//div[.='Ja']")).click();
            uploadDocument(TestData.getClaimItem().getFileLoc());
        } else {
            $(By.xpath("//div[contains(@class, 'list-inner')]//div[.='Nej']")).click();
        }
        return this;
    }

    public SelfServicePage uploadDocument(String filePath) {
        $(By.name("Filedata")).sendKeys(filePath);
        waitForUploadCompleted();
        $(By.xpath(".//button[.='Ok']")).click();
        waitForAjaxCompleted();
        return this;
    }

    /**
     * The method waits for file upload is completed
     */
    public void waitForUploadCompleted() {
        waitForDisplayed(By.xpath("//div[contains(text(),'100 %')]"));
    }

    public String getDescriptionText(int lineNumber) {
        return findTheFieldInSsGrid("descriptionColumn", lineNumber).getText();
    }

    public String getPurchasePrice(int lineNumber) {
        return findTheFieldInSsGrid("purchasePrice", lineNumber).getText();
    }

    public String getPurchaseDate(int lineNumber) {
        return findTheFieldInSsGrid("purchaseDate", lineNumber).getText();
    }

    public String getNewPrice(int lineNumber) {
        return findTheFieldInSsGrid("newPrice", lineNumber).getText();
    }

    public String getCategoryText(int lineNumber) {
        return findTheFieldInSsGrid("categoryColumn", lineNumber).getText();
    }

    public String getCustomerDemandPrice(int lineNumber) {
        return findTheFieldInSsGrid("customerDemand", lineNumber).getText();
    }

    public SelfServicePage deleteLine() {
        SelenideElement deleteButton = $(By.xpath(".//div[contains(@class,'delete-icon')]/a"));

        clickJS(deleteButton);
        waitForAjaxCompleted();
        return this;
    }


    public SelfServicePage doAssert(Consumer<Asserts> assertFunc) {
        assertFunc.accept(new Asserts());
        return this;
    }

    public class Asserts {

        public Asserts assertLineIsDeleted() {
            refresh();
            ElementsCollection ssLines = $$(By.xpath(".//*[@id='selfService_grid']//div[contains(@class,'x-grid3-body')]//div[contains(@class,'x-grid3-row')]"));
            assertEquals(ssLines.size(), 2, "Line was not deleted");
            return this;
        }

        public Asserts assertPurchaseDateIsNotEmpty(int lineNumber) {
            assertFalse(getPurchaseDate(lineNumber).equals(" "), "Purchase Date should not be empty");
            return this;
        }

        public Asserts assertNewPriceIsNotEmpty(int lineNumber) {
            assertFalse(getNewPrice(lineNumber).equals(" "), "Purchase Price should not be empty");
            return this;
        }

        public Asserts assertPurchasePriceIsNotEmpty(int lineNumber) {
            assertFalse(getPurchasePrice(lineNumber).equals(" "), "Purchase Price should not be empty");
            return this;
        }

        public Asserts assertDescriptionIsNotEmpty(int lineNumber) {
            assertFalse(getDescriptionText(lineNumber).equals(" "), "Description should not be empty");
            return this;
        }

        public Asserts assertCategoryIsNotEmpty(int lineNumber) {
            assertFalse(getCategoryText(lineNumber).equals(" "), "Category should not be empty");
            return this;
        }

        public Asserts assertCustomerDemandIsNotEmpty(int lineNumber) {
            assertFalse(getCustomerDemandPrice(lineNumber).equals(" "), "Customer demand should not be empty");
            return this;
        }

        public Asserts assertCategoryIsMarkedAsRequired(int lineNumber) {
            ElementsCollection selfServiceLines = $$(By.xpath(".//*[@id='selfService_grid']//div[contains(@class,'x-grid3-body')]//div[contains(@class,'x-grid3-row')]"));
            selfServiceLines.get(lineNumber).$(By.xpath(".//div[contains(@class,'categoryColumn')]")).shouldHave(Condition.cssClass("x-grid3-cell-error-box"));
            return this;
        }

        public Asserts assertDocumentationIsMarkedAsRequired(int lineNumber) {
            ElementsCollection selfServiceLines = $$(By.xpath(".//*[@id='selfService_grid']//div[contains(@class,'x-grid3-body')]//div[contains(@class,'x-grid3-row')]"));
            selfServiceLines.get(lineNumber).$(By.xpath(".//div[contains(@class,'documentation')]")).shouldHave(Condition.cssClass("x-grid3-cell-error-box"));
            return this;
        }

        public Asserts assertRequiredFieldsAlertIsPresent() {
            assertTrue(isAlertPresent());
            acceptAlert();
            return this;
        }

        public Asserts assertAttachIconIsPresent() {
            assertTrue($(By.xpath(".//div[contains(@class, 'fileName')]//a/img[contains(@src, 'attach_icon.png')]")).isDisplayed(), "Attach icon should be displayed");
            return this;
        }
    }
}








