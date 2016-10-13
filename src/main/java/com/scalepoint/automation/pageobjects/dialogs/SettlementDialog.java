package com.scalepoint.automation.pageobjects.dialogs;

import com.google.common.base.Function;
import com.scalepoint.automation.pageobjects.extjs.*;
import com.scalepoint.automation.services.externalapi.VoucherAgreementApi;
import com.scalepoint.automation.utils.OperationalUtils;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.Voucher;
import com.scalepoint.automation.utils.driver.Browser;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.scalepoint.automation.utils.Wait.waitForVisible;

public class SettlementDialog extends BaseDialog {

    public static final int DEPRECIATION_COLUMN = 4;
    public static final int TOTAL_AMOUNT_OF_VALUATION = 5;
    public static final int AMOUNT_OF_VALUATION = 3;

    @FindBy(id = "description-textfield-inputEl")
    private ExtInput description;

    @FindBy(id = "group-combobox")
    private ExtComboBox category;

    @FindBy(id = "pseudocategory-combobox")
    private ExtComboBox subCategory;

    @FindBy(id = "age-months-combobox")
    private ExtComboBox month;

    @FindBy(id = "available-vouchers-combobox")
    private ExtComboBox avoucher;

    @FindBy(id = "vouchers-combobox")
    private ExtComboBox voucher;

    @FindBy(id = "quantity-textfield-inputEl")
    private ExtInput quantity;

    @FindBy(id = "customer-demand-textfield-inputEl")
    private ExtInput customerDemand;

    @FindBy(id = "new-price-textfield")
    private ExtText newPrice;

    @FindBy(id = "depreciation-textfield-inputEl")
    private TextInput depreciation;

    @FindBy(id = "reject-checkbox")
    private ExtCheckbox rejected;

    @FindBy(id = "age-defined-radiogroup")
    private ExtRadioGroup age;

    @FindBy(id = "reviewed-checkbox")
    private ExtCheckbox reviewed;

    @FindBy(id = "valuations-grid")
    private ExtRadioGroup valuation;

    @FindBy(id = "total-cash-compensation-text")
    private TextBlock cashCompensationValue;

    @FindBy(id = "total-depreciation-text")
    private TextBlock deprecationValue;

    @FindBy(id = "ok-button")
    private Button ok;

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

    @FindBy(id = "cancel-button")
    private Button cancel;

    @FindBy(id = "manual-valuation-card-edit-valuation")
    private Link editValuation;

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

    @FindBy(css = "#rule-suggestion-grid-body table")
    private Table ruleSuggestion;

    @FindBy(id = "automatic-depreciation-checkbox")
    private ExtCheckbox automaticDepreciation;

    @FindBy(id = "marketprice-card-price-inputEl")
    private TextBlock marketPrice;

    @FindBy(id = "marketprice-card-supplier-inputEl")
    private TextBlock marketPriceSupplier;

    @FindBy(id = "productmatch-card-supplier")
    private TextBlock statusSupplier;

    private String enteredDescription;

    @Override
    public SettlementDialog ensureWeAreAt() {
        Wait.waitForAjaxComplete();
        waitForVisible(cancel);
        return this;
    }

    public SettlementDialog fillBaseData(ClaimItem claimItem) {
        return fillDescription(claimItem.getTextFieldSP()).
                fillCustomerDemand(claimItem.getCustomerDemand_500()).
                fillNewPrice(claimItem.getNewPriceSP_2400()).
                fillCategory(claimItem.getExistingCat1_Born()).
                fillSubCategory(claimItem.getExistingSubCat1_Babyudstyr());
    }

    public SettlementDialog fillDescription(String descriptionText) {
        this.enteredDescription = descriptionText;
        description.setValue(descriptionText);
        return this;
    }

    public SettlementDialog fillNewPrice(int amount) {
        newPrice.enter(String.valueOf(amount));
        return this;
    }

    public SettlementDialog fillCustomerDemand(int amount) {
        customerDemand.clear();
        customerDemand.sendKeys(String.valueOf(amount));
        newPrice.getWrappedElement().click();
        return this;
    }

