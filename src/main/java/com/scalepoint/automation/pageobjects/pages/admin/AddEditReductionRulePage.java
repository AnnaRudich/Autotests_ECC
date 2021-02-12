package com.scalepoint.automation.pageobjects.pages.admin;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.google.common.base.Strings;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.data.entity.input.ReductionRule;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import ru.yandex.qatools.htmlelements.element.CheckBox;

import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.scalepoint.automation.utils.Wait.waitForPageLoaded;
import static org.testng.Assert.assertEquals;

@EccPage
public class AddEditReductionRulePage extends AdminBasePage {

    @FindBy(name = "name")
    private WebElement nameField;

    @FindBy(name = "published")
    private WebElement publishBox;

    @FindBy(id = "depreciation_type_policy")
    private WebElement policyRuleButton;

    @FindBy(name = "max_depreciation")
    private WebElement maxDepreciation;

    @FindBy(id = "depreciation_discretionary")
    private WebElement discretionaryButton;

    @FindBy(name = "use_rounding")
    private CheckBox useRounding;

    @FindBy(name = "roundbase")
    private WebElement roundbase;

    @FindBy(css = "input[value=Save]")
    private WebElement saveButton;

    @FindBy(css = "input[value=Cancel]")
    private WebElement cancelButton;

    @FindBy(xpath = "//table[@id='rulelines']/tbody/tr[1]/th[2]")
    private WebElement descriptionColumnName;

    @FindBy(xpath = "//table[@id='rulelines']/tbody/tr[1]/th[3]")
    private WebElement ageFromColumnName;

    @FindBy(xpath = "//table[@id='rulelines']/tbody/tr[1]/th[4]")
    private WebElement ageToColumnName;

    @FindBy(xpath = "//table[@id='rulelines']/tbody/tr[1]/th[5]")
    private WebElement newItemColumnName;

    @FindBy(xpath = "//table[@id='rulelines']/tbody/tr[1]/th[6]")
    private WebElement priceFromColumnName;

    @FindBy(xpath = "//table[@id='rulelines']/tbody/tr[1]/th[7]")
    private WebElement priceToColumnName;

    @FindBy(xpath = "//table[@id='rulelines']/tbody/tr[1]/th[8]")
    private WebElement documentationColumnName;

    @FindBy(xpath = "//table[@id='rulelines']/tbody/tr[1]/th[9]")
    private WebElement claimantRatingColumnName;

    @FindBy(xpath = "//table[@id='rulelines']/tbody/tr[1]/th[10]")
    private WebElement claimReductionColumnName;

    @FindBy(xpath = "//table[@id='rulelines']/tbody/tr[1]/th[11]")
    private WebElement cashReductionColumnName;

    @FindBy(css = "input[value=\"+\"]")
    private WebElement addButton;

