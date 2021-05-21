package com.scalepoint.automation.pageobjects.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.utils.NumberFormatUtils;
import com.scalepoint.automation.utils.OperationalUtils;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.threadlocal.Browser;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Condition.not;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;
import static org.assertj.core.api.Assertions.assertThat;

@EccPage
public class OrderDetailsPage extends BaseClaimPage {

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

    @FindBy(css = ".order-wrapper-table")
    private WebElement orderDetails;

    ElementsCollection orderTotalRows = $$("#total table tr");

    @Override
    protected void ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForAjaxCompletedAndJsRecalculation();
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/matching_engine/customer_order.jsp";
    }

    public String getLegendItemText() {
        return legendItem.getText();
    }

    public String getIdemnityText() {
        return $(idemnityText).getText();
    }

    public Double getIdemnityValue() {
        return OperationalUtils.toNumber(idemnityValue.getText());
    }

    public String getOrderedItemsText() {
        return $(orderedItemsText).getText();
    }

    public Double getOrderedItemsValue() {
        return OperationalUtils.toNumber(orderedItemsValue.getText());
    }

    public String getWithdrawText() {
        return $(withdrawallsText).getText();
    }

    public Double getWithdrawValue() {
        return OperationalUtils.toNumber(withdrawallsValue.getText());
    }

    public String getDepositText() {
        return $(depositsText).getText();
    }

    public Double getDepositValue() {
        return OperationalUtils.toNumber(depositsValue.getText());
    }

    public String getRemainingIdemnityText() {
        return $(remainingIdemnityText).getText();
    }

    public Double getRemainingValue() {
        return OperationalUtils.toNumber(remainingIdemnityValue.getText());
    }

    public OrderDetailsPage refreshPageToGetOrders(){
        Browser.driver().navigate().refresh();
        return this;
    }

    public CustomerOrderEditPage showOrder(){
        hoverAndClick($(showButton));
        return Page.at(CustomerOrderEditPage.class);
    }

    private SelenideElement orderDetailsShouldBe(Condition condition){

        return $("#orders")
                .find(".order-wrapper-table .suborder-header tbody")
                .shouldBe(condition);
    }

    public OrderDetailsPage doAssert(Consumer<Asserts> assertFunc) {
        assertFunc.accept(new Asserts());
        return OrderDetailsPage.this;
    }

    public OrderDetailsPage doSuborderAssert(String description, Consumer<Suborders.Suborder.Asserts> assertFunc) {
        assertFunc.accept(new Suborders().getSuborderByDescription(description).new Asserts());
        return OrderDetailsPage.this;
    }

    public class Asserts {

        public void assertCompensationAmount(Double expectedCompensationAmount){
            BigDecimal actualCompensationAmount = new OrderTotals().getCompensationAmount();
            assertThat(actualCompensationAmount)
                    .as("compensationAmount was: " + actualCompensationAmount + " but should be: " + NumberFormatUtils.formatBigDecimalToHaveTwoDigits(expectedCompensationAmount))
                    .isEqualTo(NumberFormatUtils.formatBigDecimalToHaveTwoDigits(expectedCompensationAmount));
        }
        public void assertAmountScalepointHasPaidToSupplier(Double expectedGoodsAmount){
            BigDecimal actualGoodsAmount = new OrderTotals().getGoods();
            assertThat(actualGoodsAmount)
                    .as("amount Scalepoint has paid to Supplier was: " + actualGoodsAmount + " but should be: " + NumberFormatUtils.formatBigDecimalToHaveTwoDigits(expectedGoodsAmount))
                    .isEqualTo(NumberFormatUtils.formatBigDecimalToHaveTwoDigits(expectedGoodsAmount));
        }

        public void assertAmountScalepointPaidToCustomer(Double expectedPayouts){
            BigDecimal actualPayouts = new OrderTotals().getPayouts();
            assertThat(actualPayouts)
                    .as("amount Scalepoint paid to customer was: " + actualPayouts + "but should be: " + NumberFormatUtils.formatBigDecimalToHaveTwoDigits(expectedPayouts))
                    .isEqualTo(NumberFormatUtils.formatBigDecimalToHaveTwoDigits(expectedPayouts));
        }

        public void assertAmountCustomerHasPaidToScalepoint(Double expectedDeposit){
            BigDecimal actualDeposit = new OrderTotals().getDeposits();
            assertThat(actualDeposit)
                    .as("amount customer has paid to Scalepoint was :" + "but should be: " + NumberFormatUtils.formatBigDecimalToHaveTwoDigits(expectedDeposit))
                    .isEqualTo(NumberFormatUtils.formatBigDecimalToHaveTwoDigits(expectedDeposit));
        }

        public void assertRemainingCompensationTotal(Double expectedRemainingCompensation) {
            BigDecimal actualRemainingCompensation = new OrderTotals().getRemainingCompensation();
            assertThat(actualRemainingCompensation).
                    as("remaining compensation was: " + actualRemainingCompensation + " but should be :" + NumberFormatUtils.formatBigDecimalToHaveTwoDigits(expectedRemainingCompensation))
                    .isEqualTo(NumberFormatUtils.formatBigDecimalToHaveTwoDigits(expectedRemainingCompensation));
        }

        public void assertDetailsAreVisible(){

            assertThat(orderDetailsShouldBe(Condition.visible).exists()).
                    as("Order details should be displayed")
                    .isTrue();
        }
        public void assertDetailsAreInvisible(){

            assertThat(orderDetailsShouldBe(not(Condition.exist)).exists()).
                    as("Order details should not be displayed")
                    .isFalse();
        }

    }

    class OrderTotals{
        private BigDecimal compensationAmount; //IC to Scalepoint
        private BigDecimal goods;//Scalepoint has paid to supplier
        private BigDecimal payouts;//Scalepoint paid to customer
        private BigDecimal deposits;//Customer has paid to Scalepoint
        private BigDecimal remainingCompensation;



        BigDecimal getCompensationAmount() {
            return compensationAmount;
        }

        BigDecimal getGoods() {
            return goods;
        }

        BigDecimal getPayouts() {
            return payouts;
        }

        BigDecimal getDeposits() {
            return deposits;
        }

        BigDecimal getRemainingCompensation() {
            return remainingCompensation;
        }


        OrderTotals() {
            this.compensationAmount = getValueForTotalRowWithIndex(0);
            this.goods = getValueForTotalRowWithIndex(2);
            this.payouts = getValueForTotalRowWithIndex(3);
            this.deposits = getValueForTotalRowWithIndex(4);
            this.remainingCompensation = getValueForTotalRowWithIndex(6);
        }

        private BigDecimal getValueForTotalRowWithIndex(int rowIndex){
            return NumberFormatUtils.formatBigDecimalToHaveTwoDigits((getTextForTotalRowWithIndex(rowIndex).replace(',', '.')));
        }

        private String getTextForTotalRowWithIndex(int rowIndex){
            ElementsCollection totalRows = $$("#total table tr");
            By amountTextSelector = By.xpath("td[2]");
            return totalRows.get(rowIndex).find(amountTextSelector).getText();
        }
    }

    public class Suborders{

        List<Suborder> subordersList;
        BigDecimal totalOrderAmount;

        Suborders(){
            totalOrderAmount = NumberFormatUtils
                    .formatBigDecimalToHaveTwoDigits(OperationalUtils.getDoubleValue($("#orders .order-table td:first-of-type").getText()));
            subordersList = $$("#orders table.order-wrapper-table,table.Cancelled")
                    .stream()
                    .map(Suborder::new)
                    .collect(Collectors.toList());
        }

        Suborder getSuborderByDescription(String itemDescription){

            return subordersList
                    .stream()
                    .filter(suborder -> StringUtils.containsIgnoreCase(suborder.itemDescription, itemDescription))
                    .findFirst()
                    .orElseThrow(NoSuchElementException::new);
        }

        public class Suborder{

            String itemDescription;
            BigDecimal unitPrice;
            int quantity;
            BigDecimal price;
            BigDecimal totalPrice;

            Suborder(SelenideElement element){

                itemDescription = element.find(".suborder-table a").getText();
                unitPrice = NumberFormatUtils
                        .formatBigDecimalToHaveTwoDigits(OperationalUtils.getDoubleValue(element.find(".suborder-table td:nth-of-type(3)").getText()));
                quantity = Integer.parseInt(element.find(".suborder-table td:nth-of-type(4)").getText());
                price = NumberFormatUtils
                        .formatBigDecimalToHaveTwoDigits(OperationalUtils.getDoubleValue(element.find(".suborder-table td:nth-of-type(5)").getText()));
                totalPrice = NumberFormatUtils
                        .formatBigDecimalToHaveTwoDigits(OperationalUtils.getDoubleValue(element.find(".suborder-summary td").getText()));
            }

            public class Asserts {

                public Asserts assertUnitPrice(BigDecimal expectedUnitPrice) {
                    assertThat(unitPrice)
                            .as("unitPrice was: " + unitPrice + " but should be: " + expectedUnitPrice)
                            .isEqualTo(expectedUnitPrice);
                    return this;
                }

                public Asserts assertQuantity(int expectedQuantity) {
                    assertThat(quantity)
                            .as("quantity was: " + quantity + " but should be: " + expectedQuantity)
                            .isEqualTo(expectedQuantity);
                    return this;
                }

                public Asserts assertPrice(BigDecimal expectedPrice) {
                    assertThat(price)
                            .as("unitPrice was: " + price + " but should be: " + expectedPrice)
                            .isEqualTo(expectedPrice);
                    return this;
                }

                public Asserts assertTotalPrice(BigDecimal expectedTotalPrice) {
                    assertThat(totalPrice)
                            .as("totalPrice was: " + totalPrice + " but should be: " + expectedTotalPrice)
                            .isEqualTo(expectedTotalPrice);
                    return this;
                }
            }
        }
    }
}
