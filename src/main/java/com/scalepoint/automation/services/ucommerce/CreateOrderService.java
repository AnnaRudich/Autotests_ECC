package com.scalepoint.automation.services.ucommerce;

import com.scalepoint.automation.services.externalapi.OauthTestAccountsApi;
import com.scalepoint.automation.services.restService.common.BaseService;
import com.scalepoint.automation.shared.VoucherInfo;
import com.scalepoint.automation.shared.XpriceInfo;
import com.scalepoint.automation.utils.Configuration;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.data.entity.order.*;
import com.scalepoint.automation.utils.data.response.Token;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
/*
 * https://jira.scalepoint.com/browse/CLAIMSHOP-5907
 */
public class CreateOrderService extends BaseService {

    private final String TEST_DESCRIPTION = "Test description";
    private final Double compensationAmount = Constants.PRICE_100;
    private final Double extraPayAmount = Constants.PRICE_50;
    private final Double amountNet = 80.2;
    private final int quantity = 1;
    private final String locale = Configuration.getLocale().getValue().toUpperCase();

    private Token token;

    public CreateOrderService(){

        super();
        this.token = new OauthTestAccountsApi().sendRequest(OauthTestAccountsApi.Scope.SHOP).getToken();
    }

    public void createOrderForProduct(XpriceInfo xpriceInfo, String claimNumber){
        given().log().all()
                .header(token.getAuthorizationHeader())
                .contentType("application/xml")
                .body(buildProductOrderRequestBody(xpriceInfo, claimNumber))
                .when()
                .post(Configuration.getCreateOrderWebServiceUrl())
                .then().statusCode(200).log();
    }

    public void createOrderForProductExtraPay(VoucherInfo voucherInfo, String claimNumber, String customerPhone, String customerMail, Boolean isEvoucher){
        given().log().all()
                .header(token.getAuthorizationHeader())
                .contentType("application/xml")
                .body(buildVoucherOrderExtraPayRequestBody(voucherInfo, claimNumber, customerPhone, customerMail, isEvoucher))
                .when()
                .post(Configuration.getCreateOrderWebServiceUrl())
                .then().statusCode(200).log();
    }

    public void createOrderForVoucher(VoucherInfo voucherInfo, String claimNumber, String customerPhone, String customerMail, Boolean isEvoucher){
        given().log().all()
                .header(token.getAuthorizationHeader())
                .contentType("application/xml")
                .body(buildVoucherOrderRequestBody(voucherInfo, claimNumber, customerPhone, customerMail, isEvoucher))
                .when()
                .post(Configuration.getCreateOrderWebServiceUrl())
                .then().statusCode(200).log();
    }


    public CreateOrderRequest buildProductOrderRequestBody(XpriceInfo xpriceInfo, String claimNumber){

        setUserIdByClaimNumber(claimNumber);

        Payments payments = Payments.builder().deposits(buildDeposits(buildScalepointAccount(locale + data.getUserId()))).build();

        OrderedItem orderedItem = OrderedItem.builder()
                .product(buildProduct(xpriceInfo.getPriceModelID(), xpriceInfo.getPriceModelType(), xpriceInfo.getAgreementId(),
                        xpriceInfo.getDiscountValue(), xpriceInfo.getPriceSourceType(), xpriceInfo.getPriceSourceSupplierID(),
                        xpriceInfo.getProductId(), xpriceInfo.getProductKey()))
                .build();

        Account account = Account.builder().accountID(locale + data.getUserId()).build();
        return CreateOrderRequest.builder().order(buildOrder(payments, orderedItem, xpriceInfo.getSupplierId())).account(account).build();
    }