    @Override
    protected void ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForPageLoaded();
        $(nameField).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/Admin/reduction_rule_edit.jsp";
    }

    public ReductionRulesPage save() {
        $(saveButton).click();
        return at(ReductionRulesPage.class)
                .selectRefreshOption();
    }

    public AddEditReductionRulePage addLine() {
        $(addButton).click();
        return this;
    }


    public ReductionRulesPage saveAndExpectSuccess() {
        $(saveButton).click();
        acceptAlert();
        return at(ReductionRulesPage.class).selectRefreshOption();
    }

    public AddEditReductionRulePage saveAndExpectWarning() {
        $(saveButton).click();
        acceptAlert();
        return this;
    }

    public ReductionRulesPage cancel() {
        $(cancelButton).click();
        return at(ReductionRulesPage.class);
    }

    /**
     * This method enables publishing for RR if it's not enabled
     */
    public void publishRR() {
        if (!publishBox.isSelected()) {
            publishBox.click();
        }
    }

    public void selectPolicyRuleOption() {
        if (!policyRuleButton.isSelected()) {
            policyRuleButton.click();
        }
    }

    public void selectDiscretionaryRuleOption() {
        if (!discretionaryButton.isSelected()) {
            discretionaryButton.click();
        }
    }

    public ReductionRulesPage fillSimpleDiscretionaryRRAndSave(ReductionRule rr) {
        nameField.sendKeys(rr.getRrName());
        publishRR();
        selectDiscretionaryRuleOption();
        $(maxDepreciation).setValue(rr.getMaxDepreciation());

        addLine();

        getReductionRuleLines().get(0)
                .setDescription(rr.getDescription1())
                .setAgeFrom(rr.getAgeFrom2())
                .setAgeTo(rr.getAgeTo2())
                .setClaimReduction(rr.getClaimReduction1())
                .setPriceFrom(rr.getPriceRangeFrom2())
                .setPriceTo(rr.getPriceRangeTo2());

        return save();
    }

    public ReductionRulesPage fillSimpleDiscretionaryRRWithRoundingsAndSave(ReductionRule rr) {
        return fillDiscretionaryRRAndSave(rr, "5", rr.getAgeFrom1(), rr.getAgeTo1(), rr.getClaimReduction1(),
                rr.getPriceRangeFrom1(), rr.getPriceRangeTo1(), null, null, rr.getMaxDepreciation());
    }

    public ReductionRulesPage fillDiscretionaryRRAndSave(ReductionRule rr, String roundbase, String ageFrom, String ageTo,
                                                         String claimReduction, String priceRangeFrom, String priceRangeTo,
                                                         String documentationValue, String ratingDropValue, String maxDepreciationValue) {
        nameField.sendKeys(rr.getRrName());
        publishRR();
        selectDiscretionaryRuleOption();
        ReductionRuleLine reductionRuleLine = getReductionRuleLines().get(0);
        reductionRuleLine.setDescription(rr.getDescription1());
        $(maxDepreciation).setValue(maxDepreciationValue);
        if (Strings.isNullOrEmpty(roundbase)) {
            this.useRounding.deselect();
        } else {
            this.useRounding.select();
            this.roundbase.sendKeys(roundbase);
        }
        reductionRuleLine
                .setAgeFrom(ageFrom)
                .setAgeTo(ageTo)
                .setClaimReduction(claimReduction)
                .setPriceFrom(priceRangeFrom)
                .setPriceTo(priceRangeTo);
        if (!Strings.isNullOrEmpty(documentationValue)) {
            selectDocumentationDropValue(0, documentationValue);
        }
        if (!Strings.isNullOrEmpty(ratingDropValue)) {
            selectRatingDropValue(0, ratingDropValue);
        }
        return save();
    }

    public String getDescriptionColumnHeader() {
        return $(descriptionColumnName).getText();
    }

    public String getAgeFromColumnHeader() {
        return $(ageFromColumnName).getText();
    }

    public String getAgeToColumnHeader() {
        return $(ageToColumnName).getText();
    }

    public String getNewItemColumnHeader() {
        return $(newItemColumnName).getText();
    }

    public String getPriceFromColumnHeader() {
        return $(priceFromColumnName).getText();
    }

    public String getPriceToColumnHeader() {
        return $(priceToColumnName).getText();
    }

    public String getDocumetationColumnHeader() {
        return $(documentationColumnName).getText();
    }

    public String getClaimantRatingColumnHeader() {
        return $(claimantRatingColumnName).getText();
    }

    public String getClaimReductionColumnHeader() {
        return $(claimReductionColumnName).getText();
    }

    public String getCashReductionColumnHeader() {
        return $(cashReductionColumnName).getText();
    }


    public AddEditReductionRulePage fillGeneralForTwoLines(ReductionRule rr) {
        nameField.sendKeys(rr.getRrName());
        publishRR();
        selectPolicyRuleOption();
        addLine();
        addLine();
        List<ReductionRuleLine> lines = getReductionRuleLines();
        lines.get(0)
                .setDescription(rr.getDescription1())
                .setClaimReduction(rr.getClaimReduction1());
        lines.get(1)
                .setDescription(rr.getDescription1())
                .setClaimReduction(rr.getClaimReduction1());
        return this;
    }

    public AddEditReductionRulePage fillGeneralInfoPolicyRR(ReductionRule rr) {
        nameField.sendKeys(rr.getRrName());
        publishRR();
        selectPolicyRuleOption();
        return this;
    }

    public AddEditReductionRulePage fillGeneralInfoDiscretionaryRR(ReductionRule rr) {
        nameField.sendKeys(rr.getRrName());
        publishRR();
        selectDiscretionaryRuleOption();
        return this;
    }


    public AddEditReductionRulePage fillPriceRangeForTwoLines(String priceFromValueFirstLine, String priceToValueFirstLine, String priceFromValueSecondLine, String priceToValueSecondLine) {
        List<ReductionRuleLine> reductionRuleLines = getReductionRuleLines();
        reductionRuleLines.get(0)
                .setPriceFrom(priceFromValueFirstLine)
                .setPriceTo(priceToValueFirstLine);
        reductionRuleLines.get(1)
                .setPriceFrom(priceFromValueSecondLine)
                .setPriceTo(priceToValueSecondLine);
        return this;
    }

    public AddEditReductionRulePage fillAgeRangeForTwoLines(String ageFromValueFirstLine, String ageToValueFirstLine, String ageFromValueSecondLine, String ageToValueSecondLine) {
        List<ReductionRuleLine> reductionRuleLines = getReductionRuleLines();
        reductionRuleLines.get(0)
                .setAgeFrom(ageFromValueFirstLine)
                .setAgeTo(ageToValueFirstLine);
        reductionRuleLines.get(1)
                .setAgeFrom(ageFromValueSecondLine)
                .setAgeTo(ageToValueSecondLine);
        return this;
    }

    //LineNumber is 0 for the first RR line
    public AddEditReductionRulePage selectDocumentationDropValue(int lineNumber, String documentationValue) {
        getReductionRuleLines().get(lineNumber).setDocumentation(documentationValue);
        return this;
    }

    //LineNumber is 0 for the first RR line
    public AddEditReductionRulePage selectRatingDropValue(int lineNumber, String ratingValue) {
        getReductionRuleLines().get(lineNumber).setClaimantRating(ratingValue);
        return this;
    }

    //LineNumber is 0 for the first RR line
    public AddEditReductionRulePage fillPriceRangeForLine(int lineNumber, String priceFromValue, String priceToValue) {
        $(By.xpath("//input[@id='priceFrom|" + lineNumber + "']")).sendKeys(priceFromValue);
        $(By.xpath("//input[@id='priceTo|" + lineNumber + "']")).sendKeys(priceToValue);
        return this;
    }

    //LineNumber is 0 for the first RR line
    public AddEditReductionRulePage fillAgeRangeForLine(int lineNumber, String ageFromValue, String ageToValue) {
        $(By.xpath("//input[@id='from|" + lineNumber + "']")).sendKeys(ageFromValue);
        $(By.xpath("//input[@id='to|" + lineNumber + "']")).sendKeys(ageToValue);
        return this;
    }

    //LineNumber is 0 for the first RR line
    public AddEditReductionRulePage fillDescriptionForLine(int lineNumber, String descriptionValue) {
        getReductionRuleLines().get(lineNumber).setDescription(descriptionValue);
        return this;
    }

    //LineNumber is 0 for the first RR line
    public AddEditReductionRulePage fillClaimReductionForLine(int lineNumber, String reductionValue) {
        getReductionRuleLines().get(lineNumber).setClaimReduction(reductionValue);
        return this;
    }

    public String[] readDocumentationComboboxValues() {

        String[] documentationComboboxValues = $$("[id=\"documentation|0\"] > option").stream().map(element -> element.getText()).toArray(String[]::new);

        return documentationComboboxValues;
    }

    public String[] readRatingComboboxValues() {
        int i = 1;
        String[] ratingComboboxValues = new String[4];
        while (i < 5) {
            ratingComboboxValues[i - 1] = $(By.xpath("//select[@id='claimantRating|0']/option" + "[" + i + "]")).getText();
            i++;
        }
        return ratingComboboxValues;
    }

    public List<ReductionRuleLine> getReductionRuleLines(){

        return $$("#rulelines tr").stream()
                .skip(1)
                .map(ReductionRuleLine::new)
                .collect(Collectors.toList());
    }

    class ReductionRuleLine{

        private SelenideElement description;
        private SelenideElement ageFrom;
        private SelenideElement ageTo;
        private SelenideElement newItem;
        private SelenideElement priceFrom;
        private SelenideElement priceTo;
        private SelenideElement documentation;
        private SelenideElement claimantRating;
        private SelenideElement claimReduction;
        private SelenideElement cashReduction;
        private SelenideElement deleteLine;

        public ReductionRuleLine(SelenideElement element){
            description = element.find("[name=desc]");
            ageFrom = element.find("[name=from]");
            ageTo = element.find("[name=to]");
            newItem = element.find("[name=newitem");
            priceFrom = element.find("[name=priceFrom");
            priceTo = element.find("[name=priceTo");
            documentation = element.find("[name=documentation");
            claimantRating = element.find("[name=claimantRating");
            claimReduction = element.find("[name=claimreduction");
            cashReduction = element.find("[name=cashreduction");
            deleteLine = element.find("a");
        }

        public ReductionRuleLine setDescription(String value){
            description.setValue(value);
            return this;
        }

        public ReductionRuleLine setClaimReduction(String value){
            claimReduction.setValue(value);
            return this;
        }

        public ReductionRuleLine setAgeFrom(String value){
            ageFrom.setValue(value);
            return this;
        }

        public ReductionRuleLine setAgeTo(String value){
            ageTo.setValue(value);
            return this;
        }

        public ReductionRuleLine setPriceFrom(String value){
            priceFrom.setValue(value);
            return this;
        }

        public ReductionRuleLine setPriceTo(String value){
            priceTo.setValue(value);
            return this;
        }

        public ReductionRuleLine setClaimantRating(String value){
            claimantRating.selectOption(value);
            return this;
        }

        public ReductionRuleLine setDocumentation(String value){
            documentation.selectOption(value);
            return this;
        }
    }

    public AddEditReductionRulePage assertDescriptionColumnHeaderPresent() {
        Assert.assertNotNull(getDescriptionColumnHeader());
        return this;
    }

    public AddEditReductionRulePage assertDocumentationComboboxValuesAre(String[] expectedDocumentationComboboxValues) {
        assertEquals(readDocumentationComboboxValues(), expectedDocumentationComboboxValues);
        return this;
    }

    public AddEditReductionRulePage assertRatingComboboxValuesAre(String[] expectedRatingComboboxValues) {
        assertEquals(readRatingComboboxValues(), expectedRatingComboboxValues);
        return this;
    }
}
