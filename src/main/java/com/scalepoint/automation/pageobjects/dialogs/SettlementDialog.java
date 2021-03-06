package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.ex.ElementNotFound;
import com.codeborne.selenide.ex.ElementShould;
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
import com.scalepoint.automation.utils.data.entity.input.ClaimItem;
import com.scalepoint.automation.utils.data.entity.input.PseudoCategory;
import com.scalepoint.automation.utils.threadlocal.Browser;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import ru.yandex.qatools.htmlelements.element.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Condition.*;
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
    private static final By GROUP_COMBOBOX = By.id("group-combobox");
    private static final By PSEUDOCATEGORY_COMBOBOX = By.id("pseudocategory-combobox");
    private static final By AGE_MONTHS_COMBOBOX = By.id("age-months-combobox");
    private static final By AVAILABLE_VOUCHER_COMBOBOX = By.id("available-vouchers-combobox");
    private static final By VOUCHER_COMBOBOX = By.id("vouchers-combobox");
    private static final By REJECTION_REASON_COMBOBOX = By.id("reject-reason-combobox");
    private static final By DAMAGE_TYPE_COMBOBOX = By.id("damage-type-combobox");
    private static final By DISCRETIONARY_REASON_COMBOBOX = By.id("discretionary-reason-combobox");
    private static final By DEPRECIATION_TYPE_COMBOBOX = By.id("depreciation-type-combobox");
    private static final By AGE_DEFINED_RADIOGROUP = By.id("age-defined-radiogroup");
    private static final By REVIEWED_CHECKBOX = By.id("reviewed-checkbox");
    private static final By DEPRECIATE_DISCOUNTED_CHECKBOX = By.id("depreciate-discounted-checkbox");
    private static final By ACTIVE_CHECKBOX = By.id("active-checkbox");
    private static final By AUTOMATIC_DEPRECATION_CHECKBOX = By.id("automatic-depreciation-checkbox");
    private static final By DOCUMENTATION_OK_CHECKBOX = By.id("documentation-ok-checkbox");
    public static final String ARIA_CHECKED = "aria-checked";
    public static final String TR_CONTAINS_CLASS = ".//tr[contains(@class, '";
    public static final String REJECT_REASON_COMBOBOX_INPUT_EL = "reject-reason-combobox-inputEl";
    public static final String DISCRETIONARY_REASON_COMBOBOX_INPUT_WRAP = "discretionary-reason-combobox-inputWrap";
    public static final String BORDER_COLOR = "border-color";
    public static final String CLASS = "class";
    public static final String X_FORM_TEXT_WRAP_INVALID = "x-form-text-wrap-invalid";
    public static final String REJECT_REASON_COMBOBOX_INPUT_WRAP = "reject-reason-combobox-inputWrap";

    @FindBy(id = "description-textfield-inputEl")
    private SelenideElement description;
    @FindBy(css = "#voucher-supplier-link a")
    private SelenideElement voucherSupplierLink;
    @FindBy(id = "quantity-textfield-inputEl")
    private SelenideElement quantity;
    @FindBy(id = "customer-demand-textfield-inputEl")
    private SelenideElement customerDemand;
    @FindBy(id = "new-price-textfield-inputEl")
    private SelenideElement newPrice;
    @FindBy(id = "depreciation-textfield-inputEl")
    private SelenideElement depreciationPercentage;
    @FindBy(id = "discretionary-replacement-textfield-inputEl")
    private SelenideElement discretionaryPrice;
    @FindBy(id = "voucher-face-value-text")
    private SelenideElement voucherFaceValue;
    @FindBy(id = "voucher-price-text")
    private SelenideElement voucherCashValue;
    @FindBy(id = "cancel-button")
    private SelenideElement cancelButton;
    @FindBy(id = "age-years-textfield-inputEl")
    private SelenideElement ageYears;
    @FindBy(id="age-months-combobox-inputEl")
    private SelenideElement ageMonths;
    @FindBy(id = "automatic-depreciation-checkbox-labelEl")
    private SelenideElement automaticDepreciationLabel;
    @FindBy(id = "marketprice-card-supplier-inputEl")
    private SelenideElement marketPriceSupplier;
    @FindBy(id = "damage-type-combobox-trigger-picker")
    private SelenideElement damageTypePicker;
    @FindBy(xpath = "//div[@id='status_product_match_card']//div[contains(@id, 'displayfield')]/b")
    private SelenideElement statusMatchedDisplayField;

    private Table getFirstValuation(){

        return new Table($(By.cssSelector("#valuations-grid-body table:first-child")));
    }

    private Table getRuleSuggestion(){

        return new Table($(By.cssSelector("#rule-suggestion-grid-body table")));
    }

    private List<Table> getValuations(){

        return $$(By.cssSelector("#valuations-grid-body table")).stream()
                .map(Table::new)
                .collect(Collectors.toList());
    }

    private Link getAddValuation(){

        return new Link($(By.id("add-valuation-button")));
    }

    private Link getVoucherLink(){

        return new Link($(By.cssSelector("#voucher-supplier-link a")));
    }

    private Button getVoucherValuationCard(){

        return new Button($(By.id("voucher-valuation-card-edit-valuation")));
    }

    private Button getVoucherTermAndConditions(){

        return new Button($(By.id("voucher-valuation-card-valuation-terms")));
    }

    private Button getOk(){

        return new Button($(By.id("ok-button")));
    }

    private Button getFindShopButton(){

        return new Button($(By.id("voucher-valuation-card-find-shop")));
    }

    private Button getAddButton(){

        return new Button($(By.id("add-button")));
    }

    private Button getNotCheapestReasonEdit(){

        return new Button($(By.id("not-cheapest-reason-edit-button")));
    }

    private TextBlock getStatusSupplier(){

        return new TextBlock($(By.id("productmatch-card-supplier")));
    }

    private TextBlock getNotCheapestReasonDisplay(){

        return new TextBlock($(By.id("not-cheapest-reason-display-inputEl")));
    }

    private TextBlock getCashCompensationValue(){

        return new TextBlock($(By.id("total-cash-compensation-text")));
    }

    private TextBlock getDeprecationAmount(){

        return new TextBlock($(By.id("total-depreciation-text")));
    }

    private CheckBox getRejectCheckbox(){

        return new CheckBox($(By.id("reject-checkbox-displayEl")));
    }

    private CheckBox getDamageCheckbox(){

        return new CheckBox($(By.id("damage-checkbox-displayEl")));
    }

    private GroupExtComboBox getCategory(){

        return new GroupExtComboBox($(GROUP_COMBOBOX));
    }

    private PseudoCategoryComboBox getSubCategory(){

        return new PseudoCategoryComboBox($(PSEUDOCATEGORY_COMBOBOX));
    }

    private AgeMonthsComboBox getAgeMonth(){

        return new AgeMonthsComboBox($(AGE_MONTHS_COMBOBOX));
    }

    private AvailableVouchersExtComboBox getAvailableVoucher(){

        return new AvailableVouchersExtComboBox($(AVAILABLE_VOUCHER_COMBOBOX));
    }

    private VoucherComboBox getVoucher(){

        return new VoucherComboBox($(VOUCHER_COMBOBOX));
    }

    private ExtRadioGroupTypeDiv getAge(){

        return new ExtRadioGroupTypeDiv($(AGE_DEFINED_RADIOGROUP));
    }

    private ExtCheckboxTypeDiv getReviewed(){

        return new ExtCheckboxTypeDiv($(REVIEWED_CHECKBOX));
    }

    private ExtCheckboxTypeDiv getCombineDiscountDepreciation(){

        return new ExtCheckboxTypeDiv($(DEPRECIATE_DISCOUNTED_CHECKBOX));
    }

    private ExtCheckboxTypeDiv getIncludeInClaim(){

        return new ExtCheckboxTypeDiv($(ACTIVE_CHECKBOX));
    }

    private ExtCheckboxTypeDiv getAutomaticDepreciation(){

        return new ExtCheckboxTypeDiv($(AUTOMATIC_DEPRECATION_CHECKBOX));
    }

    private RejectReasonComboBox getRejectReason(){

        return new RejectReasonComboBox($(REJECTION_REASON_COMBOBOX));
    }

    private DamageTypeComboBox damageType(){

        return new DamageTypeComboBox($(DAMAGE_TYPE_COMBOBOX));
    }

    private DiscretionaryReasonExtComboBox getDiscretionaryReason(){

        return new DiscretionaryReasonExtComboBox($(DISCRETIONARY_REASON_COMBOBOX));
    }

    private DepreciationTypeExtComboBox getDepreciationTypeComboBox(){

        return new DepreciationTypeExtComboBox($(DEPRECIATION_TYPE_COMBOBOX));
    }

    private ExtCheckboxTypeDiv getSufficientDocumentation(){

        return new ExtCheckboxTypeDiv($(DOCUMENTATION_OK_CHECKBOX));
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

        public FormFiller withAvailableVoucher(String voucher) {

            sid.fillAvailableVoucher(voucher);
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
    protected void ensureWeAreAt() {

        waitForAjaxCompletedAndJsRecalculation();
        cancelButton.should(Condition.visible);
        JavascriptHelper.loadSnippet(Snippet.SID_GROUPS_LOADED);
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

    private SettlementDialog setInputValue(SelenideElement input, String value) {

        input.should(visible);
        input.clear();
        input.setValue(value);
        JavascriptHelper.blur();
        waitForAjaxCompletedAndJsRecalculation();
        return this;
    }

    public void setSubCategoryByIndex(int index) {

        getSubCategory().select(index);
        waitForJavascriptRecalculation();
    }

    public SettlementDialog uncheckedDocumentation() {

        getSufficientDocumentation().set(false);
        return this;
    }

    public SettlementDialog setDescription(String descriptionText) {
        return setInputValue(description, descriptionText);
    }

    public SettlementDialog setDescriptionAndWaitForCategoriesToAutoSelect(String descriptionText) {

        SettlementDialog settlementDialog = setDescription(descriptionText);
        waitForAjaxCompleted();
        $(By.id("pseudocategory-combobox-inputEl")).should(visible);
        return settlementDialog;
    }

    public SettlementDialog setNewPrice(Double amount) {

        SelenideElement element = $(newPrice).should(Condition.visible);
        element.doubleClick();
        element.sendKeys(Keys.DELETE);
        element.setValue(OperationalUtils.format(amount))
                .pressTab();
        waitForJavascriptRecalculation();
        return this;
    }

    public SettlementDialog setDiscretionaryPrice(Double amount) {

        return setInputValue(discretionaryPrice, OperationalUtils.format(amount));
    }

    public SettlementDialog setCustomerDemand(Double amount) {

        return setInputValue(customerDemand, OperationalUtils.format(amount));
    }

    public SettlementDialog setDepreciation(Integer amount) {

        return setInputValue(depreciationPercentage, amount.toString());
    }

    public SettlementDialog setCategory(String categoryName) {

        getCategory().select(categoryName);
        waitForJavascriptRecalculation();
        return this;
    }

    public SettlementDialog setSubCategory(String subCategoryName) {

        if (subCategoryName == null) {

            return this;
        }
        getSubCategory().select(subCategoryName);
        waitForJavascriptRecalculation();
        return this;
    }

    public SettlementDialog setCategory(PseudoCategory categoryInfo) {

        setCategory(categoryInfo.getGroupName());
        setSubCategory(categoryInfo.getCategoryName());
        return this;
    }

    public SettlementDialog fillVoucher(String voucherName) {

        VoucherComboBox voucher = getVoucher();
        try {

            if (!voucherSupplierLink.getText().contains(voucherName)){
                voucher.select(voucherName);
            }
        }catch (ElementShould e){
            voucher.select(voucherName);
        }

        return this;
    }

    public SettlementDialog fillAvailableVoucher(String voucherName) {

        getAvailableVoucher().select(voucherName);
        return this;
    }

    public SettlementDialog enableAge() {

        getAge().select(1);
        return this;
    }

    public SettlementDialog enableAge(String years) {

        enableAge();
        enterAgeYears(years);
        waitForJavascriptRecalculation();
        return this;
    }

    public SettlementDialog enterAgeYears(String _ageYears) {

        return setInputValue($(ageYears), _ageYears);
    }

    public SettlementDialog selectMonth(String monthName) {

        getAgeMonth().select(monthName);
        waitForJavascriptRecalculation();
        return this;
    }

    public SettlementDialog disableAge() {

        getAge().select(0);
        waitForJavascriptRecalculation();
        return this;
    }

    private Double fetchDepreciationAmount() {

        waitForLoaded();
        $(getDeprecationAmount()).should(visible);
        return OperationalUtils.getDoubleValue(getDeprecationAmount().getText());
    }

    public SettlementDialog setReviewed(boolean state) {

        getReviewed().set(state);
        return this;
    }

    public SettlementDialog addOneMoreManualLine() {

        SelenideElement button = $(ADD_BUTTON);
        clickButton(button, false);

        return BaseDialog.at(SettlementDialog.class);
    }

    public SettlementPage closeSidWithOk() {

        return closeSidWithOk(SettlementPage.class, OK_BUTTON);
    }

    public SettlementDialog clickOK() {

        $(getOk()).click();
        try {

            $(getOk()).click();
        } catch (Throwable  e) {

            logger.info(e.getMessage());
        }
        return this;
    }

    public <T extends Page> T closeSidWithOk(Class<T> pageClass) {

        return closeSidWithOk(pageClass, OK_BUTTON);
    }

    public <T extends BaseDialog> T tryToCloseSidWithOkButExpectDialog(Class<T> dialogClass) {

        $(OK_BUTTON).should(visible).click();
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

        SelenideElement button = $(buttonBy);
        clickButton(button, acceptAlert);
        button.should(disappear);
        Wait.waitForAjaxCompletedAndJsRecalculation();

        return Page.at(pageClass);
    }

    private void clickButton(SelenideElement button,  boolean acceptAlert){

        hoverAndClick(button);

        if (acceptAlert) {

            acceptAlert();
        }
    }

    public SettlementDialog setDiscountAndDepreciation(Boolean state) {

        getCombineDiscountDepreciation().set(state);
        waitForJavascriptRecalculation();
        return this;
    }

    public SettlementDialog includeInClaim(Boolean state) {

        Wait.waitForLoaded();
        getIncludeInClaim().set(state);
        waitForJavascriptRecalculation();
        return this;
    }

    private Double voucherFaceValueFieldText() {

        Wait.waitForLoaded();
        voucherFaceValue.should(visible);
        return OperationalUtils.getDoubleValue(voucherFaceValue.getText());
    }

    private Double voucherCashValueFieldText() {

        Wait.waitForLoaded();
        voucherCashValue.should(visible);
        return OperationalUtils.getDoubleValue(voucherCashValue.getText());
    }

    public Double customerDemandValue() {

        Wait.waitForLoaded();
        SelenideElement selenideElement = customerDemand.should(visible);
        return OperationalUtils.getDoubleValue(selenideElement.getValue());
    }

    public Double getCashCompensation() {

        $(getCashCompensationValue()).should(visible);
        return OperationalUtils.getDoubleValue(getCashCompensationValue().getText());
    }

    public Double DeprecationValue() {

        $(getDeprecationAmount()).should(visible);
        return OperationalUtils.getDoubleValue(getDeprecationAmount().getText());
    }

    private String getCategoryText() {

        Wait.waitForLoaded();
        $(getCategory()).should(visible);
        return getCategory().getValue();
    }

    private String getSubCategoryText() {

        PseudoCategoryComboBox subCategory = getSubCategory();
        Wait.waitForLoaded();
        $(getSubCategory()).should(visible);
        return subCategory.getValue();
    }

    public String getDescriptionText() {

        Wait.waitForLoaded();
        SelenideElement selenideElement = description.should(visible);
        return selenideElement.getValue();
    }

    public AddValuationDialog openAddValuationForm() {

        getAddValuation().click();
        return BaseDialog.at(AddValuationDialog.class);
    }

    public String getNotCheapestChoiceReason() {

        return $(getNotCheapestReasonDisplay()).getText();
    }

    public SettlementDialog rejectClaim() {

        getRejectCheckbox().select();
        return this;
    }

    public SettlementDialog enableDamage() {

        getDamageCheckbox().select();
        return this;
    }

    public EditVoucherValuationDialog openVoucherValuationCard() {

        $(getVoucherValuationCard()).click();
        return BaseDialog.at(EditVoucherValuationDialog.class);
    }

    public VoucherTermsAndConditionsDialog openVoucherTermAndConditions() {

        $(getVoucherTermAndConditions()).click();
        return BaseDialog.at(VoucherTermsAndConditionsDialog.class);
    }

    public FindShopDialog openFindShopDialog() {

        getFindShopButton().click();
        return BaseDialog.at(FindShopDialog.class);
    }

    public SettlementDialog applyReductionRuleByValue(Integer reductionRuleValue) {

        Wait.waitForLoaded();
        boolean foundRule = false;
        List<List<WebElement>> rowsNames = getRuleSuggestion().getRows();
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

        getAutomaticDepreciation().set(state);
        Wait.waitForLoaded();
        return this;
    }

    public Integer getDepreciationPercentage() {

        return Integer.valueOf($(depreciationPercentage).getValue());
    }

    private boolean isMarketPriceSupplierDisplayed() {

        try {

            return marketPriceSupplier.isDisplayed();
        } catch (NoSuchElementException e) {

            return false;
        }
    }

    private boolean isRejectReasonVisible() {

        return getRejectReason().exists();
    }

    private boolean isDamageTypeVisible() {

        return damageType().exists();
    }

    private boolean isRejectReasonEnabled() {

        return getRejectReason().isEnabled();
    }

    private boolean isDamageTypeEnabled() {

        return damageType().isEnabled();
    }

    private boolean isRejectReasonDisabled(String visibleText) {

        new Actions(driver).click(driver.findElement(By.id("reject-reason-combobox"))).build().perform();
        $(By.id(REJECT_REASON_COMBOBOX_INPUT_EL)).setValue(visibleText);
        return driver.findElement(By.xpath("//span[text()='" + visibleText + "']")).getAttribute("style").equalsIgnoreCase("color: silver;");
    }

    private boolean isDiscretionaryReasonVisible() {

        try {

            return Wait.forCondition1s(driver -> getDiscretionaryReason().isDisplayed());
        } catch (Exception e) {

            return false;
        }
    }

    private boolean isDiscretionaryReasonEnabled() {

        return getDiscretionaryReason().isEnabled();
    }

    public SettlementDialog selectDiscretionaryReason(String visibleText) {
        int trials = 2;

        do {

            try {

                getDiscretionaryReason().select(visibleText);
                break;
            } catch (ElementNotFound e) {

                trials--;
                continue;
            }
        }while (trials > 0);
        waitForJavascriptRecalculation();
        return this;
    }

    public SettlementDialog selectRejectReason(String visibleText) {

        $(getRejectReason()).should(visible);
        new Actions(driver).click($(By.id("reject-reason-combobox"))).build().perform();
        $(By.id(REJECT_REASON_COMBOBOX_INPUT_EL)).setValue(visibleText);
        new Actions(driver).click($(By.xpath("//span[text()='" + visibleText + "']"))).build().perform();
        Events.events.fireEvent(WebDriverRunner.driver(), $(By.id(REJECT_REASON_COMBOBOX_INPUT_EL)),"focus", "keydown", "keypress", "input", "keyup", "change");
        waitForJavascriptRecalculation();
        return this;
    }

    public SettlementDialog clickDamageTypePicker(){

        damageTypePicker.click();
        return this;
    }

    public SettlementDialog selectDamageType(String visibleText){

        damageType().select(visibleText);
        waitForJavascriptRecalculation();
        return this;
    }

    public SettlementDialog viewDamageTypeValidationErrorMessage(){

        new Actions(driver).moveToElement($("#damage-type-combobox-inputEl")).build().perform();
        return this;
    }

    public SettlementDialog setDepreciationType(DepreciationType depreciation) {

        getDepreciationTypeComboBox().select(depreciation.index);
        waitForJavascriptRecalculation();
        return this;
    }

    public SettlementDialog setDepreciationType(String visibleText) {
        getDepreciationTypeComboBox().select(visibleText);
        return this;
    }

    public NotCheapestChoiceDialog editNotCheapestReason() {

        $(getNotCheapestReasonEdit()).should(visible).click();

        return BaseDialog.at(NotCheapestChoiceDialog.class);
    }

    public SettlementDialog selectOtherCategoryIfNotChosen() {

        String value = getCategory().getValue();
        if (StringUtils.isBlank(value)) {

            getCategory().select("??vrige");
        }
        return this;
    }

    private String getDiscretionaryReasonText() {

        return getDiscretionaryReason().getValue();
    }

    private String getRejectReasonText() {

        return getRejectReason().getValue();
    }

    private String getDamageTypeText() {

        return damageType().getValue();
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

    String discountDistributionLocator = ".//tr[contains(@class, '%s')]/td[contains(@data-columnid,'editValuation')]/div";

    public EditVoucherValuationDialog openEditDiscountDistributionForVoucher() {

        $(By.xpath(String.format(discountDistributionLocator, VOUCHER.getClassName())))
                .hover()
                .doubleClick();
        return BaseDialog.at(EditVoucherValuationDialog.class);
    }

    public boolean isDiscountDistributionDisplayed() {

        try {

            return $(By.xpath(String.format(discountDistributionLocator, VOUCHER.getClassName()))).has(visible);
        } catch (ElementNotFound e) {

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

        List<String> comboBoxOptions = getVoucher().getComboBoxOptions();
        return comboBoxOptions.stream().map(VoucherDropdownElement::new).collect(Collectors.toList());
    }

    public ValuationGrid valuationGrid(){

        return new ValuationGrid();
    }

    public SettlementDialog setValuation(ValuationGrid.Valuation valuation) {

        return new ValuationGrid()
                .getValuationRow(valuation)
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

    public AgeDatePicker openAgeDatePicker() {

        $("#purchase-date-button-btnInnerEl").click();
        isDataPickerOpened();
        return new AgeDatePicker();
    }

    private void isDataPickerOpened() {

        $(".x-datepicker").shouldBe(Condition.visible);
    }


    public class AgeDatePicker {

        public AgeDatePicker openMonthYearLists() {

            $(".x-btn-split").click();
            $(".x-monthpicker").shouldBe(Condition.visible);
            return this;
        }

        public AgeDatePicker selectYear(String yearToSelect) {

            if (findYearInTheList(yearToSelect).isPresent()) {

                findYearInTheListAndClick(yearToSelect);
            } else {

                navigateToPreviousPeriodPage();
                findYearInTheListAndClick(yearToSelect);
            }
            confirmSelection();
            return this;
        }

        private void confirmSelection() {

            $$(".x-monthpicker-buttons a").get(0).click();
        }

        private Optional<SelenideElement> findYearInTheList(String year) {

            ElementsCollection listOfYears = $$(".x-monthpicker-year a");
            return listOfYears.stream().filter(y -> y.getText().equals(year)).findAny();
        }

        private void findYearInTheListAndClick(String year){

            findYearInTheList(year)
                    .orElseThrow(java.util.NoSuchElementException::new).click();
        }

        private void navigateToPreviousPeriodPage() {

            $(".x-monthpicker-yearnav-prev").click();
        }

        public SettlementDialog closePicker() {

            $(".x-datepicker-selected").click();
            return at(SettlementDialog.class);
        }
    }

    public SettlementDialog doAssert(Consumer<Asserts> func) {

        func.accept(new Asserts());
        return SettlementDialog.this;
    }

    public Boolean isAlertOnVoucherDiscountChangePresent(){

        ElementsCollection e = $$("div[role='alertdialog']");
        return !e.isEmpty();
    }

    public class Asserts {

        public Asserts assertDiscretionaryReasonValuePresent(String expectedValue) {

            List<String> options = getDiscretionaryReason().getComboBoxOptions();
            Assert.assertTrue(options.stream().anyMatch(i -> i.contains(expectedValue)));
            return this;
        }

        public Asserts assertCashValueIs(Double expectedCashValue) {

            OperationalUtils.assertEqualsDoubleWithTolerance(getCashCompensation(), expectedCashValue,
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

        public Asserts assertThereIsNoAlertOnVoucherDiscountChange(){

            assertThat(isAlertOnVoucherDiscountChangePresent())
                    .as("alert saying the voucher discount has changed should not be present")
                    .isFalse();
            return this;
        }

        public Asserts assertReviewedNotPresent() {

            assertFalse(getReviewed().isChecked(), "Reviewed checkbox must be disabled");
            return this;
        }

        public Asserts assertOkButtonIsDisabled(){

            $(getOk()).shouldHave(attribute("aria-disabled", "true"));
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

            VoucherComboBox voucher = getVoucher();
            if (voucher.isDisplayed()) {
                return voucher.getComboBoxOptions();
            } else {
                return getAvailableVoucher().getComboBoxOptions();
            }
        }

        public Asserts assertIncludeInClaimSelected() {

            Assert.assertTrue(getIncludeInClaim().isChecked(), "The 'Include in Claim' must be selected'");
            return this;
        }

        public Asserts assertIncludeInClaimNotSelected() {

            Assert.assertFalse(getIncludeInClaim().isChecked(), "The 'Include in Claim' must be unselected'");
            return this;
        }

        public Asserts assertNotCheapestReasonIs(String reason) {

            assertEquals(getNotCheapestChoiceReason(), reason, "Reason must be: " + reason);
            return this;
        }

        public Asserts assertSubCategoriesListEqualTo(List<String> expectedSubCategoriesList) {

            PseudoCategoryComboBox subCategory = getSubCategory();
            $(subCategory).should(visible);
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

            assertThat(voucherCashValueFieldText()).isEqualTo(OperationalUtils.getDoubleValue(expectedValue));
            return this;
        }

        public Asserts assertAgeYearsEnabled() {

            assertTrue(ageYears.isEnabled(), "Age Years field must be enabled");
            return this;
        }

        public Asserts assertAgeIs(int years, int months) {

            assertEquals($(ageYears).attr("value"), years + "");
            assertEquals($(ageMonths).attr("value"), months + "");
            return this;
        }

        public Asserts assertMonthMenuEnabled() {

            assertTrue(getAgeMonth().isEnabled(), "Month DropDown must be enabled");
            return this;
        }

        public Asserts assertAgeYearsDisabled() {

            assertFalse(ageYears.isEnabled(), "Age Years field must be disabled");
            return this;
        }

        public Asserts assertMonthMenuDisabled() {

            assertFalse(getAgeMonth().isEnabled(), "Month DropDown must be disabled");
            return this;
        }

        public Asserts assertMonthValueIs(String expectedMonthValue) {

            assertEquals(getAgeMonth().getValue().trim(), expectedMonthValue, "The month is not saved");
            return this;
        }

        public Asserts assertYearsValueIs(String expectedValue) {

            assertEquals(ageYears.getValue(), expectedValue, "The age year is not saved");
            return this;
        }

        public Asserts assertDepreciationValueIs(Double expectedDepreciationValue) {

            assertEqualsDouble(Double.valueOf(depreciationPercentage.getValue()), expectedDepreciationValue, "Depreciation percentage incorrect");
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

        public Asserts assertBrandTextIs(String selectedVoucher) {

            assertEquals($(getVoucherLink()).getText(), selectedVoucher, "Wrong voucher is Displayed");
            return this;
        }

        public Asserts assertVoucherCardIsShown() {

            SelenideElement voucherCard = $("#status_voucher_replacement_card");
            assertThat(voucherCard.is(Condition.visible)).as("voucher card is not displayed").isTrue();
            return this;
        }

        public Asserts assertScalepointSupplierNotVisible() {

            assertFalse(getStatusSupplier().isDisplayed(), "Scalepoint supplier must not be visible");
            return this;
        }

        public Asserts assertThereIsNoReductionRules(){

            assertThat($("#rule-suggestion-grid-body div.x-grid-item-container")
                    .attr("id").startsWith("ext-element")).as("there should be no reduction rules suggested").isTrue();
            return this;
        }

        public Asserts assertThereIsReductionRuleSuggested(){

            assertThat($("#rule-suggestion-grid-body table.x-grid-item").isDisplayed()).as("there should be reduction rule suggested").isTrue();
            return this;
        }

        public Asserts assertScalepointSupplierVisible(String supplier) {

            waitForAjaxCompleted();
            assertTrue(getStatusSupplier().getText().contains(supplier), "Scalepoint supplier must be visible");
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

            assertEquals($(depreciationPercentage).getValue(), percentage);
            return this;
        }

        public Asserts assertAutomaticDepreciationLabelColor() {

            boolean isLabelInRedColor = automaticDepreciationLabel.getAttribute("style").contains("color: red;");
            assertEquals(getAutomaticDepreciation().isChecked(), !isLabelInRedColor);
            return this;
        }

        public Asserts assertIsSufficientDocumentationCheckboxDisplayedAndItIsChecked() {

            assertThat(getSufficientDocumentation().isChecked())
                    .as("Sufficient documentation is unchecked")
                    .isTrue();
            return this;
        }

        public Asserts assertIsSufficientDocumentationCheckboxDisplayedAndItIsUnchecked() {

            assertThat(getSufficientDocumentation().isChecked())
                    .as("Sufficient documentation is checked")
                    .isFalse();
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

            ValuationGrid.ValuationRow valuationRow = new ValuationGrid().getValuationRow(VOUCHER);
            assertEquals(valuationRow.getCashCompensation(), newPrice - (newPrice * getVoucherPercentage() / 100), 0.0);
            return this;
        }
    }
}

