package com.scalepoint.automation.pageobjects.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.dialogs.AddInternalNoteDialog;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.threadlocal.Window;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Condition.not;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.scalepoint.automation.utils.Wait.waitForPageLoaded;
import static org.assertj.core.api.Assertions.assertThat;

@EccPage
public class CustomerOrderEditPage extends BaseClaimPage {

    @FindBy(name = "cancelButton")
    private WebElement cancelButton;
//
//    @FindBy(xpath = "//div[@class='table-content']//tr[1]/td[1]")
//    private WebElement idemnityText;
//
//    @FindBy(xpath = "//div[@class='table-content']//tr[1]/td[2]")
//    private WebElement idemnityValue;
//
//    @FindBy(xpath = "//div[@class='table-content']//tr[3]/td[1]")
//    private WebElement orderedItemsText;
//
//    @FindBy(xpath = "//div[@class='table-content']//tr[3]/td[2]")
//    private WebElement orderedItemsValue;
//
//    @FindBy(xpath = "//div[@class='table-content']//tr[4]/td[1]")
//    private WebElement withdrawallsText;
//
//    @FindBy(xpath = "//div[@class='table-content']//tr[4]/td[2]")
//    private WebElement withdrawallsValue;
//
//    @FindBy(xpath = "//div[@class='table-content']//tr[5]/td[1]")
//    private WebElement depositsText;
//
//    @FindBy(xpath = "//div[@class='table-content']//tr[5]/td[2]")
//    private WebElement depositsValue;
//
//    @FindBy(xpath = "//div[@class='table-content']//tr[7]/td[1]")
//    private WebElement remainingIdemnityText;
//
//    @FindBy(xpath = "//div[@class='table-content']//tr[7]/td[2]")
//    private WebElement remainingIdemnityValue;
//
//    @FindBy(id = "btnShowOrder")
//    private WebElement showButton;
//
//    @FindBy(name = "cancelButton")
//    private WebElement cancelButton;
//
//    @FindBy(xpath = "//input[contains(@name,'article')]")
//    private WebElement articleCheckbox;
//
//    @FindBy(css = ".order-wrapper-table")
//    private WebElement orderDetails;
//
//    ElementsCollection orderTotalRows = $$("#total table tr");
//
//
    @Override
    protected void ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForPageLoaded();
        $(cancelButton).shouldHave(Condition.attribute("disabled", "true"));
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/matching_engine/customer_order_edit.jsp";
    }

    private AddInternalNoteDialog cancelItem(Suborders.Suborder suborder){
        suborder
                .setCheckBox(true);
        return cancel();
    }

    public AddInternalNoteDialog cancelItemByDescription(String itemDescription){
        return cancelItem(new Suborders().getSuborderByDescription(itemDescription));
    }

    public AddInternalNoteDialog cancelItemByIndex(int index){

        return cancelItem(new Suborders().getSuborderByIndex(index));
    }

    public AddInternalNoteDialog cancelAllItems(){
        new Suborders()
                .subordersList
                .forEach(suborder -> suborder.setCheckBox(true));
        return cancel();
    }

    private AddInternalNoteDialog cancel(){

        $(cancelButton).click();
        getAlertTextAndAccept();
        Window.get().switchToLast();
        return BaseDialog.at(AddInternalNoteDialog.class);
    }

