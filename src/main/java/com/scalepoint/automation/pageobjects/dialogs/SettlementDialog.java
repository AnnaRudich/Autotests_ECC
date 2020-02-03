package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.impl.Events;
import com.scalepoint.automation.grid.ValuationGrid;
import com.scalepoint.automation.pageobjects.extjs.*;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.TextSearchPage;
import com.scalepoint.automation.utils.JavascriptHelper;
import com.scalepoint.automation.utils.JavascriptHelper.Snippet;
import com.scalepoint.automation.utils.OperationalUtils;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.PseudoCategory;
import com.scalepoint.automation.utils.threadlocal.Browser;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import ru.yandex.qatools.htmlelements.element.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.scalepoint.automation.grid.ValuationGrid.Valuation.NEW_PRICE;
import static com.scalepoint.automation.grid.ValuationGrid.Valuation.VOUCHER;
import static com.scalepoint.automation.utils.OperationalUtils.assertEqualsDouble;
import static com.scalepoint.automation.utils.Wait.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.*;


public class SettlementDialog extends BaseDialog {

    private static final By OK_BUTTON = By.id("ok-button");
    private static final By ADD_BUTTON = By.id("add-button");
    private static final By CANCEL_BUTTON = By.id("cancel-button");
    public static final String ARIA_CHECKED = "aria-checked";
    public static final String TR_CONTAINS_CLASS = ".//tr[contains(@class, '";
    public static final String REJECT_REASON_COMBOBOX_INPUT_EL = "reject-reason-combobox-inputEl";
    public static final String DISCRETIONARY_REASON_COMBOBOX_INPUT_WRAP = "discretionary-reason-combobox-inputWrap";
    public static final String BORDER_COLOR = "border-color";
    public static final String CLASS = "class";
    public static final String X_FORM_TEXT_WRAP_INVALID = "x-form-text-wrap-invalid";
    public static final String REJECT_REASON_COMBOBOX_INPUT_WRAP = "reject-reason-combobox-inputWrap";

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
    private WebElement newPrice;

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

    @FindBy(id = "damage-type-combobox")
    private ExtComboBox damageType;

    @FindBy(id = "damage-type-combobox-trigger-picker")
    private WebElement damageTypePicker;

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

    @FindBy(id = "damage-checkbox-displayEl")
    private CheckBox damageCheckbox;

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

        public FormFiller withCategory(PseudoCategory category) {
            sid.setCategory(category.getGroupName());
            sid.setSubCategory(category.getCategoryName());
            return this;
        }

        public FormFiller withSubCategory(String subcategory) {
            sid.setSubCategory(subcategory);
            return this;
        }

        public FormFiller withSubCategoryFromTheListByIndex(int index) {
            sid.setSubCategoryByIndex(index);
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

        public FormFiller withValuation(ValuationGrid.Valuation valuation) {
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
        Wait.waitForJavascriptRecalculation();
        Wait.waitForAjaxCompleted();
        $(cancelButton).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
        JavascriptHelper.loadSnippet(Snippet.SID_GROUPS_LOADED);
        return this;
    }


    static final Pattern PATTERN = Pattern.compile("(?<voucherName>.*)\\((?<distance>[a-z0-9]*)\\s*km-(?<percentage>\\d*)%\\)");

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
        return fill(formFiller -> {
            formFiller.withText(claimItem.getTextFieldSP())
                    .withCategory(claimItem.getCategoryBabyItems())
                    .withCustomerDemandPrice(claimItem.getCustomerDemand())
                    .withNewPrice(claimItem.getNewPriceSP());
        });
    }

    public SettlementDialog fill(String description, String category, String subcategory, Double newPrice) {
        return fill(formFiller -> {
            formFiller.withText(description)
                    .withCategory(category)
                    .withSubCategory(subcategory)
                    .withNewPrice(newPrice);
        });
    }

    private SettlementDialog setExtInputValue(ExtInput input, String value) {
        waitForVisible(input);
        input.clear();
        input.enter(value);
        simulateBlurEvent(input);
        waitForJavascriptRecalculation();
        return this;
    }

    public void setSubCategoryByIndex(int index) {
        subCategory.select(index);
        waitForJavascriptRecalculation();
    }