    public SettlementDialog fillDepreciation(int amount) {
        depreciation.clear();
        depreciation.sendKeys(String.valueOf(amount));
        description.getWrappedElement().click();
        return this;
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

    /**
     * @param monthName should be digit
     */
    public SettlementDialog selectMonth(String monthName) {
        month.select(monthName);
        return this;
    }

    public SettlementDialog fillVoucher(String voucherName) {
        if (voucher.isDisplayed()) {
            voucher.select(voucherName);
        } else {
            Wait.waitUntilVisible(avoucher);
            avoucher.select(voucherName);
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

    public SettlementDialog fillVoucher(int voucherName) {
        avoucher.select(voucherName);
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

    public int getAgeDefined() {
        return age.getSelected();
    }

    public SettlementDialog SetRejected(boolean state) {
        rejected.set(state);
        return this;
    }

    public boolean getRejected() {
        return rejected.isSelected();
    }

    public SettlementDialog FillInItem(ClaimItem claimItem) {
        Wait.waitForLoaded();
        fillDescription(claimItem.getTextFieldSP());
        fillNewPrice(claimItem.getNewPriceSP_2400());
        fillCategory(claimItem.getExistingCat1_Born());
        fillSubCategory(claimItem.getExistingSubCat1_Babyudstyr());
        setReviewed(true);
        return this;
    }

    public String FetchCashCompensation() {
        return cashCompensationValue.getText();
    }

    public Double fetchDepreciation() {
        Wait.waitForLoaded();
        waitForVisible(deprecationValue);
        return OperationalUtils.getDoubleValue(deprecationValue.getText());
    }

    public SettlementDialog setReviewed(boolean state) {
        reviewed.set(state);
        return this;
    }

    public boolean getReviewed() {
        return reviewed.isSelected();
    }

    public boolean isReviewedEnabled() {
        return reviewed.isEnabled();
    }

    public boolean isReviewedPresent() {
        try {
            reviewed.isEnabled();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public SettlementDialog SelectValuation(int index) {
        valuation.select(index);
        return this;
    }

    public int getValuation() {
        return valuation.getSelected();
    }

    public void ok() {
        waitForVisible(ok);
        if (StringUtils.isBlank(description.getText())) {
            description.setValue(enteredDescription);
        }
        ok.click();
        Wait.waitForElementDisappear(ok);
        Wait.waitForAjaxComplete();
    }

    public boolean isVoucherListed(Voucher _voucher) {
        List<String> options;
        if (voucher.isDisplayed())
            options = voucher.getComboBoxOptions();
        else
            options = avoucher.getComboBoxOptions();
//        reporter.reportScreenshot("Voucher Listed");
        return options.stream().anyMatch(i -> i.contains(_voucher.getVoucherNameSP()));
    }

    public void cancel() {
        cancel.click();
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

    public boolean isAmountOfValuationEqualTo(Integer amount, Valuation valuation) {
        return anyMatchFromValuationsTable(amount.toString(), valuation, AMOUNT_OF_VALUATION);
    }

    public boolean isTotalAmountOfValuationEqualTo(String amount, Valuation valuation) {
        return anyMatchFromValuationsTable(amount, valuation, TOTAL_AMOUNT_OF_VALUATION);
    }

    public boolean isTotalAmountOfValuationEqualTo(Integer amount, Valuation valuation) {
        return anyMatchFromValuationsTable(amount.toString(), valuation, TOTAL_AMOUNT_OF_VALUATION);
    }

    public boolean isDepreciationPercentEqualTo(String amount, Valuation valuation) {
        return anyMatchFromValuationsTable(amount, valuation, DEPRECIATION_COLUMN);
    }

    private boolean anyMatchFromValuationsTable(String value, Valuation valuation, int column) {
        Wait.waitForLoaded();
        waitForVisible(firstValuation);
        String foundText = getValuationColumnValue(valuation, column);
        boolean equals = OperationalUtils.toNumber(foundText).equals(Double.parseDouble(value));
        logger.info("Valuation requested: {} found: {} matched: {}", value, foundText, equals);
        return equals;
    }

    private String getValuationColumnValue(Valuation valuation, int column) {
            return driver.findElement(By.xpath(".//*[contains(@class, '" + valuation.className + "')]//td[" + column + "]")).getText();
    }

    public boolean isIncludeInClaimSet() {
        Wait.waitForLoaded();
        waitForVisible(includeInClaim);
        return includeInClaim.isSelected();
    }

    public List<String> getCategoriesList() {
        Wait.waitForLoaded();
        waitForVisible(subCategory);
        List<String> stringList = new ArrayList<>();
        List<String> allCategories = subCategory.getComboBoxOptions();
        for (String allCategory : allCategories) {
            String normalizedString = allCategory.replaceAll("[\\s\\.:,%]", "").replaceAll("(\\[)?(.+?)(\\])?", "$2");
            stringList.add(normalizedString);
        }
        return stringList;
    }

    public SettlementDialog selectValuation(Valuation valuation) {
        for (int i = 0; i < 5; i++) {
            driver.findElement(By.cssSelector("tr." + valuation.className + " .x-form-radio-default")).click();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    private void waitTillValuationRecalculated(Valuation valuation) {
        String valuationName = driver.findElement(By.xpath(".//tr[contains(@class, '" + valuation.className + "')]//td[2]//div")).getText();
        List<String> valuationNameParts = splitByWord(valuationName);
        try {
            Wait.For((Function<WebDriver, Object>) webDriver -> {
                String assessmentBasedOn = driver.findElement(By.xpath(".//div[contains(@class, 'assessmentBaseManualText')]//div[@role='textbox']")).getText();
                List<String> assessmentTextParts = splitByWord(assessmentBasedOn);
                return !Collections.disjoint(valuationNameParts, assessmentTextParts);
            });
        } catch (Exception e) {
            logger.info("Couldn't compare current valuation with assessmentText");
        }
    }

    private List<String> splitByWord(String valuationName) {
        return Arrays.stream(valuationName.split(" ")).map(String::toLowerCase).collect(Collectors.toList());
    }


    public Double voucherFaceValueFieldText() {
        Wait.waitForLoaded();
        waitForVisible(voucherFaceValue);
        return OperationalUtils.getDoubleValue(voucherFaceValue.getText());
    }

    public Double voucherCashValueFieldText() {
        Wait.waitForLoaded();
        waitForVisible(voucherCashValue);
        return OperationalUtils.getDoubleValue(voucherCashValue.getText());
    }

    public Double customerDemandValue() {
        Wait.waitForLoaded();
        waitForVisible(customerDemand);
        return OperationalUtils.getDoubleValue(customerDemand.getText());
    }

    public Double cashCompensationValue() {
        waitForVisible(cashCompensationValue);
        return OperationalUtils.getDoubleValue(cashCompensationValue.getText());
    }

    public Double DeprecationValue() {
        waitForVisible(deprecationValue);
        return OperationalUtils.getDoubleValue(deprecationValue.getText());
    }

    public String getCategoryText() {
        Wait.waitForLoaded();
        waitForVisible(category);
        return category.getValue();
    }

    public String getSubCategoryText() {
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
        waitForVisible(ageYears);
        ageYears.enter(_ageYears);
        ageYears.sendKeys(Keys.TAB);
        return this;
    }

    public boolean ageYearsIsEnabled() {
        waitForVisible(ageYears);
        return ageYears.isEnabled();
    }

    public boolean monthMenuIsEnabled() {
        waitForVisible(month);
        return month.isEnabled();
    }

    public String getMonthValue() {
        waitForVisible(month);
        return month.getValue();
    }

    public String getAgeYears() {
        Wait.waitForLoaded();
        waitForVisible(ageYears);
        return ageYears.getText();
    }

    public String getBrandText() {
        Wait.waitForLoaded();
        waitForVisible(brand);
        return brand.getText();
    }

    public EditVoucherValuationDialog openVoucherValuationCard() {
        waitForVisible(voucherValuationCard);
        voucherValuationCard.click();
        return BaseDialog.at(EditVoucherValuationDialog.class);
    }

    public VoucherTermsAndConditionsDialog openVoucherTermAndConditions() {
        waitForVisible(voucherTermAndConditions);
        voucherTermAndConditions.click();
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
        Wait.For(webDriver -> {
            try {
                String valuationDepreciation = Browser.driver().findElement(By.xpath(".//tr[contains(@class, '" + Valuation.NEW_PRICE + "')]//td[4]//div")).getText();
                return reductionRuleValue.toString().equals(valuationDepreciation);
            } catch (Exception e) {
                logger.error("Can't compare reduction and depreciation! " + e.getMessage(), e);
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

    public Double getDepreciationValue() {
        return Double.valueOf(depreciation.getText());
    }

    public String getDepreciation(){
        return depreciation.getText();
    }

    public boolean isMarketPriceVisible() {
        try {
            return getValuationColumnValue(Valuation.MARKET_PRICE, 1) != null;
        } catch (Exception e) {
            return false;
        }
    }

    public String marketPriceSupplier() {
        return marketPriceSupplier.getText();
    }

    public boolean isMarketPriceSupplierVisible() {
        try {
            return marketPriceSupplier.exists();
        } catch (Exception e) {
            return false;
        }
    }

    public enum Valuation {
        NOT_SELECTED("valuation-type-NOT_SELECTED"),
        CUSTOMER_DEMAND("valuation-type-CUSTOMER_DEMAND"),
        VOUCHER("valuation-type-VOUCHER"),
        NEW_PRICE("valuation-type-NEW_PRICE"),
        MARKET_PRICE("valuation-type-MARKET_PRICE");

        private String className;

        Valuation(String className) {
            this.className = className;
        }
    }

    public boolean isScalepointSupplierNotVisible(){
        return (!statusSupplier.exists());
    }

    public boolean isScalepointSupplierVisible(String _supplier){
        return statusSupplier.getText().contains(_supplier);
    }
}
