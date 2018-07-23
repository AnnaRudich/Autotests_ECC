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

import java.util.List;
import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.OperationalUtils.assertEqualsDouble;
import static org.testng.AssertJUnit.assertTrue;

public class ReplacementDialog extends BaseDialog {

    @FindBy(xpath = "//div[@id='replacementWindow_header-targetEl']//img[contains(@class,'x-tool-close')]")
    private Button cancelButton;

    @FindBy(xpath = "//div[contains(@id, 'replaceProductsGrid')]//table/tbody/tr")
    private List<WebElement> itemsList;

    @FindBy(xpath = "//td[contains(@class,'grid-cell-row-checker')]")
    private Radio selectItemCheckbox;

    @FindBy(xpath = "//tr/td[contains(@class,'x-grid-cell-faceValue')]")
    private WebElement voucherFaceValue;

    @FindBy(xpath = "//tr/td[contains(@class,'x-grid-cell-cashValue')]")
    private WebElement itemPrice;

    @FindBy(xpath = "//table//td//input[contains(@id, 'radiofield')]")
    private Radio payCompleteAmountRadio;

    @FindBy(id = "replacementType3")
    private Button sendChequeButton;

    @FindBy(xpath = "//div[contains(@class,'x-message-box')]//div[contains(@id,'messagebox')]//span[contains(@id,'button')][1]")
    private WebElement closeButton;

    @FindBy(xpath = "//span[@id='replacement-button-shop-btnEl']")
    private WebElement goToShopButton;

    @FindBy(xpath = "//div[contains(@id, 'headercontainer')]//div[contains(@id, 'headercontainer')]//div[contains(@class, 'x-column-header-checkbox')]//span")
    private WebElement selectAllItemsCheckbox;

    //$x("//*[@name='faceValue'][contains(@class, 'focus')]") ifInputIsEditable

    @Override
    public ReplacementDialog ensureWeAreAt() {
        Wait.waitForAjaxCompleted();
        switchToLast();
        Wait.waitForVisible(cancelButton);
        return this;
    }

    private By nextButtonByXpath = By.xpath("//span[@id='replacement-button-next-btnIconEl']");
    private By finishButtonByXpath = By.xpath("//span[@id='replacement-button-finish-btnIconEl']");


    public void closeReplacementDialog() {
        Wait.waitForVisible(cancelButton);
        cancelButton.click();
    }

    private Double getVoucherFaceValue() {
        return OperationalUtils.toNumber(voucherFaceValue.getText());
    }

    private Double getItemPriceValue() {
        return OperationalUtils.toNumber(itemPrice.getText().replaceAll("[^\\.,0123456789]", ""));
    }

    public ReplacementDialog editVoucherFaceValue(Double newPrice){
        voucherFaceValue.click();
        $(By.xpath("//input[@name='faceValue']")).setValue(newPrice.toString()).pressEnter();

        return this;
    }

    public CustomerDetailsPage completeClaimUsingCompPayment() {
        payCompleteAmountRadio.click();
        //only sequential double click activates Next button
        payCompleteAmountRadio.click();
        $(nextButtonByXpath).click();
        sendChequeButton.click();
        $(finishButtonByXpath).click();
        closeButton.click();
        return Page.at(CustomerDetailsPage.class);
    }

    public ShopWelcomePage goToShop() {
        goToShopButton.click();
        return Page.at(ShopWelcomePage.class);
    }

    public CustomerDetailsPage replaceAllItems() {
        selectAllItemsCheckbox.click();
        $(nextButtonByXpath).click();
        $(finishButtonByXpath).click();
        Wait.waitForVisible(cancelButton);
        closeButton.click();
        return Page.at(CustomerDetailsPage.class);
    }


    public ReplacementDialog replaceItemByIndex(int index) {
        itemsList.get(index);
        selectItemCheckbox.click();
        $(nextButtonByXpath).click();
        $(finishButtonByXpath).click();
        return ReplacementDialog.this;
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
            assertTrue("items list should be empty", itemsList.isEmpty());
            return this;
        }

        public Asserts assertGoToShopIsNotDisplayed() {
            assertTrue("goToShop should not ve visible", Wait.invisibilityOfElement(goToShopButton));
            return this;
        }


        public ReplacementDialog back() {
            return ReplacementDialog.this;
        }
    }
}
