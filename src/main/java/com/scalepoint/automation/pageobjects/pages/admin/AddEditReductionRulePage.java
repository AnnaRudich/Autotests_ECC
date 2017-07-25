package com.scalepoint.automation.pageobjects.pages.admin;

import com.google.common.base.Strings;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.data.entity.ReductionRule;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import ru.yandex.qatools.htmlelements.element.CheckBox;

import static com.scalepoint.automation.utils.Wait.waitForVisible;
import static org.testng.Assert.assertEquals;

@EccPage
public class AddEditReductionRulePage extends AdminBasePage {

    @FindBy(name = "name")
    private WebElement nameField;

    @FindBy(name = "published")
    private WebElement publishBox;

    @FindBy(id = "depreciation_type_policy")
    private WebElement policyRuleButton;

    @FindBy(id = "depreciation_discretionary")
    private WebElement discretionaryButton;

    @FindBy(name = "use_rounding")
    private CheckBox useRounding;

    @FindBy(name = "roundbase")
    private WebElement roundbase;

    @FindBy(xpath = "//table[@id='rulelines']/tbody/tr[2]/td[2]/input[@id='desc|0']")
    private WebElement descriptionFirstField;

    @FindBy(xpath = "//table[@id='rulelines']/tbody/tr[3]/td[2]/input[@id='desc|1']")
    private WebElement descriptionSecondField;

    @FindBy(xpath = "//table[@id='rulelines']/tbody/tr[2]/td[3]/input[@id='from|0']")
    private WebElement ageFromFirstField;

    @FindBy(xpath = "//table[@id='rulelines']/tbody/tr[2]/td[4]/input[@id='to|0']")
    private WebElement ageToFirstField;

    @FindBy(xpath = "//table[@id='rulelines']/tbody/tr[2]//input[@id='claimreduction|0']")
    private WebElement clReductiontoFirstField;

    @FindBy(xpath = "//table[@id='rulelines']/tbody/tr[3]//input[@id='claimreduction|1']")
    private WebElement clReductiontoSecondField;

    @FindBy(xpath = "//tr[2]/td/button[2]")
    private WebElement saveButton;

    @FindBy(xpath = "//tr[2]/td/button[1]")
    private WebElement cancelButton;

    @FindBy(xpath = "//table[@id='rulelines']/tbody/tr[2]//input[@id='priceFrom|0']")
    private WebElement priceFromFirstField;

    @FindBy(xpath = "//table[@id='rulelines']/tbody/tr[2]//input[@id='priceTo|0']")
    private WebElement priceToFirstField;

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

    @FindBy(xpath = "//table[@id='rulelines']/tbody/tr[3]//input[@id='priceFrom|1']")
    private WebElement priceFromSecondField;

    @FindBy(xpath = "//table[@id='rulelines']/tbody/tr[3]//input[@id='priceTo|1']")
    private WebElement priceToSecondField;

    @FindBy(xpath = "//table[@id='rulelines']/tbody/tr[3]/td[3]/input[@id='from|1']")
    private WebElement ageFromSecondField;

    @FindBy(xpath = "//table[@id='rulelines']/tbody/tr[3]/td[4]/input[@id='to|1']")
    private WebElement ageToSecondField;

    @FindBy(xpath = "//select[@id='documentation|0']")
    private WebElement documentationFirstField;


