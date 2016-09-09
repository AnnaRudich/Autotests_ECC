package com.scalepoint.automation.pageobjects.pages;

import com.scalepoint.automation.utils.*;
import com.scalepoint.automation.utils.annotations.EccPage;
import com.scalepoint.automation.utils.driver.Browser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;

import java.util.HashSet;
import java.util.Set;

import static com.scalepoint.automation.utils.Wait.waitForVisible;

@EccPage
public class CustomerDetailsPage extends Page {

    private static final String URL = "webshop/jsp/matching_engine/customer_details.jsp";

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

    private String attachmentIconByClNameXpath = "//tr[@class='dimwhitebg']//span[contains(text(), '$1')]/ancestor::td[1]/a";

    @FindBy(xpath = "//fieldset[@id='field2']//tr[12]/td[2]")
    private WebElement damageDateValueGB;
    @FindBy(xpath = "//fieldset[@id='field2']//tr[11]/td[2]")
    private WebElement damageDateValueDK;
    @FindBy(xpath = "//fieldset[@id='field2']//tr[10]/td[2]")
    private WebElement damageDateValue;

    @Override
    protected String geRelativeUrl() {
        return URL;
    }

    @Override
    public CustomerDetailsPage ensureWeAreOnPage() {
        waitForUrl(URL);
        waitForVisible(reopenClaim);
        waitForVisible(cancelClaimButton);
        return this;
    }

    public void ReopenClaim() {
        EccActions eccActions = new EccActions(Browser.driver());
        reopenClaim.click();

        if (EccActions.isAlertPresent()) {
            EccActions.AcceptAlert();
        } else if (!EccActions.isAlertPresent()) {
            eccActions.switchToWindow();
        }
        Wait.waitForPageLoaded();
        eccActions.switchToWindow();
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
        Window.WindowManager windowManager = Window.get();
        windowManager.openDialog(driver.findElement(By.id("genoptag")));
        windowManager.closeDialog(driver.findElement(By.id("btn_reopen")));
        return at(SettlementPage.class);
    }

    /**
     * The method selects reopen Claim option, confirm alert and waits for settlement page is displayed
     */
    public void selectReopenClaimOptionWithAlert() {
        Set<String> countriesWithoutAlert = new HashSet<String>() {
            {
                add("NO");
                add("SE");
                add("DK");
            }
        };
        if (countriesWithoutAlert.contains(Configuration.getLocale()))
            reopenClaim();
        else
            clickAndAcceptAlertAndWaitForStables(viewClaimOptionsButton, By.id("finishCaseBtn-btnInnerEl"));
    }

    /**
     * The method selects reopen Claim option, confirm alert if alert exist and waits for settlement page is displayed
     */
    public void selectReopenClaimOptionAcceptAlertIfExist() {
        clickAndAcceptAlertIfPresentAndWaitForStables(viewClaimOptionsButton, By.id("finishCaseBtn-btnInnerEl"));
    }

    /**
     * This method returns Deposits field value
     */
    public Double getDepositFieldValue() {
        return OperationalUtils.toNumber(getText(depositField));
    }

    /**
     * This method show(hide) claim info
     */
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
        return isElementPresent(find(attachmentIconByClNameXpath, clName));
    }

    public String getDamageDate() {
        String locale = Configuration.getLocale();
        switch (locale) {
            case "dk":
            case "es":
            case "ch":
                return getText(damageDateValueDK);
            case "gb":
                return getText(damageDateValueGB);
            default:
                return getText(damageDateValue);
        }

    }
}
