package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.pageobjects.pages.CustomerDetailsPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.oldshop.ShopWelcomePage;
import com.scalepoint.automation.utils.OperationalUtils;
import com.scalepoint.automation.utils.Wait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Radio;

import java.util.List;
import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.scalepoint.automation.utils.OperationalUtils.assertEqualsDouble;
import static com.scalepoint.automation.utils.Wait.*;
import static org.assertj.core.api.Assertions.assertThat;

public class ReplacementDialog extends BaseDialog {

    @FindBy(xpath = "//div[@id='replacementWindow_header-targetEl']//img[contains(@class,'x-tool-close')]")
    private Button cancelButton;

    @FindBy(xpath = "//span/div[contains(@id, 'replacementOptionsSection')]/div[.//b]")
    private List<WebElement> replacementOptionsList;

    @FindBy(xpath = "//td[contains(@class,'grid-cell-row-checker')]")
    private Radio selectItemCheckbox;

    @FindBy(xpath = "//tr/td[contains(@class,'x-grid-cell-faceValue')]")
    private WebElement voucherFaceValue;

    @FindBy(xpath = "//tr/td[contains(@class,'x-grid-cell-cashValue')]")
    private WebElement itemPrice;

    @FindBy(xpath = "//input[contains(@class, 'x-form-radio')]")
    private Radio payCompleteAmountRadio;

    @FindBy(id = "replacementType3")
    private Button sendChequeButton;

    @FindBy(xpath = "//span[@id='replacement-button-shop-btnEl']")
    private WebElement goToShopButton;

    //    @FindBy(xpath = "//div[contains(@id, 'headercontainer')]//div[contains(@id, 'headercontainer')]//div[contains(@class, 'x-column-header-checkbox')]//span")
    private By selectAllItemsCheckbox = By.xpath("//div[contains(@id, 'headercontainer')]//div[contains(@id, 'headercontainer')]//div[contains(@class, 'x-column-header-checkbox')]//span");

    @FindBy(xpath = "//span[contains(text(), 'OK')]/ancestor::a")
    private Button alertOk;

    @Override
    public ReplacementDialog ensureWeAreAt() {
        Wait.waitForAjaxCompleted();
        switchToLast();
        waitForVisible(cancelButton);
        return this;
    }

    private By nextButtonByXpath = By.xpath("//span[@id='replacement-button-next-btnIconEl']");
    private By finishButtonByXpath = By.xpath("//span[@id='replacement-button-finish-btnIconEl']");
    private By itemsListByXpath = By.xpath("//span/div[contains(@id, 'replacementOptionsSection')]/div[.//b]");
    private By voucherFaceValueInputByXpath = By.xpath("//input[@name='faceValue']");
    private By selectItemCheckboxByXpath = By.xpath("//td[contains(@class,'grid-cell-row-checker')]");
    private By goToShopButtonByXpath = By.xpath("//span[@id='replacement-button-shop-btnEl']");
    private By closeButtonByXpath = By.xpath("//div[contains(@class,'x-message-box')]//div[contains(@id,'messagebox')]//span[contains(@id,'button')][1]");
    private By bankSection = By.xpath("(//div[@id ='bankSection']//input[contains(@id, 'radiofield')])[1]");
    private By regNumberInput = By.xpath("//label[contains(text(), 'Reg. nummer:')]/ancestor::tr//input[@type='text']");
    private By accountNumberInput = By.xpath("//label[contains(text(), 'Kontonummer:')]/ancestor::tr//input[@type='text']");


    public void closeReplacementDialog() {
        waitForVisible(cancelButton);
        cancelButton.click();
    }

    private Double getVoucherFaceValue() {
        return OperationalUtils.toNumber(voucherFaceValue.getText());
    }


    private Double getItemPriceValue() {
        return OperationalUtils.toNumber(itemPrice.getText().replaceAll("[^\\.,0123456789]", ""));
    }

    public ReplacementDialog editVoucherFaceValue(Double newPrice) {
        voucherFaceValue.click();
        $(voucherFaceValueInputByXpath).setValue(newPrice.toString()).pressEnter();
        return this;
    }

    private void selectBankSectionAndFill(String regNumber, String accountNumber){
        $(bankSection).click();
        $(regNumberInput).setValue(regNumber);
        $(accountNumberInput).setValue(accountNumber);
    }

    public CustomerDetailsPage completeClaimUsingCashPayoutToBankAccount(String regNumber, String accountNumber){
        payCompleteAmountRadio.click();
        $(nextButtonByXpath).click();
        selectBankSectionAndFill(regNumber, accountNumber);
        $(finishButtonByXpath).click();
        waitForLoaded();
        acceptReplacementAlert();
        return Page.at(CustomerDetailsPage.class);
    }

    private void acceptReplacementAlert(){
        $(By.xpath("//span[contains(text(), 'OK')]//following-sibling::span")).click();
    }

    public ShopWelcomePage goToShop() {
        goToShopButton.click();
        return Page.at(ShopWelcomePage.class);
    }

    public CustomerDetailsPage replaceAllItems() {
        $(selectAllItemsCheckbox).waitUntil(Condition.visible, 15L).click();
        $(nextButtonByXpath).click();
        $(finishButtonByXpath).click();
        waitForSpinnerToDisappear();
        waitForVisible(alertOk).click();
        return Page.at(CustomerDetailsPage.class);
    }


    public ReplacementDialog replaceItemByIndex(int index) {
        $$(itemsListByXpath).get(index);
        selectItemCheckbox.click();
        $(nextButtonByXpath).click();
        return ReplacementDialog.this;
    }

    private WebElement findReplacementOptionByText(String replacementTypeLabel) {
        return $$(itemsListByXpath).find(Condition.text(replacementTypeLabel))
                .find(By.xpath(".//tr/td/input[contains(@class, 'x-form-radio')]"));
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
            assertThat(Wait.isElementNotPresent(selectItemCheckboxByXpath)).as("there should not be items in the list").isTrue();
            return this;
        }

        public Asserts assertGoToShopIsNotDisplayed() {
            assertThat(Wait.isElementNotPresent(goToShopButtonByXpath)).as("goToShopButton should not be present").isTrue();
            return this;
        }

        public ReplacementDialog back() {
            return ReplacementDialog.this;
        }
    }
}
