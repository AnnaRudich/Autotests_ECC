package com.scalepoint.automation.pageobjects.dialogs;

import com.scalepoint.automation.pageobjects.extjs.*;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.TextSearchPage;
import com.scalepoint.automation.services.externalapi.VoucherAgreementApi;
import com.scalepoint.automation.utils.JavascriptHelper;
import com.scalepoint.automation.utils.JavascriptHelper.Snippet;
import com.scalepoint.automation.utils.OperationalUtils;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.driver.Browser;
import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.Op;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Link;
import ru.yandex.qatools.htmlelements.element.Table;
import ru.yandex.qatools.htmlelements.element.TextBlock;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.OperationalUtils.assertEqualsDouble;
import static com.scalepoint.automation.utils.Wait.waitForVisible;
import static org.testng.Assert.*;

public class SettlementDialog extends BaseDialog {

    private static final int DEPRECIATION_COLUMN = 4;
    private static final int TOTAL_AMOUNT_OF_VALUATION = 5;
    private static final int AMOUNT_OF_VALUATION = 3;

    @FindBy(id = "description-textfield-inputEl")
    private ExtInput description;

    @FindBy(id = "group-combobox")
    private ExtComboBox category;

    @FindBy(id = "pseudocategory-combobox")
    private ExtComboBox subCategory;

    @FindBy(id = "age-months-combobox")
    private ExtComboBox month;

    @FindBy(id = "available-vouchers-combobox")
    private ExtComboBox availableVoucher;

    @FindBy(id = "vouchers-combobox")
    private ExtComboBox voucher;

    @FindBy(id = "quantity-textfield-inputEl")
    private ExtInput quantity;

    @FindBy(id = "customer-demand-textfield-inputEl")
    private ExtInput customerDemand;

    @FindBy(id = "new-price-textfield-inputEl")
    private ExtInput newPrice;

    @FindBy(id = "depreciation-textfield-inputEl")
    private ExtInput depreciationPercentage;

    @FindBy(id = "discretionary-replacement-textfield-inputEl")
    private ExtInput discretionaryPrice;

    @FindBy(id = "age-defined-radiogroup")
    private ExtRadioGroup age;

    @FindBy(id = "reviewed-checkbox")
    private ExtCheckbox reviewed;

    @FindBy(id = "total-cash-compensation-text")
    private TextBlock cashCompensationValue;

    @FindBy(id = "total-depreciation-text")
    private TextBlock deprecationAmount;

    @FindBy(id = "ok-button")
    private Button ok;

    @FindBy(id = "add-button")
    private Button addButton;

    @FindBy(id = "voucher-face-value-text")
    private WebElement voucherFaceValue;

    @FindBy(id = "voucher-price-text")
    private WebElement voucherCashValue;

    @FindBy(id = "depreciate-discounted-checkbox")
    private ExtCheckbox combineDiscountDepreciation;

    @FindBy(id = "active-checkbox")
    private ExtCheckbox includeInClaim;

    @FindBy(css = "#valuations-grid-body table")
    private List<Table> valuations;

    @FindBy(css = "#valuations-grid-body table:first-child")
    private Table firstValuation;

    @FindBy(id = "cancel-button")//
    private Button cancel;

    @FindBy(id = "add-valuation-button")
    private Link addValuation;

    @FindBy(id = "age-years-textfield-inputEl")
    private ExtInput ageYears;

    @FindBy(css = "#voucher-supplier-link a")
    private Link brand;

    @FindBy(id = "voucher-valuation-card-edit-valuation")
    private Button voucherValuationCard;

    @FindBy(id = "voucher-valuation-card-valuation-terms")
    private Button voucherTermAndConditions;

    @FindBy(id = "not-cheapest-reason-edit-button")
    private Button notCheapestReasonEdit;

    @FindBy(css = "#rule-suggestion-grid-body table")
    private Table ruleSuggestion;

    @FindBy(id = "automatic-depreciation-checkbox")
    private ExtCheckbox automaticDepreciation;

    @FindBy(id = "marketprice-card-supplier-inputEl")
    private WebElement marketPriceSupplier;

    @FindBy(id = "productmatch-card-supplier")
    private TextBlock statusSupplier;

    @FindBy(id = "reject-reason-combobox")
    private ExtComboBox rejectReason;

