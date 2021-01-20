package com.scalepoint.automation.pageobjects.pages.suppliers;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.pageobjects.pages.LoginPage;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccAdminPage;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import ru.yandex.qatools.htmlelements.element.Link;

import java.util.List;
import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;
import static com.scalepoint.automation.utils.Wait.waitForPageLoaded;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

@EccAdminPage
public class DefaultSettingsPage extends BaseSupplierAdminNavigation {

    @FindBy(xpath = ".//a[contains(@href, 'logout')]")
    private Link signOutLink;
    @FindBy(xpath = "//button[contains(@class,'open-selected-supplier-btn')]")
    private WebElement openSelectedButton;
    @FindBy(xpath = "//input[contains(@id,'searchfield')]")
    private WebElement vouchersSearchField;
    @FindBy(xpath = "//div[1]/table/tbody/tr/td[2]/div")
    private WebElement firstVoucherItem;
    @FindBy(xpath = "//span[contains(@class,'column-header-text')]")
    private List<WebElement> columnsTitles;
    @FindBy(xpath = "//td[contains(@class,'tick')]/div")
    private WebElement tickedActiveOrExclField;
    @FindBy(xpath = "id('vouchersGridId-body')//tr")
    private List<WebElement> allVouchersList;

    private String byVoucherNameXpath = "id('vouchersGridId')//div[contains(.,'$1')]";
    private String byExclusiveXpath = "//td[contains(@class, 'x-grid-cell-voucherListExclusiveId ')]";
    private String byActiveXpath = "//td[contains(@class, 'x-grid-cell-voucherListActiveId ')]";

    @Override
    protected void ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForAjaxCompletedAndJsRecalculation();
        waitForPageLoaded();
        $(".defaultSettings").waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
    }

    @Override
    protected String getRelativeUrl() {
        return "#vouchers";
    }

    public void addVoucherSearchQuery(String query) {
        vouchersSearchField.sendKeys(query);
    }

    /**
     * This method execute Search via Search field on the top of the page and waits for some results
     *
     * @param query Query value
     */
    public void makeVouchersSearch(String query) {
        hoverAndClick($(By.xpath("//input[contains(@name,'searchfield')]")));
        vouchersSearchField.clear();
        logger.info("Search for voucher " + query);
        vouchersSearchField.sendKeys(query);
        vouchersSearchField.sendKeys(Keys.ENTER);
        Wait.waitForAjaxCompleted();
        Wait.waitForStaleElements(By.xpath("id('vouchersGridId-body')//table[contains(@class,'x-grid-with-row-lines')]"));
    }

    /**
     * This method implemented for technical use - if you don't want to create new voucher to verify categories mapping etc.
     * It waits for Categories tab element visibility to be confident that voucher is opened
     */
    public void openFirstVoucher() {
        Wait.waitForStaleElement(By.xpath("//div[1]/table/tbody/tr/td[2]/div"));
        $(firstVoucherItem).doubleClick();
        Wait.waitForStaleElement(By.xpath("//div[@id='categoriesVoucherTabId']"));
    }

    public boolean isExclusiveColumnDisplayed() {
        for (WebElement element : columnsTitles) {
            if (element.getText().contains("Exclusive"))
                return true;
        }
        return false;
    }

    public boolean isActiveOrExclFieldTicked() {
        return tickedActiveOrExclField.isDisplayed();
    }

    public LoginPage signOut() {
        signOutLink.click();
        return at(LoginPage.class);
    }

    public boolean isVoucherCreated(String voucherName) {
        makeVouchersSearch(voucherName);
        String xpath = byVoucherNameXpath.replace("$1", voucherName);
        try {
            WebElement option = $(By.xpath(xpath));
            return Wait.forCondition(ExpectedConditions.textToBePresentInElement(option, voucherName));
        } catch (Error e) {
            return false;
        }
    }

    private boolean isTickDisplayed(String query, String XpathLocator) {
        makeVouchersSearch(query);
        return $(By.xpath(XpathLocator)).getAttribute("class").contains("tick");
    }

    public DefaultSettingsPage doAssert(Consumer<Asserts> assertsFunc) {
        assertsFunc.accept(new Asserts());
        return DefaultSettingsPage.this;
    }

    public class Asserts {
        public Asserts assertVoucherPresent(String voucherName) {
            assertTrue(isVoucherCreated(voucherName));
            return this;
        }

        public Asserts assertVoucherAbsent(String voucherName) {
            assertFalse(isVoucherCreated(voucherName));
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

        public Asserts assertsIsExclusiveTickForVoucherDisplayed(String voucherName) {
            assertTrue(isTickDisplayed(voucherName, byExclusiveXpath));
            return this;
        }

        public Asserts assertsIsActiveTickForVoucherDisplayed(String voucherName) {
            assertTrue(isTickDisplayed(voucherName, byExclusiveXpath));
            return this;
        }

        public Asserts assertsIsNotActiveTickForVoucherDisplayed(String voucherName) {
            assertFalse(isTickDisplayed(voucherName, byActiveXpath));
            return this;
        }
    }
}

