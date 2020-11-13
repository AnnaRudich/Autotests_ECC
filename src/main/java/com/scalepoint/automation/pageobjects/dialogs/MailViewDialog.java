package com.scalepoint.automation.pageobjects.dialogs;

import com.scalepoint.automation.pageobjects.pages.MailsPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.selfService2.LoginSelfService2Page;
import com.scalepoint.automation.pageobjects.pages.selfservice.LoginSelfServicePage;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.threadlocal.Browser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Link;

import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForPageLoaded;
import static com.scalepoint.automation.utils.Wait.waitForVisible;
import static org.assertj.core.api.Assertions.assertThat;

@EccPage
public class MailViewDialog extends BaseDialog {

    @FindBy(xpath = "//a[contains(@href,'LoginToShop?selfService')]")
    private Link selfServiceLink;

    @FindBy(xpath = "//a[contains(@href,'LoginToShop?login')]")
    private Link loginToShopLink;

    @FindBy(id = "show-mail-window-cancel-button-btnInnerEl")
    private WebElement cancelButton;

    @Override
    public void ensureWeAreAt() {
        waitForPageLoaded();
    }

    public MailsPage cancel(){
        $(cancelButton).click();
        return Page.at(MailsPage.class);
    }

    public LoginSelfServicePage findSelfServiceLinkAndOpenIt() {
        waitForVisible(selfServiceLink);
        String link = selfServiceLink.getWrappedElement().getAttribute("href");
        Browser.open(link);
        return Page.at(LoginSelfServicePage.class);
    }

    public LoginSelfService2Page findSelfServiceNewLinkAndOpenIt() {
        waitForVisible(selfServiceLink);
        String link = selfServiceLink.getWrappedElement().getAttribute("href");
        Browser.open(link);
        return Page.at(LoginSelfService2Page.class);
    }

    public LoginShopPage findLoginToShopLinkAndOpenIt() {
        String link = findLoginToShopLink();
        Browser.open(link);
        return Page.at(LoginShopPage.class);
    }

    public String findLoginToShopLink() {
        waitForVisible(loginToShopLink);
        return loginToShopLink.getWrappedElement().getAttribute("href");
    }

    public MailViewDialog doAssert(Consumer<Asserts> assertFunc) {
        assertFunc.accept(new MailViewDialog.Asserts());
        return MailViewDialog.this;
    }

    public class Asserts {

        public void isSelfServiceLinkVisible() {

            assertThat($(selfServiceLink).exists())
                    .isTrue();
        }

        public void isTextVisible(String text) {

            assertThat($("div[id|=panel][id$=innerCt]")
                    .find(By.xpath(String.format("//*[contains(text(), '%s')]", text)))
                    .exists())
                    .isTrue();
        }
    }
}
