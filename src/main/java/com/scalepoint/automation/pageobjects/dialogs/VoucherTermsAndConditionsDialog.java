package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import java.util.function.Consumer;

import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

public class VoucherTermsAndConditionsDialog extends BaseDialog {

    @Override
    protected void ensureWeAreAt() {

        waitForAjaxCompletedAndJsRecalculation();
        termsAndConditions.should(Condition.visible);
    }

    @FindBy(id = "terms-conditions-condidtions")
    private SelenideElement termsAndConditions;

    @FindBy(id = "terms-conditions-close-button")
    private SelenideElement close;

    @FindBy(id = "terms-conditions-offer")
    private SelenideElement offerText;

    @FindBy(id = "terms-conditions-question")
    private SelenideElement questionsText;

    public String getTermsAndConditions() {

        return termsAndConditions.getText();
    }

    public void close() {

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