    @Override
    protected Page ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForVisible(nameField);
        return this;
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/Admin/reduction_rule_edit.jsp";
    }

    public ReductionRulesPage save() {
        saveButton.click();
        return at(ReductionRulesPage.class);
    }


    public ReductionRulesPage saveAndExpectSuccess() {
        saveButton.click();
        acceptAlert();
        return at(ReductionRulesPage.class);
    }

    public AddEditReductionRulePage saveAndExpectWarning() {
        saveButton.click();
        acceptAlert();
        return this;
    }

    public ReductionRulesPage cancel() {
        cancelButton.click();
        return at(ReductionRulesPage.class);
    }

    public void addPriceFrom(String priceFromValue) {
        priceFromFirstField.clear();
        priceFromFirstField.sendKeys(priceFromValue);
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

    public void fillSimplePolicyRRAndSave(ReductionRule rr) {
        nameField.sendKeys(rr.getRrName());
        publishRR();
        selectPolicyRuleOption();
        descriptionFirstField.sendKeys(rr.getDescription1());
        ageFromFirstField.sendKeys(rr.getAgeFrom1());
        ageToFirstField.sendKeys(rr.getAgeTo1());
        clReductiontoFirstField.sendKeys(rr.getClaimReduction1());
        priceFromFirstField.sendKeys(rr.getPriceRangeFrom1());
        priceToFirstField.sendKeys(rr.getPriceRangeTo1());
        save();
    }

    public void fillPolicyRRAndSave(ReductionRule rr, String ageFrom, String ageTo, String claimReduction, String priceRangeFrom,
                                    String priceRangeTo, String documentationValue, String ratingDropValue) {
        nameField.sendKeys(rr.getRrName());
        publishRR();
        selectPolicyRuleOption();
        descriptionFirstField.sendKeys(rr.getDescription1());
        ageFromFirstField.sendKeys(ageFrom);
        ageToFirstField.sendKeys(ageTo);
        clReductiontoFirstField.sendKeys(claimReduction);
        priceFromFirstField.sendKeys(priceRangeFrom);
        priceToFirstField.sendKeys(priceRangeTo);
        selectDocumentationDropValue(0, documentationValue);
        selectRatingDropValue(0, ratingDropValue);
        save();
    }


    public ReductionRulesPage fillSimpleDiscretionaryRRAndSave(ReductionRule rr) {
        nameField.sendKeys(rr.getRrName());
        publishRR();
        selectDiscretionaryRuleOption();
        descriptionFirstField.sendKeys(rr.getDescription1());
        ageFromFirstField.sendKeys(rr.getAgeFrom2());
        ageToFirstField.sendKeys(rr.getAgeTo2());
        clReductiontoFirstField.sendKeys(rr.getClaimReduction1());
        priceFromFirstField.sendKeys(rr.getPriceRangeFrom2());
        priceToFirstField.sendKeys(rr.getPriceRangeTo2());
        return save();
    }

    public ReductionRulesPage fillSimpleDiscretionaryRRWithRoundingsAndSave(ReductionRule rr) {
        return  fillDiscretionaryRRAndSave(rr, "5", rr.getAgeFrom1(), rr.getAgeTo1(), rr.getClaimReduction1(),
                rr.getPriceRangeFrom1(), rr.getPriceRangeTo1(), null, null);
    }

    public ReductionRulesPage fillDiscretionaryRRAndSave(ReductionRule rr, String roundbase, String ageFrom, String ageTo,
                                                         String claimReduction, String priceRangeFrom, String priceRangeTo,
                                                         String documentationValue, String ratingDropValue) {
        nameField.sendKeys(rr.getRrName());
        publishRR();
        selectDiscretionaryRuleOption();
        descriptionFirstField.sendKeys(rr.getDescription1());
        if(Strings.isNullOrEmpty(roundbase)){
            this.useRounding.deselect();
        }else{
            this.useRounding.select();
            this.roundbase.sendKeys(roundbase);
        }
        ageFromFirstField.sendKeys(ageFrom);
        ageToFirstField.sendKeys(ageTo);
        clReductiontoFirstField.sendKeys(claimReduction);
        priceFromFirstField.sendKeys(priceRangeFrom);
        priceToFirstField.sendKeys(priceRangeTo);
        if(!Strings.isNullOrEmpty(documentationValue)) {
            selectDocumentationDropValue(0, documentationValue);
        }
        if(!Strings.isNullOrEmpty(ratingDropValue)) {
            selectRatingDropValue(0, ratingDropValue);
        }
        return save();
    }

    public String getDescriptionColumnHeader() {
        return getText(descriptionColumnName);
    }

    public String getAgeFromColumnHeader() {
        return getText(ageFromColumnName);
    }

    public String getAgeToColumnHeader() {
        return getText(ageToColumnName);
    }

    public String getNewItemColumnHeader() {
        return getText(newItemColumnName);
    }

    public String getPriceFromColumnHeader() {
        return getText(priceFromColumnName);
    }

    public String getPriceToColumnHeader() {
        return getText(priceToColumnName);
    }

    public String getDocumetationColumnHeader() {
        return getText(documentationColumnName);
    }

    public String getClaimantRatingColumnHeader() {
        return getText(claimantRatingColumnName);
    }

    public String getClaimReductionColumnHeader() {
        return getText(claimReductionColumnName);
    }

    public String getCashReductionColumnHeader() {
        return getText(cashReductionColumnName);
    }


    public AddEditReductionRulePage fillGeneralForTwoLines(ReductionRule rr) {
        nameField.sendKeys(rr.getRrName());
        publishRR();
        selectPolicyRuleOption();
        descriptionFirstField.sendKeys(rr.getDescription1());
        clReductiontoFirstField.sendKeys(rr.getClaimReduction1());
        descriptionSecondField.sendKeys(rr.getDescription1());
        clReductiontoSecondField.sendKeys(rr.getClaimReduction1());
        return this;
    }

    public AddEditReductionRulePage fillClaimReductionForTwoLines(String claimReduction1, String claimReduction2) {
        clReductiontoFirstField.clear();
        clReductiontoFirstField.sendKeys(claimReduction1);
        clReductiontoSecondField.clear();
        clReductiontoSecondField.sendKeys(claimReduction2);
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
        priceFromFirstField.sendKeys(priceFromValueFirstLine);
        priceToFirstField.sendKeys(priceToValueFirstLine);
        priceFromSecondField.sendKeys(priceFromValueSecondLine);
        priceToSecondField.sendKeys(priceToValueSecondLine);
        return this;
    }

    public AddEditReductionRulePage fillAgeRangeForTwoLines(String ageFromValueFirstLine, String ageToValueFirstLine, String ageFromValueSecondLine, String ageToValueSecondLine) {
        ageFromFirstField.sendKeys(ageFromValueFirstLine);
        ageToFirstField.sendKeys(ageToValueFirstLine);
        ageFromSecondField.sendKeys(ageFromValueSecondLine);
        ageToSecondField.sendKeys(ageToValueSecondLine);
        return this;
    }

    //LineNumber is 0 for the first RR line
    public AddEditReductionRulePage selectDocumentationDropValue(int lineNumber, String documentationValue) {
        clickAndWaitForDisplaying(By.xpath("//select[@id='documentation|" + lineNumber + "']"), By.xpath("//select[@id='documentation|0']/option[contains(text(),'" + documentationValue + "')]"));
        find(By.xpath("//select[@id='documentation|" + lineNumber + "']/option[contains(text(),'" + documentationValue + "')]")).click();
        return this;
    }

    //LineNumber is 0 for the first RR line
    public AddEditReductionRulePage selectRatingDropValue(int lineNumber, String ratingValue) {
        clickAndWaitForDisplaying(By.xpath("//select[@id='claimantRating|" + lineNumber + "']"), By.xpath("//select[@id='claimantRating|0']/option[contains(text(),'" + ratingValue + "')]"));
        find(By.xpath("//select[@id='claimantRating|" + lineNumber + "']/option[contains(text(),'" + ratingValue + "')]")).click();
        return this;
    }

    //LineNumber is 0 for the first RR line
    public AddEditReductionRulePage fillPriceRangeForLine(int lineNumber, String priceFromValue, String priceToValue) {
        sendKeys(By.xpath("//input[@id='priceFrom|" + lineNumber + "']"), priceFromValue);
        sendKeys(By.xpath("//input[@id='priceTo|" + lineNumber + "']"), priceToValue);
        return this;
    }

    //LineNumber is 0 for the first RR line
    public AddEditReductionRulePage fillAgeRangeForLine(int lineNumber, String ageFromValue, String ageToValue) {
        sendKeys(By.xpath("//input[@id='from|" + lineNumber + "']"), ageFromValue);
        sendKeys(By.xpath("//input[@id='to|" + lineNumber + "']"), ageToValue);
        return this;
    }

    //LineNumber is 0 for the first RR line
    public AddEditReductionRulePage fillDescriptionForLine(int lineNumber, String descriptionValue) {
        sendKeys(By.xpath("//input[@id='desc|" + lineNumber + "']"), descriptionValue);
        return this;
    }

    //LineNumber is 0 for the first RR line
    public AddEditReductionRulePage fillClaimReductionForLine(int lineNumber, String reductionValue) {
        sendKeys(By.xpath("//input[@id='claimreduction|" + lineNumber + "']"), reductionValue);
        return this;
    }

    public String[] readDocumentationComboboxValues() {
        int i = 1;
        String[] documentationComboboxValues = new String[3];
        while (i < 4) {
            documentationComboboxValues[i - 1] = getText(By.xpath("//select[@id='documentation|0']/option" + "[" + i + "]"));
            i++;
        }
        return documentationComboboxValues;
    }

    public String[] readRatingComboboxValues() {
        int i = 1;
        String[] ratingComboboxValues = new String[4];
        while (i < 5) {
            ratingComboboxValues[i - 1] = getText(By.xpath("//select[@id='claimantRating|0']/option" + "[" + i + "]"));
            i++;
        }
        return ratingComboboxValues;
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
