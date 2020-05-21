package com.scalepoint.automation.services.restService;

import com.scalepoint.automation.services.restService.common.BaseService;
import com.scalepoint.automation.shared.XpriceInfo;
import com.scalepoint.automation.utils.Constants;
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
import com.scalepoint.automation.utils.data.entity.order.SubOrder;
import com.scalepoint.automation.utils.data.entity.order.SubOrders;
import com.scalepoint.automation.utils.data.entity.order.SubTotalInvoicePrice;
import com.scalepoint.automation.utils.data.entity.order.SubTotalPurchasePrice;
import com.scalepoint.automation.utils.data.entity.order.Supplier;
import com.scalepoint.automation.utils.data.entity.order.SupplierShopPrice;
import com.scalepoint.automation.utils.data.entity.order.TotalInvoicePrice;
import com.scalepoint.automation.utils.data.entity.order.TotalPurchasePrice;
import com.scalepoint.automation.utils.data.entity.order.Voucher;
import com.scalepoint.automation.utils.threadlocal.Browser;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class CreateOrderService extends BaseService {

    public void createOrderForProduct(XpriceInfo xpriceInfo, String claimNumber){
        given().log().all()
                .contentType("application/xml")
                .body(buildProductOrderRequestBody(xpriceInfo, claimNumber))
                .when()
                .post("https://qa14.scalepoint.com/webapp/ScalePoint/dk/resteasy/uCommerce/CreateOrder")
                .then().statusCode(200).log();

        Browser.driver().navigate().refresh();
    }

    public void createOrderForVoucher(String claimNumber){
        given().log().all()
                .contentType("application/xml")
                .body(buildVoucherOrderRequestBody(claimNumber))
                .when()
                .post("https://qa14.scalepoint.com/webapp/ScalePoint/dk/resteasy/uCommerce/CreateOrder")//getEccUrl()+basePath("resteasy/uCommerce/CreateOrder")
                .then().statusCode(200).log();

        Browser.driver().navigate().refresh();
    }


    public CreateOrderRequest buildProductOrderRequestBody(XpriceInfo xpriceInfo, String claimNumber){

        setUserIdByClaimNumber(claimNumber);

        Payments payments = Payments.builder().deposits(buildDeposits(buildScalepointAccount("DK" + data.getUserId()))).build();

        OrderedItem orderedItem = OrderedItem.builder()
                .product(buildProduct(xpriceInfo.getPriceModelID(), xpriceInfo.getPriceModelType(), xpriceInfo.getAgreementId(),
                        xpriceInfo.getDiscountValue(), xpriceInfo.getPriceSourceType(), xpriceInfo.getPriceSourceSupplierID(),
                        xpriceInfo.getProductId(), xpriceInfo.getProductKey()))
                .build();

        //SupplierID validated
        SubOrder suborder = SubOrder.builder()
                .orderLines(buildOrderLines(orderedItem, 1, "Test description"))
                .subTotalInvoicePrice(buildSubTotalInvoicePrice(Constants.PRICE_100, 80.20))//if we can use net = 100?
                .subTotalPurchasePrice(buildSubTotalPurchasePrice(Constants.PRICE_100, 80.20))
                .supplier(buildSupplier("DK24473")).build();

        Order order = Order.builder().orderTotalInvoicePrice(buildOrderTotalInvoicePrice()).orderTotalPurchasePrice(buildOrderTotalPurchasePrice())
                .payments(payments).shippingAddress(buildShippingAddress()).suborders(buildSubOrders(suborder)).build();


        Account account = Account.builder().accountID("DK" + data.getUserId()).build();
        return CreateOrderRequest.builder().order(order).account(account).build();
    }

    public CreateOrderRequest buildVoucherOrderRequestBody(String claimNumber){

        setUserIdByClaimNumber(claimNumber);

        OrderTotalPurchasePrice orderTotalPurchasePrice = OrderTotalPurchasePrice.builder()
                .amount(100.00)
                .amountNet(80.20).build();

        OrderTotalInvoicePrice orderTotalInvoicePrice = OrderTotalInvoicePrice.builder()
                .amount(100.00)
                .amountNet(80.20).build();

        List<Deposit> listOfDeposits = new ArrayList<>();
        ScalepointAccount scalepointAccount = ScalepointAccount.builder()
                .accountID("DK" + data.getUserId()).build();

        Deposit deposit = Deposit.builder()
                .amount(100.00)
                .scalepointAccount(scalepointAccount).build();

        listOfDeposits.add(deposit);

        Deposits deposits = Deposits.builder()
                .depositsTotal(100.00)
                .deposit(listOfDeposits).build();

        Payments payments = Payments.builder().deposits(deposits).build();

        OrderedItem orderedItem = OrderedItem.builder()
                .voucher(buildVoucher(VoucherType.EVOUCHER,"DK2405254" )).build();

        BasePurchasePrice basePurchasePrice = BasePurchasePrice.builder()
                .amount(Constants.PRICE_100)
                .amountNet(Constants.PRICE_100).build();

        TotalPurchasePrice totalPurchasePrice = TotalPurchasePrice.builder()
                .amount(Constants.PRICE_100)
                .amountNet(Constants.PRICE_100).build();

        TotalInvoicePrice totalInvoicePrice = TotalInvoicePrice.builder()
                .amount(Constants.PRICE_100)
                .amountNet(Constants.PRICE_100).build();

        Freightprice freightprice = Freightprice.builder().build();

        OrderLine orderLine = OrderLine.builder()
                .quantity(1)
                .description("Test Description")
                .freightprice(freightprice)
                .basePurchasePrice(basePurchasePrice)
                .totalInvoicePrice(totalInvoicePrice)
                .totalPurchasePrice(totalPurchasePrice)
                .orderedItem(orderedItem).build();

        List<OrderLine> listOfOrderLines = new ArrayList<>();
        listOfOrderLines.add(orderLine);

        OrderLines orderLines = OrderLines.builder()
                .orderLine(listOfOrderLines).build();

        SubTotalPurchasePrice subTotalPurchasePrice = SubTotalPurchasePrice.builder()
                .amount(Constants.PRICE_100)
                .amountNet(Constants.PRICE_100).build();

        SubTotalInvoicePrice subTotalInvoicePrice = SubTotalInvoicePrice.builder()
                .amount(Constants.PRICE_100)
                .amountNet(80.20).build();

        //SupplierID validated
        Supplier supplier = Supplier.builder()
                .supplierID("DK24473").build();

        SubOrder suborder = SubOrder.builder().orderLines(orderLines)
                .subTotalInvoicePrice(subTotalInvoicePrice)
                .subTotalPurchasePrice(subTotalPurchasePrice)
                .supplier(supplier).build();

        List<SubOrder> listOfSuborders = new ArrayList<>();
        listOfSuborders.add(suborder);

        SubOrders suborders = SubOrders.builder().suborder(listOfSuborders).build();

        ShippingAddress shippingAddress = ShippingAddress.builder()
                .firstName("Gerald")
                .lastName("Monroe").build();

        Order order = Order.builder()
                .orderTotalInvoicePrice(orderTotalInvoicePrice)
                .orderTotalPurchasePrice(orderTotalPurchasePrice)
                .payments(payments)
                .shippingAddress(shippingAddress)
                .suborders(suborders).build();


        Account account = Account.builder()
                .accountID("DK" + data.getUserId()).build();

        return CreateOrderRequest.builder().order(order).account(account).build();
    }


    private OrderTotalPurchasePrice buildOrderTotalPurchasePrice(){
        return OrderTotalPurchasePrice.builder()
                .amount(Constants.PRICE_100)
                .amountNet(80.20).build();
    }

    private OrderTotalInvoicePrice buildOrderTotalInvoicePrice(){
        return OrderTotalInvoicePrice.builder()
                .amount(Constants.PRICE_100)
                .amountNet(80.20).build();
    }

    private Deposits buildDeposits(ScalepointAccount scalepointAccount){
        Double amountValue = Constants.PRICE_100;
        List<Deposit> listOfDeposits = new ArrayList<>();
        Deposit deposit = Deposit.builder().amount(amountValue).scalepointAccount(scalepointAccount).build();
        listOfDeposits.add(deposit);
        return Deposits.builder().depositsTotal(amountValue).deposit(listOfDeposits).build();
    }

    private AgreementData buildProductAgreementData(String priceModelID, String priceModelType, String agreementID, Double discountValue,
                                                    String priceSourceType, String priceSourceSupplierID){
        RecommendedPrice recommendedPrice = RecommendedPrice.builder().build();
        MarketPrice marketPrice = MarketPrice.builder().build();
        SupplierShopPrice supplierShopPrice = SupplierShopPrice.builder().build();
        return AgreementData.builder()
                .priceModelID("DK"+priceModelID)
                .priceModelType(priceModelType)
                .agreementID("DK"+agreementID)
                .discountValue(discountValue)
                .priceSourceType(priceSourceType)
                .priceSourceSupplierID("DK" + priceSourceSupplierID)
                .recommendedPrice(recommendedPrice).marketPrice(marketPrice).supplierShopPrice(supplierShopPrice).build();
    }
    private Product buildProduct(String priceModelID, String priceModelType, String agreementID, Double discountValue,
                                 String priceSourceType, String priceSourceSupplierID, String productId, String skuNumber){
        return  Product.builder()
                .productID("DK" + productId)
                .skuNumber(skuNumber)
                .agreementData(buildProductAgreementData(priceModelID, priceModelType, agreementID, discountValue, priceSourceType, priceSourceSupplierID)).build();

    }

    private Voucher buildVoucher(VoucherType voucherType, String id){
        return Voucher.builder()
                .customerEmail("aru@scalepoint.com")
                .customerPhone("44222222")
                .voucherID(id)
                .voucherType(voucherType.name())
                .purchaseDiscount("0.1000").build();
    }

    private BasePurchasePrice buildBasePurchasePrice(Double amount, Double net){
        return BasePurchasePrice.builder().amount(amount).amountNet(net).build();
    }

    private TotalPurchasePrice buildTotalPurchasePrice(Double amount, Double net){
        return TotalPurchasePrice.builder().amount(amount).amountNet(net).build();
    }

    private TotalInvoicePrice buildTotalInvoicePrice(Double amount, Double net){
        return TotalInvoicePrice.builder().amount(amount).amountNet(net).build();
    }

    private Freightprice buildFreightPrice(Double amount, Double net){
        return Freightprice.builder().amount(amount).amountNet(net).build();
    }

    private OrderLine buildOrderLine(int quantity, String description, OrderedItem orderedItem){
        return OrderLine.builder().quantity(quantity)
                .description(description)
                .freightprice(buildFreightPrice(0.0, 0.0))
                .basePurchasePrice(buildBasePurchasePrice(Constants.PRICE_100,80.2))
                .totalInvoicePrice(buildTotalInvoicePrice(Constants.PRICE_100, 80.2))
                .totalPurchasePrice(buildTotalPurchasePrice(Constants.PRICE_100, 80.2))
                .orderedItem(orderedItem).build();
    }

    private Supplier buildSupplier(String id){
        return Supplier.builder().supplierID(id).build();
    }

    private OrderLines buildOrderLines(OrderedItem orderedItem, int orderLineQuantity, String orderLineDescription){
        List<OrderLine> listOfOrderLines = new ArrayList<OrderLine>(){
            {
                add(buildOrderLine(orderLineQuantity, orderLineDescription, orderedItem));
            }
        };
        return OrderLines.builder().orderLine(listOfOrderLines).build();
    }

    private  SubTotalPurchasePrice buildSubTotalPurchasePrice(Double amount, Double net) {
        return SubTotalPurchasePrice.builder().amount(amount).amountNet(net).build();
    }

    private SubTotalInvoicePrice buildSubTotalInvoicePrice(Double amount, Double net) {
        return SubTotalInvoicePrice.builder().amount(amount).amountNet(net).build();
    }

    private ShippingAddress buildShippingAddress(){
        return ShippingAddress.builder().firstName("Gerald").lastName("Monroe").build();
    }

    private SubOrders buildSubOrders(SubOrder suborder){
        List<SubOrder> listOfSuborders = new ArrayList<SubOrder>(){
            {
                add(suborder);
            }
        };
        return SubOrders.builder().suborder(listOfSuborders).build();
    }

    private ScalepointAccount buildScalepointAccount(String accountId){
        return ScalepointAccount.builder()
                .accountID(accountId).build();
    }



    enum VoucherType{
           PHYSICAL_VOUCHER,
           EVOUCHER;
    }
}
