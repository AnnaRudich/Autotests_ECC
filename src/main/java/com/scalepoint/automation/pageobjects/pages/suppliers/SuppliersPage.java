package com.scalepoint.automation.pageobjects.pages.suppliers;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialogSelenide;
import com.scalepoint.automation.pageobjects.dialogs.eccadmin.CreateSupplierDialog;
import com.scalepoint.automation.pageobjects.dialogs.eccadmin.suppliersdialog.SupplierDialog;
import com.scalepoint.automation.pageobjects.pages.LoginPage;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccAdminPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Link;

import java.util.List;
import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.*;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;


/**
 * This class represents Suppliers page elements and actions
 * User: kke
 */
@EccAdminPage
public class SuppliersPage extends BaseSupplierAdminNavigation {

    @FindBy(xpath = "//span[contains(@class,'create-supplier-btn')]")
    private WebElement createSupplierButton;

    @FindBy(xpath = "//tbody[contains(@id,'gridview')]//tr")
    private List<WebElement> allSuppliersList;

    @FindBy(css = "div#suppliersGridId-body table tr:first-of-type td:nth-of-type(2) div")
    private WebElement firstSupplierItem;

    @FindBy(css = "input[id^=searchfield]")
    private WebElement suppliersSearchField;

    @FindBy(xpath = "//div[@id='suppliersGridId']//div[contains(@id,'targetEl')and contains(@id, 'headercontainer')]")
    private WebElement columnsTitles;

    @FindBy(xpath = "//td[contains(@class,'tick')]/div")
    private WebElement tickedExclOrVouchersField;

    @FindBy(xpath = "//*[contains(@id, 'suggest_item_td')]")
    private List<WebElement> allSuggestionsList;

    @FindBy(xpath = "//a[@href='toME.action']")
    private WebElement toMatchingEngineLink;

    @FindBy(xpath = ".//a[contains(@href, 'logout')]")
    private Link signOutLink;

    private String bySupplierNameXpath = "//tbody[contains(@id,'gridview')]//tr[contains(.,'%s')]";
    private String byVoucherXpath = "//td[contains(@class, 'x-grid-cell-supplierListVouchersId ')]";
    private String byExclusiveXpath = "//td[contains(@class, 'x-grid-cell-supplierListExclusiveId ')]";

    @Override
    protected void ensureWeAreOnPage() {
        waitForAjaxCompletedAndJsRecalculation();
        try {
            $(createSupplierButton).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
        } catch (Exception e) {
            refresh();
            $(createSupplierButton).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
        }
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
        return BaseDialogSelenide.at(CreateSupplierDialog.class);
    }

    public LoginPage signOut() {
        signOutLink.click();
        return at(LoginPage.class);
    }

    public SupplierDialog openFirstSupplier() {
        waitForStaleElements(By.xpath("id('suppliersGridId-body')//table[contains(@class,'x-grid-with-row-lines')]"));
        WebElement supplier = allSuppliersList.get(0);
        $(supplier).doubleClick();
        waitForStaleElement(By.xpath("//span[contains(text(),'General')]"));
        return BaseDialog.at(SupplierDialog.class);
    }

    private WebElement getOption(String supplierName) {
        return $(By.xpath(String.format(bySupplierNameXpath, supplierName)));
    }

    public SupplierDialog editSupplier(String supplierName) {
        $(By.xpath("//input[contains(@name, 'searchfield')]")).click();
        makeSupplierSearch(supplierName);
        waitForStaleElements(By.xpath("//tbody[contains(@id,'gridview')]//td[2]/div"));

        SelenideElement element = $(getOption(supplierName));
        if (element.getText().contains(supplierName)) {
            element
                    .scrollTo()
                    .doubleClick();
        }

        return BaseDialog.at(SupplierDialog.class);
    }

    public void makeSupplierSearch(String query) {
        SelenideElement element = $(suppliersSearchField);
        element.clear();
        element.setValue(query);
        element.pressEnter();
        Wait.waitForAjaxCompleted();
    }

    public boolean isSupplierCreated(String supplierName) {
        hoverAndClick($(By.xpath("//input[contains(@name,'searchfield')]")));
        makeSupplierSearch(supplierName);
        waitForStaleElements(By.xpath("id('suppliersGridId-body')//table[contains(@class,'x-grid-with-row-lines')]"));
        String xpath = String.format(bySupplierNameXpath, supplierName);
        try {
            WebElement option = $(By.xpath(xpath));
            return option.getText().contains(supplierName);
        } catch (Error e) {
            return false;
        }
    }

    private boolean isTickDisplayed(String query, String XpathLocator) {
        makeSupplierSearch(query);
        return $(By.xpath(XpathLocator)).getAttribute("class").contains("tick");
    }

    public boolean isExclusiveColumnDisplayed() {
        return columnsTitles.getText().contains("Exclusive");
    }

    public boolean isColumnDisplayed(String columnName) {
        return columnsTitles.getText().contains(columnName);
    }

    public boolean isExclOrVoucherFieldTicked() {
        return tickedExclOrVouchersField.isDisplayed();
    }

    @FindBy(xpath = ".//a[contains(@href, 'toME.action')]")
    private Link toMeLink;

    private boolean isToMeLinkDisplayed() {
        try {
            return toMeLink.isDisplayed();
        } catch (Exception e) {
            return false;
        }
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

        public Asserts assertsIsToMatchingEngineLinkDisplayed() {
            assertTrue(toMatchingEngineLink.isDisplayed());
            return this;
        }

        public Asserts assertsIsToMatchingEngineLinkNotDisplayed() {
            assertFalse(isToMeLinkDisplayed());
            return this;
        }

        public Asserts assertsIsExclusiveColumnDisplayed() {
            assertTrue(isExclusiveColumnDisplayed());
            return this;
        }

        public Asserts assertsIsExclusiveColumnNotDisplayed() {
            assertFalse(isExclusiveColumnDisplayed());
            return this;
        }

        public Asserts assertsIsVoucherTickForSupplierDisplayed(String supplierName) {
            assertTrue(isTickDisplayed(supplierName, byVoucherXpath));
            return this;
        }

        public Asserts assertsIsVoucherTickForSupplierNotDisplayed(String supplierName) {
            assertFalse(isTickDisplayed(supplierName, byVoucherXpath));
            return this;
        }

        public Asserts assertsIsExclusiveTickForSupplierDisplayed(String supplierName) {
            assertTrue(isTickDisplayed(supplierName, byExclusiveXpath));
            return this;
        }

        public Asserts assertsIsVouchersColumnNotDisplayed() {
            assertFalse(isColumnDisplayed("Vouchers"));
            return this;
        }
    }

}

