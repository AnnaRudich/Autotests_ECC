package com.scalepoint.automation.pageobjects.modules.oldshop;

import com.scalepoint.automation.pageobjects.modules.Module;
import com.scalepoint.automation.pageobjects.pages.oldshop.ShopShoppingCartPage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.scalepoint.automation.pageobjects.pages.Page.at;

public class AccountBox extends Module {

    @FindBy(xpath = "//div[@class='AccountInfoBox_button']/a")
    private WebElement checkOutNowButton;

    public ShopShoppingCartPage toShoppingCart() {
        checkOutNowButton.click();
        return at(ShopShoppingCartPage.class);
    }
}
