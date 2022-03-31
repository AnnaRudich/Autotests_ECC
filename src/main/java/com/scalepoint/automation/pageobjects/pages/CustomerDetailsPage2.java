package com.scalepoint.automation.pageobjects.pages;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.modules.CustomerDetails;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.annotations.page.RequiredParameters;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;

import java.time.Duration;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.scalepoint.automation.utils.Wait.verifyElementVisible;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;
import static org.assertj.core.api.Assertions.assertThat;

@EccPage
@RequiredParameters("shnbr=%s")
public class CustomerDetailsPage2 extends BaseClaimPage {

    @FindBy(id = "annuller_sag")
    private SelenideElement cancelClaimButton;
    @FindBy(id = "damage_date_value")
    private SelenideElement damageDate;
    @FindBy(id = "claim_no")
    private SelenideElement claimNumber;
    @FindBy(xpath = "//a[@href='javascript:EditDamageDate()']")
    private SelenideElement damageDateEdit;
    @FindBy(xpath = "//button[contains(@onclick, 'newPassword')]")
    private SelenideElement newPasswordButton;
    @FindBy(id = "firstname")
    private SelenideElement firstName;
    @FindBy(id = "lastname")
    private SelenideElement lastName;
    @FindBy(id = "address1")
    private SelenideElement address1;
    @FindBy(id = "zipcode")
    private SelenideElement zipCode;
    @FindBy(id = "city")
    private SelenideElement city;
    @FindBy(id = "policy_type")
    private SelenideElement policyType;

    private Button getReopenClaim(){

        return new Button($(By.id("genoptag")));
    }

    private CustomerDetails customerDetails = new CustomerDetails();

    private ClaimsInformation claimsInformation;

    public List<LossItem> getLossItemsInClaimsCalculation(){

        return $$("#field4 .table-content table.settlement-revision>tbody>tr:nth-child(2)").stream()
                .map(element -> new LossItemInClaimsCalculation(element))
                .collect(Collectors.toList());
    }

    public List<LossItem> getLossItemsInDraft(){

        return $$("#field5 .table-content tr:nth-child(2)").stream()
                .map(element -> new LossItemInDraft(element))
                .collect(Collectors.toList());
    }

    public LossItem getLossItemInClaimsCalculationByIndex(int index){

        return getLossItemsInClaimsCalculation()
                .get(index);
    }

    public LossItem getLossItemInDraftByIndex(int index){

        return getLossItemsInDraft()
                .get(index);
    }

    public CustomerDetailsPage2 cancelClaim() {

        cancelClaimButton.click();
        By alertMessageBy = By.xpath(".//div[contains(@id, 'messagebox')]//span[text()='Yes']//ancestor::a");
        verifyElementVisible($(alertMessageBy));
        $(alertMessageBy).click();
        return at(CustomerDetailsPage2.class);
    }

    public SettlementPage reopenClaim() {

        $(By.id("genoptag")).click();
        $(By.id("reopen-claim-button")).click();
        return at(SettlementPage.class);
    }

    @Getter
    class ClaimsInformation{

        ElementsCollection claimInformationValues;
        String company;
        String claimsHandler;
        String claimsNumber;
        String status;
        String settleMethod;

        ClaimsInformation(){

            claimInformationValues = $$("#claims-info-table tr td:nth-child(2)")
                    .should(CollectionCondition.size(5), Duration.ofMillis(TIME_OUT_IN_MILISECONDS));
            company = claimInformationValues.get(0).getText();
            claimsHandler = claimInformationValues.get(1).getText();
            claimsNumber = claimInformationValues.get(2).getText();
            status = claimInformationValues.get(3).getText();
            settleMethod = claimInformationValues.get(4).getText();
        }
    }

    @Getter
    public class LossItem{

        protected ElementsCollection columns;
        String product;

        LossItem(SelenideElement element){

            columns = element.findAll("td");
        }

        public LossItem doAssert(Consumer<Asserts> assertFunc) {

            assertFunc.accept(new Asserts());
            return this;
        }

        public class Asserts {

            public Asserts assertProduct(String expectedProduct) {

                assertThat(product).isEqualTo(expectedProduct);
                return this;
            }

        }
    }

    @Getter
    public class LossItemInClaimsCalculation extends  LossItem{

        LossItemInClaimsCalculation(SelenideElement element){

            super(element);
            SelenideElement testElement = columns.get(2).find("span:nth-child(2)");
            product = testElement.getText();
            System.out.println();
        }
    }

    @Getter
    public class LossItemInDraft extends  LossItem{

        LossItemInDraft(SelenideElement element){

            super(element);
            product = columns.get(2).getText();
        }
    }

    @Override
    protected String getRelativeUrl() {


        return "webshop/jsp/matching_engine/customer_details.jsp";
    }

    @Override
    protected void ensureWeAreOnPage() {

        waitForUrl(getRelativeUrl());
        waitForAjaxCompletedAndJsRecalculation();

        if (driver.getCurrentUrl().contains(getRelativeUrl())) {

            $(getReopenClaim()).should(Condition.visible);
            cancelClaimButton.should(Condition.visible);
        } else {

            claimNumber.should(Condition.visible);
        }
    }

    public CustomerDetailsPage2 doAssert(Consumer<Asserts> assertFunc) {

        assertFunc.accept(new Asserts());
        return CustomerDetailsPage2.this;
    }

    public class Asserts {

        Asserts(){

            claimsInformation = new ClaimsInformation();
        }

        public Asserts assertClaimNumber(String expectedClaimNumber) {

            assertThat(claimsInformation.getClaimsNumber()).isEqualTo(expectedClaimNumber);
            return this;
        }

        public Asserts assertThatDraftIsEmpty() {

            assertThat(getLossItemsInDraft().size()).isZero();
            return this;
        }
    }
}
