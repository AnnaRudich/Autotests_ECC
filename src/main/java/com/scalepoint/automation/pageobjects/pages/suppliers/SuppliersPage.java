package com.scalepoint.automation.pageobjects.pages.suppliers;

import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.eccadmin.CreateSupplierDialog;
import com.scalepoint.automation.pageobjects.dialogs.eccadmin.SupplierDialog;
import com.scalepoint.automation.pageobjects.pages.BestFitPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccAdminPage;
import com.scalepoint.automation.utils.data.entity.Supplier;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.function.Consumer;

import static com.scalepoint.automation.utils.Wait.*;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;


/**
 * This class represents Suppliers page elements and actions
 * User: kke
 */
@EccAdminPage
public class SuppliersPage extends BaseEccAdminNavigation {

    @FindBy(xpath = "//span[contains(@class,'create-supplier-btn')]")
    private WebElement createSupplierButton;

    @FindBy(xpath = "//tbody[contains(@id,'gridview')]//tr")
    private List<WebElement> allSuppliersList;

    @FindBy(css = "div#suppliersGridId-body table tr:first-of-type td:nth-of-type(2) div")
    private WebElement firstSupplierItem;

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
        waitForVisible(createSupplierButton);
        return this;
    }

    @Override
    protected String getRelativeUrl() {
        return "#suppliers";
    }

    /**
     * This method selects Create supplier option
     */
    public CreateSupplierDialog selectCreateSupplier() {
        createSupplierButton.click();
        return BaseDialog.at(CreateSupplierDialog.class);
    }


    public SupplierDialog openFirstSupplier() {
        waitForStaleElements(By.xpath("id('suppliersGridId-body')//table[contains(@class,'x-grid-with-row-lines')]"));
        WebElement supplier = allSuppliersList.get(0);
        doubleClick(supplier);
        waitForStaleElement(By.xpath("//span[contains(text(),'General')]"));
        return BaseDialog.at(SupplierDialog.class);
    }

    public SupplierDialog.GeneralTab openEditSupplierDialog(Supplier supplier) {
        find(By.xpath("//input[contains(@name, 'searchfield')]")).click();
        makeSupplierSearch(supplier.getSupplierName());
        WebElement option = find(bySupplierNameXpath, supplier.getSupplierName());
        waitForStaleElements((By.xpath("//tbody[contains(@id,'gridview')]//td[2]/div")));
        if (option.getText().contains(supplier.getSupplierName()) && option.getText().contains(supplier.getSupplierCVR())) {
            scrollTo(option);
            doubleClick(option);
            waitForStaleElement((By.xpath("//span[contains(text(),'General')]")));
        }
        return BaseDialog.at(SupplierDialog.GeneralTab.class);
    }

    public void makeSupplierSearch(String query) {
        suppliersSearchField.clear();
        setValue(suppliersSearchField, query);
        suppliersSearchField.sendKeys(Keys.ENTER);
        Wait.waitForAjaxCompleted();
    }

    public boolean isSupplierCreated(String supplierName) {
        find(By.xpath("//input[contains(@name,'searchfield')]")).click();
        makeSupplierSearch(supplierName);
        waitForStaleElements(By.xpath("id('suppliersGridId-body')//table[contains(@class,'x-grid-with-row-lines')]"));
        String xpath = bySupplierNameXpath.replace("$1", supplierName);
        try {
            WebElement option = find(By.xpath(xpath));
            return option.getText().contains(supplierName);
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

    public SuppliersPage doAssert(Consumer<Asserts> assertsFunc) {
        assertsFunc.accept(new Asserts());
        return SuppliersPage.this;
    }

    public class Asserts {
        public Asserts assertSupplierPresent(String supplierName) {
            assertTrue(isSupplierCreated(supplierName));
            return this;
        }

        public Asserts assertSupplierAbsent(String supplierName) {
            assertFalse(isSupplierCreated(supplierName));
            return this;
        }
    }

}

