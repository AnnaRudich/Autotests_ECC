package com.scalepoint.automation.pageobjects.dialogs;

import com.scalepoint.automation.pageobjects.extjs.ExtComboBox;
import com.scalepoint.automation.utils.Wait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import ru.yandex.qatools.htmlelements.element.Button;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.scalepoint.automation.utils.Wait.waitForAllElementsVisible;
import static com.scalepoint.automation.utils.Wait.waitForVisible;

public class FindShopDialog extends BaseDialog {

    @FindBy(id = "search-voucher-vouchers-combo")
    private ExtComboBox vouchersCombo;

    @FindBy(id = "search-voucher-search-button")
    private WebElement searchVoucherButton;

    @FindBy(id = "search-voucher-close-button")
    private WebElement closeButton;

    @FindBy(id = "search-voucher-address-input-inputEl")
    private WebElement postalCodeInput;

    @FindBy(xpath = "//span[contains(@class,'x-btn-inner-default-small')][contains(text(),'Ok')]")
    private Button alertOk;

    @Override
    public FindShopDialog ensureWeAreAt() {
        waitForVisible(postalCodeInput);
        processAlertIfPresent();
        return this;
    }

    private FindShopDialog processAlertIfPresent() {
        try {
            WebElement element = driver.findElement(By.xpath("//span[contains(@class,'x-btn-inner-default-small')][contains(text(),'Ok')]"));
            element.click();
        } catch (Exception ignored) {
        }
        return this;
    }

    public FindShopDialog findShops(String voucherName, String postalCode) {
        vouchersCombo.select(voucherName);
        postalCodeInput.clear();
        postalCodeInput.sendKeys(postalCode);
        searchVoucherButton.click();
        Wait.waitForAjaxCompleted();
        processAlertIfPresent();
        return this;
    }

    public List<ShopRow> parseShopResults() {
        List<WebElement> rows = waitForAllElementsVisible(driver.findElements(By.xpath("//div[@id='search-voucher-shop-list']//tr")));
        return rows.stream().map(ShopRow::new).collect(Collectors.toList());
    }

    public FindShopDialog doAssert(Consumer<Asserts> assertsFunc) {
        assertsFunc.accept(new Asserts());
        return FindShopDialog.this;
    }

    public class Asserts {
        public Asserts assertDistanceToShopIs(String voucherName, String shopName, String postalCode, Integer expectedDistance) {
            Optional<ShopRow> shopRow = findShops(voucherName, postalCode)
                    .parseShopResults()
                    .stream().filter(row -> row.name.equals(shopName)).findFirst();
            if (shopRow.isPresent()) {
                Assert.assertEquals(shopRow.get().distance, expectedDistance);
            } else {
                Assert.fail(shopName + " not found!");
            }
            return this;
        }
    }

    private class ShopRow {
        private String name;
        private String address;
        private String phone;
        private String zipCode;
        private String city;
        private Integer distance;

        public ShopRow(WebElement row) {
            name = getText(row, "name");
            address = getText(row, "address1");
            phone = getText(row, "phone");
            zipCode = getText(row, "zipcode");
            city = getText(row, "city");
            String distanceText = getText(row, "distance");
            distance = Integer.valueOf(distanceText.replaceAll(".* (\\d*) .*", "$1"));
        }

        private String getText(WebElement row, String dataColumnId) {
            return row.findElement(By.xpath(".//td[@data-columnid='"+dataColumnId+"']")).getText();
        }

    }
}
