package com.scalepoint.automation.services.restService;

import com.scalepoint.automation.services.restService.common.BaseService;
import com.scalepoint.automation.utils.data.entity.order.Account;
import com.scalepoint.automation.utils.data.entity.order.AgreementData;
import com.scalepoint.automation.utils.data.entity.order.BasePurchasePrice;
import com.scalepoint.automation.utils.data.entity.order.CreateOrderRequest;
import com.scalepoint.automation.utils.data.entity.order.Deposit;
import com.scalepoint.automation.utils.data.entity.order.Deposits;
import com.scalepoint.automation.utils.data.entity.order.Freightprice;
import com.scalepoint.automation.utils.data.entity.order.MarketPrice;
import com.scalepoint.automation.utils.data.entity.order.Order;
import com.scalepoint.automation.utils.data.entity.order.OrderLine;
import com.scalepoint.automation.utils.data.entity.order.OrderLines;
import com.scalepoint.automation.utils.data.entity.order.OrderTotalInvoicePrice;
import com.scalepoint.automation.utils.data.entity.order.OrderTotalPurchasePrice;
import com.scalepoint.automation.utils.data.entity.order.OrderedItem;
import com.scalepoint.automation.utils.data.entity.order.Payments;
import com.scalepoint.automation.utils.data.entity.order.Product;
import com.scalepoint.automation.utils.data.entity.order.RecommendedPrice;
import com.scalepoint.automation.utils.data.entity.order.ScalepointAccount;
import com.scalepoint.automation.utils.data.entity.order.ShippingAddress;
import com.scalepoint.automation.utils.data.entity.order.SubTotalInvoicePrice;
import com.scalepoint.automation.utils.data.entity.order.SubTotalPurchasePrice;
import com.scalepoint.automation.utils.data.entity.order.SubOrder;
import com.scalepoint.automation.utils.data.entity.order.SubOrders;
import com.scalepoint.automation.utils.data.entity.order.Supplier;
import com.scalepoint.automation.utils.data.entity.order.SupplierShopPrice;
import com.scalepoint.automation.utils.data.entity.order.TotalInvoicePrice;
import com.scalepoint.automation.utils.data.entity.order.TotalPurchasePrice;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class CreateOrderService extends BaseService {

    public void createOrder(String claimNumber){
        given().log().all()
                .contentType("application/xml")
                .body(buildOrderRequestBody(claimNumber))
                .when()
                .post("https://qa14.scalepoint.com/webapp/ScalePoint/dk/resteasy/uCommerce/CreateOrder")
                .then().statusCode(200).log();
    }


    public CreateOrderRequest buildOrderRequestBody(String claimNumber){

        setUserIdByClaimNumber(claimNumber);

        OrderTotalPurchasePrice orderTotalPurchasePrice = OrderTotalPurchasePrice.builder().amount(400.00).amountNet(320.80).build();
        OrderTotalInvoicePrice orderTotalInvoicePrice = OrderTotalInvoicePrice.builder().amount(400.00).amountNet(320.80).build();

        List<Deposit> listOfDeposits = new ArrayList<>();
        ScalepointAccount scalepointAccount = ScalepointAccount.builder().accountID("DK" + data.getUserId()).build();
        Deposit deposit = Deposit.builder().amount(400.00).scalepointAccount(scalepointAccount).build();
        listOfDeposits.add(deposit);
        Deposits deposits = Deposits.builder().depositsTotal(400.00).deposit(listOfDeposits).build();

        Payments payments = Payments.builder().deposits(deposits).build();

        RecommendedPrice recommendedPrice = RecommendedPrice.builder().build();
        MarketPrice marketPrice = MarketPrice.builder().build();
        SupplierShopPrice supplierShopPrice = SupplierShopPrice.builder().build();

        AgreementData agreementData = AgreementData.builder()
                .recommendedPrice(recommendedPrice).marketPrice(marketPrice).supplierShopPrice(supplierShopPrice).build();

        Product product = Product.builder()
                .agreementData(agreementData).build();

        OrderedItem orderedItem = OrderedItem.builder().product(product).build();

        BasePurchasePrice basePurchasePrice = BasePurchasePrice.builder().amount(100.00).amountNet(80.20).build();
        TotalPurchasePrice totalPurchasePrice = TotalPurchasePrice.builder().amount(100.00).amountNet(80.20).build();
        TotalInvoicePrice totalInvoicePrice = TotalInvoicePrice.builder().amount(100.00).amountNet(80.20).build();
        Freightprice freightprice = Freightprice.builder().build();

        OrderLine orderLine = OrderLine.builder().description("APPLE iPhone SE 16GB Guld").freightprice(freightprice).basePurchasePrice(basePurchasePrice).totalInvoicePrice(totalInvoicePrice)
                .totalPurchasePrice(totalPurchasePrice).orderedItem(orderedItem).build();

        List<OrderLine> listOfOrderLines = new ArrayList<>();
        listOfOrderLines.add(orderLine);

        OrderLines orderLines = OrderLines.builder().orderLine(listOfOrderLines).build();

        SubTotalPurchasePrice subTotalPurchasePrice = SubTotalPurchasePrice.builder().amount(200.00).amountNet(160.40).build();
        SubTotalInvoicePrice subTotalInvoicePrice = SubTotalInvoicePrice.builder().amount(200.00).amountNet(160.40).build();
        Supplier supplier = Supplier.builder().supplierID("DK24368").build();
        SubOrder suborder = SubOrder.builder().orderLines(orderLines)
                .subTotalInvoicePrice(subTotalInvoicePrice).subTotalPurchasePrice(subTotalPurchasePrice).supplier(supplier).build();

        List<SubOrder> listOfSuborders = new ArrayList<>();
        listOfSuborders.add(suborder);

        SubOrders suborders = SubOrders.builder().suborder(listOfSuborders).build();

        ShippingAddress shippingAddress = ShippingAddress.builder().firstName("Gerald").lastName("Monroe").build();

        Order order = Order.builder().orderTotalInvoicePrice(orderTotalInvoicePrice).orderTotalPurchasePrice(orderTotalPurchasePrice)
                .payments(payments).shippingAddress(shippingAddress).suborders(suborders).build();


        Account account = Account.builder().accountID("DK" + data.getUserId()).build();
        return CreateOrderRequest.builder().order(order).account(account).build();
    }
}