    @FindBy(id = "discretionary-reason-combobox")
    private ExtComboBox discretionaryReason;

    @FindBy(id = "depreciation-type-combobox")
    private ExtComboBox depreciationType;

    @FindBy(id = "not-cheapest-reason-display-inputEl")
    private TextBlock notCheapestReasonDisplay;

    @Override
    public SettlementDialog ensureWeAreAt() {
        Wait.waitForAjaxCompleted();
        waitForVisible(cancel);
        JavascriptHelper.loadSnippet(Snippet.SID_GROUPS_LOADED);
        return this;
    }

    public SettlementDialog fillBaseData(ClaimItem claimItem) {
        return fillDescription(claimItem.getTextFieldSP()).
                fillCustomerDemand(claimItem.getCustomerDemand_500()).
                fillNewPrice(claimItem.getNewPriceSP_2400()).
                fillCategory(claimItem.getExistingCat1_Born()).
                fillSubCategory(claimItem.getExistingSubCat1_Babyudstyr());
    }

    public SettlementDialog fillBaseData(String description, String category, String subcategory, Double newPrice) {
        return fillDescription(description).
                fillNewPrice(newPrice).
                fillCategory(category).
                fillSubCategory(subcategory);
    }

    private SettlementDialog setExtInputValue(ExtInput input, String value) {
        waitForVisible(input);

        input.enter(value);
        simulateBlurEvent(input);

        return this;
    }

    private void simulateBlurEvent(ExtElement input) {
        ExtInput inputForClick = input == quantity ? description : quantity;
        inputForClick.getRootElement().click();
    }

    public SettlementDialog fillDescription(String descriptionText) {
        return setExtInputValue(description, descriptionText);
    }

    public SettlementDialog fillNewPrice(Double amount) {
        return setExtInputValue(newPrice, OperationalUtils.format(amount));
    }

    public SettlementDialog fillDiscretionaryPrice(Double amount) {
        return setExtInputValue(discretionaryPrice, OperationalUtils.format(amount));
    }

    public SettlementDialog fillCustomerDemand(Double amount) {
        return setExtInputValue(customerDemand, OperationalUtils.format(amount));
    }

    public SettlementDialog fillDepreciation(Integer amount) {
        return setExtInputValue(depreciationPercentage, amount.toString());
    }

    public SettlementDialog fillCategory(String categoryName) {
        category.select(categoryName);
        return this;
    }

    public SettlementDialog fillSubCategory(String subCategoryName) {
        subCategory.select(subCategoryName);
        return this;
    }

    public SettlementDialog fillCategory(VoucherAgreementApi.AssignedCategory categoryInfo) {
        fillCategory(categoryInfo.getCategory());
        fillSubCategory(categoryInfo.getSubCategory());
        return this;
    }

    public SettlementDialog selectMonth(String monthName) {
        month.select(monthName);
        return this;
    }

    public SettlementDialog fillVoucher(String voucherName) {
        if (voucher.isDisplayed()) {
            voucher.select(voucherName);
        } else {
            Wait.waitUntilVisible(availableVoucher);
            availableVoucher.select(voucherName);
        }
        return this;
    }

