package com.scalepoint.automation.pageobjects.pages.oldshop;

import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@EccPage
public class ShopOrderConfirmationPage extends Page {

    @FindBy(xpath = "//div[@class='checkout_button']/a[1]")
    private WebElement finishButton;

    @FindBy(id = "cardNumber")
    private WebElement cardNumberField;

    private String byExpMonthXpath = "//select[@name='expiryMonth']/option[contains(.,'$1')]";
    private String byExpYearXpath = "//select[@name='expiryYear']/option[contains(.,'$1')]";

    @FindBy(id = "cardHolderName")
    private WebElement cardHolderNameField;

    @FindBy(id = "CVC")
    private WebElement cvcField;

    @FindBy(xpath = "//input[@name='Send']")
    private WebElement submitButton;

    @FindBy(xpath = "//input[@name='continue']")
    private WebElement continueOption;

    @Override
    protected Page ensureWeAreOnPage() {
        return null;
    }

    @Override
    protected String getRelativeUrl() {
        return null;
    }

    public void selectFinishOptionWithoutQuit() {
        finishButton.click();
    }
}