    public SettlementDialog uncheckedDocumentation() {
        if (sufficientDocumentation.getAttribute(ARIA_CHECKED).equals("true")) {
            forCondition(ExpectedConditions.elementToBeClickable(sufficientDocumentation));
            clickUsingJsIfSeleniumClickReturnError(sufficientDocumentation);
        }
        waitForJavascriptRecalculation();
        return this;
    }

    private void simulateBlurEvent(ExtElement input) {
        ExtInput inputForClick = input == quantity ? description : quantity;
        inputForClick.getRootElement().click();
    }

    public SettlementDialog setDescription(String descriptionText) {
        return setExtInputValue(description, descriptionText);
    }

    public SettlementDialog setDescriptionAndWaitForCategoriesToAutoSelect(String descriptionText) {
        SettlementDialog settlementDialog = setDescription(descriptionText);
        Wait.waitForAjaxCompleted();
        Wait.waitForDisplayed(By.id("pseudocategory-combobox-inputEl"));
        return settlementDialog;
    }

    public SettlementDialog setNewPrice(Double amount) {
        SelenideElement element = $(newPrice).waitUntil(Condition.visible, 6000);
        element.doubleClick();
        element.sendKeys(Keys.DELETE);
        element.setValue(OperationalUtils.format(amount))
                .pressTab();
        waitForJavascriptRecalculation();
        return this;
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
        waitForJavascriptRecalculation();
        return this;
    }

    public SettlementDialog setSubCategory(String subCategoryName) {
        if (subCategoryName == null) {
            return this;
        }
        subCategory.select(subCategoryName);
        waitForJavascriptRecalculation();
        return this;
    }

    public SettlementDialog setCategory(PseudoCategory categoryInfo) {
        setCategory(categoryInfo.getGroupName());
        setSubCategory(categoryInfo.getCategoryName());
        return this;
    }

    public SettlementDialog fillVoucher(String voucherName) {
        if (voucher.isDisplayed()) {
            $("#vouchers-combobox-trigger-picker")
                    .hover()
                    .click();
            $$("#vouchers-combobox-picker-listEl div")
                    .stream()
                    .filter(element -> element.text().contains(voucherName))
                    .findFirst()
                    .get()
                    .click();
        } else {
            Wait.waitUntilVisible(availableVoucher);
            availableVoucher.select(voucherName);
        }
        waitForJavascriptRecalculation();
        return this;
    }

    public SettlementDialog enableAge() {
        age.select(1);
        return this;
    }

    public SettlementDialog enableAge(String years) {
        enableAge();
        enterAgeYears(years);
        waitForJavascriptRecalculation();
        return this;
    }

    public SettlementDialog enterAgeYears(String _ageYears) {
        return setExtInputValue(ageYears, _ageYears);
    }

    public SettlementDialog selectMonth(String monthName) {
        ageMonth.select(monthName);
        waitForJavascriptRecalculation();
        return this;
    }

    public SettlementDialog disableAge() {
        age.select(0);
        waitForJavascriptRecalculation();
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

    public SettlementPage addOneMoreManualLine() {
        return closeSid(SettlementPage.class, ADD_BUTTON, false);
    }

    public SettlementPage closeSidWithOk() {
        return closeSidWithOk(SettlementPage.class, OK_BUTTON);
    }

    public SettlementDialog clickOK() {
        $(ok).click();
        try {
            $(ok).click();
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
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
            forCondition(ExpectedConditions.elementToBeClickable(button));
            try {
                clickAndWait(buttonBy, button);
            } catch (TimeoutException e) {
                logger.error(e.getMessage());
                clickAndWait(buttonBy, button);
            }
            Wait.waitForAjaxCompleted();
            Wait.waitForJavascriptRecalculation();
        } catch (UnhandledAlertException ignored) {
        }

        if (acceptAlert) {
            acceptAlert();
        }

        return Page.at(pageClass);
    }

    private void clickAndWait(By buttonBy, WebElement button) {
        clickUsingJsIfSeleniumClickReturnError(button);
        Wait.waitElementDisappeared(buttonBy);
    }

    public SettlementDialog setDiscountAndDepreciation(Boolean state) {
        waitForVisible(combineDiscountDepreciation);
        combineDiscountDepreciation.set(state);
        waitForJavascriptRecalculation();
        return this;
    }

    public SettlementDialog includeInClaim(Boolean state) {
        Wait.waitForLoaded();
        Wait.waitForEnabled(includeInClaim);
        includeInClaim.set(state);
        waitForJavascriptRecalculation();
        return this;
    }

    private Double voucherFaceValueFieldText() {
        Wait.waitForLoaded();
        waitForVisible(voucherFaceValue);
        return OperationalUtils.getDoubleValue(voucherFaceValue.getText());
    }

    private Double voucherCashValueFieldText() {
        Wait.waitForLoaded();
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

    public SettlementDialog enableDamage() {
        damageCheckbox.select();
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
                String valuationDepreciation = Browser.driver().findElement(By.xpath(TR_CONTAINS_CLASS + NEW_PRICE + "')]//td[4]//div")).getText();
                return reductionRuleValue.toString().equals(valuationDepreciation);
            } catch (Exception e) {
                logger.error("Can't compare reduction and depreciationPercentage! " + e.getMessage(), e);
            }
            return true;
        });
        waitForJavascriptRecalculation();
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