    /*we can't control SID recalculation/redraw, so just wait*/
    public SettlementDialog waitASecond() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this;
    }

    public SettlementDialog enableAge() {
        age.select(1);
        return this;
    }

    public SettlementDialog enableAge(String years) {
        enableAge();
        enterAgeYears(years);
        return this;
    }

    public SettlementDialog disableAge() {
        age.select(0);
        return this;
    }

    private Double fetchDepreciationAmount() {
        Wait.waitForLoaded();
        waitForVisible(deprecationAmount);
        return OperationalUtils.getDoubleValue(deprecationAmount.getText());
    }

    public SettlementDialog setReviewed(boolean state) {
        reviewed.set(state);
        return this;
    }

    public SettlementPage closeSidWithOk() {
        return closeSidWithOk(SettlementPage.class, ok);
    }

    public SettlementDialog clickOK() {
        ok.click();
        return this;
    }

    public <T extends Page> T closeSidWithOk(Class<T> pageClass) {
        return closeSidWithOk(pageClass, ok);
    }

    public TextSearchPage add() {
        TextSearchPage textSearchPage = closeSidWithOk(TextSearchPage.class, addButton);
        if (isAlertPresent()) {
            acceptAlert();
        }
        return textSearchPage;
    }

    public <T extends Page> T closeSidWithOk(Class<T> pageClass, Button button) {
        return closeDialog(pageClass, button);
    }

    public SettlementPage cancel() {
        return cancel(SettlementPage.class);
    }

    public <T extends Page> T cancel(Class<T> pageClass) {
        return closeDialog(pageClass, cancel);
    }

    private <T extends Page> T closeDialog(Class<T> pageClass, Button button) {
        waitForVisible(button);
        button.click();

        Wait.waitElementDisappeared(button);
        Wait.waitForAjaxCompleted();
        return Page.at(pageClass);
    }

    public SettlementDialog setDiscountAndDepreciation(Boolean state) {
        waitForVisible(combineDiscountDepreciation);
        combineDiscountDepreciation.set(state);
        return this;
    }

    public SettlementDialog includeInClaim(Boolean state) {
        Wait.waitForLoaded();
        Wait.waitForEnabled(includeInClaim);
        includeInClaim.set(state);
        return this;
    }

    private String getValuationColumnValue(Valuation valuation, int column) {
        return driver.findElement(By.xpath(".//*[contains(@class, '" + valuation.className + "')]//td[" + column + "]")).getText();
    }

    public SettlementDialog selectValuation(Valuation valuation) {
        By xpath = By.xpath("//tr[contains(@class, '" + valuation.className + "')]//div[@role='button']");
        Wait.waitForStaleElement(xpath);

        WebElement webElement = Browser.driver().findElement(xpath);
        boolean checked = webElement.getAttribute("class").contains("x-grid-checkcolumn-checked");
        if (!checked) {
            //one click doesn't work, each click renew dom so we should wait for stale element each time
            for (int i = 0; i < 3; i++) {
                Wait.waitForStaleElement(xpath);
                webElement = Browser.driver().findElement(xpath);
                webElement.click();
            }

        }
        waitASecond();
        return this;
    }

    private Double voucherFaceValueFieldText() {
        Wait.waitForLoaded();
        waitForVisible(voucherFaceValue);
        return OperationalUtils.getDoubleValue(voucherFaceValue.getText());
    }

    private Double voucherCashValueFieldText() {
        waitForVisible(voucherCashValue);
        return OperationalUtils.getDoubleValue(voucherCashValue.getText());
    }

    public Double customerDemandValue() {
        Wait.waitForLoaded();
        waitForVisible(customerDemand);
        return OperationalUtils.getDoubleValue(customerDemand.getText());
    }

    public Double getCashCompensationValue() {
        waitForVisible(cashCompensationValue);
        return OperationalUtils.getDoubleValue(cashCompensationValue.getText());
    }

    public Double DeprecationValue() {
        waitForVisible(deprecationAmount);
        return OperationalUtils.getDoubleValue(deprecationAmount.getText());
    }

    private String getCategoryText() {
        Wait.waitForLoaded();
        waitForVisible(category);
        return category.getValue();
    }

    private String getSubCategoryText() {
        Wait.waitForLoaded();
        waitForVisible(subCategory);
        return subCategory.getValue();
    }

    public String getDescriptionText() {
        Wait.waitForLoaded();
        waitForVisible(description);
        return description.getText();
    }

    public AddValuationDialog addValuation() {
        addValuation.click();
        return at(AddValuationDialog.class);
    }

    public SettlementDialog enterAgeYears(String _ageYears) {
        return setExtInputValue(ageYears, _ageYears);
    }

    public String getNotCheapestChoiceReason() {
        return $(notCheapestReasonDisplay).getText();
    }

    public EditVoucherValuationDialog openVoucherValuationCard() {
        $(voucherValuationCard).click();
        return BaseDialog.at(EditVoucherValuationDialog.class);
    }

    public VoucherTermsAndConditionsDialog openVoucherTermAndConditions() {
        $(voucherTermAndConditions).click();
        return at(VoucherTermsAndConditionsDialog.class);
    }

    public SettlementDialog applyReductionRuleByValue(Integer reductionRuleValue) {
        Wait.waitForLoaded();
        boolean foundRule = false;
        List<List<WebElement>> rowsNames = ruleSuggestion.getRows();
        for (List<WebElement> list : rowsNames) {
            String reduction = list.get(1).getText().replaceAll("%", "");
            if (reduction.equals(reductionRuleValue.toString())) {
                list.get(2).findElement(By.tagName("img")).click();
                foundRule = true;
            }
        }
        if (!foundRule) {
            throw new IllegalStateException("Reduction rule with value: " + reductionRuleValue + " not found");
        }
        Wait.forCondition(webDriver -> {
            try {
                String valuationDepreciation = Browser.driver().findElement(By.xpath(".//tr[contains(@class, '" + Valuation.NEW_PRICE + "')]//td[4]//div")).getText();
                return reductionRuleValue.toString().equals(valuationDepreciation);
            } catch (Exception e) {
                logger.error("Can't compare reduction and depreciationPercentage! " + e.getMessage(), e);
            }
            return true;
        });

        return this;
    }

    public SettlementDialog automaticDepreciation(boolean state) {
        Wait.waitForEnabled(automaticDepreciation);
        automaticDepreciation.set(state);
        Wait.waitForLoaded();
        return this;
    }

    public Integer getDepreciationPercentage() {
        return Integer.valueOf(depreciationPercentage.getText());
    }

    private boolean isMarketPriceSupplierDisplayed() {
        try {
            return marketPriceSupplier.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public enum Valuation {
        NOT_SELECTED("valuation-type-NOT_SELECTED"),
        CUSTOMER_DEMAND("valuation-type-CUSTOMER_DEMAND"),
        VOUCHER("valuation-type-VOUCHER"),
        NEW_PRICE("valuation-type-NEW_PRICE"),
        MARKET_PRICE("valuation-type-MARKET_PRICE"),
        ANDEN_VURDERING("valuation-type-DISCRETIONARY_VALUATION");

        private String className;

        Valuation(String className) {
            this.className = className;
        }
    }

    private boolean isRejectReasonVisible() {
        return rejectReason.exists();
    }

    private boolean isRejectReasonEnabled() {
        return $(rejectReason).isEnabled();
    }

    private boolean isDiscretionaryReasonVisible() {
        return (discretionaryReason.exists());
    }

    private boolean isDiscretionaryReasonEnabled() {
        waitForVisible(discretionaryReason);
        return discretionaryReason.isEnabled();
    }

    public SettlementDialog selectDiscretionaryReason(String visibleText) {
        waitForVisible(discretionaryReason);
        discretionaryReason.select(visibleText);
        return this;
    }

    public SettlementDialog selectDepreciationType(int index) {
        waitForVisible(depreciationType);
        depreciationType.select(index);
        return this;
    }

    public SettlementDialog selectDepreciationType(String visibleText) {
        waitForVisible(depreciationType);
        depreciationType.select(visibleText);
        return this;
    }

    public NotCheapestChoiceDialog editNotCheapestReason() {
        waitForVisible(notCheapestReasonEdit);
        notCheapestReasonEdit.click();

        return at(NotCheapestChoiceDialog.class);
    }

    public SettlementDialog selectOtherCategoryIfNotChosen() {
        String value = category.getValue();
        if (StringUtils.isBlank(value)) {
            category.select("Øvrige");
        }
        return this;
    }

    private String getDiscretionaryReasonText() {
        return discretionaryReason.getValue();
    }

    private boolean isDiscretionaryReasonHasRedBorder() {
        String redBorder = "#c30";
        return driver.findElement(By.id("discretionary-reason-combobox-inputWrap")).getAttribute("class").contains("x-form-text-wrap-invalid")
                && driver.findElement(By.id("discretionary-reason-combobox-inputWrap")).getCssValue("border-color").contains(redBorder);
    }


    //ASSERTS
    public SettlementDialog assertDiscretionaryReasonValuePresent(String expectedValue) {
        List<String> options = discretionaryReason.getComboBoxOptions();
        Assert.assertTrue(options.stream().anyMatch(i -> i.contains(expectedValue)));
        return this;
    }

    public SettlementDialog assertCashValueIs(Double expectedCashValue) {
        assertEqualsDouble(getCashCompensationValue(), expectedCashValue, "Cash compensation is incorrect");
        return this;
    }

    public SettlementDialog assertVoucherCashValueIs(Double expectedVoucherCashValue) {
        assertEqualsDouble(voucherCashValueFieldText(), expectedVoucherCashValue, "Voucher cash value is incorrect");
        return this;
    }

    public SettlementDialog assertVoucherFaceValueIs(Double expectedVoucherCashValue) {
        assertEqualsDouble(voucherFaceValueFieldText(), expectedVoucherCashValue, "Voucher face value is incorrect");
        return this;
    }

    public SettlementDialog assertDepreciationAmountIs(Double expectedDepreciation) {
        assertEqualsDouble(fetchDepreciationAmount(), expectedDepreciation, "Depreciation is incorrect");
        return this;
    }

    public SettlementDialog assertDescriptionIs(String expectedDescription) {
        assertEquals(getDescriptionText(), expectedDescription, "The Description is not saved");
        return this;
    }

    public SettlementDialog assertCategoryTextIs(String expectedCategory) {
        assertEquals(getCategoryText(), expectedCategory, "The Category is not Saved");
        return this;
    }

    public SettlementDialog assertSubCategoryTextIs(String expectedSubCategory) {
        assertEquals(getSubCategoryText(), expectedSubCategory, "The Subcategory is not Saved");
        return this;
    }

    public SettlementDialog assertReviewedNotPresent() {
        try {
            assertFalse(reviewed.isEnabled(), "Reviewed checkbox msu be disabled");
        } catch (Exception ignored) {
        }
        return this;
    }

    public <T extends BaseDialog> T assertAfterOkWeGet(Class<T> dialogClass) {
        waitForVisible(ok).click();
        try {
            return at(dialogClass);
        } catch (Exception e) {
            Assert.fail("We must get after ok: " + dialogClass);
            throw e;
        }
    }

    public SettlementDialog assertVoucherListed(String voucherTitle) {
        List<String> options;
        if (voucher.isDisplayed())
            options = voucher.getComboBoxOptions();
        else
            options = availableVoucher.getComboBoxOptions();
        Assert.assertTrue(options.stream().anyMatch(i -> i.contains(voucherTitle)), "Voucher " + voucherTitle + " must be present");
        return this;
    }

    public SettlementDialog assertAmountOfValuationEqualTo(Double amount, Valuation valuation) {
        Assert.assertTrue(anyMatchFromValuationsTable(amount, valuation, AMOUNT_OF_VALUATION), valuation.name() + " has not been added");
        return this;
    }

    public SettlementDialog assertTotalAmountOfValuationIs(Double amount, Valuation valuation) {
        Assert.assertTrue(anyMatchFromValuationsTable(amount, valuation, TOTAL_AMOUNT_OF_VALUATION), valuation.name() + " has not been added");
        return this;
    }

    public SettlementDialog assertDepreciationPercentageEqualTo(Integer amount, Valuation valuation) {
        Assert.assertTrue(anyMatchFromValuationsTable(amount.doubleValue(), valuation, DEPRECIATION_COLUMN), valuation.name() + " has not been added");
        return this;
    }

    private boolean anyMatchFromValuationsTable(Double value, Valuation valuation, int column) {
        Wait.waitForLoaded();
        waitForVisible(firstValuation);
        String foundText = getValuationColumnValue(valuation, column);
        boolean equals = OperationalUtils.toNumber(foundText).equals(value);
        logger.info("Valuation requested: {} found: {} matched: {}", value, foundText, equals);
        return equals;
    }

    public SettlementDialog assertIncludeInClaimSelected() {
        Assert.assertTrue(includeInClaim.isSelected(), "The 'Include in Claim' must be selected'");
        return this;
    }

    public SettlementDialog assertIncludeInClaimNotSelected() {
        Assert.assertFalse(includeInClaim.isSelected(), "The 'Include in Claim' must be unselected'");
        return this;
    }

    public SettlementDialog assertNotCheapestReasonIs(String reason) {
        assertEquals(getNotCheapestChoiceReason(), reason, "Reason must be: " + reason);
        return this;
    }

    public SettlementDialog assertSubCategoriesListEqualTo(List<String> expectedSubCategoriesList) {
        waitForVisible(subCategory);
        List<String> stringList = new ArrayList<>();
        List<String> allCategories = subCategory.getComboBoxOptions();
        for (String allCategory : allCategories) {
            String normalizedString = allCategory.replaceAll("[\\s\\.:,%]", "").replaceAll("(\\[)?(.+?)(\\])?", "$2");
            stringList.add(normalizedString);
        }
        assertEqualsNoOrder(stringList.toArray(), expectedSubCategoriesList.toArray(), "Category is not selected");
        return this;
    }

    public SettlementDialog assertVoucherCashValueIs(String expectedValue) {
        assertEquals(voucherCashValueFieldText(), OperationalUtils.getDoubleValue(expectedValue));
        return this;
    }

    public SettlementDialog assertAgeYearsEnabled() {
        assertTrue($(ageYears).isEnabled(), "Age Years field must be enabled");
        return this;
    }

    public SettlementDialog assertMonthMenuEnabled() {
        assertTrue($(month).isEnabled(), "Month DropDown must be enabled");
        return this;
    }

    public SettlementDialog assertAgeYearsDisabled() {
        assertFalse($(ageYears).isEnabled(), "Age Years field must be disabled");
        return this;
    }

    public SettlementDialog assertMonthMenuDisabled() {
        assertFalse(month.isEnabled(), "Month DropDown must be disabled");
        return this;
    }

    public SettlementDialog assertMonthValueIs(String expectedMonthValue) {
        assertEquals(month.getValue().trim(), expectedMonthValue, "The month is not saved");
        return this;
    }

    public SettlementDialog assertYearsValueIs(String expectedValue) {
        assertEquals(ageYears.getText(), expectedValue, "The age year is not saved");
        return this;
    }

    public SettlementDialog assertDepreciationValueIs(Double expectedDepreciationValue) {
        assertEqualsDouble(Double.valueOf(depreciationPercentage.getText()), expectedDepreciationValue, "Depreciation percentage incorrect");
        return this;
    }

    public SettlementDialog assertMarketPriceVisible() {
        String failMessage = "Market price must be visible";
        try {
            if (getValuationColumnValue(Valuation.MARKET_PRICE, 1) == null) {
                Assert.fail(failMessage);
            }
        } catch (Exception e) {
            Assert.fail(failMessage);
        }
        return this;
    }

    public SettlementDialog assertMarketPriceSupplierInvisible() {
        assertFalse(isMarketPriceSupplierDisplayed(), "Market price must be not visible");
        return this;
    }

    public SettlementDialog assertRejectReasonDisabled() {
        assertFalse(isRejectReasonEnabled(), "Reject Reason must be disabled");
        return this;
    }

    public SettlementDialog assertRejectReasonVisible() {
        assertTrue(isRejectReasonVisible(), "Reject Reason must be visible");
        return this;
    }

    public SettlementDialog assertDiscretionaryReasonInvisible() {
        assertFalse(isDiscretionaryReasonVisible(), "Discretionary Reason must be invisible");
        return this;
    }

    public SettlementDialog assertDiscretionaryReasonVisible() {
        assertTrue(isDiscretionaryReasonVisible(), "Discretionary Reason must be visible");
        return this;
    }

    public SettlementDialog assertDiscretionaryReasonDisabled() {
        assertFalse(isDiscretionaryReasonEnabled(), "Discretionary Reason must be disabled");
        return this;
    }

    public SettlementDialog assertDiscretionaryReasonEnabled() {
        assertTrue(isDiscretionaryReasonEnabled(), "Discretionary Reason must be enabled");
        return this;
    }

    public SettlementDialog assertDiscretionaryReasonHasRedBorder() {
        Assert.assertTrue(isDiscretionaryReasonHasRedBorder(), "Discretionary Reason field should have red border");
        return this;
    }

    public SettlementDialog assertDiscretionaryReasonHasNormalBorder() {
        Assert.assertFalse(isDiscretionaryReasonHasRedBorder(), "Discretionary Reason field should have normal border");
        return this;
    }

    public SettlementDialog assertDiscretionaryReasonEqualTo(String reason) {
        assertEquals(getDiscretionaryReasonText(), reason, "Wrong reason selected for New Price");
        return this;
    }

    public SettlementDialog assertBrandTextIs(String brandLink) {
        assertEquals($(brand).getText(), brandLink, "Wrong Brand is Displayed");
        return this;
    }

    public SettlementDialog assertScalepointSupplierNotVisible() {
        assertFalse(statusSupplier.exists(), "Scalepoint supplier must not be visible");
        return this;
    }

    public SettlementDialog assertScalepointSupplierVisible(String supplier) {
        assertTrue(statusSupplier.getText().contains(supplier), "Scalepoint supplier must be visible");
        return this;
    }

}