//
//    public String getLegendItemText() {
//        return legendItem.getText();
//    }
//
//    public String getIdemnityText() {
//        return $(idemnityText).getText();
//    }
//
//    public Double getIdemnityValue() {
//        return OperationalUtils.toNumber(idemnityValue.getText());
//    }
//
//    public String getOrderedItemsText() {
//        return $(orderedItemsText).getText();
//    }
//
//    public Double getOrderedItemsValue() {
//        return OperationalUtils.toNumber(orderedItemsValue.getText());
//    }
//
//    public String getWithdrawText() {
//        return $(withdrawallsText).getText();
//    }
//
//    public Double getWithdrawValue() {
//        return OperationalUtils.toNumber(withdrawallsValue.getText());
//    }
//
//    public String getDepositText() {
//        return $(depositsText).getText();
//    }
//
//    public Double getDepositValue() {
//        return OperationalUtils.toNumber(depositsValue.getText());
//    }
//
//    public String getRemainingIdemnityText() {
//        return $(remainingIdemnityText).getText();
//    }
//
//    public Double getRemainingValue() {
//        return OperationalUtils.toNumber(remainingIdemnityValue.getText());
//    }
//
//    public CustomerOrderEditPage cancelItem() {
//        showButton.click();
//        articleCheckbox.click();
//        cancelButton.click();
//        getAlertTextAndAccept();
//        Window.get().switchToLast();
//        return BaseDialog.at(AddInternalNoteDialog.class)
//                .addInternalNote("Autotests", CustomerOrderEditPage.class);
//    }
//
//    public CustomerOrderEditPage refreshPageToGetOrders(){
//        Browser.driver().navigate().refresh();
//        return this;
//    }
//
//    public CustomerOrderEditPage showOrder(){
//        hoverAndClick($(showButton));
//        return Page.at(CustomerOrderEditPage.class);
//    }
//
//    private SelenideElement orderDetailsShouldBe(Condition condition){
//
//        return $("#orders")
//                .find(".order-wrapper-table .suborder-header tbody")
//                .shouldBe(condition);
//    }
//
//    public CustomerOrderEditPage doAssert(Consumer<Asserts> assertFunc) {
//        assertFunc.accept(new Asserts());
//        return CustomerOrderEditPage.this;
//    }
//
//    public CustomerOrderEditPage doSuborderAssert(String description, Consumer<Suborders.Suborder.Asserts> assertFunc) {
//        assertFunc.accept(new Suborders().getSuborderByDescription(description).new Asserts());
//        return CustomerOrderEditPage.this;
//    }
//
//    public class Asserts {
//
//        public void assertCompensationAmount(Double expectedCompensationAmount){
//            BigDecimal actualCompensationAmount = new OrderTotals().getCompensationAmount();
//            assertThat(expectedCompensationAmount.equals(actualCompensationAmount))
//                    .as("compensationAmount was: " + actualCompensationAmount + " but should be: " + expectedCompensationAmount)
//                    .isTrue();
//        }
//        public void assertAmountScalepointHasPaidToSupplier(Double expectedGoodsAmount){
//            BigDecimal actualGoodsAmount = new OrderTotals().getGoods();
//            assertThat(expectedGoodsAmount.equals(actualGoodsAmount))
//                    .as("amount Scalepoint has paid to Supplier was: " + actualGoodsAmount + " but should be: " + expectedGoodsAmount)
//                    .isTrue();
//        }
//
//        public void assertAmountScalepointPaidToCustomer(Double expectedPayouts){
//            BigDecimal actualPayouts = new OrderTotals().getPayouts();
//            assertThat(expectedPayouts.equals(actualPayouts))
//                    .as("amount Scalepoint paid to customer was: " + actualPayouts + "but should be: " + expectedPayouts)
//                    .isTrue();
//        }
//
//        public void assertAmountCustomerHasPaidToScalepoint(Double expectedDeposit){
//            BigDecimal actualDeposit = new OrderTotals().getDeposits();
//            assertThat(expectedDeposit.equals(actualDeposit))
//                    .as("amount customer has paid to Scalepoint was :" + "but should be: " + expectedDeposit)
//                    .isTrue();
//        }
//
//        public void assertRemainingCompensationTotal(Double expectedRemainingCompensation) {
//            BigDecimal actualRemainingCompensation = new OrderTotals().getRemainingCompensation();
//            assertThat(expectedRemainingCompensation.equals(actualRemainingCompensation)).
//                    as("remaining compensation was: " + actualRemainingCompensation + " but should be :" + expectedRemainingCompensation)
//                    .isTrue();
//        }
//
//        public void assertDetailsAreVisible(){
//
//            assertThat(orderDetailsShouldBe(Condition.visible).exists()).
//                    as("Order details should be displayed")
//                    .isTrue();
//        }
//        public void assertDetailsAreInvisible(){
//
//            assertThat(orderDetailsShouldBe(not(Condition.exist)).exists()).
//                    as("Order details should not be displayed")
//                    .isFalse();
//        }
//
//    }
//
//    class OrderTotals{
//        private BigDecimal compensationAmount; //IC to Scalepoint
//        private BigDecimal goods;//Scalepoint has paid to supplier
//        private BigDecimal payouts;//Scalepoint paid to customer
//        private BigDecimal deposits;//Customer has paid to Scalepoint
//        private BigDecimal remainingCompensation;
//
//
//
//        BigDecimal getCompensationAmount() {
//            return compensationAmount;
//        }
//
//        BigDecimal getGoods() {
//            return goods;
//        }
//
//        BigDecimal getPayouts() {
//            return payouts;
//        }
//
//        BigDecimal getDeposits() {
//            return deposits;
//        }
//
//        BigDecimal getRemainingCompensation() {
//            return remainingCompensation;
//        }
//
//
//        OrderTotals() {
//            this.compensationAmount = getValueForTotalRowWithIndex(0);
//            this.goods = getValueForTotalRowWithIndex(2);
//            this.payouts = getValueForTotalRowWithIndex(3);
//            this.deposits = getValueForTotalRowWithIndex(4);
//            this.remainingCompensation = getValueForTotalRowWithIndex(6);
//        }
//
//        private BigDecimal getValueForTotalRowWithIndex(int rowIndex){
//            return NumberFormatUtils.formatBigDecimalToHaveTwoDigits((getTextForTotalRowWithIndex(rowIndex).replace(',', '.')));
//        }
//
//        private String getTextForTotalRowWithIndex(int rowIndex){
//            ElementsCollection totalRows = $$("#total table tr");
//            By amountTextSelector = By.xpath("td[2]");
//            return totalRows.get(rowIndex).find(amountTextSelector).getText();
//        }
//    }
//
    public class Suborders{

        List<Suborder> subordersList;
        BigDecimal totalOrderAmount;

        Suborders(){
//            totalOrderAmount = NumberFormatUtils
//                    .formatBigDecimalToHaveTwoDigits(OperationalUtils.getDoubleValue($("#orders .order-table td:first-of-type").getText()));
            subordersList = $$(".suborder-table")
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

    Suborder getSuborderByIndex(int index){

        return subordersList.get(0);
    }

        public class Suborder{

            String itemDescription;
//            BigDecimal unitPrice;
//            int quantity;
//            BigDecimal price;
            SelenideElement checkbox;

            Suborder(SelenideElement element){

                checkbox = element.find("input[type=\"checkbox\"]");
                itemDescription = element.find("#productLink").getText();
//                unitPrice = NumberFormatUtils
//                        .formatBigDecimalToHaveTwoDigits(OperationalUtils.getDoubleValue(element.find(".suborder-table td:nth-of-type(3)").getText()));
//                quantity = Integer.parseInt(element.find(".suborder-table td:nth-of-type(4)").getText());
//                price = NumberFormatUtils
//                        .formatBigDecimalToHaveTwoDigits(OperationalUtils.getDoubleValue(element.find(".suborder-table td:nth-of-type(5)").getText()));
            }

            public Suborder setCheckBox(boolean status){

                checkbox.setSelected(status);
                return this;
            }

            public class Asserts {

//                public Asserts assertUnitPrice(BigDecimal expectedUnitPrice) {
//                    assertThat(unitPrice)
//                            .as("unitPrice was: " + unitPrice + " but should be: " + expectedUnitPrice)
//                            .isEqualTo(expectedUnitPrice);
//                    return this;
//                }
//
//                public Asserts assertQuantity(int expectedQuantity) {
//                    assertThat(quantity)
//                            .as("quantity was: " + quantity + " but should be: " + expectedQuantity)
//                            .isEqualTo(expectedQuantity);
//                    return this;
//                }
//
//                public Asserts assertPrice(BigDecimal expectedPrice) {
//                    assertThat(price)
//                            .as("unitPrice was: " + price + " but should be: " + expectedPrice)
//                            .isEqualTo(expectedPrice);
//                    return this;
//                }
            }
        }
    }
}
