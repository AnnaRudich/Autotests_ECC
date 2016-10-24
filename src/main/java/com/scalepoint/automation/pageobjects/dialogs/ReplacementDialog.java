package com.scalepoint.automation.pageobjects.dialogs;

import com.scalepoint.automation.pageobjects.pages.CustomerDetailsPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.oldshop.ShopWelcomePage;
import com.scalepoint.automation.utils.Configuration;
import com.scalepoint.automation.utils.OperationalUtils;
import com.scalepoint.automation.utils.Wait;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Radio;

import static com.scalepoint.automation.utils.Wait.waitForVisible;

public class ReplacementDialog extends BaseDialog {

    @FindBy(id = "btn_cancel")
    private Button cancelButton;

    @FindBy(xpath = "//table[@class='valuationTable']//tr[2]/td[3]")
    private WebElement voucherFaceValue;

    @FindBy(xpath = "//table[@class='valuationTable']//tr[2]/td[4]")
    private WebElement itemPrice;

    @FindBy(id = "replace_money_radio")
    private Radio payCompleteAmountRadio;

    @FindBy(id = "btn_next")
    private Button nextButton;

    @FindBy(id = "replacementType3")
    private Button sendChequeButton;

    @FindBy(id = "btn_finish")
    private WebElement finishButton;

    @FindBy(id = "btn_close")
    private WebElement closeButton;

    @FindBy(xpath = "//button[@id='btn_replace_through_shop']")
    private WebElement goToShopButton;

    @Override
    public ReplacementDialog ensureWeAreAt() {
        Wait.waitForAjaxComplete();
        switchToLast();
        waitForVisible(cancelButton);
        return this;
    }

    public void closeReplacementDialog() {
        closeDialog(cancelButton);
    }

    public Double getVoucherFaceValue() {
        if (Configuration.isDK()) {
            return OperationalUtils.toNumber(voucherFaceValue.getText().split("rdi")[1].replaceAll("[^\\.,0123456789]", ""));
        } else {
            return OperationalUtils.toNumber(voucherFaceValue.getText().split("£")[1].replaceAll("[^\\.,0123456789]", ""));
        }
    }

    public Double getItemPriceValue() {
        return OperationalUtils.toNumber(itemPrice.getText().replaceAll("[^\\.,0123456789]", ""));
    }

    public CustomerDetailsPage completeClaimUsingCompPayment() {
        payCompleteAmountRadio.click();
        //only sequential double click activates Next button
        payCompleteAmountRadio.click();
        nextButton.click();
        sendChequeButton.click();
        finishButton.click();
        closeDialog(closeButton);
        return Page.at(CustomerDetailsPage.class);
    }

    public ShopWelcomePage goToShop() {
        closeDialog(goToShopButton);
        return Page.at(ShopWelcomePage.class);
    }
}
