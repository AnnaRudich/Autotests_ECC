package com.scalepoint.automation.pageobjects.modules;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.pageobjects.dialogs.AddGenericItemDialog;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.LossImportDialog;
import com.scalepoint.automation.pageobjects.dialogs.SendSelfServiceRequestDialog;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;

import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;

public class ClaimOperationsMenu extends Module {

    @FindBy(id = "selfServiceBtn")
    private WebElement requestSelfServiceBtn;

    @FindBy(id = "addGenericItemBtn")
    private WebElement addGenericItemBtn;

    @FindBy(id = "excelImportBtn")
    private Button excelImportBtn;

    @FindBy(xpath = "//span[contains(@style,'findInCatalogIcon.png')]/ancestor::a")
    private WebElement findInCatalogueBtn;

    @FindBy(id = "addByHandBtn")
    private WebElement addManuallyBtn;

    public SendSelfServiceRequestDialog requestSelfService() {
        hoverAndClick($(requestSelfServiceBtn));
        return BaseDialog.at(SendSelfServiceRequestDialog.class);
    }

    public AddGenericItemDialog addGenericItem() {
        hoverAndClick($(addGenericItemBtn));
        return BaseDialog.at(AddGenericItemDialog.class);
    }

    public LossImportDialog openImportExcelDialog() {
        logger.info("Main: {}", driver.getWindowHandle());
        $(excelImportBtn).click();
        return BaseDialog.at(LossImportDialog.class);
    }


    public void findInCatalogue() {
        hoverAndClick($(findInCatalogueBtn));
    }

    public SettlementDialog addManually() {
        hoverAndClick($(addManuallyBtn));
        return BaseDialog.at(SettlementDialog.class);
    }

    public ClaimOperationsMenu doAssert(Consumer<ClaimOperationsMenu.Asserts> assertFunc) {
        assertFunc.accept(new ClaimOperationsMenu.Asserts());
        return this;
    }

    public class Asserts {

        private void isButtonDisabled(Boolean isEnabled, WebElement button) {
            $(button).shouldHave(Condition.attribute("aria-disabled", String.valueOf(isEnabled)));
        }

        public ClaimOperationsMenu.Asserts assertAddManuallyButtonIsDisabled() {
            isButtonDisabled(true, addManuallyBtn);
            return this;
        }

        public ClaimOperationsMenu.Asserts assertRequestSelfServiceButtonIsDisabled() {
            isButtonDisabled(true, requestSelfServiceBtn);
            return this;
        }

        public ClaimOperationsMenu.Asserts assertImportExcelButtonIsDisabled() {
            isButtonDisabled(true, requestSelfServiceBtn);
            return this;
        }

        public ClaimOperationsMenu.Asserts assertAddManuallyButtonIsEnabled() {
            isButtonDisabled(false, addManuallyBtn);
            return this;
        }

        public ClaimOperationsMenu.Asserts assertRequestSelfServiceButtonIsEnabled() {
            isButtonDisabled(false, requestSelfServiceBtn);
            return this;
        }

        public ClaimOperationsMenu.Asserts assertImportExcelButtonIsEnabled() {
            isButtonDisabled(false, requestSelfServiceBtn);
            return this;
        }
    }
}
