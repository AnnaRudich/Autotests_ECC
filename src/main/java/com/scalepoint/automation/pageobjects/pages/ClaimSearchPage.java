package com.scalepoint.automation.pageobjects.pages;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.shared.ClaimStatus;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.data.entity.Claim;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompleted;
import static com.scalepoint.automation.utils.Wait.waitForPageLoaded;
import static org.assertj.core.api.Assertions.assertThat;

public class ClaimSearchPage extends Page {

    @Override
    protected ClaimSearchPage ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForAjaxCompleted();
        waitForJavascriptRecalculation();
        waitForPageLoaded();
        $(searchButton).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
        return this;
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/matching_engine/claim_search.jsp";
    }

    @FindBy(id = "namefield")
    private WebElement namefieldInput;

    @FindBy(id = "customernumberfield")
    private WebElement customernumberfieldInput;

    @FindBy(id = "claimnofield")
    private WebElement claimnofieldInput;

    @FindBy(id = "phonefield")
    private WebElement phonefieldInput;

    @FindBy(id = "settlementdatefield")
    private WebElement settlementdatefieldInput;

    @FindBy(id = "claimhandler")
    private WebElement claimhandlerInput;

    @FindBy(id = "addressfield")
    private WebElement addressfieldInput;

    @FindBy(id = "company")
    private WebElement companySelect;

    @FindBy(id = "claimstate")
    private WebElement claimstateSelect;

    @FindBy(id = "postalfield")
    private WebElement postalfieldInput;

    @FindBy(id = "soeg")
    private WebElement searchButton;


    public List<ClaimRow> getClaimRows() {
        return $(".claim-search-table").findAll("tr")
                .stream().skip(1).map(ClaimRow::new).collect(Collectors.toList());
    }

    public ClaimSearchPage fillClaimNumber(String claimNumber) {
        claimnofieldInput.clear();
        claimnofieldInput.sendKeys(claimNumber);
        return this;
    }

    public ClaimSearchPage fillCustomerNumber(String customerNumber) {
        customernumberfieldInput.clear();
        customernumberfieldInput.sendKeys(customerNumber);
        return this;
    }

    public ClaimSearchPage fillCompany(String company) {
        new Select(companySelect).selectByVisibleText(company);
        return this;
    }

    public ClaimSearchPage fillCustomerName(String customerName) {
        namefieldInput.clear();
        namefieldInput.sendKeys(customerName);
        return this;
    }

    public ClaimSearchPage search() {
        searchButton.click();
        Wait.waitForAjaxCompleted();
        return this;
    }

    public ClaimSearchPage fillClaimHandler(String claimHandler) {
        claimhandlerInput.clear();
        claimhandlerInput.sendKeys(claimHandler);
        return this;
    }

    public static class ClaimRow {

        private String name;
        private String claimNumber;
        private String claimHandler;
        private String company;
        private String claimState;
        private String settlementDate;
        private String activeDate;

        ClaimRow(WebElement element) {
            this.name = element.findElement(By.xpath("./td[1]//label")).getText();
            this.claimNumber = element.findElement(By.xpath("./td[2]")).getText();
            this.claimHandler = element.findElement(By.xpath("./td[3]")).getText();
            this.company = element.findElement(By.xpath("./td[4]")).getText();
            this.claimState = element.findElement(By.xpath("./td[5]")).getText();
            this.settlementDate = element.findElement(By.xpath("./td[6]")).getText();
            this.activeDate = element.findElement(By.xpath("./td[7]")).getText();
        }

        public String getName() {
            return name;
        }

        public String getClaimNumber() {
            return claimNumber;
        }

        public String getClaimHandler() {
            return claimHandler;
        }

        public String getCompany() {
            return company;
        }

        public String getClaimState() {
            return claimState;
        }

        public String getSettlementDate() {
            return settlementDate;
        }

        public String getActiveDate() {
            return activeDate;
        }
    }

    public ClaimSearchPage doAssert(Claim claim, Consumer<ClaimSearchPage.Asserts> assertsFunc) {
        assertsFunc.accept(new ClaimSearchPage.Asserts(claim));
        return ClaimSearchPage.this;
    }

    public ClaimSearchPage doAssert(Consumer<ClaimSearchPage.Asserts> assertsFunc) {
        return doAssert(null, assertsFunc);
    }

    public class Asserts {

        private Claim claim;
        private List<ClaimRow> claimRows;

        public Asserts(Claim claim) {
            this.claim = claim;
            this.claimRows = getClaimRows();
        }

        public Asserts isOnlyOnList() {
            assertThat(claimRows.stream().allMatch(claimRow -> claimRow.getClaimNumber().equals(this.claim.getClaimNumber()))).isTrue();
            return this;
        }

        public Asserts isClaimState(ClaimStatus state) {
            assertThat(claimRows.stream().filter(claimRow -> claimRow.getClaimNumber().equals(this.claim.getClaimNumber())).findFirst().orElseThrow(() -> new NoSuchElementException("Can't find claim with number " + this.claim.getClaimNumber()))
                    .getClaimState())
                    .isEqualToIgnoringCase(state.getText());
            return this;
        }

        public Asserts isClaimCompany(String companyName) {
            assertThat(claimRows.stream().filter(claimRow -> claimRow.getClaimNumber().equals(this.claim.getClaimNumber())).findFirst().orElseThrow(() -> new NoSuchElementException("Can't find claim with number " + this.claim.getClaimNumber())).getCompany())
                    .isEqualToIgnoringCase(companyName);
            return this;
        }

        public Asserts areClaimsMatchingCompany(String companyName) {
            assertThat(claimRows.stream().map(ClaimRow::getCompany)).allMatch(row -> row.equals(companyName));
            return this;
        }

        public Asserts areClaimsMatchingName() {
            assertThat(claimRows.stream().map(ClaimRow::getName)).allMatch(row -> row.contains(claim.getFirstName()));
            return this;
        }

        public Asserts areClaimsMatchingClaimHandler(String claimHandler) {
            assertThat(claimRows.stream().map(ClaimRow::getClaimHandler)).allMatch(row -> row.equals(claimHandler));
            return this;
        }
    }

}
