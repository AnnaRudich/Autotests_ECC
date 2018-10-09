package com.scalepoint.automation.pageobjects.pages;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.pageobjects.modules.CustomerDetails;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.annotations.page.RequiredParameters;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.DateUtils.getDateFromString;
import static com.scalepoint.automation.utils.OperationalUtils.assertEqualsDouble;
import static com.scalepoint.automation.utils.Wait.visible;
import static com.scalepoint.automation.utils.Wait.waitForInvisible;
import static com.scalepoint.automation.utils.Wait.waitForVisible;
import static org.assertj.core.api.Assertions.assertThat;

@EccPage
@RequiredParameters("shnbr=%s")
public class CustomerDetailsPage extends BaseClaimPage {

    @FindBy(id = "genoptag")
    private Button reopenClaim;

    @FindBy(id = "annuller_sag")
    private WebElement cancelClaimButton;

    @FindBy(id = "damage_date_value")
    private WebElement damageDate;

    @FindBy(id = "claim_no")
    private WebElement claimNumber;

    @FindBy(xpath = "//a[@href='javascript:EditDamageDate()']")
    private WebElement damageDateEdit;

    @FindBy(xpath = "//button[contains(@onclick, 'newPassword')]")
    private WebElement newPasswordButton;

    private CustomerDetails customerDetails = new CustomerDetails();

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/matching_engine/customer_details.jsp";
    }

    protected String getAlternativeUrl() {
        return "webshop/jsp/matching_engine/details.jsp";
    }

    @Override
    public CustomerDetailsPage ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl(), getAlternativeUrl());
        if (driver.getCurrentUrl().contains(getRelativeUrl())) {
            waitForVisible(reopenClaim);
            waitForVisible(cancelClaimButton);
        } else {
            waitForVisible(claimNumber);
        }
        return this;
    }

    public String getClaimNumber() {
        return claimNumber.getText();
    }

    public CustomerDetailsPage cancelClaim() {
        cancelClaimButton.click();
        By alertMessageBy = By.xpath(".//div[contains(@id, 'messagebox')]//span[text()='Yes']//ancestor::a");
        Wait.waitForDisplayed(alertMessageBy);
        $(alertMessageBy).click();
        return at(CustomerDetailsPage.class);
    }

    public SettlementPage reopenClaim() {
        $(By.id("genoptag")).click();
        $(By.id("reopen-claim-button")).click();
        return at(SettlementPage.class);
    }

    public void newSelfServicePassword(){
        newPasswordButton.click();
    }

    public CustomerDetailsPage selectDamageDate(LocalDate date){
        $(By.xpath("//a[@href='javascript:EditDamageDate()']")).click();
        $(By.id("damageDate-inputEl")).click();
        waitForVisible($(By.xpath("//div[contains(@id, 'datepicker') and contains(@class, 'x-datepicker-default')]")));
        $(By.xpath("//a[contains(@id, 'splitbutton')]")).click();
        waitForVisible($(By.xpath("//div[contains(@class, 'x-monthpicker-body')]")));
        $(By.xpath("//div[@class='x-monthpicker-item x-monthpicker-month']/a[text()='"+date.getMonth().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("da-DK")).substring(0,3).toLowerCase()+"']")).click();
        $(By.xpath("//div[@class='x-monthpicker-item x-monthpicker-year']/a[text()='"+date.getYear()+"']")).click();
        clickUsingJsIfSeleniumClickReturnError($(By.xpath("//div[@class='x-monthpicker-buttons']//span[contains(text(),'OK')]")).shouldBe(Condition.visible));
        waitForInvisible($(By.xpath("//div[contains(@class, 'x-monthpicker-body')]")));
        $(By.xpath("//table//td[contains(@class, 'x-datepicker-active')]/div[text()='"+date.getDayOfMonth()+"']")).click();
        waitForInvisible($(By.xpath("//div[contains(@id, 'datepicker') and contains(@class, 'x-datepicker-default')]")));
        $(By.xpath("//div[contains(@id, 'toolbar')]//div[@class='x-box-inner']//a")).click();
        waitForInvisible($(By.id("damageDate-inputEl")));
        return this;
    }

    public CustomerDetailsPage doAssert(Consumer<Asserts> assertFunc) {
        assertFunc.accept(new Asserts());
        return CustomerDetailsPage.this;
    }

    public class Asserts {

        public Asserts assertCustomerCashValueIs(Double expectedPrice) {
            assertEqualsDouble(customerDetails.getCashValue(), expectedPrice, "Voucher cash value %s should be assertEqualsDouble to not depreciated voucher cash value %s");
            return this;
        }

        public Asserts assertCustomerFaceValueIs(Double expectedPrice) {
            assertEqualsDouble(customerDetails.getVoucherValue(), expectedPrice, "Voucher face value %s should be assertEqualsDouble to not depreciated new price %s");
            return this;
        }

        public Asserts assertCustomerFaceValueTooltipIs(Double expectedPrice) {
            assertEqualsDouble(customerDetails.getFaceTooltipValue(), expectedPrice, "Voucher face value %s should be assertEqualsDouble to not depreciated new price %s");
            return this;
        }

        public Asserts assertDamageDateIsEqual(LocalDate localDate) {
            assertThat(getDateFromString(damageDate.getText())).isEqualTo(localDate);
            return this;
        }

        public Asserts assertDamageDateIsEmpty() {
            assertThat(damageDate.getText().equals(""));
            return this;
        }

        public Asserts assertIsDamageDateEditAvailable() {
            assertThat(visible(damageDateEdit)).isTrue();
            return this;
        }

        public Asserts assertIsDamageDateEditNotAvailable() {
            Boolean isDisplayed;
            try {
                isDisplayed = damageDateEdit.isDisplayed();
            } catch (NoSuchElementException ex) {
                logger.info(ex.getMessage());
                isDisplayed = false;
            }
            assertThat(isDisplayed).isFalse();
            return this;
        }
    }


}
