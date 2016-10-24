package com.scalepoint.automation.pageobjects.pages;

import com.scalepoint.automation.pageobjects.pages.oldshop.ShopCataloguePage;
import com.scalepoint.automation.utils.Configuration;
import com.scalepoint.automation.utils.OperationalUtils;
import com.scalepoint.automation.utils.Wait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Link;

public class ShopWelcomePage extends Page {

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
        waitForUrl(getRelativeUrl());
        Wait.waitForVisible(productCashValue);
        Wait.waitForVisible(productFaceValue);
        return this;
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/shop/welcome.jsp";
    }

    public Double getProductFaceValue() {
        if (Configuration.isDK()) {
            return OperationalUtils.getDoubleValue(productFaceValue.getText().split(" ")[2]);
        } else {
            return OperationalUtils.getDoubleValue(productFaceValue.getText().split(" ")[3]);
        }
    }

    public Double getProductCashValue() {
        return OperationalUtils.getDoubleValue(productCashValue.getText().replaceAll("kr.", "").trim());
    }

    public ShopCataloguePage toCatalogue() {
        Wait.waitForStableElement(By.xpath("//a[@id='menu_id_2']"));
        clickAndWaitForStable(By.xpath("//a[@id='menu_id_2']"), By.xpath("//input[@id='TextSearch_text']"));
        return at(ShopCataloguePage.class);
    }

    public void logout() {
        logout.click();
    }
}

