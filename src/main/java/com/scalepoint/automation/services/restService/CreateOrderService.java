package com.scalepoint.automation.services.restService;

import com.scalepoint.automation.services.restService.common.BaseService;
import com.scalepoint.automation.utils.data.entity.order.Account;
import com.scalepoint.automation.utils.data.entity.order.AgreementData;
import com.scalepoint.automation.utils.data.entity.order.BasePurchasePrice;
import com.scalepoint.automation.utils.data.entity.order.CreateOrderRequest;
import com.scalepoint.automation.utils.data.entity.order.Deposit;
import com.scalepoint.automation.utils.data.entity.order.Deposits;
import com.scalepoint.automation.utils.data.entity.order.Order;
import com.scalepoint.automation.utils.data.entity.order.OrderLine;
import com.scalepoint.automation.utils.data.entity.order.OrderLines;
import com.scalepoint.automation.utils.data.entity.order.OrderTotalInvoicePrice;
import com.scalepoint.automation.utils.data.entity.order.OrderTotalPurchasePrice;
import com.scalepoint.automation.utils.data.entity.order.OrderedItem;
import com.scalepoint.automation.utils.data.entity.order.Payments;
import com.scalepoint.automation.utils.data.entity.order.Product;
import com.scalepoint.automation.utils.data.entity.order.ScalepointAccount;
import com.scalepoint.automation.utils.data.entity.order.ShippingAddress;
import com.scalepoint.automation.utils.data.entity.order.SubTotalInvoicePrice;
import com.scalepoint.automation.utils.data.entity.order.SubTotalPurchasePrice;
import com.scalepoint.automation.utils.data.entity.order.Suborder;
import com.scalepoint.automation.utils.data.entity.order.Suborders;
import com.scalepoint.automation.utils.data.entity.order.Supplier;
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
                .post("http://qa14.scalepoint.com/webapp/ScalePoint/dk/resteasy/uCommerce/CreateOrder")
                .then().statusCode(200).log();
    }



    public CreateOrderRequest buildOrderRequestBody(String claimNumber){
        //String userId = "DK556";

        setUserIdByClaimNumber(claimNumber);

        OrderTotalPurchasePrice orderTotalPurchasePrice = OrderTotalPurchasePrice.builder().amount(400.00).amountNet(320.8).build();
        OrderTotalInvoicePrice orderTotalInvoicePrice = OrderTotalInvoicePrice.builder().amount(400.00).amountNet(320.8).build();

        List<Deposit> listOfDeposits = new ArrayList<>();
        ScalepointAccount scalepointAccount = ScalepointAccount.builder().accountID("DK" + data.getUserId()).build();
        Deposit deposit = Deposit.builder().amount(400.00).scalepointAccount(scalepointAccount).build();
        listOfDeposits.add(deposit);
        Deposits deposits = Deposits.builder().listOfDeposits(listOfDeposits).build();

        Payments payments = Payments.builder().deposits(deposits).build();

        AgreementData agreementData = AgreementData.builder().build();
        Product product = Product.builder().productID("DK3828512").agreementData(agreementData).skuNumber("1986156").build();//get product data using solrApi
        OrderedItem orderedItem = OrderedItem.builder().product(product).build();

        BasePurchasePrice basePurchasePrice = BasePurchasePrice.builder().amount(100.0).amountNet(80.2).build();
        TotalPurchasePrice totalPurchasePrice = TotalPurchasePrice.builder().amount(100.0).amountNet(80.2).build();
        TotalInvoicePrice totalInvoicePrice = TotalInvoicePrice.builder().amount(100.0).amountNet(80.2).build();

        OrderLine orderLine = OrderLine.builder().basePurchasePrice(basePurchasePrice).totalInvoicePrice(totalInvoicePrice)
                .totalPurchasePrice(totalPurchasePrice).orderedItem(orderedItem).build();

        List<OrderLine> listOfOrderLines = new ArrayList<>();
        listOfOrderLines.add(orderLine);

        OrderLines orderLines = OrderLines.builder().listOfOrderLines(listOfOrderLines).build();

        SubTotalPurchasePrice subTotalPurchasePrice = SubTotalPurchasePrice.builder().amount(200.00).amountNet(160.4).build();
        SubTotalInvoicePrice subTotalInvoicePrice = SubTotalInvoicePrice.builder().amount(200.00).amountNet(160.4).build();
        Supplier supplier = Supplier.builder().supplierID("DK24368").build();
        Suborder suborder = Suborder.builder().orderLines(orderLines)
                .subTotalInvoicePrice(subTotalInvoicePrice).subTotalPurchasePrice(subTotalPurchasePrice).supplier(supplier).build();

        List<Suborder> listOfSuborders = new ArrayList<>();
        listOfSuborders.add(suborder);

        Suborders suborders = Suborders.builder().suborders(listOfSuborders).build();

        ShippingAddress shippingAddress = ShippingAddress.builder().firstName("Gerald").lastName("Monroe").build();

        Order order = Order.builder().orderTotalInvoicePrice(orderTotalInvoicePrice).orderTotalPurchasePrice(orderTotalPurchasePrice)
                .payments(payments).shippingAddress(shippingAddress).suborders(suborders).build();


        Account account = Account.builder().accountID("DK" + data.getUserId()).build();
        return CreateOrderRequest.builder().order(order).account(account).build();
    }
}
