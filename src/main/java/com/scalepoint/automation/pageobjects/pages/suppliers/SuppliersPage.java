package com.scalepoint.automation.pageobjects.pages.suppliers;

import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.RandomUtils;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccAdminPage;
import com.scalepoint.automation.utils.data.entity.Supplier;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

import static com.scalepoint.automation.utils.Wait.waitForStableElement;
import static com.scalepoint.automation.utils.Wait.waitForStableElements;


/**
 * This class represents Suppliers page elements and actions
 * User: kke
 */
@EccAdminPage
public class SuppliersPage extends Page {

    @FindBy(xpath = "//span[contains(@class,'create-supplier-btn')]")
    private WebElement createSupplierButton;

    @FindBy(xpath = "//tbody[contains(@id,'gridview')]//tr")
    private List<WebElement> allSuppliersList;

    @FindBy(css = "div#suppliersGridId-body table tr:first-of-type td:nth-of-type(2) div")
    private WebElement firstSupplierItem;

    //@FindBy(xpath = "//input[contains(@class,'supplierListSearchField')]")
    @FindBy(xpath = "//input[contains(@name,'searchfield')]")
    private WebElement suppliersSearchField;

    @FindBy(xpath = "//div[@id='suppliersGridId']//div[contains(@id,'targetEl')and contains(@id, 'headercontainer')]")
    private WebElement columnsTitles;

    @FindBy(xpath = "//td[contains(@class,'tick')]/div")
    private WebElement tickedExclOrVouchersField;

    @FindBy(xpath = "//*[contains(@id, 'suggest_item_td')]")
    private List<WebElement> allSuggestionsList;

    private String bySupplierNameXpath = "//tbody[contains(@id,'gridview')]//tr[contains(.,'$1')]";

    @Override
    protected Page ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        return this;
    }

    @Override
    protected String getRelativeUrl() {
        return "#suppliers";
    }

    /**
     * This method selects Create supplier option
     */
    public void selectCreateSupplier() {
        createSupplierButton.click();
    }

    /**
     * This method implemented for technical use - if you don't want to create new supplier to verify shop creation etc.
     * It waits for General tab element visibility to be confident that supplier form is opened
     */
    public void openFirstSupplier() {
        doubleClick(By.cssSelector("div#suppliersGridId-body table tr:first-of-type td:nth-of-type(2) div"));
        waitForStableElement(By.xpath("//span[contains(text(),'General')]"));
    }


    public String openRandomSupplier() {
        waitForStableElements(By.xpath("id('suppliersGridId-body')//table[contains(@class,'x-grid-with-row-lines')]"));
        WebElement supplier = allSuppliersList.get(RandomUtils.randomInt(allSuppliersList.size()));
        doubleClick(supplier);
        waitForStableElement(By.xpath("//span[contains(text(),'General')]"));
        return getInputValue(find(By.xpath("//input[contains(@name,'name')]")));
    }

    public void openRandomSupplierNoReturn() {
        waitForStableElements(By.xpath("id('suppliersGridId-body')//table[contains(@class,'x-grid-with-row-lines')]"));
        WebElement supplier = allSuppliersList.get(RandomUtils.randomInt(allSuppliersList.size()));
        doubleClick(supplier);
        waitForStableElement(By.xpath("//span[contains(text(),'General')]"));
    }

    public String openRandomSupplierByIC() {
        waitForStableElements(By.xpath("id('suppliersGridId-body')//table[contains(@class,'x-grid-with-row-lines')]"));
        WebElement supplier = allSuppliersList.get(RandomUtils.randomInt(allSuppliersList.size()));
        doubleClick(supplier);
        waitForStableElement(By.xpath("//span[contains(text(),'General')]"));
        return getInputValue(find(By.xpath("//label[contains(text(),'Supplier name')]/ancestor::td/following-sibling::td/div")));
    }


    public void openNewSupplierForEditing(Supplier supplier) {
        find(By.xpath("//input[contains(@name, 'searchfield')]")).click();
        makeSupplierSearch(supplier.getSupplierName());
        WebElement option = find(bySupplierNameXpath, supplier.getSupplierName());
        waitForStableElements((By.xpath("//tbody[contains(@id,'gridview')]//td[2]/div")));
        if (option.getText().contains(supplier.getSupplierName()) && option.getText().contains(supplier.getSupplierCVR())) {
            scrollTo(option);
            doubleClick(option);
            waitForStableElement((By.xpath("//span[contains(text(),'General')]")));
        }
    }

    public void openSupplierForEditing(String name) {
        find(By.xpath("//input[contains(@name, 'searchfield')]")).click();
        makeSupplierSearch(name);
        waitForStableElements((By.xpath("//tbody[contains(@id,'gridview')]//td[2]/div")));
        WebElement item = find(bySupplierNameXpath, name);
        if (item.getText().contains(name)) {
            scrollTo(item);
            doubleClick(item);
            waitForStableElement((By.xpath("//span[contains(text(),'General')]")));
        }
    }

    /**
     * This method execute Search via Search field on the top of the page and waits for some results
     *
     * @param query Query value
     */
    public void makeSupplierSearch(String query) {
        suppliersSearchField.clear();
        setValue(suppliersSearchField, query);
        suppliersSearchField.sendKeys(Keys.ENTER);
        Wait.waitForAjaxComplete();
    }

    /**
     * This method checks if suppliers list contains new supplier or not
     */
    public boolean isNewSupplierCreated(Supplier supplier) {
        find(By.xpath("//input[contains(@name,'searchfield')]")).click();
        makeSupplierSearch(supplier.getSupplierName());
        waitForStableElements(By.xpath("id('suppliersGridId-body')//table[contains(@class,'x-grid-with-row-lines')]"));
        String xpath = bySupplierNameXpath.replace("$1", supplier.getSupplierName());
        try {
            WebElement option = find(By.xpath(xpath));
            return option.getText().contains(supplier.getSupplierName())
                    && option.getText().contains(supplier.getSupplierCVR());
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isExclusiveColumnDisplayed() {
        return columnsTitles.getText().contains("Exclusive");
    }

    public boolean isExclOrVoucherFieldTicked() {
        return tickedExclOrVouchersField.isDisplayed();
    }

    /**
     * This method checks if Exclusive or Has vouchers field ticket depending on Supplier's data
     */
    public boolean isExclOrVoucherFieldTickedSupList(Supplier supplier) {
        driver.findElement(By.xpath("//input[contains(@name,'searchfield')]")).click();
        makeSupplierSearch(supplier.getSupplierName());
        waitForStableElements(By.xpath("id('suppliersGridId-body')//table[contains(@class,'x-grid-with-row-lines')]"));
        String xpath = bySupplierNameXpath.replace("$1", supplier.getSupplierName());
        try {
            WebElement item = find(By.xpath(xpath));
            return item.getText().contains(supplier.getSupplierName()) && isExclOrVoucherFieldTicked();
        } catch (Exception e) {
            return false;
        }
    }
}

