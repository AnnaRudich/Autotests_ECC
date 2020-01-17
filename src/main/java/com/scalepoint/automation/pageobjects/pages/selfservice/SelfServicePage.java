package com.scalepoint.automation.pageobjects.pages.selfservice;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ex.ElementShould;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.data.TestData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.scalepoint.automation.utils.OperationalUtils.unifyStr;
import static com.scalepoint.automation.utils.Wait.*;
import static org.assertj.core.api.Assertions.assertThat;
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

    //looks like has to be moved to other class
    public SelenideElement getRandomElement(ElementsCollection list) {
        int index = new Random().nextInt(list.size() - 1);
        return list.get(index);
    }

    public SelfServicePage addCustomerNote(String noteText) {
        unfocusField();
        clickJS($(By.xpath(".//a[contains(@href, 'showCustomerNoteDialog')]")));
        $("#cutomer_note").sendKeys(noteText);
        $(By.xpath(".//button[.='OK']")).click();
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

    public SelfServiceGrid getSelfServiceGrid(){

        return new SelfServiceGrid();
    }


    public SelfServicePage doAssert(Consumer<Asserts> assertFunc) {
        assertFunc.accept(new Asserts());
        return this;
    }

    public class Asserts {

        public Asserts assertRequiredFieldsAlertIsPresent() {
            assertTrue(isAlertPresent());
            acceptAlert();
            return this;
        }
    }

    public class SelfServiceGrid {

        private ElementsCollection rows;
        SelenideElement customerComment = $("#customer_comment");

        SelfServiceGrid() {
            getRows();
        }

        private ElementsCollection findAllRows(){

            return rows =$("#selfService_grid")
                    .findAll("[class*='scroller'] table[class*='row']");
        }

        public List<SelfServiceGridRow> getRows(){

            return findAllRows()
                    .stream()
                    .map(SelfServiceGridRow::new)
                    .collect(Collectors.toList());
        }

        public SelfServiceGrid addCustomerComment(String commentText) {
            customerComment.click();
            customerComment.sendKeys(commentText);
            return this;
        }

        public SelfServiceGrid doAssert(Consumer<SelfServiceGrid.Asserts> assertFunc) {
            assertFunc.accept(new SelfServiceGrid.Asserts());
            return this;
        }

        public class Asserts {

            public Asserts assertRowsSize(int size) {
                assertThat(rows.size())
                        .isEqualTo(size);
                return this;
            }
        }

        public SelfServicePage selfServicePage(){
            return SelfServicePage.this;
        }

        public class SelfServiceGridRow {

            final long WAIT_FOR_VISIBILITY = 6000;
            SelenideElement row;
            SelenideElement category;
            SelenideElement acquired;
            SelenideElement purchaseDate;
            SelenideElement purchasePrice;
            SelenideElement newPrice;
            SelenideElement customerDemand;
            SelenideElement documentation;
            SelenideElement description;
            SelenideElement fileName;
            SelenideElement deleteIcon;

            SelfServiceGridRow(SelenideElement row) {
                this.row = row;
                category = row.find("[class*=categoryColumn] div div");
                acquired = row.find("[class*=acquired] div div");
                purchaseDate = row.find("[class*=purchaseDate] div div");
                purchasePrice = row.find("[class*=purchasePrice] div div");
                newPrice = row.find("[class*=newPrice] div div");
                customerDemand = row.find("[class*=customerDemand] div div");
                documentation = row.find("[class*=documentation] div div");
                description = row.find("[class*=description] div div");
                fileName = row.find("[class*=fileName] div");
                deleteIcon = row.find("[class*=delete-icon] a");
            }

            public SelfServiceGridRow addDescription(String text) {
                $("#selfService_grid input")
                        .waitUntil(visible, WAIT_FOR_VISIBILITY)
                        .setValue(text);
                unfocusField();
                return this;
            }

            public SelfServiceGridRow addDescriptionSelectFirstSuggestion(String text) {
                $("#selfService_grid input")
                        .waitUntil(visible, WAIT_FOR_VISIBILITY)
                        .setValue(text);
                ElementsCollection listItems = chooseDisplayed(".x-layer.x-combo-list")
                        .findAll(".x-combo-list-item");
                listItems.get(0).click();
                return this;
            }

            public SelfServiceGridRow selectRandomCategory() {
                category.click();
                SelenideElement comboList = $("#categorySelectorPanel + div").shouldBe(hidden);
                int attempts = 2;
                do {
                    try {
//                        Wait.waitMillis(500);
                        $("input#cs_category_group + img[class*='arrow']")
                                .click();
                        comboList.waitUntil(visible, 500);
                        break;
                    }catch (ElementShould e) {
                        if(attempts-- > 0){
                            continue;
                        }else {
                            throw e;
                        }
                    }
                }while(true);
                ElementsCollection cgItems = $$("[id*=cg_item]");
                getRandomElement(cgItems)
                        .click();
                return this;
            }

            public SelfServiceGridRow selectRandomAcquired(){
                acquired.click();
                chooseDisplayed("input+img")
                        .click();
                ElementsCollection listItems = chooseDisplayed(".x-layer.x-combo-list")
                        .findAll(".x-combo-list-item");
                getRandomElement(listItems)
                        .click();
                return this;
            }

            public SelfServiceGridRow selectRandomPurchaseDate() {
                purchaseDate.click();
                chooseDisplayed("input+img")
                        .click();
                ElementsCollection monthList = $$("[class*='date-mp-month'] a");
                getRandomElement(monthList).click();
                ElementsCollection yearList = $$("[class*='date-mp-year'] a");
                getRandomElement(yearList).click();
                $("button.x-date-mp-ok").click();
                return this;
            }

            public SelfServiceGridRow addPurchasePrice(String text) {
                purchasePrice.click();
                chooseDisplayed("input")
                        .setValue(text)
                        .pressEnter();
                return this;
            }

            public SelfServiceGridRow addNewPrice(String text) {
                newPrice.click();
                chooseDisplayed("input")
                        .setValue(text)
                        .pressEnter();
                return this;
            }

            public SelfServiceGridRow addCustomerDemandPrice(String text) {
                customerDemand.click();
                chooseDisplayed("input")
                        .setValue(text)
                        .pressEnter();
                return this;
            }

            public SelfServiceGrid deleteRow(){
                getRows();
                deleteIcon.click();
                findAllRows().shouldHave(CollectionCondition.sizeLessThan(rows.size()));
                return new SelfServiceGrid();
            }

            public String getDescription(){
                return description.getText();
            }

            public SelfServiceGridRow uploadDocumentation(boolean hasDocumentation) {
                documentation.click();
                chooseDisplayed("input+img")
                        .click();
                ElementsCollection listItems = chooseDisplayed(".x-layer.x-combo-list")
                        .findAll(".x-combo-list-item");
                if (hasDocumentation) {
                    listItems.stream()
                            .filter(element -> element.getText().equals("Ja"))
                            .findFirst()
                            .get()
                            .click();
                    uploadDocument(TestData.getClaimItem().getFileLoc());
                } else {
                    listItems.stream().filter(element -> element.getText()
                            .equals("Nej")).findFirst()
                            .get()
                            .click();
                }
                return this;
            }

            public SelfServiceGridRow doAssert(Consumer<SelfServiceGridRow.Asserts> assertFunc) {
                assertFunc.accept(new SelfServiceGridRow.Asserts());
                return this;
            }

            public class Asserts {

                public Asserts assertPurchaseDateIsNotEmpty() {
                    assertThat(purchaseDate.getText())
                            .as("Purchase Date should not be empty")
                            .isNotEqualTo(" ");
                    return this;
                }

                public Asserts assertNewPriceIsNotEmpty() {
                    assertThat(newPrice.getText())
                            .as("Purchase Price should not be empty")
                            .isNotEqualTo(" ");
                    return this;
                }

                public Asserts assertCategoryIsNotEmpty() {
                    assertThat(category.getText())
                            .as("Purchase Price should not be empty")
                            .isNotEqualTo(" ");
                    return this;
                }

                public Asserts assertPurchasePriceIsNotEmpty() {
                    assertThat(purchasePrice.getText())
                            .as("Purchase Price should not be empty")
                            .isNotEqualTo(" ");
                    return this;
                }

                public Asserts assertDescriptionIsNotEmpty() {
                    assertThat(description.getText())
                            .as("Customer demand should not be empty")
                            .isNotEqualTo(" ");
                    return this;
                }

                public Asserts assertCustomerDemandIsNotEmpty() {
                    assertThat(customerDemand.getText())
                            .as("Customer demand should not be empty")
                            .isNotEqualTo(" ");
                    return this;
                }

                public Asserts assertDocumentationIsMarkedAsRequired() {
                    row.find("[class*=documentation] div").shouldHave(Condition.cssClass("x-grid3-cell-error-box"));
                    return this;
                }

                public Asserts assertCategoryIsMarkedAsRequired() {
                    row.find("[class*=categoryColumn] div").shouldHave(Condition.cssClass("x-grid3-cell-error-box"));
                    return this;
                }

                public Asserts assertAttachIconIsPresent() {
                    assertThat(fileName.find("img").isDisplayed())
                            .as("Attach icon should be displayed")
                            .isTrue();
                    return this;
                }
            }

            public SelfServiceGrid selfServiceGrid(){
                return new SelfServiceGrid();
            }
        }
    }

    public SelenideElement chooseDisplayed(String locator){
        int attempts = 2;
        do {
            try {
//                Wait.waitMillis(500);
                return $$(locator).stream().filter(element -> element.isDisplayed()).findFirst().get();
            }catch (NoSuchElementException e) {
                if(attempts-- >0){
                    continue;
                }else {
                    throw e;
                }
            }
        }while (true);
    }
}








