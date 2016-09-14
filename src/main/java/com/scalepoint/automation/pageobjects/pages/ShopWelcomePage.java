package com.scalepoint.automation.pageobjects.pages;

import com.scalepoint.automation.utils.Configuration;
import com.scalepoint.automation.utils.Wait;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Link;

public class ShopWelcomePage extends Page {

    private static final String URL = "webshop/jsp/shop/welcome.jsp";

    @FindBy(xpath = "//div[@class='product_grid']/table/tbody/tr[3]/td[1]")
    private WebElement productCashValue;

    @FindBy(xpath = "//div[@class='product_grid']/table/tbody/tr[4]/td[1]")
    private WebElement productFaceValue;

    @FindBy(xpath = "//div[contains(@class, 'current_user label_text')]/span/a")
    private Link logout;

    @FindBy(css = ".LoginBox_button .button")
    private Button login;

    @Override
    protected Page ensureWeAreOnPage() {
        waitForUrl(URL);
        Wait.waitForVisible(productCashValue);
        Wait.waitForVisible(productFaceValue);
        return this;
    }

    @Override
    protected String geRelativeUrl() {
        return URL;
    }

    public Double getProductFaceValue() {
        if (Configuration.isDK()) {
            return getDoubleValue(productFaceValue.getText().split(" ")[2]);
        } else {
            return getDoubleValue(productFaceValue.getText().split(" ")[3]);
        }
    }

    public Double getProductCashValue() {
        return getDoubleValue(productCashValue.getText().replaceAll("kr.", "").trim());
    }

    private static double getDoubleValue(String input) {
        String[] array = input.split(" ");
        return Double.parseDouble((array[array.length - 1]).replaceAll("\\.", "").replace(",", "."));
    }

    public static Double doubleString(String s) {
        return Double.parseDouble(s);
    }

    public void logout() {
        logout.click();
    }
}

