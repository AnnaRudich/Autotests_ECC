package com.scalepoint.automation.pageobjects.dialogs;

import com.scalepoint.automation.pageobjects.extjs.*;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.EccActions;
import com.scalepoint.automation.utils.OperationalUtils;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.EccPage;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.Voucher;
import com.scalepoint.automation.utils.driver.Browser;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.*;

import java.util.ArrayList;
import java.util.List;

import static com.scalepoint.automation.utils.Wait.waitForVisible;

@EccPage
public class SettlementDialog extends Page {

    private static final String URL = "webshop/jsp/matching_engine/dialog/settlement_item_dialog.jsp";

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

    @FindBy(id = "btn_cancel")
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

    EccActions eccActions = new EccActions(Browser.current());

    @Override
    protected String geRelativeUrl() {
        return URL;
    }

    @Override
    public SettlementDialog ensureWeAreOnPage() {
        waitForUrl(URL);
        waitForVisible(cancel);
        return this;
    }

    public SettlementDialog EnterDescription(String descriptionText) {
        description.enter(descriptionText);
        return this;
    }

    public SettlementDialog EnterQuantityAmount(int amount) {
        quantity.enter(String.valueOf(amount));
        return this;
    }

    public SettlementDialog EnterNewPriceAmount(int amount) {
        newPrice.enter(String.valueOf(amount));
        return this;
    }

    public SettlementDialog EnterCustomerDemandAmount(int amount) {
        customerDemand.clear();
        customerDemand.sendKeys(String.valueOf(amount));
        newPrice.getWrappedElement().click();
        return this;
    }

    public SettlementDialog EnterDepreciationAmount(int amount) {
        depreciation.clear();
        depreciation.sendKeys(String.valueOf(amount));
        description.getWrappedElement().click();
        return this;
    }

    public SettlementDialog SelectCategory(String categoryName) {
        category.select(categoryName);
        return this;
    }

    public SettlementDialog SelectSubCategory(String subCategoryName) {
        subCategory.select(subCategoryName);
        return this;
    }

    /**
     * @param monthName should be digit
     */
    public SettlementDialog SelectMonth(String monthName) {
        month.select(monthName);
        return this;
    }

    public SettlementDialog SelectVoucher(String voucherName) {
        if (voucher.isDisplayed())
            voucher.select(voucherName);
        else
            Wait.waitUntilVisible((WebElement) avoucher);
        avoucher.select(voucherName);
        return this;
    }

    public SettlementDialog SelectVoucher(int voucherName) {
        avoucher.select(voucherName);
        return this;
    }

    public SettlementDialog EnableAge(ClaimItem claimItem, String def) {
        if (def.equals(claimItem.getAgeStatus())) age.select(1);
        else age.select(0);
        return this;
    }

    public SettlementDialog DisableAge(ClaimItem claimItem, String def) {
        if (def.equals(claimItem.getAgeStatus())) age.select(0);
        else age.select(1);
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
        EnterDescription(claimItem.getTextFieldSP());
        SelectCategory(claimItem.getExistingCat1());
        SelectSubCategory(claimItem.getExistingSubCat1());
        EnterNewPriceAmount(Integer.valueOf(claimItem.getNewPriceSP()));
        SetReviewed(true);
        return this;
    }

    public String FetchCashCompensation() {
        return cashCompensationValue.getText();
    }

    public Double FetchDepreciation() {
        Wait.waitForLoaded();
        waitForVisible(deprecationValue);
        return getDoubleValue(deprecationValue.getText());
    }

    public SettlementDialog SetReviewed(boolean state) {
        reviewed.set(state);
        return this;
    }

    public boolean getReviewed() {
        return reviewed.isSelected();
    }

    public boolean isReviewedEnabled() {
        return reviewed.isEnabled();
    }

    public SettlementDialog SelectValuation(int index) {
        valuation.select(index);
        return this;
    }

    public int getValuation() {
        return valuation.getSelected();
    }

