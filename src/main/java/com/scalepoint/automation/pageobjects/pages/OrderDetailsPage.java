package com.scalepoint.automation.pageobjects.pages;

import com.codeborne.selenide.ElementsCollection;
import com.scalepoint.automation.pageobjects.dialogs.AddInternalNoteDialog;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.utils.OperationalUtils;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.threadlocal.Window;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$$;
import static com.scalepoint.automation.utils.Wait.waitForPageLoaded;
import static org.assertj.core.api.Assertions.assertThat;

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


    ElementsCollection orderTotalRows = $$("#total table tr");


    @Override
    protected void ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        replaceAmpInUrl();
        waitForPageLoaded();
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

    public OrderDetailsPage doAssert(Consumer<OrderDetailsPage.Asserts> assertFunc) {
        assertFunc.accept(new OrderDetailsPage.Asserts());
        return OrderDetailsPage.this;
    }

    public class Asserts {

    public void assertCompensationAmount(Double expectedCompensationAmount){
           Double actualCompensationAmount = new OrderTotals().getCompensationAmount();
           assertThat(expectedCompensationAmount.equals(actualCompensationAmount))
                   .as("compensationAmount was: " + actualCompensationAmount + " but should be: " + expectedCompensationAmount)
                   .isTrue();
    }
    public void assertAmountScalepointHasPaidToSupplier(Double expectedGoodsAmount){
           Double actualGoodsAmount = new OrderTotals().getGoods();
           assertThat(expectedGoodsAmount.equals(actualGoodsAmount))
                .as("amount Scalepoint has paid to Supplier was: " + actualGoodsAmount + " but should be: " + expectedGoodsAmount)
                .isTrue();
    }

    public void assertAmountScalepointPaidToCustomer(Double expectedPayouts){
        Double actualPayouts = new OrderTotals().getPayouts();
        assertThat(expectedPayouts.equals(actualPayouts))
                .as("amount Scalepoint paid to customer was: " + actualPayouts + "but should be: " + expectedPayouts)
                .isTrue();
    }

    public void assertAmountCustomerHasPaidToScalepoint(Double expectedDeposit){
        Double actualDeposit = new OrderTotals().getDeposits();
        assertThat(expectedDeposit.equals(actualDeposit))
                .as("amount customer has paid to Scalepoint was :" + "but should be: " + expectedDeposit)
                .isTrue();
    }

    public void assertRemainingCompensationTotal(Double expectedRemainingCompensation) {
        Double actualRemainingCompensation = new OrderTotals().getRemainingCompensation();
        assertThat(expectedRemainingCompensation.equals(actualRemainingCompensation)).
                as("remaining compensation was: " + actualRemainingCompensation + " but should be :" + expectedRemainingCompensation)
                .isTrue();
    }

}

    class OrderTotals{
        private Double compensationAmount; //IC to Scalepoint
        private Double goods;//Scalepoint has paid to supplier
        private Double payouts;//Scalepoint paid to customer
        private Double deposits;//Customer has paid to Scalepoint
        private Double remainingCompensation;



        Double getCompensationAmount() {
            return compensationAmount;
        }

        Double getGoods() {
            return goods;
        }

        Double getPayouts() {
            return payouts;
        }

        Double getDeposits() {
            return deposits;
        }

        Double getRemainingCompensation() {
            return remainingCompensation;
        }


        OrderTotals() {
            this.compensationAmount = getValueForTotalRowWithIndex(0);
            this.goods = getValueForTotalRowWithIndex(2);
            this.payouts = getValueForTotalRowWithIndex(3);
            this.deposits = getValueForTotalRowWithIndex(4);
            this.remainingCompensation = getValueForTotalRowWithIndex(6);
        }

        private Double getValueForTotalRowWithIndex(int rowIndex){
            return Double.valueOf(getTextForTotalRowWithIndex(rowIndex).replace(',', '.'));
        }

        private String getTextForTotalRowWithIndex(int rowIndex){
            ElementsCollection totalRows = $$("#total table tr");
            By amountTextSelector = By.xpath("td[2]");
            return totalRows.get(rowIndex).find(amountTextSelector).getText();
        }
    }
}
