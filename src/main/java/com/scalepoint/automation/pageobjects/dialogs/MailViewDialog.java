package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.pages.MailsPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.selfService2.LoginSelfService2Page;
import com.scalepoint.automation.pageobjects.pages.selfservice.LoginSelfServicePage;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.threadlocal.Browser;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Link;

import java.util.function.Consumer;

import static com.codeborne.selenide.Condition.not;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;
import static org.assertj.core.api.Assertions.assertThat;

@EccPage
public class MailViewDialog extends BaseDialog {

    @FindBy(id = "show-mail-window-cancel-button-btnInnerEl")
    private SelenideElement cancelButton;

    private Link getSelfServiceLink(){

        return new Link($(By.xpath("//a[contains(@href,'LoginToShop?selfService')]")));
    }

    private Link getLoginToShopLink(){

        return new Link($(By.xpath("//a[contains(@href,'LoginToShop?login')]")));
    }

    @Override
    public void ensureWeAreAt() {
        waitForAjaxCompletedAndJsRecalculation();
    }

    public MailsPage cancel(){

        cancelButton.click();
        return Page.at(MailsPage.class);
    }

    public LoginSelfServicePage findSelfServiceLinkAndOpenIt() {

        $(getSelfServiceLink()).should(visible);
        String link = getSelfServiceLink().getWrappedElement().getAttribute("href");
        Browser.open(link);
        return Page.at(LoginSelfServicePage.class);
    }

    public LoginSelfService2Page findSelfServiceNewLinkAndOpenIt() {

        $(getSelfServiceLink()).should(visible);
        String link = getSelfServiceLink().getWrappedElement().getAttribute("href");
        Browser.open(link);
        return Page.at(LoginSelfService2Page.class);
    }

    public String findLoginToShopLink() {

        $(getLoginToShopLink()).should(visible);
        return getLoginToShopLink().getWrappedElement().getAttribute("href");
    }

    public MailViewDialog doAssert(Consumer<Asserts> assertFunc) {

        assertFunc.accept(new MailViewDialog.Asserts());
        return MailViewDialog.this;
    }

    public class Asserts {

        public void isSelfServiceLinkVisible() {

            assertThat($(getSelfServiceLink()).exists())
                    .isTrue();
        }

        public void isTextVisible(String text) {

            assertThat($("div[id|=panel][id$=innerCt]")
                    .has(Condition.text(text)))
                    .isTrue();
        }

        public void isTextInvisible(String text) {

            assertThat($("div[id|=panel][id$=innerCt]")
                    .has(not(Condition.text(text))))
                    .isTrue();
        }
    }
}
