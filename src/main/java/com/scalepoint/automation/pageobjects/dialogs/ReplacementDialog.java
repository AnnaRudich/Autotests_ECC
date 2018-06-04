package com.scalepoint.automation.pageobjects.dialogs;

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

import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.OperationalUtils.assertEqualsDouble;

public class ReplacementDialog extends BaseDialog {

//    @FindBy(id = "btn_cancel")
    @FindBy(xpath = "//div[contains(@class,'x-window')]//img[contains(@class,'x-tool-close')][1]")
    private Button cancelButton;

    //    @FindBy(xpath = "//table[@class='valuationTable']//tr[2]/td[3]")
    @FindBy(xpath = "//div[contains(@id,'replaceProductsGrid')]//table//tr/td[5]")
    private WebElement voucherFaceValue;

    //    @FindBy(xpath = "//table[@class='valuationTable']//tr[2]/td[4]")
    @FindBy(xpath = "//div[contains(@id,'replaceProductsGrid')]//table//tr/td[6]")
    private WebElement itemPrice;

    //    @FindBy(id = "replace_money_radio")
    @FindBy(xpath = "//table//td//input[contains(@id, 'radiofield')]")
    private Radio payCompleteAmountRadio;

    @FindBy(id = "//div[contains(@class, 'x-docked-bottom')]//div[contains(@id, 'toolbar')]/div[contains(@id, 'toolbar')]//a[4]")
    private Button nextButton;

    @FindBy(id = "replacementType3")
    private Button sendChequeButton;

    @FindBy(id = "//div[contains(@class, 'x-docked-bottom')]//div[contains(@id, 'toolbar')]/div[contains(@id, 'toolbar')]//a[5]")
    private WebElement finishButton;

    @FindBy(id = "btn_cancel")
    private WebElement okButton;

    //    @FindBy(id = "btn_close")
    @FindBy(xpath = "//div[contains(@class,'x-message-box')]//div[contains(@id,'messagebox')]//span[contains(@id,'button')][1]")
    private WebElement closeButton;

    //    @FindBy(xpath = "//button[@id='btn_replace_through_shop']")
    @FindBy(xpath = "//div[contains(@class, 'x-docked-bottom')]//div[contains(@id, 'toolbar')]/div[contains(@id, 'toolbar')]//a[1]")
    private WebElement goToShopButton;

    //    @FindBy(name = "select_all")
    @FindBy(xpath = "//div[contains(@id, 'headercontainer')]//div[contains(@id, 'headercontainer')]//div[contains(@class, 'x-column-header-checkbox')]//span")
    private WebElement selectAllItemsCheckbox;

    @Override
    public ReplacementDialog ensureWeAreAt() {
        Wait.waitForAjaxCompleted();
        switchToLast();
//        waitForVisible(cancelButton);
        return this;
    }

    String buttonLocator = "//div[contains(@class, 'x-docked-bottom')]//div[contains(@id, 'toolbar')]/div[contains(@id, 'toolbar')]//a[%s]";
    By nextButtonByXpath = By.xpath(String.format(buttonLocator, "4"));
    By finishButtonByXpath = By.xpath(String.format(buttonLocator, "5"));


    public void closeReplacementDialog() {
//        closeDialog(cancelButton);
        Wait.waitForVisible(cancelButton);
        cancelButton.click();
    }

    private Double getVoucherFaceValue() {
//        return OperationalUtils.toNumber(voucherFaceValue.getText().split("rdi")[1].replaceAll("[^\\.,0123456789]", ""));
        return OperationalUtils.toNumber(voucherFaceValue.getText());
    }

    private Double getItemPriceValue() {
        return OperationalUtils.toNumber(itemPrice.getText().replaceAll("[^\\.,0123456789]", ""));
    }

    public CustomerDetailsPage completeClaimUsingCompPayment() {
        payCompleteAmountRadio.click();
        //only sequential double click activates Next button
        payCompleteAmountRadio.click();
        $(nextButtonByXpath).click();
        sendChequeButton.click();
        $(finishButtonByXpath).click();
//        closeDialog(closeButton);
        closeButton.click();
        return Page.at(CustomerDetailsPage.class);
    }

    public ShopWelcomePage goToShop() {
//        closeDialog(goToShopButton);
        goToShopButton.click();
        return Page.at(ShopWelcomePage.class);
    }

    public CustomerDetailsPage replaceAllItems() {
        selectAllItemsCheckbox.click();
//        selectAllItemsCheckbox.click();
        $(nextButtonByXpath).click();
        $(finishButtonByXpath).click();
//        clickAndWaitForDisplaying(nextButton, By.id("btn_close"));
//        closeDialog(closeButton);
        Wait.waitForVisible(closeButton);
        closeButton.click();
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

        public ReplacementDialog back() {
            return ReplacementDialog.this;
        }
    }
}
