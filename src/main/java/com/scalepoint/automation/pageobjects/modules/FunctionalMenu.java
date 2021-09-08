package com.scalepoint.automation.pageobjects.modules;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.LossImportDialog;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Link;

import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;

public class FunctionalMenu extends Module {

    @FindBy(xpath = "//span[contains(@style,'findInCatalogIcon.png')]/ancestor::a")
    private WebElement findInCatalogue;

    @FindBy(id = "addByHandBtn")
    private WebElement addManually;

    @FindBy(xpath = "//span[contains(@style,'selfServiceRequestIcon.png')]/ancestor::a")
    private Link requestSelfService;

    @FindBy(id = "excelImportBtn")
    private Button importExcel;

    public void findInCatalogue() {
        hoverAndClick($(findInCatalogue));
    }

    public SettlementDialog addManually() {
        hoverAndClick($(addManually));
        return BaseDialog.at(SettlementDialog.class);
    }

    public void RequestSelfService() {
        requestSelfService.click();
    }

    public void ClickImportClaimSheet() {
        importExcel.click();
    }

    public LossImportDialog openImportExcelDialog() {
        logger.info("Main: {}", driver.getWindowHandle());
        $("#excelImportBtn").click();
        return BaseDialog.at(LossImportDialog.class);
    }


    public FunctionalMenu doAssert(Consumer<FunctionalMenu.Asserts> assertFunc) {
        assertFunc.accept(new FunctionalMenu.Asserts());
        return this;
    }

    public class Asserts {

        private void isButtonDisabled(Boolean isEnabled, WebElement button){
            $(button).shouldHave(Condition.attribute("aria-disabled", String.valueOf(isEnabled)));
        }

        public Asserts assertAddManuallyButtonIsDisabled() {
            isButtonDisabled(true, addManually);
            return this;
        }

        public Asserts assertRequestSelfServiceButtonIsDisabled() {
            isButtonDisabled(true, requestSelfService);
            return this;
        }

        public Asserts assertImportExcelButtonIsDisabled() {
            isButtonDisabled(true, requestSelfService);
            return this;
        }

        public Asserts assertAddManuallyButtonIsEnabled() {
            isButtonDisabled(false, addManually);
            return this;
        }

        public Asserts assertRequestSelfServiceButtonIsEnabled() {
            isButtonDisabled(false, requestSelfService);
            return this;
        }

        public Asserts assertImportExcelButtonIsEnabled() {
            isButtonDisabled(false, requestSelfService);
            return this;
        }


    }

}

