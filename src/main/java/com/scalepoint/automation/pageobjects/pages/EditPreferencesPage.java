package com.scalepoint.automation.pageobjects.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.utils.OperationalUtils;
import org.openqa.selenium.support.FindBy;

import java.util.function.Consumer;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by bza on 11/9/2017.
 */
public class EditPreferencesPage extends Page {

    @FindBy(id = "btnGenerate")
    private SelenideElement buttonGenerate;
    @FindBy(id = "cancel")
    private SelenideElement buttonCancel;

    @Override
    protected void ensureWeAreOnPage() {

        waitForUrl(getRelativeUrl());
        waitForAjaxCompletedAndJsRecalculation();
        buttonGenerate.should(Condition.visible);
    }

    @Override
    protected String getRelativeUrl() {

        return "webshop/jsp/matching_engine/preferences.jsp";
    }

    public String generateNewPassword() {

        $(buttonGenerate)
                .should(and("can be clickable", visible, enabled))
                .hover()
                .click();
        return getAlertTextAndAccept().split(" ")[1];
    }

    public boolean checkGeneratePasswordButton() {

        return isDisplayed(buttonGenerate);
    }

    public MyPage cancel() {

        buttonCancel.click();
        return Page.at(MyPage.class);
    }

    public EditPreferencesPage doAssert(Consumer<Asserts> assertsFunc) {

        assertsFunc.accept(new Asserts());
        return EditPreferencesPage.this;
    }

    public class Asserts {

        public Asserts assertIsGenerateButtonVisible() {

            assertThat(checkGeneratePasswordButton()).isTrue();
            return this;
        }

        public Asserts assertsIsGeneratedPasswordCorrect(String generatedPassword) {

            OperationalUtils.assertStringMatchingPattern("(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?!.*[&%$]#).{6,}", generatedPassword);
            return this;
        }
    }
}
