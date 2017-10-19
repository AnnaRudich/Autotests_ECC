package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.impl.Events;
import com.scalepoint.automation.pageobjects.extjs.ExtCheckbox;
import com.scalepoint.automation.pageobjects.extjs.ExtComboBox;
import com.scalepoint.automation.pageobjects.extjs.ExtElement;
import com.scalepoint.automation.pageobjects.extjs.ExtInput;
import com.scalepoint.automation.pageobjects.extjs.ExtRadioGroup;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.TextSearchPage;
import com.scalepoint.automation.services.externalapi.VoucherAgreementApi;
import com.scalepoint.automation.utils.JavascriptHelper;
import com.scalepoint.automation.utils.JavascriptHelper.Snippet;
import com.scalepoint.automation.utils.OperationalUtils;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.threadlocal.Browser;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.CheckBox;
import ru.yandex.qatools.htmlelements.element.Link;
import ru.yandex.qatools.htmlelements.element.Table;
import ru.yandex.qatools.htmlelements.element.TextBlock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.OperationalUtils.assertEqualsDouble;
import static com.scalepoint.automation.utils.OperationalUtils.assertEqualsDoubleWithTolerance;
import static com.scalepoint.automation.utils.Wait.forCondition;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompleted;
import static com.scalepoint.automation.utils.Wait.waitForDisplayed;
import static com.scalepoint.automation.utils.Wait.waitForStaleElements;
import static com.scalepoint.automation.utils.Wait.waitForVisible;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertEqualsNoOrder;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;


public class SettlementDialog extends BaseDialog {

    private static final By OK_BUTTON = By.id("ok-button");
    private static final By ADD_BUTTON = By.id("add-button");
    private static final By CANCEL_BUTTON = By.id("cancel-button");

    @FindBy(id = "description-textfield-inputEl")
    private ExtInput description;

    @FindBy(id = "group-combobox")
    private ExtComboBox category;

    @FindBy(id = "pseudocategory-combobox")
    private ExtComboBox subCategory;

    @FindBy(id = "age-months-combobox")
    private ExtComboBox ageMonth;

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

    @FindBy(id = "voucher-valuation-card-find-shop")
    private Button findShopButton;

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
    private WebElement cancelButton;

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

    @FindBy(id = "automatic-depreciation-checkbox-labelEl")
    private WebElement automaticDepreciationLabel;

    @FindBy(id = "marketprice-card-supplier-inputEl")
    private WebElement marketPriceSupplier;

    @FindBy(id = "productmatch-card-supplier")
    private TextBlock statusSupplier;

    @FindBy(id = "reject-reason-combobox")
    private ExtComboBox rejectReason;

    @FindBy(id = "discretionary-reason-combobox")
    private ExtComboBox discretionaryReason;

    @FindBy(id = "depreciation-type-combobox")
    private ExtComboBox depreciationTypeComboBox;

    @FindBy(id = "not-cheapest-reason-display-inputEl")
    private TextBlock notCheapestReasonDisplay;

    @FindBy(xpath = "//div[@id='status_product_match_card']//div[contains(@id, 'displayfield')]/b")
    private WebElement statusMatchedDisplayField;

    @FindBy(id = "documentation-ok-checkbox-inputEl")
    private CheckBox sufficientDocumentation;

    @FindBy(id = "reject-checkbox-displayEl")
    private CheckBox rejectCheckbox;

    public enum ValuationGridColumn {
        CHECK_COLUMN("active-valuation-checkcolumn"),
        TYPE("description"),
        CASH_COMPENSATION("cashCompensation"),
        DEPRECIATION_COLUMN("depreciation"),
        TOTAL_AMOUNT_OF_VALUATION("totalPrice"),
        EDIT_VALUATION("editValuation"),
        NULL(null);

        private String dataColumnId;

        ValuationGridColumn(String dataColumnId) {
            this.dataColumnId = dataColumnId;
        }

        public static ValuationGridColumn getColumn(String dataColumnId) {
            for (ValuationGridColumn valuationGridColumn : ValuationGridColumn.values()) {
                if (dataColumnId.equals(valuationGridColumn.dataColumnId)) {
                    return valuationGridColumn;
                }
            }
            return NULL;
        }
    }

    public enum Valuation {
        NOT_SELECTED("valuation-type-NOT_SELECTED"),
        CUSTOMER_DEMAND("valuation-type-CUSTOMER_DEMAND"),
        VOUCHER("valuation-type-VOUCHER"),
        NEW_PRICE("valuation-type-NEW_PRICE"),
        MARKET_PRICE("valuation-type-MARKET_PRICE"),
        DISCRETIONARY("valuation-type-DISCRETIONARY_VALUATION"),
        CATALOG_PRICE("valuation-type-CATALOG_PRICE"),
        USED_PRICE("valuation-type-USED_PRICE");

