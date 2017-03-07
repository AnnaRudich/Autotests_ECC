package com.scalepoint.automation.pageobjects.dialogs;

import com.scalepoint.automation.utils.Wait;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import ru.yandex.qatools.htmlelements.element.Button;

import java.util.function.Consumer;

public class VoucherTermsAndConditionsDialog extends BaseDialog {

    @FindBy(id = "terms-conditions-condidtions")
    private WebElement termsAndConditions;

    @FindBy(id = "terms-conditions-close-button")
    private Button close;

    @FindBy(id = "terms-conditions-offer")
    private WebElement offerText;

    @FindBy(id = "terms-conditions-question")
    private WebElement questionsText;

    @Override
    protected VoucherTermsAndConditionsDialog ensureWeAreAt() {
        Wait.waitForVisible(termsAndConditions);
        return this;
    }

    public String getTermsAndConditions(){
        return termsAndConditions.getText();
    }

    public void close(){
        close.click();
    }

    public VoucherTermsAndConditionsDialog doAssert(Consumer<Asserts> assertFunc) {
        assertFunc.accept(new Asserts());
        return VoucherTermsAndConditionsDialog.this;
    }

    public class Asserts {
        public Asserts assertTermsAndConditionsTextIs(String expectedText) {
            Assert.assertEquals(getTermsAndConditions(), expectedText, "The Terms And Conditions are different");
            return this;
        }

        public Asserts assertOfferBoxContainsVoucherName(String expectedVoucherName) {
            Assert.assertTrue(offerText.getText().contains(expectedVoucherName));
            return this;
        }

        public Asserts assertOfferBoxContainsCorrectPercentage(Integer expectedVoucherPercentage) {
            Assert.assertTrue(offerText.getText().contains(expectedVoucherPercentage.toString()));
            return this;
        }

        public Asserts assertQuestionsBoxContainsCorrectPhone(String expectedPhone) {
            Assert.assertTrue(questionsText.getText().contains(expectedPhone));
            return this;
        }
    }
}