    public void OK() {
        waitForVisible(ok);
        ok.click();
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

    public SettlementDialog SetDiscountAndDepreciation(Boolean state) {
        waitForVisible(combineDiscountDepreciation);
        combineDiscountDepreciation.set(state);
        return this;
    }

    public SettlementDialog SetIncludeInClaim(Boolean state) {
        Wait.waitForLoaded();
        Wait.waitForEnabled(includeInClaim);
        includeInClaim.set(state);
        return this;
    }

    public boolean isValuationPresent(String _valuation) {
        Wait.waitForLoaded();
        waitForVisible(firstValuation);
        List<WebElement> rows = new ArrayList<>();
        for (Table table : valuations) {
            rows.addAll(table.getColumnByIndex(3));
        }
        Double expectedValue = doubleString(_valuation);
        return rows.stream().anyMatch(row -> OperationalUtils.toNumber(row.getText()).equals(expectedValue));
    }

    public boolean isNewValuationPresent(String _valuation) {
        Wait.waitForLoaded();
        waitForVisible(firstValuation);
        List<WebElement> rows = new ArrayList<>();
        for (Table table : valuations) {
            rows.addAll(table.getColumnByIndex(5));
        }
        Double expectedValue = doubleString(_valuation);
        return rows.stream().anyMatch(row -> OperationalUtils.toNumber(row.getText()).equals(expectedValue));
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

    public SettlementDialog SelectValuation(String _valuation) {
        Wait.waitForLoaded();
        waitForVisible(firstValuation);
        List<List<WebElement>> row = new ArrayList<>();
        for (Table table : valuations) {
            row.addAll(table.getRows());
        }
//        row.stream().filter(valuation -> valuation.current(2).getText().contains(_valuation));
        for (List<WebElement> list : row) {
            String valuation = list.get(2).getText();
            if (valuation.equals(_valuation)) {
                list.get(1).findElement(By.className("x-form-radio-default")).click();
            }
        }
//        List<WebElement> rows = new ArrayList<>();
//        for (Table table: valuations) {
//            rows.addAll(table.getColumnByIndex(2));
//        }
//        rows.stream().filter(valuation -> valuation.getText().equals(_valuation)).findFirst().current().click();

        return this;
    }


    public Double VoucherFaceValueFieldText() {
        Wait.waitForLoaded();
        waitForVisible(voucherFaceValue);
        return getDoubleValue(voucherFaceValue.getText());
    }

    public Double VoucherCashValueFieldText() {
        Wait.waitForLoaded();
        waitForVisible(voucherCashValue);
        return getDoubleValue(voucherCashValue.getText());
    }

    public Double CustomerDemandValue() {
        Wait.waitForLoaded();
        waitForVisible(customerDemand);
        return getDoubleValue(customerDemand.getText());
    }

    public Double CashCompensationValue() {
        waitForVisible(cashCompensationValue);
        return getDoubleValue(cashCompensationValue.getText());
    }

    public Double DeprecationValue() {
        waitForVisible(deprecationValue);
        return getDoubleValue(deprecationValue.getText());
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

    public void AddValuation() {
        waitForVisible(addValuation);
        addValuation.click();
    }

    public SettlementDialog EnterAgeYears(String _ageYears) {
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

    public SettlementDialog OpenVoucherValuationCard() {
        waitForVisible(voucherValuationCard);
        voucherValuationCard.click();
        return this;
    }

    public SettlementDialog OpenVoucherTermAndConditions() {
        waitForVisible(voucherTermAndConditions);
        voucherTermAndConditions.click();
        return this;
    }

    public SettlementDialog ApplyReductionRuleByValue(String _reductionRule) {
        Wait.waitForLoaded();
        List<List<WebElement>> rowsNames = ruleSuggestion.getRows();
        for (List<WebElement> list : rowsNames) {
            String reduction = list.get(1).getText().replaceAll("%", "");
            if (reduction.equals(_reductionRule)) {
                list.get(2).click();
            }
        }
        return this;
    }

    public SettlementDialog AutomaticDepreciation(boolean state) {
        Wait.waitForEnabled(automaticDepreciation);
        automaticDepreciation.set(state);
        Wait.waitForLoaded();
        return this;
    }

    public String getDepreciationValue() {
        return depreciation.getText();
    }


    public static double getDoubleValue(String input) {
        String[] array = input.split(" ");
        double result = Double.parseDouble((array[array.length - 1]).replaceAll("\\.", "").replace(",", "."));
        return result;
    }

    public static Double doubleString(String s) {
        return Double.parseDouble(s);
    }

}