        private String className;

        Valuation(String className) {
            this.className = className;
        }
    }

    public enum DepreciationType {
        POLICY(0),
        DISCRETIONARY(1);

        int index;

        DepreciationType(int i) {
            index = i;
        }
    }

    public static class FormFiller {

        private SettlementDialog sid;

        public FormFiller(SettlementDialog settlementDialog) {
            this.sid = settlementDialog;
        }

        public FormFiller withText(String text) {
            sid.setDescription(text);
            return this;
        }

        public FormFiller withCategory(String category) {
            sid.setCategory(category);
            return this;
        }

        public FormFiller withCategory(VoucherAgreementApi.AssignedCategory category) {
            sid.setCategory(category);
            return this;
        }

        public FormFiller withSubCategory(String subcategory) {
            sid.setSubCategory(subcategory);
            return this;
        }

        public FormFiller withNewPrice(Double newPrice) {
            sid.setNewPrice(newPrice);
            return this;
        }

        public FormFiller withCustomerDemandPrice(Double customerDemandPrice) {
            sid.setCustomerDemand(customerDemandPrice);
            return this;
        }

        public FormFiller withVoucher(String voucher) {
            sid.fillVoucher(voucher);
            return this;
        }

        public FormFiller withDepreciation(int depreciation, DepreciationType depreciationType) {
            sid.setDepreciation(depreciation);
            sid.setDepreciationType(depreciationType);
            return this;
        }

        public FormFiller withDepreciation(int depreciation) {
            sid.setDepreciation(depreciation);
            return this;
        }

        public FormFiller withDepreciation(DepreciationType depreciationType) {
            sid.setDepreciationType(depreciationType);
            return this;
        }

        public FormFiller withReductionRule(Integer reductionRuleValue) {
            sid.applyReductionRuleByValue(reductionRuleValue);
            return this;
        }

        public FormFiller withDiscretionaryPrice(double discretionaryPrice) {
            sid.setDiscretionaryPrice(discretionaryPrice);
            return this;
        }

        public FormFiller withAge(int years, int month) {
            sid.enableAge(Integer.toString(years))
                    .selectMonth(Integer.toString(month));
            return this;
        }

        public FormFiller withAgeDisabled() {
            sid.disableAge();
            return this;
        }

        public FormFiller withValuation(Valuation valuation) {
            sid.setValuation(valuation);
            return this;
        }

        public FormFiller withDiscountAndDepreciation(boolean enabled) {
            sid.setDiscountAndDepreciation(enabled);
            return this;
        }

        public FormFiller withDiscretionaryReason(String discretionaryReason) {
            sid.selectDiscretionaryReason(discretionaryReason);
            return this;
        }
    }

    @Override
    public SettlementDialog ensureWeAreAt() {
        Wait.waitForAjaxCompleted();
        waitForVisible(cancelButton);
        JavascriptHelper.loadSnippet(Snippet.SID_GROUPS_LOADED);
        return this;
    }

