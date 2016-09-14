/*
package com.ecc.fip.tests.settlementDialog;

import com.ecc.fip.api.ApiHelper;
import com.ecc.fip.api.Bug;
import com.ecc.fip.pages.*;
import com.ecc.fip.pages.blocks.adminpage.AdminPageLinks;
import com.ecc.fip.pages.blocks.adminpage.EditFunctionTemplatePage;
import com.ecc.fip.pages.blocks.adminpage.FunctionalTemplatesPage;
import com.ecc.fip.pages.blocks.adminpage.TemplateFunctions;
import com.ecc.fip.pages.blocks.settlementItemDialog.ReplacementDialog;
import com.ecc.fip.pages.blocks.settlementItemDialog.SettlementDialog;
import com.ecc.fip.pages.blocks.settlementpage.*;
import com.ecc.fip.tests.ATest;
import com.ecc.fip.util.EccActions;
import com.ecc.flows.fastflows.FastFlows;
import com.ecc.util.data.entity.ClaimItem;
import com.ecc.util.data.entity.Client;
import com.ecc.util.data.entity.InsuranceCompany;
import com.ecc.util.data.entity.Voucher;
import com.google.inject.Inject;
import org.apache.http.client.fluent.Executor;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertEquals;

*/
/**
 * Created by asa on 2016-05-13.
 *//*

public class DepreciationDeductedCombinedTests extends ATest {

    @Inject
    LoginPage loginPage;
    @Inject
    SettlementPage settlementPage;
    @Inject
    SettlementDialog settlementDialog;
    @Inject
    AdminPage adminPage;
    @Inject
    FastFlows fastFlows;
    @Inject
    Client client;
    @Inject
    ClaimItem claimItem;
    @Inject
    ApiHelper apiHelper;
    @Inject
    Voucher voucher;
    @Inject
    InsuranceCompany insuranceCompany;
    @Inject
    FunctionalTemplatesPage functionalTemplatesPage;
    @Inject
    EditFunctionTemplatePage editFunctionTemplatePage;
    @Inject
    LoginShopPage loginShopPage;
    @Inject
    TabsMenu tabsMenu;
    @Inject
    MailsPage mailsPage;
    @Inject
    SelfServiceEmailDialog selfServiceEmailDialog;
    @Inject
    MyHomePage myHomePage;
    @Inject
    ShopWelcomePage shopWelcomePage;
    @Inject
    MainMenu mainMenu;
    @Inject
    CustomerDetails customerDetails;
    @Inject
    CompleteClaimPage completeClaimPage;
    @Inject
    ClaimInfoPage claimInfoPage;
    @Inject
    ReplacementDialog replacementDialog;
    @Inject
    NewCustomerPage newCustomerPage;
    @Inject
    EccActions eccActions;

    private Map<String, String> categories = new HashMap<>();
    String recipientEmail;

    @BeforeClass
    public void beforeClass() {
        recipientEmail = System.getProperty("recipient", "vne@scalepoint.com");
        Executor executor = fastFlows.login(credentials.getMyAdmin10Login(), credentials.getMyAdmin10Pass());
        apiHelper.addMappedVoucher(executor, voucher, categories);
    }

    @BeforeMethod
    public void beforeMethod() {
        loginPage.Login(credentials.getMyAdmin10Login(), credentials.getMyAdmin10Pass());
    }

    @AfterMethod
    public void afterMethod() {
        driver.quit();
    }

    private String password = "12341234";

    */
