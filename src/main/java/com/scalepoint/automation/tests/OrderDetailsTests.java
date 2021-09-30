package com.scalepoint.automation.tests;

import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.pages.CustomerDetailsPage;
import com.scalepoint.automation.pageobjects.pages.CustomerOrderEditPage;
import com.scalepoint.automation.pageobjects.pages.OrderDetailsPage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.ucommerce.CreateOrderService;
import com.scalepoint.automation.shared.VoucherInfo;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.utils.NumberFormatUtils;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.UserAttributes;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.entity.input.ClaimItem;
import com.scalepoint.automation.utils.data.entity.input.Translations;
import com.scalepoint.automation.utils.data.entity.input.Voucher;
import com.scalepoint.automation.utils.data.entity.translations.OrderDetails;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static com.scalepoint.automation.pageobjects.pages.MailsPage.MailType.*;
import static com.scalepoint.automation.services.usersmanagement.CompanyCode.BASIC_ADMIN_ROLE;
import static org.assertj.core.api.Assertions.assertThat;

@Jira("https://jira.scalepoint.com/browse/CHARLIE-540")
@RequiredSetting(type = FTSetting.USE_UCOMMERCE_SHOP, enabled = false)
public class OrderDetailsTests extends BaseTest {

    /**
     * GIVEN: SP User
     * WHEN: User navigates to Order page
     * THEN: There 5 lines with 0,00 amount each:
     * Scalepoint har betalt til Scalepoint (Erstatning) :  0,00
     * Scalepoint har betalt til leverandør (Varekøb) :  0,00
     * Scalepoint har betalt til kunde (Udbetalinger) :  0,00
     * Kunde har betalt til Scalepoint (Indbetalinger) :  0,00
     * Tilbageværende erstatning :  0,00
     */
    @Test(groups = {TestGroups.ORDER_DETAILS}, dataProvider = "testDataProvider",
            description = "CHARLIE-540 ME: Order page; Verify Order page default")
    public void charlie540_ordersPageIsEmpty(User user, Claim claim, Translations translations) {
        String companyName = user.getCompanyName();

        OrderDetailsPage ordersPage = loginAndCreateClaim(user, claim).
                toOrdersDetailsPage();

        OrderDetails orderDetails = translations.getOrderDetails();

        Assert.assertEquals(ordersPage.getLegendItemText(), orderDetails.getTotalText());
        Assert.assertEquals(ordersPage.getIdemnityText(), orderDetails.getIndemnity(companyName));
        assertThat(ordersPage.getIdemnityValue())
                .as("Idemnity value is not 0")
                .isEqualTo(0.0d);

        Assert.assertEquals(ordersPage.getOrderedItemsText(), orderDetails.getOrderedItemsText());
        assertThat(ordersPage.getOrderedItemsValue())
                .as("Ordered value is 0")
                .isEqualTo(0.0d);

        Assert.assertEquals(ordersPage.getWithdrawText(), orderDetails.getWithdrawallsText());
        assertThat(ordersPage.getWithdrawValue())
                .as("Withdraw value is 0")
                .isEqualTo(0.0d);

        Assert.assertEquals(ordersPage.getDepositText(), orderDetails.getDepositsText());
       assertThat(ordersPage.getDepositValue())
               .as("Deposits value is 0")
               .isEqualTo(0.0d);

        Assert.assertEquals(ordersPage.getRemainingIdemnityText(), orderDetails.getRemainingCompensationText());
        assertThat(ordersPage.getRemainingValue())
                .as("Remaining value is 0")
                .isEqualTo(0.0d);
    }