    public CreateOrderRequest buildVoucherOrderExtraPayRequestBody(VoucherInfo voucherInfo, String claimNumber, String customerPhone, String customerMail, Boolean isEvoucher){

        VoucherType voucherType = isEvoucher ? VoucherType.EVOUCHER : VoucherType.PHYSICAL_VOUCHER;

        setUserIdByClaimNumber(claimNumber);

        Payments payments = Payments.builder().deposits(buildDeposits(buildScalepointAccount(locale + data.getUserId()), buildPaymentGateway())).build();

        OrderedItem orderedItem = OrderedItem.builder()
                .voucher(buildVoucher(voucherType, locale + voucherInfo.getVoucherId(), customerMail, customerPhone, voucherInfo.getPurchaseDiscount()))
                .build();

        Account account = Account.builder().accountID(locale + data.getUserId()).build();
        return CreateOrderRequest.builder().order(buildOrder(payments, orderedItem, voucherInfo.getVoucherSupplierId(),compensationAmount, extraPayAmount)).account(account).build();
    }

    public CreateOrderRequest buildVoucherOrderRequestBody(VoucherInfo voucherInfo, String claimNumber, String customerPhone, String customerMail, Boolean isEvoucher){

        VoucherType voucherType = isEvoucher ? VoucherType.EVOUCHER : VoucherType.PHYSICAL_VOUCHER;

        setUserIdByClaimNumber(claimNumber);

        Payments payments = Payments.builder().deposits(buildDeposits(buildScalepointAccount(locale + data.getUserId()))).build();

        OrderedItem orderedItem = OrderedItem.builder()
                .voucher(buildVoucher(voucherType, locale + voucherInfo.getVoucherId(), customerMail, customerPhone, voucherInfo.getPurchaseDiscount()))
                .build();

        Account account = Account.builder().accountID(locale + data.getUserId()).build();
        return CreateOrderRequest.builder().order(buildOrder(payments, orderedItem, voucherInfo.getVoucherSupplierId())).account(account).build();
    }


    private OrderTotalPurchasePrice buildOrderTotalPurchasePrice(Double amount, Double extraPay){
        return OrderTotalPurchasePrice.builder()
                .amount(amount *quantity + extraPay)
                .amountNet(amountNet*quantity).build();
    }

    private OrderTotalInvoicePrice buildOrderTotalInvoicePrice(Double amount, Double extraPay){
        return OrderTotalInvoicePrice.builder()
                .amount(amount* quantity + extraPay)
                .amountNet(amountNet*quantity).build();
    }

    private Deposits buildDeposits(ScalepointAccount scalepointAccount){
        Double amountValue = compensationAmount *quantity;
        List<Deposit> listOfDeposits = new ArrayList<>();
        Deposit deposit = Deposit.builder().amount(amountValue).scalepointAccount(scalepointAccount).build();
        listOfDeposits.add(deposit);
        return Deposits.builder().depositsTotal(amountValue).deposit(listOfDeposits).build();
    }

    private Deposits buildDeposits(ScalepointAccount scalepointAccount, PaymentGateway paymentGateway){
        Double depositTotal = compensationAmount+extraPayAmount;
        List<Deposit> listOfDeposits = new ArrayList<>();
        Deposit compensationDeposit = Deposit.builder().amount(compensationAmount).scalepointAccount(scalepointAccount).build();
        Deposit extraPayDeposit = Deposit.builder().amount(extraPayAmount).paymentGateway(paymentGateway).build();
        listOfDeposits.add(compensationDeposit);
        listOfDeposits.add(extraPayDeposit);
        return Deposits.builder().depositsTotal(depositTotal).deposit(listOfDeposits).build();
    }

    private AgreementData buildProductAgreementData(String priceModelID, String priceModelType, String agreementID, Double discountValue,
                                                    String priceSourceType, String priceSourceSupplierID){
        RecommendedPrice recommendedPrice = RecommendedPrice.builder().build();
        MarketPrice marketPrice = MarketPrice.builder().build();
        SupplierShopPrice supplierShopPrice = SupplierShopPrice.builder().build();
        return AgreementData.builder()
                .priceModelID(locale+priceModelID)
                .priceModelType(priceModelType)
                .agreementID(locale+agreementID)
                .discountValue(discountValue)
                .priceSourceType(priceSourceType)
                .priceSourceSupplierID(locale + priceSourceSupplierID)
                .recommendedPrice(recommendedPrice).marketPrice(marketPrice).supplierShopPrice(supplierShopPrice).build();
    }
    private Product buildProduct(String priceModelID, String priceModelType, String agreementID, Double discountValue,
                                 String priceSourceType, String priceSourceSupplierID, String productId, String skuNumber){
        return  Product.builder()
                .productID(locale + productId)
                .skuNumber(skuNumber)
                .agreementData(buildProductAgreementData(priceModelID, priceModelType, agreementID, discountValue, priceSourceType, priceSourceSupplierID)).build();

    }

