package com.scalepoint.automation.pageobjects.pages;

import com.scalepoint.automation.pageobjects.modules.CustomerDetails;
import com.scalepoint.automation.utils.OperationalUtils;
import com.scalepoint.automation.utils.Window;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;

import static com.scalepoint.automation.utils.Wait.waitForVisible;

@EccPage
public class CustomerDetailsPage extends BaseClaimPage {

    @FindBy(id = "genoptag")
    private Button reopenClaim;

    @FindBy(id = "annuller_sag")
    private WebElement cancelClaimButton;

    @FindBy(xpath = "//button[@id='genoptag']")
    private WebElement viewClaimOptionsButton;

    @FindBy(id = "kode")
    private WebElement newPasswordButton;

    @FindBy(id = "make_report_id")
    private WebElement makeReportButton;

    @FindBy(xpath = "//*[@id='field3']//fieldset[2]/table/tbody/tr[3]/td[2]")
    private WebElement depositField;

    @FindBy(xpath = "//*[@id='field2']//tr[13]/td[2]")
    private WebElement customerNumberField;

    @FindBy(xpath = " //span[contains(text(),'+')]")
    private WebElement showHideInfoButton;

    @FindBy(xpath = "//table[@class='normsmall']//table[@class='normsmall']/tbody/tr[7]/td[3]")
    private WebElement faceValue;

    @FindBy(xpath = "//table[@class='normsmall']//table[@class='normsmall']/tbody/tr[8]/td[3]")
    private WebElement cashValue;

    @FindBy(xpath = "//button{@id='btn_reopen']")
    private WebElement reopenBtn;

    @FindBy(xpath = "//fieldset[@id='field2']//tr[11]/td[2]")
    private WebElement damageDateValueDK;

    private CustomerDetails customerDetails = new CustomerDetails();

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/matching_engine/customer_details.jsp";
    }

    @Override
    public CustomerDetailsPage ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForVisible(reopenClaim);
        waitForVisible(cancelClaimButton);
        return this;
    }

    /**
     * The method selects cancel Claim option and  confirm alert
     */
    public void selectCancelClaimOption() {
        cancelClaimButton.click();
        acceptAlert();
    }

    /**
     * The method selects reopen Claim option and waits for settlement page is displayed
     */
    public SettlementPage reopenClaim() {
        boolean opened = openDialog(driver.findElement(By.id("genoptag")));
        if (opened) {
            closeDialog(driver.findElement(By.id("btn_reopen")));
        }
        return at(SettlementPage.class);
    }

    public Double getDepositFieldValue() {
        return OperationalUtils.toNumber(getText(depositField));
    }

    public void showHideClaimInfo() {
        showHideInfoButton.click();
    }

    public Double getFaceValue() {
        return OperationalUtils.toNumber(faceValue.getText());
    }

    public Double getCashValue() {
        return OperationalUtils.toNumber(cashValue.getText());
    }

    public boolean isAttachmentIconDisplays(String clName) {
        String attachmentIconByClNameXpath = "//tr[@class='dimwhitebg']//span[contains(text(), '$1')]/ancestor::td[1]/a";
        return isElementPresent(find(attachmentIconByClNameXpath, clName));
    }

    public String getDamageDate() {
        return getText(damageDateValueDK);
    }

    public CustomerDetails getCustomerDetails() {
        return customerDetails;
    }
}
