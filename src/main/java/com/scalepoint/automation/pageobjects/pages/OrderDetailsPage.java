package com.scalepoint.automation.pageobjects.pages;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.pageobjects.dialogs.AddInternalNoteDialog;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.utils.OperationalUtils;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.threadlocal.Window;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompleted;
import static com.scalepoint.automation.utils.Wait.waitForPageLoaded;

@EccPage
public class OrderDetailsPage extends Page {

    @FindBy(xpath = "//div[@class='table-header']")
    private WebElement legendItem;
    @FindBy(xpath = "//div[@class='table-content']//tr[1]/td[1]")
    private WebElement idemnityText;

    @FindBy(xpath = "//div[@class='table-content']//tr[1]/td[2]")
    private WebElement idemnityValue;

    @FindBy(xpath = "//div[@class='table-content']//tr[3]/td[1]")
    private WebElement orderedItemsText;

    @FindBy(xpath = "//div[@class='table-content']//tr[3]/td[2]")
    private WebElement orderedItemsValue;

    @FindBy(xpath = "//div[@class='table-content']//tr[4]/td[1]")
    private WebElement withdrawallsText;

    @FindBy(xpath = "//div[@class='table-content']//tr[4]/td[2]")
    private WebElement withdrawallsValue;

    @FindBy(xpath = "//div[@class='table-content']//tr[5]/td[1]")
    private WebElement depositsText;

    @FindBy(xpath = "//div[@class='table-content']//tr[5]/td[2]")
    private WebElement depositsValue;

    @FindBy(xpath = "//div[@class='table-content']//tr[7]/td[1]")
    private WebElement remainingIdemnityText;

    @FindBy(xpath = "//div[@class='table-content']//tr[7]/td[2]")
    private WebElement remainingIdemnityValue;

    @FindBy(id = "btnShowOrder")
    private WebElement showButton;

    @FindBy(name = "cancelButton")
    private WebElement cancelButton;

    @FindBy(xpath = "//input[contains(@name,'article')]")
    private WebElement articleCheckbox;

    @FindBy(id = "_OK_button")
    private WebElement okOrderButton;

    @FindBy(name = "internal_note")
    private WebElement cancelOrderNoteTextArea;

    @Override
    protected OrderDetailsPage ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        replaceAmpInUrl();
        waitForPageLoaded();
        waitForAjaxCompleted();
        waitForJavascriptRecalculation();
        $(showButton).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
        return this;
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/matching_engine/customer_order.jsp";
    }

    public String getLegendItemText() {
        return legendItem.getText();
    }

    public String getIdemnityText() {
        return getText(idemnityText);
    }

    public Double getIdemnityValue() {
        return OperationalUtils.toNumber(idemnityValue.getText());
    }

    public String getOrderedItemsText() {
        return getText(orderedItemsText);
    }

    public Double getOrderedItemsValue() {
        return OperationalUtils.toNumber(orderedItemsValue.getText());
    }

    public String getWithdrawText() {
        return getText(withdrawallsText);
    }

    public Double getWithdrawValue() {
        return OperationalUtils.toNumber(withdrawallsValue.getText());
    }

    public String getDepositText() {
        return getText(depositsText);
    }

    public Double getDepositValue() {
        return OperationalUtils.toNumber(depositsValue.getText());
    }

    public String getRemainingIdemnityText() {
        return getText(remainingIdemnityText);
    }

    public Double getRemainingValue() {
        return OperationalUtils.toNumber(remainingIdemnityValue.getText());
    }

    public OrderDetailsPage cancelItem() {
        showButton.click();
        articleCheckbox.click();
        cancelButton.click();
        getAlertTextAndAccept();
        Window.get().switchToLast();
        return BaseDialog.at(AddInternalNoteDialog.class)
                .addInternalNote("Autotests", OrderDetailsPage.class);
    }
}
