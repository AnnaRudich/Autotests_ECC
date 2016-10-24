package com.scalepoint.automation.pageobjects.pages.oldshop;

import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.OperationalUtils;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@EccPage
public class ShopShoppingCartPage extends Page {

    private final String IBAN = "NL04ABNA0482056441";

    @FindBy(xpath = "//*[@id='bd']//td[1]/a")
    private WebElement emptyCartButton;

    @FindBy(xpath = "//div[@class='checkout_button'][1]/a")
    private WebElement checkOutButton;

    @FindBy(xpath = "//div[@class='delete_icon']")
    private List<WebElement> deleteCalculationLineOption;

    @FindBy(xpath = "//td[contains(@class, 'description')]")
    private List<WebElement> itemDescriptionField;

    @FindBy(xpath = "//td[contains(@class, 'price')]")
    private List<WebElement> itemPriceField;

    @FindBy(xpath = "(//td[@class = 'label_value'])[1]")
    private WebElement subtotalField;

    @FindBy(xpath = "(//td[@class = 'label_value'])[2]")
    private WebElement totalPriceField;

    @FindBy(xpath = "(//td[@class = 'label_value'])[3]")
    private WebElement claimsAmountField;

    @FindBy(xpath = "(//td[@class = 'label_value'])[4]")
    private WebElement balanceField;

    @FindBy(id = "deposit_method_banktransfer")
    private WebElement depositMethod;

    @FindBy(xpath = "//input[@id='payer_name']")
    private WebElement depositerNameField;

    @FindBy(xpath = "//input[@id='payer_city']")
    private WebElement depositerCityField;

    @FindBy(xpath = "//input[@id='DepositView_acctNumber_iban']")
    private WebElement ibanField;

    @FindBy(id = "withdrawal_method_cheque")
    private WebElement check;

    @Override
    protected Page ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        Wait.waitForVisible(checkOutButton);
        return this;
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/shop/shopping_cart.jsp";
    }

    /**
     * The method selects Check Out option and waits for Order Verification page is displayed
     */
    public ShopCashPayoutPage selectCheckOutOption() {
        checkOutButton.click();
        return at(ShopCashPayoutPage.class);
    }


    public void selectCheckOutOptionWithPayment() {
        checkOutButton.click();
        if (isExists(depositMethod)) {
            depositMethod.click();
        } else {
            check.click();
        }
        checkOutButton.click();
    }

    private boolean isExists(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * The method selects Check Out option and waits for Order Verification page is displayed
     */
    public void selectCheckOutOptionExtra() {
        clickAndWaitForDisplaying(checkOutButton,
                By.id("deposit_method_external_creditcard"));
    }

    /**
     * This method selects delete option for cart item
     */
    public void deleteCalculationLine(Integer n) {
        deleteCalculationLineOption.get(n).click();
    }

    /**
     * This method gets Added Item description text
     */
    public String getItemDescriptionText(Integer n) {
        return OperationalUtils.unifyStr(getText(itemDescriptionField.get(n)));
    }

    /**
     * This method gets Added Item price text
     */
    public Double getItemPriceText(Integer n) {
        return OperationalUtils.toNumber(getText(itemPriceField.get(n)));
    }

    /**
     * This method gets Subtotal field text
     */
    public Double getSubtotalFieldText() {
        return OperationalUtils.toNumber(getText(subtotalField));
    }

    /**
     * This method gets Total price field text
     */
    public Double getTotalPriceFieldText() {
        return OperationalUtils.toNumber(getText(subtotalField));
    }

    /**
     * This method gets Claims amount field text
     */
    public Double getClaimsAmountFieldText() {
        return OperationalUtils.toNumber(getText(claimsAmountField));
    }

    /**
     * This method gets Balance field text
     */
    public Double getBalanceFieldText() {
        return OperationalUtils.toNumber(getText(balanceField));
    }
}

