package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ex.ElementNotFound;
import com.scalepoint.automation.pageobjects.extjs.ExtComboBoxBoundView;
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

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;
import static com.scalepoint.automation.utils.Wait.waitForAllElementsVisible;

public class FindShopDialog extends BaseDialog {

    @FindBy(id = "search-voucher-search-button")
    private SelenideElement searchVoucherButton;

    @FindBy(id = "search-voucher-close-button")
    private SelenideElement closeButton;

    @FindBy(id = "search-voucher-address-input-inputEl")
    private SelenideElement postalCodeInput;

    private Button getAlertOk(){

        return new Button($(By.xpath("//span[contains(@class,'x-btn-inner-default-small')][contains(text(),'Ok')]")));
    }

    private ExtComboBoxBoundView getVouchersCombo(){

        return new ExtComboBoxBoundView($(By.id("search-voucher-vouchers-combo")));
    }

    @Override
    protected void ensureWeAreAt() {

        waitForAjaxCompletedAndJsRecalculation();
        postalCodeInput.should(Condition.visible);
        processAlertIfPresent();
    }

    private FindShopDialog processAlertIfPresent() {
        try {

            $(By.xpath("//span[contains(@class,'x-btn-inner-default-small')][contains(text(),'Ok')]"))
                    .click();
        } catch (ElementNotFound ignored) {
        }
        return this;
    }

    public FindShopDialog findShops(String voucherName, String postalCode) {

        getVouchersCombo().select(voucherName);
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
                    .stream().filter(row -> shopName.contains(row.name)).findFirst();
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
            return row.findElement(By.xpath(".//td[@data-columnid='" + dataColumnId + "']")).getText();
        }
    }
}