    /**
     * GIVEN: SP User
     * WHEN: User creates claim
     * WHEN:  Add line on settlement 949.00
     * WHEN: Make an order using Replacement
     * WHEN: Cancel order
     * THEN: The state on Order page is the following:
     * Scalepoint har betalt til Scalepoint (Erstatning) :  949.00
     * Scalepoint har betalt til leverandør (Varekøb) :  0.00
     * Scalepoint har betalt til kunde (Udbetalinger) :  0.00
     * Kunde har betalt til Scalepoint (Indbetalinger) :  0.00
     * Tilbageværende erstatning :  949.00
     */
    @RequiredSetting(type = FTSetting.CPR_NUMBER_ON_REPLACEMENT_REQUIRED, enabled = false)
    @RequiredSetting(type = FTSetting.DISABLE_NEMKONTO_ON_REPLACEMENT_CLAIMS_HANDLER, enabled = false)
    @RequiredSetting(type = FTSetting.DISABLE_NEMKONTO_ON_REPLACEMENT_CUSTOMER, enabled = false)
    @Test(groups = {TestGroups.ORDER_DETAILS}, dataProvider = "testDataProvider",
            description = "CHARLIE-540 ME: Order page; Cancel order")
    public void charlie540_6_ordersPageWhenWeCancelOrder(User user, Claim claim, ClaimItem claimItem, Translations translations) {
        SettlementDialog settlementDialog = loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .searchByProductName(claimItem.getSetDialogTextMatch())
                .chooseCategory(claimItem.getCategoryMobilePhones())
                .sortOrderableFirst()
                .openSidForFirstProduct();
        Double price = settlementDialog.getCashCompensationValue();

        OrderDetailsPage ordersPage = settlementDialog.closeSidWithOk()
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .openReplacementWizard(true)
                .replaceAllItems()
                .toOrdersDetailsPage()
                .showOrder()
                .cancelItemByDescription(claimItem.getSetDialogTextMatch())
                .addInternalNote("Autotest", OrderDetailsPage.class);

        OrderDetails orderDetails = translations.getOrderDetails();
        Assert.assertEquals(ordersPage.getLegendItemText(), orderDetails.getTotalText());
        Assert.assertEquals(ordersPage.getIdemnityText(), orderDetails.getIndemnity(user.getCompanyName()));
        Assert.assertEquals(ordersPage.getIdemnityValue() - price, 0.0, "Idemnity value(" + ordersPage.getIdemnityValue() + ") is equal to price=" + price);
        Assert.assertEquals(ordersPage.getOrderedItemsText(), orderDetails.getOrderedItemsText());
        assertThat(ordersPage.getOrderedItemsValue())
                .as("Ordered value(" + ordersPage.getOrderedItemsValue() + " is 0")
                .isEqualTo(0.0);
        Assert.assertEquals(ordersPage.getWithdrawText(), orderDetails.getWithdrawallsText());
       assertThat(ordersPage.getWithdrawValue())
               .as("Withdraw value(" + ordersPage.getWithdrawValue() + ") is 0")
               .isEqualTo(0.0);
        Assert.assertEquals(ordersPage.getDepositText(), orderDetails.getDepositsText());
        assertThat(ordersPage.getDepositValue())
                .as("Deposits value(" + ordersPage.getDepositValue() + " is 0")
                .isEqualTo(0.0);
        Assert.assertEquals(ordersPage.getRemainingIdemnityText(), orderDetails.getRemainingCompensationText());
        Assert.assertEquals(ordersPage.getRemainingValue() - price, 0.0, "Remaining value(" + ordersPage.getRemainingValue() + " is equal to " + price);
    }

    @RequiredSetting(type = FTSetting.CPR_NUMBER_ON_REPLACEMENT_REQUIRED, enabled = false)
    @RequiredSetting(type = FTSetting.DISABLE_NEMKONTO_ON_REPLACEMENT_CLAIMS_HANDLER, enabled = false)
    @RequiredSetting(type = FTSetting.DISABLE_NEMKONTO_ON_REPLACEMENT_CUSTOMER, enabled = false)
    @Test(groups = {TestGroups.ORDER_DETAILS}, dataProvider = "testDataProvider",
            description = "CHARLIE-540 ME: Order page; Recomplete claim")
    public void charlie540_ordersPageWhenWeRecompleteAfterOrder(User user, Claim claim, ClaimItem claimItem, Translations translations) {
        SettlementDialog settlementDialog = loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .searchByProductName(claimItem.getSetDialogTextMatch())
                .chooseCategory(claimItem.getCategoryMobilePhones())
                .sortOrderableFirst()
                .openSidForFirstProduct();
        Double price = settlementDialog.getCashCompensationValue();

        OrderDetailsPage ordersPage = settlementDialog.closeSidWithOk()
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .openReplacementWizard(true)
                .replaceAllItems()
                .startReopenClaimWhenViewModeIsEnabled()
                .reopenClaim()
                .toCompleteClaimPage()
                .completeWithEmail(claim, databaseApi, false)
                .openRecentClaim()
                .toOrdersDetailsPage();

        OrderDetails orderDetails = translations.getOrderDetails();
        Assert.assertEquals(ordersPage.getLegendItemText(), orderDetails.getTotalText());
        Assert.assertEquals(ordersPage.getIdemnityText(), orderDetails.getIndemnity(user.getCompanyName()));
        Assert.assertEquals(ordersPage.getIdemnityValue() - price, 0.0, "Idemnity value(" + ordersPage.getIdemnityValue() + ") is equal to price=" + price);
        Assert.assertEquals(ordersPage.getOrderedItemsText(), orderDetails.getOrderedItemsText());
        Assert.assertEquals(ordersPage.getWithdrawText(), orderDetails.getWithdrawallsText());
        assertThat(ordersPage.getWithdrawValue())
                .as("Withdraw value(" + ordersPage.getWithdrawValue() + ") is equals to 0")
                .isEqualTo(0.0);
        Assert.assertEquals(ordersPage.getDepositText(), orderDetails.getDepositsText());
        Assert.assertEquals(ordersPage.getRemainingIdemnityText(), orderDetails.getRemainingCompensationText());
        assertThat(ordersPage.getRemainingValue())
                .as("Remaining value(" + ordersPage.getRemainingValue() + " is 0")
                .isEqualTo(0.0);
    }