/**
     * GIVEN: FT "Display voucher value with depreciation deducted" ON
     * AND: FT"Compare discount and depreciation" ON
     * AND: FT "Combine discount and depreciation" ON
     * WHEN: ClaimHandler(CH) created claim
     * AND:Combine discount and depreciation UNCHECKED
     * AND: Add manual line in Category (C1) with voucher (V1) with discount(VD1) assigned based on New Price (NP)
     * AND: Depreciation D1 >0%
     * WHEN: CH add line(CL1) manually
     * THEN: Face value = New Price , Cash Value = New Price - VD1%
     * WHEN: CH add CL1 to Settlement
     * THEN: Face value = New Price
     * WHEN: CH complete claim through Replacement Wizard
     * THEN: Face value = New Price , Cash Value = New Price - VD1%
     * WHEN: User (U1) get Customer Welcome email
     * THAN: Face value = New Price , Cash Value = New Price - VD1%
     * WHEN: User (U1) login to the shop
     * THAN: Face value = New Price, Cash Value = New Price - VD1%
     * WHEN: CH review completed claim details
     * THAN: Face value = New Price , Cash Value = New Price - VD1%
     *//*

    @Bug(bug = "CHARLIE-417,CHARLIE-772")
    @Test(description = "ECC-3288 Display voucher value with 'Combine discount and depreciation' UNCHECKED")
    public void ecc3288_1_verifyDndD2AndFTRelationCombineDnDOFF() {
        settlementPage
                .Admin();
        adminPage
                .ClickLink(AdminPageLinks.FUNCTION_TEMPLATES);
        functionalTemplatesPage
                .SelectTemplate(insuranceCompany.getSpFTName());
        functionalTemplatesPage
                .Edit();
        editFunctionTemplatePage
                .EnableFunction(TemplateFunctions.DISPLAY_VOUCHER_VALUE_WITH_DEPRECATION_DEDUCTION);
        editFunctionTemplatePage
                .EnableFunction(TemplateFunctions.COMPARISON_DISCOUNT_DEPRECATION);
        editFunctionTemplatePage
                .EnableFunction(TemplateFunctions.COMBINE_DISCOUNT_DEPRECATION);
        editFunctionTemplatePage
                .SaveValues();
        adminPage
                .ReturnToSettlement();
        mainMenu
                .newCustomer();
        newCustomerPage
                .EnterFirstName(client.getFirstName())
                .EnterSurname(client.getLastName())
                .EnterClaimNumber(client.getClaimNumber())
                .Continue();
        mainMenu
                .claimInfoOpen();
        settlementPage
                .AddManually();
        settlementDialog
                .fillDescription(claimItem.getTextFieldSP())
                .fillCustomerDemand(Integer.valueOf(claimItem.getBigCustomDemandPrice()))
                .fillNewPrice(Integer.valueOf(claimItem.getNewPriceSP()))
                .fillDepreciation(Integer.valueOf(claimItem.getDepAmount1()))
                .fillCategory(claimItem.getExistingCat1())
                .fillSubCategory(claimItem.getExistingSubCat1())
                .fillVoucher(claimItem.getExistingVoucher1())
                .SetDiscountAndDepreciation(false);

        String calculatedCashValue = String.format("%.2f", Double.valueOf(calculatedCashValue()));
        String faceValue = String.format("%.2f",settlementDialog.voucherFaceValueFieldText());
        String cashValue = String.format("%.2f",settlementDialog.voucherCashValueFieldText());
        String newPrice = String.format("%.2f", Double.valueOf(claimItem.getNewPriceSP()));

        assertEquals(faceValue, newPrice,
                "Voucher face value " + faceValue + " should be equal to not depreciated new Price " + newPrice);
        assertEquals(cashValue,calculatedCashValue,
                "Voucher cash value " + cashValue + " should be equal to not depreciated voucher cash value " + calculatedCashValue);

        settlementDialog
                .ok();

        String fetchedFaceTooltipValue = String.format("%.2f",settlementPage.getFaceTooltipValue());
        assertEquals(fetchedFaceTooltipValue, newPrice,
                "Tooltip face value " + fetchedFaceTooltipValue + " should be equal to not  depreciated new price " + newPrice);

        settlementPage
                .CompleteClaim();
        completeClaimPage
                .EnterPhone(client.getPhoneNumber())
                .EnterCellPhone(client.getCellNumber())
                .EnterAddress(client.getAddress(), client.getAddress2(), client.getCity(), client.getZipCode())
                .EnterEmail(recipientEmail)
                .enterPassword(password)
                .sendSMS(false)
                .CompleteWithEmail();
        myHomePage
                .SelectRecentClient();
        tabsMenu
                .ClickMails();
        mailsPage
                .clickVisMail("Kundemail");
        selfServiceEmailDialog
                .GetLinkToLoginToShopAndNavigate();
        loginShopPage
                .enterPassword(password)
                .LoginToShop();

        String fetchedProductCashValue = String.format("%.2f",shopWelcomePage.getProductCashValue());
        String fetchedProductFaceValue = String.format("%.2f",shopWelcomePage.getProductFaceValue());
        assertEquals(fetchedProductCashValue, calculatedCashValue,
                "Voucher cash value " + shopWelcomePage.getProductCashValue() + " should be equal to not depreciated voucher cash value " + calculatedCashValue);
        assertEquals(fetchedProductFaceValue, newPrice,
                "Voucher face value " + shopWelcomePage.getProductFaceValue() + "should be equal to not depreciated new price " + newPrice);
        shopWelcomePage
                .Logout();
        loginPage
                .Open()
                .Login(credentials.getMyAdmin10Login(), credentials.getMyAdmin10Pass());
        myHomePage
                .SelectRecentClient();
        mainMenu
                .customerDetailsOpen();
        String fetchedCustomerCashValue = String.format("%.2f",customerDetails.getCashValue());
        String fetchedCustomerFaceTooltipValue = String.format("%.2f",customerDetails.getFaceTooltipValue());
        assertEquals(fetchedCustomerCashValue, calculatedCashValue,
                "Voucher cash value " + customerDetails.getCashValue() + " should be equal to not depreciated voucher cash value " + calculatedCashValue);
        assertEquals(fetchedCustomerFaceTooltipValue,newPrice,
                "Voucher face value " + customerDetails.getFaceValue() + " should be equal to not depreciated new price " + newPrice);
        claimInfoPage
                .ReopenClaim();
        settlementPage
                .CompleteClaim();
        completeClaimPage
                .EnterPhone(client.getPhoneNumber())
                .EnterCellPhone(client.getCellNumber())
                .EnterAddress(client.getAddress(), client.getAddress2(), client.getCity(), client.getZipCode())
                .EnterEmail(recipientEmail)
                .enterPassword(password)
                .sendSMS(false)
                .replaceClaim();
        eccActions
                .switchToLast();

        String fetchedReplacementDialogVoucherFaceValue = String.format("%.2f",replacementDialog.getVoucherFaceValue());
        String fetchedReplacementDialogItemPriceValue = String.format("%.2f",replacementDialog.getItemPriceValue());

        assertEquals(fetchedReplacementDialogVoucherFaceValue, newPrice,
                "Voucher face value " + fetchedReplacementDialogVoucherFaceValue +  "should be equal to not depreciated new price " + newPrice);
        assertEquals(fetchedReplacementDialogItemPriceValue,calculatedCashValue,
                "Voucher cash value " + fetchedReplacementDialogItemPriceValue + " should be equal to not depreciated voucher cash value " + calculatedCashValue);
        replacementDialog.
                closeReplacementDialog();
    }

    */
