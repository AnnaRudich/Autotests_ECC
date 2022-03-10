package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.pages.CustomerDetailsPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.OperationalUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Radio;

import java.time.Duration;
import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.scalepoint.automation.utils.OperationalUtils.assertEqualsDouble;
import static com.scalepoint.automation.utils.Wait.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.Keys.CONTROL;
import static org.openqa.selenium.Keys.DELETE;

public class ReplacementDialog extends BaseDialogSelenide {

    @Override
    protected void ensureWeAreAt() {

        waitForAjaxCompletedAndJsRecalculation();
        switchToLast();
        cancelButton.should(Condition.visible);
    }

    @FindBy(id = "replacement-button-cancel-btnInnerEl")
    private SelenideElement cancelButton;

    @FindBy(css = ".x-grid-cell-faceValue div")
    private SelenideElement voucherFaceValue;

    @FindBy(css = ".x-grid-cell-cashValue")
    private SelenideElement itemPrice;

    @FindBy(css = ".x-form-radio")
    private SelenideElement payCompleteAmountRadio;

    @FindBy(xpath = "//span[@id='replacement-button-shop-btnEl']")
    private SelenideElement goToShopButton;

    @FindBy(xpath = "//span/div[contains(@id, 'replacementOptionsSection')]/div[.//b]")
    private ElementsCollection replacementOptionsList;

    @FindBy(xpath = "//span[contains(text(), 'OK')]/ancestor::a")
    private SelenideElement alertOk;

    @FindBy(id = "replacementType3")
    private SelenideElement sendChequeButton;

    private Radio getSelectItemCheckbox(){

        return new Radio($(By.xpath("//td[contains(@class,'grid-cell-row-checker')]")));
    }

    private By selectAllItemsCheckbox = By.xpath("//div[contains(@id, 'headercontainer')]//div[contains(@id, 'headercontainer')]//div[contains(@class, 'x-column-header-checkbox')]//div[@data-ref='textEl']");
    private By nextButtonPath = By.id("replacement-button-next-btnInnerEl");
    private By finishButtonByXpath = By.id("replacement-button-finish-btnInnerEl");
    private By itemsListByXpath = By.cssSelector("#replacement-first-step-body [role=button]");
    private By voucherFaceValueInputByXpath = By.cssSelector("[name='faceValue']");
    private By selectItemCheckboxByXpath = By.xpath("//td[contains(@class,'grid-cell-row-checker')]");
    private By goToShopButtonByXpath = By.xpath("//span[@id='replacement-button-shop-btnEl']");
    private By closeButtonByXpath = By.xpath("//div[contains(@class,'x-message-box')]//div[contains(@id,'messagebox')]//span[contains(@id,'button')][1]");
    private By bankSection = By.cssSelector("#bankSection input[type=button] + span");
    private By regNumberInput = By.xpath("//div//span[contains(text(), \"Reg. nummer:\")]/ancestor::label/following::div/input");
    private By accountNumberInput = By.xpath("//div//span[contains(text(), \"Kontonummer:\")]/ancestor::label/following::div/input");


    public void closeReplacementDialog() {

        cancelButton.should(Condition.visible);
        cancelButton.click();
    }

    private Double getVoucherFaceValue() {

        return OperationalUtils.toNumber(voucherFaceValue.getText());
    }


    private Double getItemPriceValue() {

        return OperationalUtils.toNumber(itemPrice.getText().replaceAll("[^\\.,0123456789]", ""));
    }

    public ReplacementDialog editVoucherFaceValue(Double newPrice) {

        SelenideElement element = voucherFaceValue;
        hoverAndClick(element);
        SelenideElement subElement = $(voucherFaceValueInputByXpath);
        subElement
                .sendKeys(CONTROL + "a");
        subElement
                .sendKeys(DELETE);
        subElement
                .sendKeys(Integer.valueOf(newPrice.intValue()).toString());
        subElement
                .pressEnter();
        return this;
    }

    private void selectBankSectionAndFill(String regNumber, String accountNumber){

        ElementsCollection elements = $$(bankSection);
        SelenideElement element = elements.get(0);

        hoverAndClick(element);
        $(regNumberInput).setValue(regNumber);
        $(accountNumberInput).setValue(accountNumber);
    }

    public CustomerDetailsPage completeClaimUsingCashPayoutToBankAccount(String regNumber, String accountNumber){
        hoverAndClick($(payCompleteAmountRadio));
        hoverAndClick($(nextButtonPath));
        selectBankSectionAndFill(regNumber, accountNumber);
        hoverAndClick($(finishButtonByXpath));
        waitForLoaded();
        acceptReplacementAlert();
        return Page.at(CustomerDetailsPage.class);
    }

    private void acceptReplacementAlert(){
        $(By.cssSelector("div[role='alertdialog'] a")).click();
    }

    public CustomerDetailsPage replaceAllItems() {
        $(selectAllItemsCheckbox).should(Condition.visible, Duration.ofSeconds(15)).click();
        $(nextButtonPath).click();
        $(finishButtonByXpath).click();
        alertOk.should(Condition.visible).click();
        return Page.at(CustomerDetailsPage.class);
    }


    public ReplacementDialog replaceItemByIndex(int index) {
        SelenideElement element = $$(itemsListByXpath).get(index);
        hoverAndClick(element);
        $(nextButtonPath).click();
        return ReplacementDialog.this;
    }

    private WebElement findReplacementOptionByText(String replacementTypeLabel) {
        return $(By.xpath(String.format("//b[contains(text(), '%s')]/ancestor::label/preceding-sibling::span", replacementTypeLabel)));
    }

    public CustomerDetailsPage getAccessToShopForRemainingAmount() {
        findReplacementOptionByText("Giv kunden adgang").click();
        $(finishButtonByXpath).click();
        waitForSpinnerToDisappear();
        alertOk.click();
        return Page.at(CustomerDetailsPage.class);
    }


    public ReplacementDialog doAssert(Consumer<Asserts> assertFunc) {
        assertFunc.accept(new Asserts());
        return ReplacementDialog.this;
    }

    public class Asserts {
        public Asserts assertItemPriceValueIs(Double expectedPrice) {
            assertEqualsDouble(getItemPriceValue(), expectedPrice, "Voucher cash value %s should be equal to not depreciated new price %s");
            return this;
        }

        public Asserts assertVoucherFaceValueIs(Double expectedPrice) {
            assertEqualsDouble(getVoucherFaceValue(), expectedPrice, "Voucher face value %s should be equal to not depreciated new price %s");
            return this;
        }

        public Asserts assertItemsListIsEmpty() {
            assertThat(verifyElementVisible($(selectItemCheckboxByXpath))).as("there should not be items in the list").isFalse();
            return this;
        }

        public Asserts assertGoToShopIsNotDisplayed() {
            assertThat(verifyElementVisible($(goToShopButtonByXpath))).as("goToShopButton should not be present").isFalse();
            return this;
        }

        public ReplacementDialog back() {
            return ReplacementDialog.this;
        }
    }
}