    @RequiredSetting(type = FTSetting.USE_UCOMMERCE_SHOP)
    @Test(groups = {TestGroups.ORDER_DETAILS}, dataProvider = "testDataProvider",
            description = "The order details should not be visible for user without VIEW_CUSTOMER_ORDERS permission")
    public void orderDetailsInvisibilityTest(@UserAttributes(company = BASIC_ADMIN_ROLE)User user, Claim claim, ClaimItem claimItem) {

        Boolean isEvoucher = false;
        VoucherInfo voucherInfo = getVoucherInfo(isEvoucher);

        loginAndCreateClaim(user, claim)
                .openSid()
                .setBaseData(claimItem)
                .closeSidWithOk()
                .toCompleteClaimPage()
                .fillClaimFormWithPassword(claim)
                .completeWithEmail(claim, databaseApi, true)
                .openRecentClaim();

        new CreateOrderService().createOrderForProductExtraPay
                (voucherInfo, claim.getClaimNumber(), claim.getPhoneNumber(), claim.getEmail(), isEvoucher);

        new CustomerDetailsPage()
                .toOrdersDetailsPage()
                .doAssert(orderDetailsPage -> orderDetailsPage.assertDetailsAreInvisible())
                .toMailsPage()
                .doAssert(mailsPage ->
                        mailsPage.noOtherMailsOnThePage(Arrays.asList(SETTLEMENT_NOTIFICATION_TO_IC, CUSTOMER_WELCOME
                        )));
    }

    @RequiredSetting(type = FTSetting.USE_UCOMMERCE_SHOP)
    @Test(groups = {TestGroups.ORDER_DETAILS}, dataProvider = "testDataProvider",
            description = "The order details should only be visible when having VIEW_CUSTOMER_ORDERS permission")
    public void orderDetailsVisibilityTest(User user, Claim claim, ClaimItem claimItem) {

        Boolean isEvoucher = false;
        VoucherInfo voucherInfo = getVoucherInfo(isEvoucher);

        loginAndCreateClaim(user, claim)
                .openSid()
                .setBaseData(claimItem)
                .closeSidWithOk()
                .toCompleteClaimPage()
                .fillClaimFormWithPassword(claim)
                .completeWithEmail(claim, databaseApi, true)
                .openRecentClaim();

        new CreateOrderService().createOrderForProductExtraPay
                (voucherInfo, claim.getClaimNumber(), claim.getPhoneNumber(), claim.getEmail(), isEvoucher);

        new CustomerDetailsPage()
                .toOrdersDetailsPage()
                .doAssert(orderDetailsPage -> orderDetailsPage.assertDetailsAreVisible())
                .toMailsPage()
                .doAssert(mailsPage ->
                        mailsPage.noOtherMailsOnThePage(Arrays.asList(
                                SETTLEMENT_NOTIFICATION_TO_IC,
                                CUSTOMER_WELCOME,
                                ORDER_CONFIRMATION
                        )));
    }

    @RequiredSetting(type = FTSetting.CPR_NUMBER_ON_REPLACEMENT_REQUIRED, enabled = false)
    @RequiredSetting(type = FTSetting.DISABLE_NEMKONTO_ON_REPLACEMENT_CLAIMS_HANDLER, enabled = false)
    @RequiredSetting(type = FTSetting.DISABLE_NEMKONTO_ON_REPLACEMENT_CUSTOMER, enabled = false)
    @Test(dataProvider = "testDataProvider",
            description = "Verifies canceling of items one by one")
    public void orderEditCancelOneByOneTest(User user, Claim claim, ClaimItem claimItem, Voucher voucher) {


        BigDecimal firstItemUnitPrice = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(2841.02);
        BigDecimal secondItemUnitPrice = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(90.00);
        BigDecimal zero = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(0.00);

        createOrderWithTwoItems(user, claim, claimItem, voucher, firstItemUnitPrice, secondItemUnitPrice)
                .cancelItemByDescription(claimItem.getSetDialogTextMatch())
                .addInternalNote("Autotest1", CustomerOrderEditPage.class)
                .cancelItemByDescription(voucher.getExistingVoucherForDistances())
                .addInternalNote("Autotest2", OrderDetailsPage.class)
                .doSuborderAssert(claimItem.getSetDialogTextMatch(), suborder ->
                        suborder.assertTotalPrice(zero))
                .doSuborderAssert(voucher.getExistingVoucherForDistances(), suborder ->
                        suborder.assertTotalPrice(zero));
    }

