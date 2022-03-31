package com.scalepoint.automation.pageobjects.modules;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.dialogs.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;

import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;

public class ClaimOperationsMenu extends Module {

    @FindBy(id = "selfServiceBtn")
    private SelenideElement requestSelfServiceBtn;

    @FindBy(id = "addGenericItemBtn")
    private SelenideElement addGenericItemBtn;

    @FindBy(xpath = "//span[contains(@style,'findInCatalogIcon.png')]/ancestor::a")
    private SelenideElement findInCatalogueBtn;

    @FindBy(id = "addByHandBtn")
    private SelenideElement addManuallyBtn;

    private Button getExcelImportBtn(){

        return new Button($(By.id("excelImportBtn")));
    }

    public SendSelfServiceRequestDialog requestSelfService() {

        hoverAndClick(requestSelfServiceBtn);
        return BaseDialog.at(SendSelfServiceRequestDialog.class);
    }

    public AddGenericItemDialog addGenericItem() {

        hoverAndClick(addGenericItemBtn);
        return BaseDialog.at(AddGenericItemDialog.class);
    }

    public LossImportDialog openImportExcelDialog() {

        logger.info("Main: {}", driver.getWindowHandle());
        $(getExcelImportBtn()).click();
        return BaseDialog.at(LossImportDialog.class);
    }


    public void findInCatalogue() {

        hoverAndClick(findInCatalogueBtn);
    }

    public SettlementDialog addManually() {

        hoverAndClick(addManuallyBtn);
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
