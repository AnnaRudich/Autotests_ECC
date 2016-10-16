package com.scalepoint.automation.pageobjects.pages.suppliers;

import com.scalepoint.automation.pageobjects.pages.LoginPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.RandomUtils;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccAdminPage;
import com.scalepoint.automation.utils.data.entity.Voucher;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Link;

import java.util.List;

@EccAdminPage
public class VouchersPage extends Page {

    @FindBy(xpath=".//a[contains(@href, 'logout')]")
    private Link signOutLink;
    @FindBy(xpath=".//a[contains(@href, 'toME.action')]")
    private Link toMeLink;
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

    @Override
    protected Page ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        Wait.waitForVisible(firstVoucherItem);
        return this;
    }

    @Override
    protected String getRelativeUrl() {
        return "#vouchers";
    }

    /**
     * This method opens new voucher for editing
     */
    public void openNewVoucherForEditing(Voucher voucher) {
        find(By.xpath("//input[contains(@class,'voucherListSearchField')]")).click();
        makeVouchersSearch(voucher.getVoucherNameSP());
        List<WebElement> elements = Wait.waitForStableElements((By.xpath("id('vouchersGridId')//table[@class='x-grid3-row-table']//tr")));
        for (WebElement item : elements) {
            if (item.getText().contains(voucher.getVoucherNameSP())) {
                scrollTo(item);
                doubleClick(item);
                Wait.waitForElementDisplaying(By.xpath("//li[contains(@id,'categoriesVoucherTabId')]"));
                Wait.waitForAjaxComplete();
                break;
            }
        }

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
        vouchersSearchField.clear();
        System.out.println("Search for voucher "+query);
        vouchersSearchField.sendKeys(query);
        vouchersSearchField.sendKeys(Keys.ENTER);
        Wait.waitForAjaxComplete();
        Wait.waitForStableElements(By.xpath("id('vouchersGridId')"));
    }

    public boolean isVouchersListContainsNewVoucher(String voucherName) {
        find(By.xpath("//input[contains(@id,'searchfield')]")).click();
        makeVouchersSearch(voucherName);
        String xpath = byVoucherNameXpath.replace("$1", voucherName);
        return find(By.xpath(xpath)).isDisplayed();
    }

    /**
     * This method implemented for technical use - if you don't want to create new voucher to verify categories mapping etc.
     * It waits for Categories tab element visibility to be confident that voucher is opened
     */
    public void openFirstVoucher() {
        Wait.waitForStableElement(By.xpath("//div[1]/table/tbody/tr/td[2]/div"));
        doubleClick(firstVoucherItem);
        Wait.waitForStableElement(By.xpath("//div[@id='categoriesVoucherTabId']"));
    }

    public String openRandomVoucher() {
        Wait.waitForStableElements(By.xpath("id('vouchersGridId-body')//tr"));
        WebElement voucher = allVouchersList.get(RandomUtils.randomInt(allVouchersList.size()));
        doubleClick(voucher);
        Wait.waitForStableElement(By.xpath("//div[@id='categoriesVoucherTabId']"));
        return getInputValue(find(By.xpath("//input[@name='voucherName']")));
    }

    public String openVoucherByCount(int count) {
        Wait.waitForStableElements(By.xpath("id('vouchersGridId')//table[@class='x-grid3-row-table']//tr"));
        WebElement voucher = allVouchersList.get(count);
        doubleClick(voucher);
        Wait.waitForStableElement(By.xpath("//div[@id='categoriesVoucherTabId']"));
        return getInputValue(find(By.name("voucherName")));
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

    /**
     * This method checks if Active or Exclusive fields ticked depending on vouchers data
     */
    public boolean isActiveOrExclFieldTickedVouchersList(Voucher voucher) {
        find(By.xpath("//input[contains(@id,'searchfield')]")).click();
        makeVouchersSearch(voucher.getVoucherNameSP());
        Wait.waitForStableElements((By.xpath("id('vouchersGridId')")));
        String xpath = byVoucherNameXpath.replace("$1", voucher.getVoucherNameSP());
        try {
            WebElement item = find(By.xpath(xpath));
            return item.getText().contains(voucher.getVoucherNameSP()) && isActiveOrExclFieldTicked();
        } catch (Exception e) {
            return false;
        }
    }

    public void openVoucherForEdit(String voucherName) {
        makeVouchersSearch(voucherName);
        openFirstVoucher();
    }

    public LoginPage signOut(){
        signOutLink.click();
        return at(LoginPage.class);
    }

    public boolean isToMeLinkDisplayed(){
        try {
            return toMeLink.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}