    @Override
    protected boolean areWeAt() {
        Wait.waitForAjaxCompleted();
        try {
            return cancelButton.isDisplayed();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    public SettlementDialog fill(Consumer<SettlementDialog.FormFiller> fillFunc) {
        fillFunc.accept(new FormFiller(this));
        return this;
    }

    public SettlementDialog setBaseData(ClaimItem claimItem) {
        return setDescription(claimItem.getTextFieldSP()).
                setCustomerDemand(claimItem.getCustomerDemand()).
                setNewPrice(claimItem.getNewPriceSP()).
                setCategory(claimItem.getCategoryGroupBorn()).
                setSubCategory(claimItem.getCategoryBornBabyudstyr());
    }

    public SettlementDialog fill(String description, String category, String subcategory, Double newPrice) {
        return setDescription(description).
                setNewPrice(newPrice).
                setCategory(category).
                setSubCategory(subcategory);
    }

    private SettlementDialog setExtInputValue(ExtInput input, String value) {
        waitForVisible(input);
        input.clear();
        input.enter(value);
        simulateBlurEvent(input);
        return this;
    }

    public SettlementDialog uncheckedDocumentation() {
        if (sufficientDocumentation.getAttribute("aria-checked").equals("true")) {
            forCondition(ExpectedConditions.elementToBeClickable(sufficientDocumentation));
            clickUsingJsIfSeleniumClickReturnError(sufficientDocumentation);
        }
        return this;
    }

    private void simulateBlurEvent(ExtElement input) {
        ExtInput inputForClick = input == quantity ? description : quantity;
        inputForClick.getRootElement().click();
    }

    public SettlementDialog setDescription(String descriptionText) {
        return setExtInputValue(description, descriptionText);
    }

    public SettlementDialog setNewPrice(Double amount) {
        return setExtInputValue(newPrice, OperationalUtils.format(amount));
    }

    public SettlementDialog setDiscretionaryPrice(Double amount) {
        return setExtInputValue(discretionaryPrice, OperationalUtils.format(amount));
    }

    public SettlementDialog setCustomerDemand(Double amount) {
        return setExtInputValue(customerDemand, OperationalUtils.format(amount));
    }

    public SettlementDialog setDepreciation(Integer amount) {
        return setExtInputValue(depreciationPercentage, amount.toString());
    }

    public SettlementDialog setCategory(String categoryName) {
        category.select(categoryName);
        return this;
    }

    public SettlementDialog setSubCategory(String subCategoryName) {
        subCategory.select(subCategoryName);
        return this;
    }

    public SettlementDialog setCategory(VoucherAgreementApi.AssignedCategory categoryInfo) {
        setCategory(categoryInfo.getCategory());
        setSubCategory(categoryInfo.getSubCategory());
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

    public SettlementDialog enterAgeYears(String _ageYears) {
        return setExtInputValue(ageYears, _ageYears);
    }

    public SettlementDialog selectMonth(String monthName) {
        ageMonth.select(monthName);
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
        return closeSidWithOk(SettlementPage.class, OK_BUTTON);
    }

    public SettlementDialog clickOK() {
        $(ok).click();
        return this;
    }

    public <T extends Page> T closeSidWithOk(Class<T> pageClass) {
        return closeSidWithOk(pageClass, OK_BUTTON);
    }

    public <T extends BaseDialog> T tryToCloseSidWithOkButExpectDialog(Class<T> dialogClass) {
        WebElement button = driver.findElement(OK_BUTTON);
        waitForVisible(button);
        button.click();
        return BaseDialog.at(dialogClass);
    }

    public TextSearchPage closeSidWithAdd() {
        return closeSid(TextSearchPage.class, ADD_BUTTON, true);
    }

    public <T extends Page> T closeSidWithOk(Class<T> pageClass, By button) {
        return closeSid(pageClass, button, false);
    }

    public SettlementPage cancel() {
        return cancel(SettlementPage.class);
    }

    public <T extends Page> T cancel(Class<T> pageClass) {
        return closeSid(pageClass, CANCEL_BUTTON, false);
    }

    private <T extends Page> T closeSid(Class<T> pageClass, By buttonBy, boolean acceptAlert) {
        try {
            WebElement button = driver.findElement(buttonBy);
            waitForVisible(button);
            button.click();

            Wait.waitElementDisappeared(buttonBy);
            Wait.waitForAjaxCompleted();
        } catch (UnhandledAlertException ignored) {
        }

        if (acceptAlert) {
            acceptAlert();
        }

        try {
            Wait.forCondition((WebDriver driver) -> {
                assert driver != null;
                WebElement element = driver.findElement(By.xpath("//button[@onclick='spsubmit()']"));
                element.click();
                acceptAlert();
                return element;
            }, 10, 1000);
        } catch (Exception ignored) {
        }
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

    public AddValuationDialog openAddValuationForm() {
        addValuation.click();
        return at(AddValuationDialog.class);
    }

    public String getNotCheapestChoiceReason() {
        return $(notCheapestReasonDisplay).getText();
    }

    public SettlementDialog rejectClaim() {
        rejectCheckbox.select();
        return this;
    }

    public EditVoucherValuationDialog openVoucherValuationCard() {
        $(voucherValuationCard).click();
        return BaseDialog.at(EditVoucherValuationDialog.class);
    }

    public VoucherTermsAndConditionsDialog openVoucherTermAndConditions() {
        $(voucherTermAndConditions).click();
        return at(VoucherTermsAndConditionsDialog.class);
    }

    public FindShopDialog openFindShopDialog() {
        findShopButton.click();
        return at(FindShopDialog.class);
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

    private boolean isRejectReasonVisible() {
        return rejectReason.exists();
    }

    private boolean isRejectReasonEnabled() {
        return $(rejectReason).isEnabled();
    }

    private boolean isRejectReasonDisabled(String visibleText) {
        new Actions(driver).click(driver.findElement(By.id("reject-reason-combobox"))).build().perform();
        $(By.id("reject-reason-combobox-inputEl")).setValue(visibleText);
        return driver.findElement(By.xpath("//span[text()='" + visibleText + "']")).getAttribute("style").equalsIgnoreCase("color: silver;");
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

    public SettlementDialog selectRejectReason(String visibleText) {
        waitForVisible(rejectReason);
        new Actions(driver).click(driver.findElement(By.id("reject-reason-combobox"))).build().perform();
        $(By.id("reject-reason-combobox-inputEl")).setValue(visibleText);
        new Actions(driver).click(driver.findElement(By.xpath("//span[text()='" + visibleText + "']"))).build().perform();
        new Events().fireEvent($(By.id("reject-reason-combobox-inputEl")), "focus", "keydown", "keypress", "input", "keyup", "change");
        return this;
    }

    public SettlementDialog setDepreciationType(DepreciationType depreciation) {
        waitForVisible(depreciationTypeComboBox);
        depreciationTypeComboBox.select(depreciation.index);
        return this;
    }

    public SettlementDialog setDepreciationType(String visibleText) {
        waitForVisible(depreciationTypeComboBox);
        depreciationTypeComboBox.select(visibleText);
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

    private String getRejectReasonText() {
        return rejectReason.getValue();
    }

    private boolean isDiscretionaryReasonHasRedBorder() {
        String redBorder = "#c30";
        String redBorderRGB = "rgb(204, 51, 0)";
        waitForDisplayed(By.xpath("//*[@id='discretionary-reason-combobox-inputWrap' and contains(@class, 'x-form-text-wrap-invalid')]"));
        return driver.findElement(By.id("discretionary-reason-combobox-inputWrap")).getAttribute("class").contains("x-form-text-wrap-invalid")
                && (driver.findElement(By.id("discretionary-reason-combobox-inputWrap")).getCssValue("border-color").contains(redBorder) ||
                (driver.findElement(By.id("discretionary-reason-combobox-inputWrap")).getCssValue("border-color").contains(redBorderRGB)));
    }

    private boolean isRejectReasonHasRedBorder() {
        String redBorder = "#c30";
        String redBorderRGB = "rgb(204, 51, 0)";
        waitForDisplayed(By.xpath("//*[@id='reject-reason-combobox-inputWrap' and contains(@class, 'x-form-text-wrap-invalid')]"));
        return driver.findElement(By.id("reject-reason-combobox-inputWrap")).getAttribute("class").contains("x-form-text-wrap-invalid")
                && (driver.findElement(By.id("reject-reason-combobox-inputWrap")).getCssValue("border-color").contains(redBorder) ||
                (driver.findElement(By.id("reject-reason-combobox-inputWrap")).getCssValue("border-color").contains(redBorderRGB)));
    }

    String discountDistributionLocator = ".//tr[contains(@class, '%s')]//img";

    public EditVoucherValuationDialog openEditDiscountDistributionForVoucher() {
        IntStream.range(0, 3).forEach(i -> clickUsingJsIfSeleniumClickReturnError(waitForDisplayed(By.xpath(String.format(discountDistributionLocator, Valuation.VOUCHER.className)))));
        return at(EditVoucherValuationDialog.class);
    }

    public boolean isDiscountDistributionDisplayed() {
        try {
            return waitForDisplayed(By.xpath(String.format(discountDistributionLocator, Valuation.VOUCHER.className))).isDisplayed();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    public SettlementDialog distributeDiscountForVoucherValuation(EditVoucherValuationDialog.DistributeTo distributeTo, Integer percentage) {
        return openEditDiscountDistributionForVoucher().updatePercentage(distributeTo, percentage).saveVoucherValuation();
    }

    public EditDiscountDistributionDialog distributeDiscountForVoucherValuationWithDialog(EditVoucherValuationDialog.DistributeTo distributeTo, Integer percentage) {
        return openEditDiscountDistributionForVoucher().updatePercentageFromDialog(distributeTo, percentage);
    }

    public Integer getVoucherPercentage() {
        EditVoucherValuationDialog editDiscountDistributionDialog = openEditDiscountDistributionForVoucher();
        Integer voucherPercentage = editDiscountDistributionDialog.getVoucherPercentage();
        editDiscountDistributionDialog.saveVoucherValuation();
        return voucherPercentage;
    }

    public List<VoucherDropdownElement> parseVoucherDropdown() {
        List<String> comboBoxOptions = voucher.getComboBoxOptions();
        return comboBoxOptions.stream().map(VoucherDropdownElement::new).collect(Collectors.toList());
    }

    public ValuationRow parseValuationRow(Valuation valuation) {
        ValuationRow valuationRow = new ValuationRow(valuation);

        By xpath = By.xpath(".//tr[contains(@class, '" + valuation.className + "')]//td");
        Wait.waitForStaleElement(xpath);
        waitForAjaxCompleted();
        List<WebElement> elements = waitForStaleElements(xpath);
        for (WebElement td : elements) {
            String attribute = td.getAttribute("data-columnid");
            switch (ValuationGridColumn.getColumn(attribute)) {
                case CASH_COMPENSATION:
                    valuationRow.cashCompensation = StringUtils.isBlank(td.getText()) ? null : OperationalUtils.toNumber(td.getText());
                    break;
                case DEPRECIATION_COLUMN:
                    valuationRow.depreciationPercentage = StringUtils.isBlank(td.getText()) ? null : Integer.valueOf(td.getText());
                    break;
                case TOTAL_AMOUNT_OF_VALUATION:
                    valuationRow.totalPrice = StringUtils.isBlank(td.getText()) ? null : OperationalUtils.toNumber(td.getText());
                    break;
                case TYPE:
                    valuationRow.description = td.getText();
                    break;
            }
        }
        return valuationRow;

    }

    public boolean isValuationDisabled(Valuation valuation) {
        SelenideElement unselectable = $(By.xpath(".//tr[contains(@class, '" + valuation.className + "')]/td[2]/div[contains(@style, 'silver')]")).shouldHave(Condition.attribute("unselectable"));
        if (unselectable == null)
            return false;
        return true;
    }

    public static class VoucherDropdownElement {
        private String voucherName;
        private boolean distanceCalculated;
        private int distance;
        private int percentage;

        static final Pattern PATTERN = Pattern.compile("(?<voucherName>.*)\\((?<distance>[a-z0-9]*)\\s*km-(?<percentage>\\d*)%\\)");

        public VoucherDropdownElement(String text) {
            Matcher m = PATTERN.matcher(text);
            while (m.find()) {
                voucherName = m.group("voucherName").trim();

                String distanceValue = m.group("distance");
                if (NumberUtils.isNumber(distanceValue)) {
                    distanceCalculated = true;
                    distance = Integer.valueOf(distanceValue);
                }

                percentage = Integer.valueOf(m.group("percentage"));
            }
        }

        public String getVoucherName() {
            return voucherName;
        }

        public boolean isDistanceCalculated() {
            return distanceCalculated;
        }

        public int getDistance() {
            return distance;
        }

        public int getPercentage() {
            return percentage;
        }
    }

    public class ValuationRow {

        private Valuation valuation;
        private Double cashCompensation;
        private Integer depreciationPercentage;
        private Double totalPrice;
        private String description;

        WebDriver driver;
        By xpath;
        WebElement webElement;

        private void setUp() {
            driver = Browser.driver();
            xpath = By.xpath("//tr[contains(@class, '" + valuation.className + "')]//div[@role='button']");
            Wait.waitForStaleElement(xpath);
            webElement = driver.findElement(xpath);
        }

        public Boolean isChecked() {
            setUp();
            return webElement.getAttribute("class").contains("x-grid-checkcolumn-checked");
        }

        public ValuationRow makeActive() {
            if (!isChecked()) {
                //one click doesn't work, each click renew dom so we should wait for stale element each time
                for (int i = 0; i < 3; i++) {
                    Wait.waitForStaleElement(xpath);
                    webElement = driver.findElement(xpath);
                    webElement.click();
                }
            }
            waitASecond();
            return this;
        }

        public SettlementDialog doAssert(Consumer<Asserts> func) {
            func.accept(new Asserts());
            return SettlementDialog.this;

        }

        public class Asserts {
            public Asserts assertCashCompensationIs(Double amount) {
                OperationalUtils.assertEqualsDouble(cashCompensation, amount);
                return this;
            }

            public Asserts assertTotalAmountIs(Double amount) {
                OperationalUtils.assertEqualsDouble(totalPrice, amount);
                return this;
            }

            public Asserts assertDepreciationPercentageIs(Integer expectedDepreciationPercentage) {
                assertEquals(depreciationPercentage, expectedDepreciationPercentage);
                return this;
            }
        }

        public ValuationRow parseValuation(Valuation valuation) {
            return SettlementDialog.this.parseValuationRow(valuation);
        }

        private ValuationRow(Valuation valuation) {
            this.valuation = valuation;
        }

        public String getDescription() {
            return this.description;
        }

        public Double getCashCompensation() {
            return cashCompensation;
        }

        public Double getTotalPrice() {
            return totalPrice;
        }

        public SettlementDialog back() {
            return SettlementDialog.this;
        }
    }

    public SettlementDialog setValuation(Valuation valuation) {
        return parseValuationRow(valuation).makeActive().back();
    }

    public VoucherDropdownElement parseVoucherDropdownElement(String voucherName) {
        try {
            List<VoucherDropdownElement> voucherDropdownElements = parseVoucherDropdown();
            return voucherDropdownElements.stream()
                    .filter(el -> el.getVoucherName().equals(voucherName))
                    .findFirst()
                    .orElseThrow((Supplier<Throwable>) () -> new AssertionError(voucherName + " is not"));
        } catch (Throwable throwable) {
            throw new AssertionError(throwable.getMessage(), throwable);
        }
    }

    public SettlementDialog doAssert(Consumer<Asserts> func) {
        func.accept(new Asserts());
        return SettlementDialog.this;
    }

    public class Asserts {

        public Asserts assertDiscretionaryReasonValuePresent(String expectedValue) {
            List<String> options = discretionaryReason.getComboBoxOptions();
            Assert.assertTrue(options.stream().anyMatch(i -> i.contains(expectedValue)));
            return this;
        }

        public Asserts assertValuationIsDisabled(Valuation valuation) {
            assertTrue(isValuationDisabled(valuation));
            return this;
        }

        public Asserts assertCashValueIs(Double expectedCashValue) {
            OperationalUtils.assertEqualsDoubleWithTolerance(getCashCompensationValue(), expectedCashValue,
                    "Cash compensation is incorrect. Actual %s, Expected %s");
            return this;
        }

        public Asserts assertVoucherCashValueIs(Double expectedVoucherCashValue) {
            OperationalUtils.assertEqualsDoubleWithTolerance(voucherCashValueFieldText(), expectedVoucherCashValue,
                    "Voucher cash value is incorrect. Actual %s, Expected %s");
            return this;
        }

        public Asserts assertVoucherFaceValueIs(Double expectedVoucherCashValue) {
            OperationalUtils.assertEqualsDoubleWithTolerance(voucherFaceValueFieldText(), expectedVoucherCashValue,
                    "Voucher face value is incorrect. Actual %s, Expected %s");
            return this;
        }

        public Asserts assertDepreciationAmountIs(Double expectedDepreciation) {
            OperationalUtils.assertEqualsDoubleWithTolerance(fetchDepreciationAmount(), expectedDepreciation,
                    "Depreciation is incorrect. Actual %s, Expected %s");
            return this;
        }

        public Asserts assertDescriptionIs(String expectedDescription) {
            assertEquals(getDescriptionText(), expectedDescription, "The Description is not saved");
            return this;
        }

        public Asserts assertCategoryTextIs(String expectedCategory) {
            assertEquals(getCategoryText(), expectedCategory, "The Category is not Saved");
            return this;
        }

        public Asserts assertSubCategoryTextIs(String expectedSubCategory) {
            assertEquals(getSubCategoryText(), expectedSubCategory, "The Subcategory is not Saved");
            return this;
        }

        public Asserts assertReviewedNotPresent() {
            try {
                assertFalse(reviewed.isEnabled(), "Reviewed checkbox msu be disabled");
            } catch (Exception ignored) {
            }
            return this;
        }

        public Asserts assertVoucherListed(String voucherTitle) {
            System.out.println("AssertVoucherListed: " + voucherTitle);
            Assert.assertTrue(getVouchersList().stream().anyMatch(i -> {
                System.out.println("Found: " + i);
                return i.contains(voucherTitle);
            }), "Voucher " + voucherTitle + " must be present");
            return this;
        }

        public Asserts assertVoucherNotListed(String voucherTitle) {
            System.out.println("assertVoucherNotListed: " + voucherTitle);
            Assert.assertFalse(getVouchersList().stream().anyMatch(i -> {
                System.out.println("Found: " + i);
                return i.contains(voucherTitle);
            }), "Voucher " + voucherTitle + " must not be present");
            return this;
        }

        private List<String> getVouchersList() {
            if (voucher.isDisplayed()) {
                return voucher.getComboBoxOptions();
            } else {
                return availableVoucher.getComboBoxOptions();
            }
        }

        public Asserts assertIncludeInClaimSelected() {
            Assert.assertTrue(includeInClaim.isSelected(), "The 'Include in Claim' must be selected'");
            return this;
        }

        public Asserts assertIncludeInClaimNotSelected() {
            Assert.assertFalse(includeInClaim.isSelected(), "The 'Include in Claim' must be unselected'");
            return this;
        }

        public Asserts assertNotCheapestReasonIs(String reason) {
            assertEquals(getNotCheapestChoiceReason(), reason, "Reason must be: " + reason);
            return this;
        }

        public Asserts assertSubCategoriesListEqualTo(List<String> expectedSubCategoriesList) {
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

        public Asserts assertVoucherCashValueIs(String expectedValue) {
            assertEquals(voucherCashValueFieldText(), OperationalUtils.getDoubleValue(expectedValue));
            return this;
        }

        public Asserts assertAgeYearsEnabled() {
            assertTrue($(ageYears).isEnabled(), "Age Years field must be enabled");
            return this;
        }

        public Asserts assertAgeIs(int years, int months) {
            assertEquals($(ageYears).getText(), years + "");
            assertEquals($(ageMonth).getText(), months + "");
            return this;
        }

        public Asserts assertMonthMenuEnabled() {
            assertTrue($(ageMonth).isEnabled(), "Month DropDown must be enabled");
            return this;
        }

        public Asserts assertAgeYearsDisabled() {
            assertFalse($(ageYears).isEnabled(), "Age Years field must be disabled");
            return this;
        }

        public Asserts assertMonthMenuDisabled() {
            assertFalse(ageMonth.isEnabled(), "Month DropDown must be disabled");
            return this;
        }

        public Asserts assertMonthValueIs(String expectedMonthValue) {
            assertEquals(ageMonth.getValue().trim(), expectedMonthValue, "The month is not saved");
            return this;
        }

        public Asserts assertYearsValueIs(String expectedValue) {
            assertEquals(ageYears.getText(), expectedValue, "The age year is not saved");
            return this;
        }

        public Asserts assertDepreciationValueIs(Double expectedDepreciationValue) {
            assertEqualsDouble(Double.valueOf(depreciationPercentage.getText()), expectedDepreciationValue, "Depreciation percentage incorrect");
            return this;
        }

        public Asserts assertMarketPriceVisible() {
            String failMessage = "Market price must be visible";
            return checkVisibilityOfValuationRow(failMessage, Valuation.MARKET_PRICE);
        }

        public Asserts assertCatalogPriceVisible() {
            String failMessage = "Catalog price must be visible";
            return checkVisibilityOfValuationRow(failMessage, Valuation.CATALOG_PRICE);
        }

        public Asserts assertCatalogPriceInvisible() {
            String failMessage = "Catalog price must be invisible";
            return checkInvisibilityOfValuationRow(failMessage, Valuation.CATALOG_PRICE);
        }

        private Asserts checkVisibilityOfValuationRow(String message, Valuation valuation) {
            try {
                if (parseValuationRow(valuation).getDescription() == null) {
                    Assert.fail(message);
                }
            } catch (Exception e) {
                Assert.fail(message);
            }
            return this;
        }

        private Asserts checkInvisibilityOfValuationRow(String message, Valuation valuation) {
            try {
                if (parseValuationRow(valuation).getDescription() != null) {
                    Assert.fail(message);
                }
            } catch (Exception e) {
                Assert.fail(message);
            }
            return this;
        }

        public Asserts assertMarketPriceSupplierInvisible() {
            assertFalse(isMarketPriceSupplierDisplayed(), "Market price must be not visible");
            return this;
        }

        public Asserts assertRejectReasonDisabled() {
            assertFalse(isRejectReasonEnabled(), "Reject Reason must be disabled");
            return this;
        }

        public Asserts assertRejectReasonVisible() {
            assertTrue(isRejectReasonVisible(), "Reject Reason must be visible");
            return this;
        }

        public Asserts assertDiscretionaryReasonInvisible() {
            assertFalse(isDiscretionaryReasonVisible(), "Discretionary Reason must be invisible");
            return this;
        }

        public Asserts assertDiscretionaryReasonVisible() {
            assertTrue(isDiscretionaryReasonVisible(), "Discretionary Reason must be visible");
            return this;
        }

        public Asserts assertDiscretionaryReasonDisabled() {
            assertFalse(isDiscretionaryReasonEnabled(), "Discretionary Reason must be disabled");
            return this;
        }

        public Asserts assertDiscretionaryReasonEnabled() {
            assertTrue(isDiscretionaryReasonEnabled(), "Discretionary Reason must be enabled");
            return this;
        }

        public Asserts assertRejectReasonEnabled() {
            assertTrue(isRejectReasonEnabled(), "Reject Reason must be enabled");
            return this;
        }

        public Asserts assertRejectReasonIsDisabled(String visibleText) {
            assertTrue(isRejectReasonDisabled(visibleText), "Reject Reason should be disabled");
            return this;
        }

        public Asserts assertDiscretionaryReasonHasRedBorder() {
            Assert.assertTrue(isDiscretionaryReasonHasRedBorder(), "Discretionary Reason field should have red border");
            return this;
        }

        public Asserts assertRejectReasonHasRedBorder() {
            Assert.assertTrue(isRejectReasonHasRedBorder(), "Reject Reason field should have red border");
            return this;
        }

        public Asserts assertDiscretionaryReasonHasNormalBorder() {
            Assert.assertFalse(isDiscretionaryReasonHasRedBorder(), "Discretionary Reason field should have normal border");
            return this;
        }

        public Asserts assertDiscretionaryReasonEqualTo(String reason) {
            assertEquals(getDiscretionaryReasonText(), reason, "Wrong reason selected for New Price");
            return this;
        }

        public Asserts assertRejectReasonEqualTo(String reason) {
            assertEquals(getRejectReasonText(), reason, "Wrong reason selected");
            return this;
        }

        public Asserts assertBrandTextIs(String brandLink) {
            assertEquals($(brand).getText(), brandLink, "Wrong Brand is Displayed");
            return this;
        }

        public Asserts assertScalepointSupplierNotVisible() {
            assertFalse(statusSupplier.exists(), "Scalepoint supplier must not be visible");
            return this;
        }

        public Asserts assertScalepointSupplierVisible(String supplier) {
            assertTrue(statusSupplier.getText().contains(supplier), "Scalepoint supplier must be visible");
            return this;
        }

        public Asserts assertVoucherDropdownKnowsDistance(String voucher, Integer expectedDistance) {
            VoucherDropdownElement voucherDropdownElement = parseVoucherDropdownElement(voucher);
            assertTrue(voucherDropdownElement.distanceCalculated);
            assertEquals(voucherDropdownElement.distance, expectedDistance.intValue());
            return this;
        }

        public Asserts assertVoucherDropdownWithoutDistance(String voucher) {
            VoucherDropdownElement voucherDropdownElement = parseVoucherDropdownElement(voucher);
            assertFalse(voucherDropdownElement.distanceCalculated);
            return this;
        }

        public Asserts assertCashCompensationIsDepreciated(int percentage, Valuation valuation) {
            ValuationRow valuationRow = parseValuationRow(valuation);
            assertEqualsDoubleWithTolerance(valuationRow.getCashCompensation(), valuationRow.getTotalPrice() * (1 - (Double.valueOf(percentage) / 100)));
            return this;
        }

        public Asserts assertIsLowestPriceValuationSelected(Valuation... valuations) {
            List<ValuationRow> valuationRows = new ArrayList<>();
            Arrays.stream(valuations).forEach(v -> valuationRows.add(parseValuationRow(v)));
            assertTrue(valuationRows.stream()
                    .sorted(Comparator.comparing(valuationRow -> valuationRow.getCashCompensation()))
                    .findFirst().get()
                    .isChecked());
            return this;
        }

        public Asserts assertPriceIsSameInTwoColumns(Valuation valuation) {
            ValuationRow valuationRow = parseValuationRow(valuation);
            assertEquals(valuationRow.cashCompensation, valuationRow.totalPrice);
            return this;
        }

        public Asserts assertTotalPriceIsSameInRows(Valuation... valuations) {
            List<ValuationRow> valuationRows = new ArrayList<>();
            Arrays.stream(valuations).forEach(valuation -> valuationRows.add(parseValuationRow(valuation)));
            assertTrue(valuationRows.stream()
                            .map(price -> price.getTotalPrice()).collect(Collectors.toList()).stream()
                            .distinct().count() <= 1,
                    "Total prices are not equal");
            return this;
        }

        public Asserts assertIsVoucherDiscountApplied(Double newPrice) {
            ValuationRow valuationRow = parseValuationRow(Valuation.VOUCHER);
            assertTrue(valuationRow.getCashCompensation() == newPrice - (newPrice * getVoucherPercentage() / 100));
            return this;
        }

        public Asserts assertIsStatusMatchedNotificationContainsText(String text) {
            String statusText = statusMatchedDisplayField.getText();
            assertTrue(statusText.contains(text), "Status Matched text is: '" + statusText + "' and should contain: '" + text + "'");
            return this;
        }

        public Asserts assertDepreciationPercentageIs(String percentage) {
            assertEquals(depreciationPercentage.getText(), percentage);
            return this;
        }

        public Asserts assertAutomaticDepreciationLabelColor() {
            boolean isLabelInRedColor = automaticDepreciationLabel.getAttribute("style").contains("color: red;");
            assertTrue(automaticDepreciation.isSelected() == !isLabelInRedColor);
            return this;
        }

        public Asserts assertIsSufficientDocumentationCheckboxDisplayedAndItIsChecked() {
            assertTrue(sufficientDocumentation.getAttribute("aria-checked").equals("true"));
            return this;
        }

        public Asserts assertIsSufficientDocumentationCheckboxDisplayedAndItIsUnchecked() {
            assertTrue(sufficientDocumentation.getAttribute("aria-checked").equals("false"));
            return this;
        }

    }


}