    private Voucher buildVoucher(VoucherType voucherType, String voucherId, String customerEmail,
                                 String customerPhone, Double purchaseDiscount){
        return Voucher.builder()
                .customerEmail(customerEmail)
                .customerPhone(customerPhone)
                .voucherID(voucherId)
                .voucherType(voucherType.name())
                .purchaseDiscount(purchaseDiscount).build();
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

    private OrderLine buildOrderLine(int quantity, String description, OrderedItem orderedItem, Double amount){
        return OrderLine.builder().quantity(quantity)
                .description(description)
                .freightprice(buildFreightPrice(0.0, 0.0))
                .basePurchasePrice(buildBasePurchasePrice(amount,amountNet))
                .totalInvoicePrice(buildTotalInvoicePrice(amount, amountNet))
                .totalPurchasePrice(buildTotalPurchasePrice(amount, amountNet))
                .orderedItem(orderedItem).build();
    }

    private Supplier buildSupplier(String id){
        return Supplier.builder().supplierID(id).build();
    }

    private OrderLines buildOrderLines(OrderedItem orderedItem, int orderLineQuantity, String orderLineDescription, Double amount){
        List<OrderLine> listOfOrderLines = new ArrayList<OrderLine>(){
            {
                add(buildOrderLine(orderLineQuantity, orderLineDescription, orderedItem, amount));
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

    private PaymentGateway buildPaymentGateway(){
        return PaymentGateway.builder().build();
    }

    private Order buildOrder(Payments payments, OrderedItem orderedItem, String supplierId){
        SubOrder suborder = SubOrder.builder()
                .orderLines(buildOrderLines(orderedItem, quantity, TEST_DESCRIPTION,compensationAmount *quantity ))
                .subTotalInvoicePrice(buildSubTotalInvoicePrice(compensationAmount *quantity, amountNet*quantity))
                .subTotalPurchasePrice(buildSubTotalPurchasePrice(compensationAmount *quantity, amountNet*quantity))
                .supplier(buildSupplier(locale + supplierId)).build();

        return Order.builder().orderTotalInvoicePrice(buildOrderTotalInvoicePrice(compensationAmount, 0.0))
                .orderTotalPurchasePrice(buildOrderTotalPurchasePrice(compensationAmount, 0.0))
                .payments(payments).shippingAddress(buildShippingAddress()).suborders(buildSubOrders(suborder)).build();

    }

    private Order buildOrder(Payments payments, OrderedItem orderedItem, String supplierId, Double compensation, Double extraPay){
        SubOrder suborder = SubOrder.builder()
                .orderLines(buildOrderLines(orderedItem, quantity, TEST_DESCRIPTION, compensation *quantity+extraPay))
                .subTotalInvoicePrice(buildSubTotalInvoicePrice(compensation *quantity+extraPay, amountNet*quantity))
                .subTotalPurchasePrice(buildSubTotalPurchasePrice(compensation *quantity+extraPay, amountNet*quantity))
                .supplier(buildSupplier(locale+supplierId)).build();

        return Order.builder().orderTotalInvoicePrice(buildOrderTotalInvoicePrice(compensation, extraPay))
                .orderTotalPurchasePrice(buildOrderTotalPurchasePrice(compensation, extraPay))
                .payments(payments).shippingAddress(buildShippingAddress()).suborders(buildSubOrders(suborder)).build();
    }


    enum VoucherType{
        PHYSICAL_VOUCHER,
        EVOUCHER;
    }
}