    private boolean isDamageTypeVisible() {
        return damageType.exists();
    }

    private boolean isRejectReasonEnabled() {
        return $(rejectReason).isEnabled();
    }

    private boolean isDamageTypeEnabled() {
        return damageType.isEnabled();
    }

    private boolean isRejectReasonDisabled(String visibleText) {
        new Actions(driver).click(driver.findElement(By.id("reject-reason-combobox"))).build().perform();
        $(By.id(REJECT_REASON_COMBOBOX_INPUT_EL)).setValue(visibleText);
        return driver.findElement(By.xpath("//span[text()='" + visibleText + "']")).getAttribute("style").equalsIgnoreCase("color: silver;");
    }

    private boolean isDiscretionaryReasonVisible() {
        try {
            return Wait.forCondition1s(driver -> discretionaryReason.isDisplayed());
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isDiscretionaryReasonEnabled() {
        waitForVisible(discretionaryReason);
        return discretionaryReason.isEnabled();
    }

    public SettlementDialog selectDiscretionaryReason(String visibleText) {
        waitForVisible(discretionaryReason);
        discretionaryReason.select(visibleText);
        waitForJavascriptRecalculation();
        return this;
    }

    public SettlementDialog selectRejectReason(String visibleText) {
        waitForVisible(rejectReason);
        new Actions(driver).click(driver.findElement(By.id("reject-reason-combobox"))).build().perform();
        $(By.id(REJECT_REASON_COMBOBOX_INPUT_EL)).setValue(visibleText);
        new Actions(driver).click(driver.findElement(By.xpath("//span[text()='" + visibleText + "']"))).build().perform();
        new Events().fireEvent($(By.id(REJECT_REASON_COMBOBOX_INPUT_EL)), "focus", "keydown", "keypress", "input", "keyup", "change");
        waitForJavascriptRecalculation();
        return this;
    }

    public SettlementDialog clickDamageTypePicker(){
        damageTypePicker.click();
        return this;
    }

    public SettlementDialog selectDamageType(String visibleText){
        waitForEnabled(damageType);
        clickDamageTypePicker();
        SelenideElement element = $(By.xpath(String.format("//ul[@id='damage-type-combobox-picker-listEl']/li[text()='%s']", visibleText)));
        element.click();
        waitForJavascriptRecalculation();
        return this;
    }

    public SettlementDialog viewDamageTypeValidationErrorMessage(){

        new Actions(driver).moveToElement($("#damage-type-combobox-inputEl")).build().perform();
        return this;
    }

    public SettlementDialog setDepreciationType(DepreciationType depreciation) {
        waitForVisible(depreciationTypeComboBox);
        depreciationTypeComboBox.select(depreciation.index);
        waitForJavascriptRecalculation();
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

    private String getDamageTypeText() {
        return damageType.getValue();
    }

    private boolean isDiscretionaryReasonHasRedBorder() {
        String redBorder = "#c30";
        String redBorderRGB = "rgb(204, 51, 0)";
        if (waitForReasonInvalidAttribute("//*[@id='discretionary-reason-combobox-inputWrap' and contains(@class, 'x-form-text-wrap-invalid')]")) {
            return false;
        }
        return driver.findElement(By.id(DISCRETIONARY_REASON_COMBOBOX_INPUT_WRAP)).getAttribute(CLASS).contains(X_FORM_TEXT_WRAP_INVALID)
                && (driver.findElement(By.id(DISCRETIONARY_REASON_COMBOBOX_INPUT_WRAP)).getCssValue(BORDER_COLOR).contains(redBorder) ||
                (driver.findElement(By.id(DISCRETIONARY_REASON_COMBOBOX_INPUT_WRAP)).getCssValue(BORDER_COLOR).contains(redBorderRGB)));
    }

    private boolean waitForReasonInvalidAttribute(String xpath) {
        try {
            Wait.forCondition(d -> Browser.driver().findElement(By.xpath(xpath)).isDisplayed(), 5);
        } catch (NoSuchElementException e) {
            logger.info(e.getMessage());
            return true;
        }
        return false;
    }

    private boolean isRejectReasonHasRedBorder() {
        String redBorder = "#c30";
        String redBorderRGB = "rgb(204, 51, 0)";
        if (waitForReasonInvalidAttribute("//*[@id='reject-reason-combobox-inputWrap' and contains(@class, 'x-form-text-wrap-invalid')]")) {
            return false;
        }
        return driver.findElement(By.id(REJECT_REASON_COMBOBOX_INPUT_WRAP)).getAttribute(CLASS).contains(X_FORM_TEXT_WRAP_INVALID)
                && (driver.findElement(By.id(REJECT_REASON_COMBOBOX_INPUT_WRAP)).getCssValue(BORDER_COLOR).contains(redBorder) ||
                (driver.findElement(By.id(REJECT_REASON_COMBOBOX_INPUT_WRAP)).getCssValue(BORDER_COLOR).contains(redBorderRGB)));
    }

    String discountDistributionLocator = ".//tr[contains(@class, '%s')]//img";

    public EditVoucherValuationDialog openEditDiscountDistributionForVoucher() {
        IntStream.range(0, 3).forEach(i -> clickUsingJsIfSeleniumClickReturnError(waitForDisplayed(By.xpath(String.format(discountDistributionLocator, VOUCHER.getClassName())))));
        return at(EditVoucherValuationDialog.class);
    }

    public boolean isDiscountDistributionDisplayed() {
        try {
            return waitForDisplayed(By.xpath(String.format(discountDistributionLocator, VOUCHER.getClassName()))).isDisplayed();
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

    public String getVoucherName(){
        String voucherName = $("#vouchers-combobox-inputEl").getAttribute("value");
        Matcher m = PATTERN.matcher(voucherName);
        if (m.find())
            voucherName = m.group("voucherName").trim();
        return voucherName;
    }

    public List<VoucherDropdownElement> parseVoucherDropdown() {
        List<String> comboBoxOptions = voucher.getComboBoxOptions();
        return comboBoxOptions.stream().map(VoucherDropdownElement::new).collect(Collectors.toList());
    }

    public ValuationGrid valuationGrid(){
        return new ValuationGrid();
    }

    public SettlementDialog setValuation(ValuationGrid.Valuation valuation) {
        return new ValuationGrid()
                .parseValuationRow(valuation)
                .makeActive()
                .backToGrid()
                .toSettlementDialog();
    }

    private VoucherDropdownElement parseVoucherDropdownElement(String voucherName) {
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

    public static class VoucherDropdownElement {
        private String voucherName;
        private boolean distanceCalculated;
        private int distance;
        private int percentage;


        VoucherDropdownElement(String text) {
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

        public Asserts assertDescriptionContains(String expectedDescription) {
            assertThat(expectedDescription)
                    .as(String.format("The description should contatins: %s", expectedDescription))
                    .contains(expectedDescription);
            return this;
        }

        public Asserts assertCategoryTextIs(String expectedCategory) {
            assertEquals(getCategoryText(), expectedCategory, "The Category is not Saved");
            return this;
        }

        public Asserts assertCategoriesTextIs(PseudoCategory expectedCategory) {
            assertEquals(getCategoryText(), expectedCategory.getGroupName(), "The category group is not Saved");
            assertEquals(getSubCategoryText(), expectedCategory.getCategoryName(), "The Subcategory is not Saved");
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
            logger.info("AssertVoucherListed: " + voucherTitle);
            Assert.assertTrue(getVouchersList().stream().anyMatch(i -> {
                logger.info("Found: " + i);
                return i.contains(voucherTitle);
            }), "Voucher " + voucherTitle + " must be present");
            return this;
        }

        public Asserts assertVoucherNotListed(String voucherTitle) {
            logger.info("assertVoucherNotListed: " + voucherTitle);
            Assert.assertFalse(getVouchersList().stream().anyMatch(i -> {
                logger.info("Found: " + i);
                return i.contains(voucherTitle);
            }), "Voucher " + voucherTitle + " must not be present");
            return this;
        }

        public Asserts assertVoucherIsSelected(String voucherName){
            assertVoucherCardIsShown();
            String actualSelectedVoucher = getVoucherName();
            assertThat(actualSelectedVoucher.equals(voucherName))
                    .as("voucher selected should be " + voucherName + " but was " + actualSelectedVoucher).isTrue();
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

        public Asserts assertMarketPriceSupplierInvisible() {
            assertFalse(isMarketPriceSupplierDisplayed(), "Market price must be not visible");
            return this;
        }

        public Asserts assertRejectReasonDisabled() {
            assertFalse(isRejectReasonEnabled(), "Reject Reason must be disabled");
            return this;
        }
        public Asserts assertDamageTypeDisabled() {
            assertThat(isDamageTypeEnabled())
                    .as("Damage Type must be disabled")
                    .isFalse();
            return this;
        }

        public Asserts assertRejectReasonVisible() {
            assertTrue(isRejectReasonVisible(), "Reject Reason must be visible");
            return this;
        }

        public Asserts assertDamageTypeVisible() {
            assertThat(isDamageTypeVisible())
                    .as("Damage type must be visible")
                    .isTrue();
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

        public Asserts assertDamageTypeEnabled() {
            assertThat(isDamageTypeEnabled())
                    .as("DamageType must be enabled")
                    .isTrue();
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

        public Asserts assertDamageTypeEqualTo(String damageType) {
            assertThat(getDamageTypeText())
                    .as("Wrong damage type selected")
                    .isEqualTo(damageType);
            return this;
        }

        public Asserts assertBrandTextIs(String brandLink) {
            assertEquals($(brand).getText(), brandLink, "Wrong Brand is Displayed");
            return this;
        }

        public Asserts assertVoucherCardIsShown() {
            SelenideElement voucherCard = $("#status_voucher_replacement_card");
            assertThat(voucherCard.is(Condition.visible)).as("voucher card is not displayed").isTrue();
            return this;
        }

        public Asserts assertScalepointSupplierNotVisible() {
            assertFalse(statusSupplier.exists(), "Scalepoint supplier must not be visible");
            return this;
        }

        public Asserts assertScalepointSupplierVisible(String supplier) {
            waitForAjaxCompleted();
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
            assertEquals(automaticDepreciation.isSelected(), !isLabelInRedColor);
            return this;
        }

        public Asserts assertIsSufficientDocumentationCheckboxDisplayedAndItIsChecked() {
            assertEquals("true", sufficientDocumentation.getAttribute(ARIA_CHECKED));
            return this;
        }

        public Asserts assertIsSufficientDocumentationCheckboxDisplayedAndItIsUnchecked() {
            assertEquals("false", sufficientDocumentation.getAttribute(ARIA_CHECKED));
            return this;
        }

        public Asserts assertThereIsNoAddButton(){
            assertThat($(ADD_BUTTON).is(Condition.hidden)).isTrue().as("Add button should be visible");
            return this;
        }
        public Asserts assertHasDamageTypeValidationError(String errorMessage){
            SelenideElement element = $(By.xpath(String.format("//*[contains(text(),\"%s\")]", errorMessage)));
            assertThat(element.exists())
                    .as(String.format("Validation error message should contain following text: %s", errorMessage))
                    .isTrue();
            return this;
        }

        public Asserts assertDamageTypesRelevantForCategory(List<String> damageTypes){
            ElementsCollection elements = $$("#damage-type-combobox-picker-listEl>li");
            assertThat(elements.size())
                    .as("Damage types should contain only damage types relevant for current category")
                    .isEqualTo(damageTypes.size());
            elements
                    .stream()
                    .parallel()
                    .forEach(element -> assertThat(damageTypes.stream()
                            .filter(d -> d.equals(element.getText())).count())
                            .as(String.format("Damage type: %s is not relevant for this category", element.getText()))
                            .isEqualTo(1));
            return this;
        }

        public Asserts assertIsVoucherDiscountApplied(Double newPrice) {
            ValuationGrid.ValuationRow valuationRow = new ValuationGrid().parseValuationRow(VOUCHER);
            assertEquals(valuationRow.getCashCompensation(), newPrice - (newPrice * getVoucherPercentage() / 100), 0.0);
            return this;
        }
    }
}

