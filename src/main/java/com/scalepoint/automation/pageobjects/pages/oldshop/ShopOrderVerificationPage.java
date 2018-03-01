package com.scalepoint.automation.pageobjects.pages.oldshop;

import com.scalepoint.automation.pageobjects.pages.CustomerDetailsPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@EccPage
public class ShopOrderVerificationPage extends Page {

    @FindBy(xpath = "//input[@id='OrderVerificationView_agree_to_terms_bottom']")
    private WebElement agreeBox;

    @FindBy(xpath = "//div[@class='checkout_button'][1]/a")
    private WebElement placeMyOrderButton;

    @Override
    protected Page ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        Wait.waitForVisible(agreeBox);
        return this;
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/shop/order_verification.jsp";
    }

    /**
     * The method enables I agree to terms.. option
     */
    public ShopOrderVerificationPage selectAgreeOption() {
        if (agreeBox.isDisplayed() && !isSelected(agreeBox)) {
            clickAndWaitForDisplaying(agreeBox, By.xpath("//div[@class='checkout_button']/a"));
        }
        return this;
    }

    public CustomerDetailsPage selectPlaceMyOrderOption() {
        placeMyOrderButton.click();
        return at(CustomerDetailsPage.class);
    }

    public DibsPayType toDIBSPage() {
        placeMyOrderButton.click();
        return new DibsPayType();
    }

    public void selectPlaceMyOrderOptionExtraPayment() {
        // Todo something was changed. It could be that IBAN functionality was terned on
        clickAndWaitForDisplaying(placeMyOrderButton, By.xpath("//a[contains(@href, 'customer_order.jsp')]"));
    }
}