    @RequiredSetting(type = FTSetting.CPR_NUMBER_ON_REPLACEMENT_REQUIRED, enabled = false)
    @RequiredSetting(type = FTSetting.DISABLE_NEMKONTO_ON_REPLACEMENT_CLAIMS_HANDLER, enabled = false)
    @RequiredSetting(type = FTSetting.DISABLE_NEMKONTO_ON_REPLACEMENT_CUSTOMER, enabled = false)
    @Test(dataProvider = "testDataProvider",
            description = "Verifies canceling of all items at once")
    public void orderEditCancelAllItemsTest(User user, Claim claim, ClaimItem claimItem, Voucher voucher) {

        BigDecimal firstItemUnitPrice = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(2841.02);
        BigDecimal secondItemUnitPrice = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(90.00);
        BigDecimal zero = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(0.00);

        createOrderWithTwoItems(user, claim, claimItem, voucher, firstItemUnitPrice, secondItemUnitPrice)
                .cancelAllItems()
                .addInternalNote("Autotest", OrderDetailsPage.class)
                .doSuborderAssert(claimItem.getSetDialogTextMatch(), suborder ->
                        suborder.assertTotalPrice(zero))
                .doSuborderAssert(voucher.getExistingVoucherForDistances(), suborder ->
                        suborder.assertTotalPrice(zero));
    }

    @RequiredSetting(type = FTSetting.CPR_NUMBER_ON_REPLACEMENT_REQUIRED, enabled = false)
    @RequiredSetting(type = FTSetting.DISABLE_NEMKONTO_ON_REPLACEMENT_CLAIMS_HANDLER, enabled = false)
    @RequiredSetting(type = FTSetting.DISABLE_NEMKONTO_ON_REPLACEMENT_CUSTOMER, enabled = false)
    @Test(dataProvider = "testDataProvider",
            description = "Verifies canceling of one item out of many")
    public void orderEditCancelOneItemTest(User user, Claim claim, ClaimItem claimItem, Voucher voucher) {

        BigDecimal firstItemUnitPrice = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(2841.02);
        BigDecimal secondItemUnitPrice = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(90.00);
        BigDecimal zero = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(0.00);

        createOrderWithTwoItems(user, claim, claimItem, voucher, firstItemUnitPrice, secondItemUnitPrice)
                .cancelItemByDescription(claimItem.getSetDialogTextMatch())
                .addInternalNote("Autotest1", CustomerOrderEditPage.class)
                .toOrdersDetailsPage()
                .doSuborderAssert(claimItem.getSetDialogTextMatch(), suborder ->
                        suborder.assertTotalPrice(zero))
                .doSuborderAssert(voucher.getExistingVoucherForDistances(), suborder ->
                        suborder.assertTotalPrice(secondItemUnitPrice));
    }

    private CustomerOrderEditPage createOrderWithTwoItems(User user,
                                                          Claim claim,
                                                          ClaimItem claimItem,
                                                          Voucher voucher,
                                                          BigDecimal firstItemUnitPrice,
                                                          BigDecimal secondItemUnitPrice){
        String existingVoucher = voucher.getExistingVoucherForDistances();

        return loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .searchByProductName(claimItem.getSetDialogTextMatch())
                .chooseCategory(claimItem.getCategoryMobilePhones())
                .sortOrderableFirst()
                .openSidForFirstProduct()
                .closeSidWithOk()
                .openSidAndFill(sid ->
                        sid
                                .withCategory(claimItem.getCategoryBabyItems())
                                .withCustomerDemandPrice(1000.00)
                                .withNewPrice(100.00)
                                .withVoucher(existingVoucher))
                .closeSidWithOk()
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .openReplacementWizard(true)
                .replaceAllItems()
                .toOrdersDetailsPage()
                .doSuborderAssert(claimItem.getSetDialogTextMatch(), orderDetails ->
                        orderDetails
                                .assertUnitPrice(firstItemUnitPrice)
                                .assertQuantity(1)
                                .assertPrice(firstItemUnitPrice)
                )
                .doSuborderAssert(existingVoucher, orderDetails ->
                        orderDetails
                                .assertUnitPrice(secondItemUnitPrice)
                                .assertQuantity(1)
                                .assertPrice(secondItemUnitPrice)
                )
                .showOrder();
    }
}


