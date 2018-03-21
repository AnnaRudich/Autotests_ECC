package com.scalepoint.automation.pageobjects.pages.oldshop;

import com.scalepoint.automation.pageobjects.modules.oldshop.AccountBox;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.Configuration;
import com.scalepoint.automation.utils.OperationalUtils;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Link;

import java.util.List;
import java.util.function.Consumer;

import static com.scalepoint.automation.utils.OperationalUtils.assertEqualsDouble;

@EccPage
public class ShopWelcomePage extends ShopFlow {

    @FindBy(xpath = "//div[@class='product_grid']/table/tbody/tr[3]/td[1]")
    private WebElement productCashValue;

    @FindBy(xpath = "//div[@class='product_grid']/table/tbody/tr[4]/td[1]")
    private WebElement productFaceValue;

    @FindBy(xpath = "//div[contains(@class, 'current_user label_text')]/span/a")
    private Link logout;

    @FindBy(css = ".LoginBox_button .button")
    private Button login;

    @FindBy(xpath = "//a[@class='button_product_center']/span")
    private List<WebElement> productGridAddCartButtons;

    @FindBy(xpath = "//a[@class='button_product_center']/span")
    private WebElement firstRecommendedItem;

    private AccountBox accountBox = new AccountBox();

    @Override
    protected Page ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        Wait.waitForVisible(logout);
        return this;
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/shop/welcome.jsp";
    }

    private Double getProductFaceValue() {
        if (Configuration.isDK()) {
            return OperationalUtils.getDoubleValue(productFaceValue.getText().split(" ")[2]);
        } else {
            return OperationalUtils.getDoubleValue(productFaceValue.getText().split(" ")[3]);
        }
    }

    private Double getProductCashValue() {
        return OperationalUtils.getDoubleValue(productCashValue.getText().replaceAll("kr.", "").trim());
    }

    public ShopProductSearchPage toProductSearchPage() {
        Wait.waitForStaleElement(By.xpath("//a[@id='menu_id_2']"));
        clickAndWaitForStable(By.xpath("//a[@id='menu_id_2']"), By.xpath("//input[@id='TextSearch_text']"));
        return at(ShopProductSearchPage.class);
    }

    public ShopWelcomePage addFirstRecommendedItemToCart() {
        firstRecommendedItem.click();
        Wait.waitForDisplayed(By.xpath("//*[@id='AccountInfoBox_shopping_cart']//td[@class='description']"));
        return this;
    }

    public void logout() {
        logout.click();
    }

    public AccountBox getAccountBox() {
        return accountBox;
    }

    public ShopWelcomePage doAssert(Consumer<Asserts> assertFunc) {
        assertFunc.accept(new Asserts());
        return ShopWelcomePage.this;
    }

    public class Asserts {
        public Asserts assertProductCashValueIs(Double expectedPrice) {
            assertEqualsDouble(getProductCashValue(), expectedPrice, "Voucher cash value %s should be assertEqualsDouble to not depreciated voucher cash value %s");
            return this;
        }

        public Asserts assertItemNotPresent(String productName) {
            List<WebElement> items = findItemsByProductName(productName);
            Assert.assertTrue(items.isEmpty(), "Product with name " + productName + " is found");
            return this;
        }

        private List<WebElement> findItemsByProductName(String productName) {
            return driver.findElements(By.xpath(".//div[@class='ProductList']//a[contains(text(),'" + productName + "')]"));
        }

        public Asserts assertItemWithPricePresent(String productName, Double expectedPrice) {
            String expectedPriceValue = OperationalUtils.format(expectedPrice);
            List<WebElement> items = findItemsByProductName(productName.replace("/","/  "));
            Assert.assertTrue(items.size() >= 1, "Product with name " + productName + " not found");

            List<WebElement> prices = driver.findElements(By.xpath(".//div[@class='ProductList']//td[contains(@class,'purchase_price') and contains(text(),'" + expectedPriceValue + "')]"));
            Assert.assertTrue(prices.size() >= 1, "Product with price" + expectedPriceValue + " not found");
            return this;
        }

        public Asserts assertProductFaceValueIs(Double expectedPrice) {
            assertEqualsDouble(getProductFaceValue(), expectedPrice, "Voucher face value %s should be assertEqualsDouble to not depreciated new price %s");
            return this;
        }
    }
}

