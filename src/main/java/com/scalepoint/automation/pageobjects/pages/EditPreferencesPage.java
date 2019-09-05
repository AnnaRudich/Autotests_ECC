package com.scalepoint.automation.pageobjects.pages;

import com.scalepoint.automation.utils.OperationalUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.function.Consumer;

import static com.scalepoint.automation.utils.Wait.waitForAjaxCompleted;
import static com.scalepoint.automation.utils.Wait.waitForVisible;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by bza on 11/9/2017.
 */
public class EditPreferencesPage extends Page {

    @FindBy(id = "btnGenerate")
    private WebElement buttonGenerate;

    @FindBy(id = "confirmImport")
    private WebElement buttonCancel;

    @Override
    protected Page ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForAjaxCompleted();
        waitForVisible(buttonGenerate);
        return this;
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/matching_engine/preferences.jsp";
    }

    public String generateNewPassword() {
        clickUsingJsIfSeleniumClickReturnError(buttonGenerate);
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