/**
     * GIVEN: FT "Display voucher value with depreciation deducted" ON
     * AND: FT "Compare discount and depreciation" ON
     * AND: FT "Combine discount and depreciation" ON
     * WHEN: ClaimHandler(CH) created claim
     * AND: Add manual line in Category (C1) with voucher (V1) with discount(VD1) assigned based on New Price (NP)
     * AND: Depreciation D1 >0%
     * AND: "Compare discount and depreciation" CHECKED
     * WHEN: CH add line(CL1) manually
     * THEN: Face value = New Price - D1%, Cash Value = New Price - VD1% - D1%
     * WHEN: CH add CL1 to Settlement
     * THEN: Face value = New Price
     * WHEN: CH complete claim through Replacement Wizard
     * THEN: Face value = New Price - D1%, Cash Value = New Price - VD1% - D1%
     * WHEN: User (U1) get Customer Welcome email
     * THAN: Face value = New Price - D1%, Cash Value = New Price - VD1% - D1%
     * WHEN: User (U1) login to the shop
     * THAN: Face value = New Price - D1%, Cash Value = New Price - VD1% - D1%
     * WHEN: CH review completed claim details
     * THAN: Face value = New Price - D1%, Cash Value = New Price - VD1% - D1%
     *//*

    @Bug(bug = "CHARLIE-417")
    @Test(description = "ECC-3288 Display voucher value with 'Combine discount and depreciation' CHECKED")
    public void ecc3288_3281_2_verifyDndD2AndFTRelationCombineDDON() {
        settlementPage
                .Admin();
        adminPage
                .ClickLink(AdminPageLinks.FUNCTION_TEMPLATES);
        functionalTemplatesPage
                .SelectTemplate(insuranceCompany.getSpFTName());
        functionalTemplatesPage
                .Edit();
        editFunctionTemplatePage
                .EnableFunction(TemplateFunctions.DISPLAY_VOUCHER_VALUE_WITH_DEPRECATION_DEDUCTION);
        editFunctionTemplatePage
                .EnableFunction(TemplateFunctions.COMPARISON_DISCOUNT_DEPRECATION);
        editFunctionTemplatePage
                .EnableFunction(TemplateFunctions.COMBINE_DISCOUNT_DEPRECATION);
        editFunctionTemplatePage
                .SaveValues();
        adminPage
                .ReturnToSettlement();
        mainMenu
                .newCustomer();
        newCustomerPage
                .EnterFirstName(client.getFirstName())
                .EnterSurname(client.getLastName())
                .EnterClaimNumber(client.getClaimNumber())
                .Continue();
        mainMenu
                .claimInfoOpen();
        settlementPage
                .AddManually();
        settlementDialog
                .fillDescription(claimItem.getTextFieldSP())
                .fillCustomerDemand(Integer.valueOf(claimItem.getBigCustomDemandPrice()))
                .fillNewPrice(Integer.valueOf(claimItem.getNewPriceSP()))
                .fillDepreciation(Integer.valueOf(claimItem.getDepAmount1()))
                .fillCategory(claimItem.getExistingCat1())
                .fillSubCategory(claimItem.getExistingSubCat1())
                .fillVoucher(claimItem.getExistingVoucher1())
                .SetDiscountAndDepreciation(true);

        String calculatedFaceValue = String.format("%.2f",Double.valueOf(calculatedNewPrice()));
        String calculatedCashValue = String.format("%.2f", Double.valueOf(calculatedVoucherValue()));
        String faceValue = String.format("%.2f",settlementDialog.voucherFaceValueFieldText());
        String cashValue = String.format("%.2f",settlementDialog.voucherCashValueFieldText());

        assertEquals(faceValue, calculatedFaceValue,
                "Voucher face value " + faceValue + " should be equal to depreciated new Price " + calculatedFaceValue);
        assertEquals(cashValue,calculatedCashValue,
                "Voucher cash value " + cashValue + " should be equal to depreciated voucher cash value " + calculatedCashValue);

        settlementDialog
                .ok();

        String fetchedFaceTooltipValue = String.format("%.2f",settlementPage.getFaceTooltipValue());
        assertEquals(fetchedFaceTooltipValue, calculatedVoucherValue(),
                "Tooltip face value " + fetchedFaceTooltipValue + " should be equal to depreciated new price " + calculatedFaceValue);
        settlementPage
                .CompleteClaim();
        completeClaimPage
                .EnterPhone(client.getPhoneNumber())
                .EnterCellPhone(client.getCellNumber())
                .EnterAddress(client.getAddress(), client.getAddress2(), client.getCity(), client.getZipCode())
                .EnterEmail(recipientEmail)
                .enterPassword(password)
                .sendSMS(false)
                .CompleteWithEmail();
        myHomePage
                .SelectRecentClient();
        tabsMenu
                .ClickMails();
        mailsPage
                .clickVisMail("Kundemail");
        selfServiceEmailDialog
                .GetLinkToLoginToShopAndNavigate();
        loginShopPage
                .enterPassword(password)
                .LoginToShop();

        String fetchedProductCashValue = String.format("%.2f",shopWelcomePage.getProductCashValue());
        String fetchedProductFaceValue = String.format("%.2f",shopWelcomePage.getProductFaceValue());

        assertEquals(fetchedProductCashValue,calculatedCashValue,
                "Voucher cash value " + fetchedProductCashValue + " should be equal to depreciated voucher cash value " + calculatedCashValue);
        assertEquals(fetchedProductFaceValue,calculatedFaceValue,
                "Voucher face value " + fetchedProductFaceValue + " should be equal to depreciated new price " + calculatedFaceValue);

        shopWelcomePage
                .Logout();
        loginPage
                .Open()
                .Login(credentials.getMyAdmin10Login(), credentials.getMyAdmin10Pass());
        myHomePage
                .SelectRecentClient();
        mainMenu
                .customerDetailsOpen();

        String fetchedCustomerCashValue = String.format("%.2f",customerDetails.getCashValue());
        String fetchedCustomerFaceTooltipValue = String.format("%.2f",customerDetails.getFaceTooltipValue());

        assertEquals(fetchedCustomerCashValue, calculatedCashValue,
                "Voucher cash value "+ fetchedCustomerCashValue + " should be equal to depreciated voucher cash value " + calculatedCashValue);
        assertEquals(fetchedCustomerFaceTooltipValue,calculatedFaceValue,
                "Voucher face value " + fetchedFaceTooltipValue + " should be equal to depreciated new price " + calculatedFaceValue);

        claimInfoPage
                .ReopenClaim();
        settlementPage
                .CompleteClaim();
        completeClaimPage
                .EnterPhone(client.getPhoneNumber())
                .EnterCellPhone(client.getCellNumber())
                .EnterAddress(client.getAddress(), client.getAddress2(), client.getCity(), client.getZipCode())
                .EnterEmail(recipientEmail)
                .enterPassword(password)
                .sendSMS(false)
                .replaceClaim();
        eccActions
                .switchToLast();

        String fetchedReplacementDialogVoucherFaceValue = String.format("%.2f",replacementDialog.getVoucherFaceValue());
        String fetchedReplacementDialogItemPriceValue = String.format("%.2f",replacementDialog.getItemPriceValue());

        assertEquals(fetchedReplacementDialogVoucherFaceValue, calculatedFaceValue,
                "Voucher face value " + fetchedReplacementDialogVoucherFaceValue + " should be equal to depreciated new price " + calculatedCashValue);
        assertEquals(fetchedReplacementDialogItemPriceValue,calculatedCashValue,
                "Voucher cash value " + fetchedReplacementDialogItemPriceValue + " should be equal to depreciated voucher cash value " + calculatedCashValue);
        replacementDialog
                .closeReplacementDialog();
    }

    // Cash Value = New Price - VD1%
    private String calculatedCashValue() {
        Double calculatedCashValue = Double.valueOf(claimItem.getNewPriceSP()) - Double.valueOf(calculatedVoucherDiscount());
        return String.valueOf(calculatedCashValue);
    }

    // Cash Value = New Price - VD1% - D1%
    private String calculatedVoucherValue(){
        Double cashValue = Double.valueOf(calculatedCashValue())- (Double.valueOf(calculatedCashValue())*Double.valueOf(claimItem.getDepAmount1())/100);
        return String.valueOf(cashValue);
    }

    private String calculatedVoucherDiscount(){
        Double voucherDiscount = (Double.valueOf(claimItem.getNewPriceSP()) * Double.valueOf(voucher.getDiscount())) / 100;
        return String.valueOf(voucherDiscount);
    }

    private String calculatedDepreciation() {
        Double depreciation = Double.valueOf(claimItem.getNewPriceSP()) * Double.valueOf(claimItem.getDepAmount1()) / 100;
        return String.valueOf(depreciation);
    }

    // Face value = New Price - D1%
    private String calculatedNewPrice() {
        Double calculatedNewPrice = Double.valueOf(claimItem.getNewPriceSP()) - (Double.valueOf(claimItem.getNewPriceSP()) * Double.valueOf(claimItem.getDepAmount1())) / 100;
        return String.valueOf(calculatedNewPrice);
    }
}
*/
