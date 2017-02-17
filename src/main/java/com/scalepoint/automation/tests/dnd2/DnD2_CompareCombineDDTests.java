package com.scalepoint.automation.tests.dnd2;

import com.scalepoint.automation.BaseTest;
import com.scalepoint.automation.domain.ProductInfo;
import com.scalepoint.automation.services.externalapi.SolrApi;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

/**
 * The class represents smoke tests set for D&D2 functionality
 * run only on DK
 */
@RequiredSetting(type = FTSetting.COMPARISON_OF_DISCOUNT_DEPRECATION)
@RequiredSetting(type = FTSetting.COMBINE_DISCOUNT_DEPRECATION)
@RequiredSetting(type = FTSetting.SHOW_MARKET_PRICE)
@RequiredSetting(type = FTSetting.DISPLAY_VOUCHER_VALUE_WITH_DEPRECATION_DEDUCTION, enabled = false)
public class DnD2_CompareCombineDDTests extends BaseTest {

    /**
     * GIVEN: User U1 with Admin credentials
     * WHEN: U1 enables "Show Market Price in ME catalog" in FT
     * AND: enables "Comparison of discount and depreciation" in FT
     * AND: opens SID for any search result with Market price MP (200) > Product Price PP(100)
     * AND: adds depreciation D1
     * THAN: Product price valuation is enabled in greed
     * AND: Price and Cash compensation columns are available in greed
     * AND: Product price == PP in greed
     * AND: Cash compensation for product price == PP in greed
     * AND: Market price == MP in greed
     * AND: Cash compensation for Market price == MP - D1 in greed
     */
    @Test(dataProvider = "testDataProvider", description = "ECC-3280 Cash compensation is depreciated for Market price if Comparison of discount and depreciation is enabled in FT")
    public void ecc3280_comparisonDDSearchNoVoucher(User user, Claim claim) {
        ProductInfo product = SolrApi.findProductInvoiceLowerMarket();

        /*double marketPrice = product.getMarketPrice();
        double invoicePrice = product.getInvoicePrice();

        SettlementDialog settlementDialog = loginAndCreateClaim(user, claim)
                .toTextSearchPage(product.getModel())
                .openSidForFirstProduct()
                .fillDepreciation(13)
                .assertGridValueIs()

        settlementDialog.fillDepreciation(13);*/
        /*Assert.assertTrue(Math.abs(pages.ME_SET_DIALOG.getProductPriceGreedFirstColumnValue() - 100.0) < 0.0001,
                "Product price(" + pages.ME_SET_DIALOG.getProductPriceGreedFirstColumnValue() + ") in the SID is not correct. Should be 100.0");
        Assert.assertTrue(Math.abs(pages.ME_SET_DIALOG.getProductPriceGreedValue() - 100.0) < 0.0001,
                "Product price: " + pages.ME_SET_DIALOG.getProductPriceGreedValue() + " shouldn't be depreciated and equals = ProductPrice: " + 100.0);
        Assert.assertTrue(Math.abs(pages.ME_SET_DIALOG.getMarketPriceGreedFirstColumnValue() - 200.0) < 0.0001,
                "Market price(" + pages.ME_SET_DIALOG.getMarketPriceGreedFirstColumnValue() + ") in the SID is not correct. Should be 200.0");
        Assert.assertTrue(Math.abs(pages.ME_SET_DIALOG.getMarketPriceGreedValue() - 174.0) < 0.0001,
                "Market price(" + pages.ME_SET_DIALOG.getMarketPriceGreedValue() + ") should be depreciated and equal to 174");
        pages.ME_SET_DIALOG.closeAddEditSetDialog();
        flows.CLAIM.logout();*/
    }

   /* *//**
     * GIVEN: User U1 with Admin credentials
     * WHEN: U1 enables "Show Market Price in ME catalog" in FT
     * AND: enables "Comparison of discount and depreciation" in FT
     * AND: opens SID for any search result with Market price MP > Product Price PP
     * AND: adds depreciation D1
     * THAN: Product price valuation doesn't exist
     * AND: Price and Cash compensation columns are available in greed
     * AND: Market price == MP in greed
     * AND: Cash compensation for Market price == MP - D1 in greed
     *//*

    @Test(dataProvider = "testDataProvider", description = "ECC-3280 Cash compensation is depreciated for Market price if Comparison of discount and depreciation is enabled in FT. PP and MP are equal")
    public void ecc3280_comparisonDDSearchNoVoucherPPAndMPAreEqual() {
        pages.LOGIN.loginToME(credentials.getMyAdmin10Login(), credentials.getMyAdmin10Pass());
        flows.CLAIM.createClient(client);
        meMenu.selectTextSearchItem();
        pages.ME_T_SEARCH.makeSearch(textSearch.getOrderableProductWithIpEqualMp());
        Integer index = pages.ME_T_SEARCH.getEqualPricesIndex();
        pages.ME_T_SEARCH.openMatchDialog(index);
        pages.ME_SET_DIALOG.setDepreciationAmount("23");
        Assert.assertTrue(Math.abs(pages.ME_SET_DIALOG.getProductPriceGreedValue()-200.0)<0.0001,
                "Product price incorrect in the SID: "+pages.ME_SET_DIALOG.getProductPriceGreedValue()+" should be =200.0");
        Assert.assertTrue(Math.abs(pages.ME_SET_DIALOG.getMarketPriceGreedFirstColumnValue()-200.0)<0.0001,
                "Market price(before calculations) is incorrect in the SID: "+pages.ME_SET_DIALOG.getMarketPriceGreedFirstColumnValue()+" should be =200.0");
        Assert.assertTrue(pages.ME_SET_DIALOG.isMarketPriceValuationSelected2Columns(200.0, pages.ME_SET_DIALOG.getMarketPriceGreedValue()));
        Assert.assertTrue(Math.abs(pages.ME_SET_DIALOG.getMarketPriceGreedValue()-154.0)<0.0001,
                "Market price value : "+pages.ME_SET_DIALOG.getMarketPriceGreedValue()+"was not depreciated. Should be = 154.0");
        pages.ME_SET_DIALOG.closeAddEditSetDialog();
        flows.CLAIM.logout();
    }

    *//**
     * GIVEN: Show Market Price FT enabled
     * WHEN: Comparasing of Discount and Depreciation FT enabledad
     * <p/>
     * AND: Combine  Discount and Depreciation FT disabled
     * AND: Claim Handler (CH) created claim (C2)
     * AND: Add product with ONLY voucher replacemenent
     * AND: Product 2 (P2) has Market Price(MP) > Product Price(PP)
     * AND:  Depreciation value D1 > 0
     * THAN:  Valuation Product Price exists, but INACTIVE
     * AND:  MP valuation exists
     * AND: MP value displays in the first column, MP-D1% value displays in second column
     *//*
    @Test(dataProvider = "testDataProvider", description = "ECC-3280 CH Add product with only voucher replacement from Catalog")
    public void ecc3280_addProductWithOnlyVoucherReplacementFromCatalog() {
        pages.LOGIN.loginToME(credentials.getMyAdmin10Login(), credentials.getMyAdmin10Pass());
        meMenu.selectAdminItem();
        flows.ADMIN.disableMarketPriceCombineDD(insuranceCompany.getSpFTName());
        meAdminMenu.selectMELink();
        flows.CLAIM.createClient(client);
        meMenu.selectTextSearchItem();
        pages.ME_T_SEARCH.makeSearch(textSearch.getProductWithOnlyVoucherHandling2());
        Double pPrice = 111.0;
        Double mPrice = 250.0;
        pages.ME_T_SEARCH.openMatchDialog(0);
        pages.ME_SET_DIALOG.setDepreciationAmount(claimItem.getDepAmount1());
        Double marketPrice = pages.ME_SET_DIALOG.getMarketPriceGreedValue();
        Double productPrice = pages.ME_SET_DIALOG.getProductPriceGreedValue();
        Double depAmount = OperationalUtils.toNumber(claimItem.getDepAmount1());
        Double depreciation = OperationalUtils.getValueByPercent(mPrice, depAmount);
        mPrice = mPrice - depreciation;
        Double mpRounded = (double) Math.round(mPrice * 100) / 100;
        Assert.assertTrue(Math.abs(marketPrice-mpRounded)<0.0001,
                "Market price: "+marketPrice+" was depreciated. Should be = "+mpRounded);
        Assert.assertTrue(Math.abs(pPrice-productPrice)<0.0001,
                "Product price: "+pPrice+" was NOT depreciated. Should be equal to db value = "+productPrice);
        Double voucherReplacementPrice = pages.ME_SET_DIALOG.getMarketPriceGreedValue();
        Assert.assertTrue(Math.abs(pages.ME_SET_DIALOG.getCashCompensationFieldValue()-mpRounded)<0.0001,
                "Cash compensation: "+pages.ME_SET_DIALOG.getCashCompensationFieldValue()+" Should be equal depreciated MP = "+mpRounded);
        pages.ME_SET_DIALOG.closeAddEditSetDialog();
        flows.CLAIM.logout();
    }

    *//**
     * GIVEN: Show Market Price FT enabled
     * WHEN: Comparasing of Discount and Depreciation FT enabled
     * AND: Combine  Discount and Depreciation FT disabled
     * AND: Claim Handler (CH) created claim (C2)
     * AND: Add product with ONLY voucher replacemenent
     * AND: Product 2 (P2) has Market Price(MP) > Product Price(PP)
     * AND:  Depreciation value D1 > 0
     * THAN:  Valuation Product Price exists, but INACTIVE
     * AND:  MP valuation exists
     * AND: MP value displays in the first column, MP-D1% value displays in second column
     * ssp300
     * mp250
     *//*

    @Test(dataProvider = "testDataProvider", description = "ECC-3280 CH Add product with only voucher replacement from Catalog")
    public void ecc3280_addProductWithOnlyVoucherReplacementFromCatalogAreEqual() {
        pages.LOGIN.loginToME(credentials.getMyAdmin10Login(), credentials.getMyAdmin10Pass());
        meMenu.selectAdminItem();
        flows.ADMIN.disableMarketPriceCombineDD(insuranceCompany.getSpFTName());
        meAdminMenu.selectMELink();
        flows.CLAIM.createClient(client);
        meMenu.selectTextSearchItem();
        pages.ME_T_SEARCH.makeSearch(textSearch.getProductWithOnlyVoucherHandling());
        Integer index = pages.ME_T_SEARCH.getEqualPricesIndex();
        pages.ME_T_SEARCH.openMatchDialog(index);
        pages.ME_SET_DIALOG.setDepreciationAmount("11");
        pages.ME_SET_DIALOG.selectVoucherValuation();
        Assert.assertTrue(Math.abs(pages.ME_SET_DIALOG.getSelectedValuationValue()-282.0)<0.0001,
                "Voucher valuation: "+pages.ME_SET_DIALOG.getSelectedValuationValue()+" was depreciated. Should be = 282.0");
        Assert.assertTrue(Math.abs(pages.ME_SET_DIALOG.getCashCompensationFieldValue()-282.0)<0.0001,
                "Voucher cash value : "+pages.ME_SET_DIALOG.getCashCompensationFieldValue()+" was depreciated. Should be = 282.0");
        Assert.assertFalse(Math.abs(pages.ME_SET_DIALOG.getMarketPriceGreedValue() - 250) < 0.0001,
                "Market price valuation : " + pages.ME_SET_DIALOG.getMarketPriceGreedValue() + " was not depreciated. Should be not equal to= 250");
        pages.ME_SET_DIALOG.closeAddEditSetDialog();
        flows.CLAIM.logout();
    }

    *//**
     * GIVEN: Existing category C1 with existing group G1 and mapped to G1-C1 voucher V1
     * WHEN: User selects C1, G1 and V1 in Settlement dialog
     * AND: User adds new price P1
     * AND: User adds depreciation D1
     * THAN: Cash compensation is P1 - V1 discount - D1
     * AND: Depreciation is D1 amount of Cash Compensation
     * WHEN: User enables Comparison of discount and depreciation in FT
     * AND: User selects C1, G1 and V1 in Settlement dialog
     * AND: User adds new price P1
     * AND: User adds depreciation D1
     * THAN: Cash compensation is P1 - V1 discount
     * AND: Depreciation is 0.00
     *//*
    @Test(dataProvider = "testDataProvider", description = "ECC-3280 Cash compensation is not depreciated if Comparison of discount and depreciation is enabled in FT")
    public void ecc3280_comparisonValuationManually() {
        pages.LOGIN.loginToME(credentials.getMyAdmin10Login(), credentials.getMyAdmin10Pass());
        flows.CLAIM.createClient(client);
        pages.ME_SETTLEMENT.openAddEditDialog();
        pages.ME_SET_DIALOG.fillCategoriesVoucherPrice(claimItem.getTextFieldSP(), claimItem.getExistingCat1(), claimItem.getExistingSubCat1(), claimItem.getNewPriceSP(), claimItem.getExistingVoucher_10());
        pages.ME_SET_DIALOG.setDepreciationAmount(claimItem.getDepAmount1());
        pages.ME_SET_DIALOG.addValuation("CUSTOMER_DEMAND", claimItem.getCustomerDemand());
        pages.ME_SET_DIALOG.addValuation("USED_PRICE", claimItem.getUsedPrice());
        Double newPrice = OperationalUtils.toNumber(claimItem.getNewPriceSP());                                 //2400
        Double depAmount = OperationalUtils.toNumber(claimItem.getDepAmount1());                                //10
        Double voucherReplacementPrice = newPrice - OperationalUtils.getValueByPercent(newPrice, depAmount);    //2160
        Double customerDemand = OperationalUtils.toNumber(claimItem.getCustomerDemand());                       //500
        Double usedPrice = OperationalUtils.toNumber(claimItem.getUsedPrice());                                 //500
        Double depreciation = OperationalUtils.getValueByPercent(customerDemand, depAmount);                    //10
        Double customerDemandWithDepr = customerDemand - depreciation;                                          //490
        Double cashCompensation = customerDemandWithDepr;                                                       //490

        //Check voucher replacement with depriciation (depr isn't affected on it)
        Assert.assertTrue((Math.abs(pages.ME_SET_DIALOG.getVoucherValuationGreedSecondColumnValue())-voucherReplacementPrice)<0.0001,
                "Voucher valuation "+pages.ME_SET_DIALOG.getVoucherValuationGreedSecondColumnValue()+" is depreciated and not equal to: "+voucherReplacementPrice);
        //Check voucher replacement
        Assert.assertTrue((Math.abs(pages.ME_SET_DIALOG.getVoucherValuationGreedFirstColumnValue()-voucherReplacementPrice))<0.0001,
                "Voucher valuation "+pages.ME_SET_DIALOG.getVoucherValuationGreedFirstColumnValue()+" is depreciated and not equal to: "+voucherReplacementPrice);
        //Check used price with depr (depr isn't affected on it)
        Assert.assertTrue((Math.abs(pages.ME_SET_DIALOG.getValuationGreedSecondColumnValue("Brugtpris")-usedPrice))<0.0001,
                "Used Price valuation "+pages.ME_SET_DIALOG.getValuationGreedSecondColumnValue("Brugtpris")+" is depreciated and not equal to: "+usedPrice);
        //Check used price
        Assert.assertTrue((Math.abs(pages.ME_SET_DIALOG.getValuationGreedFirstColumnValue("Brugtpris")-usedPrice))<0.0001,
                "Used price valuation "+pages.ME_SET_DIALOG.getValuationGreedFirstColumnValue("Brugtpris")+" is depreciated and not equal to: "+usedPrice);
        //Check customer demand price with depr
        Assert.assertTrue((Math.abs(pages.ME_SET_DIALOG.getValuationGreedSecondColumnValue("Kundens krav")-customerDemandWithDepr))<0.0001,
                "Customer demand valuation "+pages.ME_SET_DIALOG.getValuationGreedSecondColumnValue("Kundens krav")+" is NOT depreciated and not equal to: "+customerDemandWithDepr);
        //Check customer demand
        Assert.assertTrue((Math.abs(pages.ME_SET_DIALOG.getValuationGreedFirstColumnValue("Kundens krav")-customerDemand))<0.0001,
                "Customer demand valuation "+pages.ME_SET_DIALOG.getValuationGreedFirstColumnValue("Kundens krav")+" is depreciated and not equal to: "+customerDemand);
        //Check cash compensation
        Assert.assertTrue((Math.abs(pages.ME_SET_DIALOG.getCashCompensationFieldValue()-cashCompensation))<0.0001,
                "Cash compensation "+pages.ME_SET_DIALOG.getCashCompensationFieldValue()+" is incorrect and not equal to: "+cashCompensation);
        //Check depreciation
        Assert.assertTrue((Math.abs(pages.ME_SET_DIALOG.getDepreciationBelowCashFieldValue()-depreciation))<0.0001,
                "Depreciation value "+pages.ME_SET_DIALOG.getDepreciationBelowCashFieldValue()+" is incorrect and not equal to: "+depreciation);
        pages.ME_SET_DIALOG.closeAddEditSetDialog();
        flows.CLAIM.logout();
    }

    *//**
     * GIVEN: Existing category C1 with existing group G1 and mapped to G1-C1 voucher V1
     * WHEN: User selects C1, G1 and V1 in Settlement dialog
     * AND: User adds new price P1
     * AND: User adds depreciation D1
     * THAN: Cash compensation is P1 - V1 discount - D1
     * AND: Depreciation is D1 amount of Cash Compensation
     * WHEN: User enables Comparison of discount and depreciation in FT
     * AND: User selects C1, G1 and V1 in Settlement dialog
     * AND: User adds new price P1
     * AND: User adds depreciation D1
     * THAN: Cash compensation is P1 - V1 discount
     * AND: Depreciation is 0.00
     *//*
    @Test(dataProvider = "testDataProvider", description = "ECC-3280 Cash compensation is not depreciated if Comparison of discount and depreciation is enabled in FT")
    public void ecc3280_comparisonDDSSImport() {
        pages.LOGIN.loginToME(credentials.getMyAdmin10Login(), credentials.getMyAdmin10Pass());
        flows.CLAIM.createClient(client);
        pages.ME_SETTLEMENT.selectRequestSSOption();
        String password = pages.SS_NOTIF_DIALOG.fillSSFormAndOK(client);
        flows.CLAIM.openSSRequestMailFromSetPage();
        flows.SELF_SERVICE.signInSS(password);

        String description = "Sony";
        Double purchasePrice = OperationalUtils.toNumber(claimItem.getPurchasePriceSP());
        Double newPrice = OperationalUtils.toNumber(claimItem.getNewPriceSP());

        flows.SELF_SERVICE.submitSSLineWithAttachFromSuggestion(description, claimItem.getPurchasePriceSP(), claimItem.getNewPriceSP(), claimItem);
        Driver.get().get(DataProvider.getLinks().getStartPage());
        pages.LOGIN.loginToME(credentials.getMyAdmin10Login(), credentials.getMyAdmin10Pass());
        pages.ME_MY_PAGE.selectLatestClient();
        pages.ME_CUSTOMER_DETAILS.selectReopenClaimOption();
        pages.ME_SETTLEMENT.openItemForEditing(description);
        pages.ME_SET_DIALOG.setDepreciationAmount(claimItem.getDepAmount1());

        Double discount = OperationalUtils.toNumber(voucher.getDiscount());
        Double purchasePriceWithDepr = purchasePrice - OperationalUtils.getValueByPercent(purchasePrice, discount);
        Double newPriceWithDepr = newPrice - OperationalUtils.getValueByPercent(newPrice, discount);
        Double voucherReplacementPrice = newPrice - OperationalUtils.getValueByPercent(newPrice, discount);

        Assert.assertTrue(pages.ME_SET_DIALOG.getSelectedValuation().contains("Gavekort baseret på Nypris"));
        //Check cash compensation and depriciations
        Assert.assertTrue((Math.abs(pages.ME_SET_DIALOG.getCashCompensationFieldValue()-voucherReplacementPrice))<0.0001,
                "Cash compensation value "+pages.ME_SET_DIALOG.getCashCompensationFieldValue()+" is not equal to voucher cash value: "+voucherReplacementPrice);
        Assert.assertTrue((Math.abs(pages.ME_SET_DIALOG.getDepreciationBelowCashFieldValue()-0.0))<0.0001,
                "Depreciation value "+pages.ME_SET_DIALOG.getDepreciationBelowCashFieldValue()+" is not equal to depreciation value: 0.0");
        //Check new price with cash compensation
        Assert.assertTrue((Math.abs(pages.ME_SET_DIALOG.getValuationGreedSecondColumnValue("Nypris")-newPriceWithDepr))<0.0001,
                "New price "+pages.ME_SET_DIALOG.getValuationGreedSecondColumnValue("Nypris")+" is not depreciated and not equal to calculated depreciated newPrice: "+newPriceWithDepr);
        //Check new price
        Assert.assertTrue((Math.abs(pages.ME_SET_DIALOG.getValuationGreedFirstColumnValue("Nypris")-newPrice))<0.0001,
                "New price "+pages.ME_SET_DIALOG.getValuationGreedFirstColumnValue("Nypris")+" is depreciated in the first column and not equal to newPrice: "+newPrice);
        //Check voucher replacement price with cash compensation
        Assert.assertTrue((Math.abs(pages.ME_SET_DIALOG.getVoucherValuationGreedSecondColumnValue()-voucherReplacementPrice))<0.0001,
                "Voucher cash value "+pages.ME_SET_DIALOG.getVoucherValuationGreedSecondColumnValue()+" is depreciated in the second column and not equal to calculated: "+voucherReplacementPrice);
        //Check voucher replacement price
        Assert.assertTrue((Math.abs(pages.ME_SET_DIALOG.getVoucherValuationGreedFirstColumnValue()-voucherReplacementPrice))<0.0001,
                "Voucher cash value "+pages.ME_SET_DIALOG.getVoucherValuationGreedFirstColumnValue()+" is depreciated in the first column and not equal to calculated: "+voucherReplacementPrice);
        //Check claimed amount price with cash compensation
        Assert.assertTrue((Math.abs(pages.ME_SET_DIALOG.getValuationGreedSecondColumnValue("Købspris")-purchasePriceWithDepr))<0.0001,
                "Purchase price value "+pages.ME_SET_DIALOG.getValuationGreedSecondColumnValue("Købspris")+" is not depreciated in the second column and not equal to calculated: "+purchasePriceWithDepr);
        //Check claimed amount price
        Assert.assertTrue((Math.abs(pages.ME_SET_DIALOG.getValuationGreedFirstColumnValue("Købspris")-purchasePrice))<0.0001,
                "Purchase price value "+pages.ME_SET_DIALOG.getValuationGreedFirstColumnValue("Købspris")+" is not correct in the first column and not equal to calculated: "+purchasePrice);
        pages.ME_SET_DIALOG.closeAddEditSetDialog();
        flows.CLAIM.logout();
    }

    *//**
     * GIVEN: Existing category C1 with existing group G1 and mapped to G1-C1 voucher V1
     * WHEN: User selects C1, G1 and V1 in Settlement dialog
     * AND: User adds new price P1
     * AND: User adds depreciation D1
     * THAN: Cash compensation is P1 - V1 discount - D1
     * AND: Depreciation is D1 amount of Cash Compensation
     * WHEN: User enables Comparison of discount and depreciation in FT
     * AND: User selects C1, G1 and V1 in Settlement dialog
     * AND: User adds new price P1
     * AND: User adds depreciation D1
     * THAN: Cash compensation is P1 - V1 discount
     * AND: Depreciation is 0.00
     *//*
    @Test(dataProvider = "testDataProvider", description = "ECC-3280 Cash compensation is not depreciated if Comparison of discount and depreciation is enabled in FT")
    public void ecc3280_comparisonDDManual() {
        pages.LOGIN.loginToME(credentials.getMyAdmin10Login(), credentials.getMyAdmin10Pass());
        meMenu.selectAdminItem();
        flows.ADMIN.disableComparisonDD(insuranceCompany.getSpFTName());
        flows.ADMIN.disableSPDD(insuranceCompany.getSpFTName());
        meAdminMenu.selectMELink();
        flows.CLAIM.createClient(client);
        pages.ME_SETTLEMENT.openAddEditDialog();
        pages.ME_SET_DIALOG.fillCategoriesVoucherPrice(claimItem.getTextFieldSP(), claimItem.getExistingCat1(), claimItem.getExistingSubCat1(), claimItem.getNewPriceSP(), claimItem.getExistingVoucher_10());
        pages.ME_SET_DIALOG.setDepreciationAmount(claimItem.getDepAmount1());
        Double newPrice = OperationalUtils.toNumber(claimItem.getNewPriceSP());
        Double discount = OperationalUtils.toNumber(voucher.getDiscount());
        Double depAmount = OperationalUtils.toNumber(claimItem.getDepAmount1());
        Double cashCompensation = newPrice - OperationalUtils.getValueByPercent(newPrice, discount);
        Double depreciation = OperationalUtils.getValueByPercent(cashCompensation, depAmount);
        cashCompensation = cashCompensation - depreciation;
        Assert.assertTrue(pages.ME_SET_DIALOG.getSelectedValuation().contains("Gavekort baseret på Nypris"));
        Assert.assertTrue((Math.abs(pages.ME_SET_DIALOG.getCashCompensationFieldValue() - cashCompensation)) < 0.0001,
                "Cash compensation value " + pages.ME_SET_DIALOG.getCashCompensationFieldValue() + " is not depreciated and not equal to calculated cash value: " + cashCompensation);
        Assert.assertTrue((Math.abs(pages.ME_SET_DIALOG.getDepreciationBelowCashFieldValue() - depreciation)) < 0.0001,
                "Depreciation  value " + pages.ME_SET_DIALOG.getDepreciationBelowCashFieldValue() + " is not equal to calculated depreciation value: " + depreciation);
        pages.ME_SET_DIALOG.closeAddEditSetDialog();
        pages.ME_SETTLEMENT.selectSaveClaimOption();
        meMenu.selectAdminItem();
        flows.ADMIN.enableComparisonDD(insuranceCompany.getSpFTName());
        meAdminMenu.selectMELink();
        Client client1 = DataProvider.getClient();
        flows.CLAIM.createClient(client1);
        pages.ME_SETTLEMENT.openAddEditDialog();
        pages.ME_SET_DIALOG.fillCategoriesVoucherPrice(claimItem.getTextFieldSP(), claimItem.getExistingCat1(), claimItem.getExistingSubCat1(), claimItem.getNewPriceSP(), claimItem.getExistingVoucher_10());
        pages.ME_SET_DIALOG.setDepreciationAmount(claimItem.getDepAmount1());
        cashCompensation = newPrice - OperationalUtils.getValueByPercent(newPrice, discount);
        Assert.assertTrue(pages.ME_SET_DIALOG.getSelectedValuation().contains("Gavekort baseret på Nypris"));
        Assert.assertTrue((Math.abs(pages.ME_SET_DIALOG.getCashCompensationFieldValue()-cashCompensation))<0.0001,
                "Cash compensation value "+pages.ME_SET_DIALOG.getCashCompensationFieldValue()+" is depreciated and not equal to calculated cash value: "+cashCompensation);
        Assert.assertTrue((Math.abs(pages.ME_SET_DIALOG.getDepreciationBelowCashFieldValue()-0.0))<0.0001,
                "Depreciation  value "+pages.ME_SET_DIALOG.getDepreciationBelowCashFieldValue()+" is not equal to 0.0");
        pages.ME_SET_DIALOG.closeAddEditSetDialog();
        flows.CLAIM.logout();
    }

    *//**
     * GIVEN: Comparasing of Discount and Depreciation FT enabled
     * AND: Combine  Discount and Depreciation FT disabled
     * AND: Claim Handler (CH) created claim (C2)
     * AND: CH add item manually, in Category C1 with voucher V1- voucher discount (VD1)
     * AND: CH enters NewPrice(NP)
     * AND: Depreciation value 0<D1<VD1
     * THAN: V1 valuation selected by default
     * AND: 0% displays in Depreciation column on the Settlement
     *//*

    @Test(dataProvider = "testDataProvider", description = "ECC-3280 Cash compensation is not depreciated if Comparison of discount and depreciation is enabled in FT")
    public void ecc3280_verifyDefaultSelectedValuationAndDepreciationValueOnSettlementPage() {
        pages.LOGIN.loginToME(credentials.getMyAdmin10Login(), credentials.getMyAdmin10Pass());
        flows.CLAIM.createClient(client);
        pages.ME_SETTLEMENT.openAddEditDialog();
        Double newPrice = OperationalUtils.toNumber(claimItem.getNewPriceSP());
        Double depAmount = OperationalUtils.toNumber(claimItem.getDepAmount2());
        Double depreciation = OperationalUtils.getValueByPercent(newPrice, depAmount);
        pages.ME_SET_DIALOG.fillCategoriesVoucherPrice(claimItem.getTextFieldSP(), claimItem.getExistingCat1(), claimItem.getExistingSubCat1(), claimItem.getNewPriceSP(), claimItem.getExistingVoucher_10());
        pages.ME_SET_DIALOG.setDepreciationAmount(claimItem.getDepAmount1());
        Assert.assertTrue(pages.ME_SET_DIALOG.isVoucherRepButtonSelected(), "Voucher valuation is NOT selected because discounted value is NOT lower that depreciated");
        Assert.assertTrue((Math.abs(pages.ME_SET_DIALOG.getDepreciationBelowCashFieldValue()-0.0))<0.0001,
                "Depreciation value "+pages.ME_SET_DIALOG.getDepreciationBelowCashFieldValue()+" is NOT 0.0 for discounted valuation - voucher");
        pages.ME_SET_DIALOG.setDepreciationAmount(claimItem.getDepAmount2());
        Assert.assertTrue(pages.ME_SET_DIALOG.isNewPriceButtonSelected(), "Depreciated new price is NOT selected because it's NOT lower");
        Assert.assertTrue((Math.abs(pages.ME_SET_DIALOG.getDepreciationBelowCashFieldValue()-depreciation))<0.0001,
                "Depreciation value "+pages.ME_SET_DIALOG.getDepreciationBelowCashFieldValue()+" is NOT correct for NOT discounted valuation and not equal to expected: "+depreciation);
        pages.ME_SET_DIALOG.closeAddEditSetDialog();
        flows.CLAIM.logout();
    }

    *//**
     * GIVEN: FT "Display voucher value with depreciation deducted" OFF
     * WHEN: ClaimHandler(CH) created claim
     * AND: Add manual line in Category (C1) with voucher (V1) with discount(VD1) assigned based on New Price (NP)
     * AND: Depreciation D1 >0%
     * WHEN: CH add line(CL1) manually
     * THEN: Face value = New Price, Cash Value = New Price - VD1%
     * WHEN: CH add CL1 to Settlement
     * THEN: Face value = New Price
     * WHEN: CH complete claim through Replacement Wizard
     * THEN: Face value = New Price, Cash Value = New Price - VD1%
     * WHEN: User (U1) get Customer Welcome email
     * THEN: Face value = New Price, Cash Value = New Price - VD1%
     * WHEN: User (U1) login to the shop
     * THAN: Face value = New Price, Cash Value = New Price - VD1%
     * WHEN: CH review completed claim details
     * THAN: Face value = New Price, Cash Value = New Price - VD1%
     *//*
    @Test(dataProvider = "testDataProvider", description = "ECC-3288 Display voucher value with depreciation deducted (off)")
    public void ecc3288_displayVVWithDDOff() {
        pages.LOGIN.loginToME(credentials.getMyAdmin10Login(), credentials.getMyAdmin10Pass());
        meMenu.selectAdminItem();
        flows.ADMIN.disableComparisonDD(insuranceCompany.getSpFTName());
        flows.ADMIN.disableDisplayVVWithDD(insuranceCompany.getSpFTName());
        meAdminMenu.selectMELink();
        flows.CLAIM.createClient(client);
        pages.ME_SETTLEMENT.openAddEditDialog();
        pages.ME_SET_DIALOG.fillCategoriesVoucherPrice(claimItem.getTextFieldSP(), claimItem.getExistingCat1(), claimItem.getExistingSubCat1(), claimItem.getNewPriceSP(), claimItem.getExistingVoucher_10());
        pages.ME_SET_DIALOG.setDepreciationAmount(claimItem.getDepAmount1());
        Double discount = OperationalUtils.doubleString(voucher.getDiscount());
        Double newPrice = OperationalUtils.doubleString(claimItem.getNewPriceSP());
        Double calculatedVoucherPrice = OperationalUtils.getCashValueRD(newPrice, discount);
        Double faceValue = pages.ME_SET_DIALOG.getVoucherFaceValueFieldText();
        Double cashValue = pages.ME_SET_DIALOG.getVoucherCashValueFieldText();
        Assert.assertTrue((Math.abs(faceValue-newPrice)<0.0001),
                "Voucher face value "+faceValue+"should be equal to new Price "+newPrice);
        Assert.assertTrue((Math.abs(cashValue-calculatedVoucherPrice)<0.0001),
                "Voucher cash value "+cashValue+"should be equal to calculated "+calculatedVoucherPrice);
        pages.ME_SET_DIALOG.saveAndCloseAddEditSetDialog();
        Assert.assertEquals(OperationalUtils.toNumber(pages.ME_SETTLEMENT.getFaceTooltipValue()), OperationalUtils.toNumber(claimItem.getNewPriceSP()));
        flows.CLAIM.completeWithMailAndLoginToShop(client, mail, credentials);
        Assert.assertTrue((Math.abs(pages.SHOP_WELCOME.getProductCashValue()-calculatedVoucherPrice)<0.0001),
                "Voucher cash value "+pages.SHOP_WELCOME.getProductCashValue()+"should be equal to calculated "+calculatedVoucherPrice);
        Assert.assertTrue((Math.abs(pages.SHOP_WELCOME.getProductFaceValue()-newPrice)<0.0001),
                "Voucher face value "+pages.SHOP_WELCOME.getProductFaceValue()+"should be equal to entered new Price "+newPrice);
        shopMenu.logoutWithoutRedirectToME();
        Driver.get().get(DataProvider.getLinks().getStartPage());
        pages.LOGIN.loginToME(credentials.getMyAdmin10Login(), credentials.getMyAdmin10Pass());
        pages.ME_MY_PAGE.selectLatestClient();
        pages.ME_CUSTOMER_DETAILS.showHideClaimInfo();
        Assert.assertTrue((Math.abs(pages.ME_CUSTOMER_DETAILS.getCashValue()-calculatedVoucherPrice)<0.0001),
                "Voucher cash value "+pages.ME_CUSTOMER_DETAILS.getCashValue()+"should be equal to calculated "+calculatedVoucherPrice);
        Assert.assertTrue((Math.abs(pages.ME_CUSTOMER_DETAILS.getFaceValue()-newPrice)<0.0001),
                "Voucher face value "+pages.ME_CUSTOMER_DETAILS.getFaceValue()+"should be equal to new Price "+newPrice);
        pages.ME_CUSTOMER_DETAILS.selectReopenClaimOptionWithAlert();
        flows.CLAIM.selectCompleteClaimAndFillCustInfo(client);
        pages.ME_BASE_INFO.openReplacementDialog();
        Assert.assertTrue((Math.abs(pages.ME_REP_WIZARD.getVoucherFaceValue()-newPrice)<0.0001),
                "Voucher face value "+pages.ME_REP_WIZARD.getVoucherFaceValue()+"should be equal to new Price "+newPrice);
        Assert.assertTrue((Math.abs(pages.ME_REP_WIZARD.getItemPriceValue()-calculatedVoucherPrice)<0.0001),
                "Voucher cash value "+pages.ME_REP_WIZARD.getItemPriceValue()+"should be equal to calculated "+calculatedVoucherPrice);
        pages.ME_REP_WIZARD.closeRepWizardDialog();
        flows.CLAIM.logout();
    }

    *//**
     * GIVEN: FT "Display voucher value with depreciation deducted" ON
     * WHEN: ClaimHandler(CH) created claim
     * AND: Add manual line in Category (C1) with voucher (V1) with discount(VD1) assigned based on New Price (NP)
     * AND: Depreciation D1 >0%
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
    @Test(dataProvider = "testDataProvider", description = "ECC-3288 Display voucher value with depreciation deducted (on)")
    public void ecc3288_displayVVWithDDOn() {
        pages.LOGIN.loginToME(credentials.getMyAdmin10Login(), credentials.getMyAdmin10Pass());
        meMenu.selectAdminItem();
        flows.ADMIN.disableComparisonDD(insuranceCompany.getSpFTName());
        flows.ADMIN.enableDisplayVVWithDD(insuranceCompany.getSpFTName());
        meAdminMenu.selectMELink();
        flows.CLAIM.createClient(client);
        pages.ME_SETTLEMENT.openAddEditDialog();
        pages.ME_SET_DIALOG.fillCategoriesVoucherPrice(claimItem.getTextFieldSP(), claimItem.getExistingCat1(), claimItem.getExistingSubCat1(), claimItem.getNewPriceSP(), claimItem.getExistingVoucher_10());
        pages.ME_SET_DIALOG.setDepreciationAmount(claimItem.getDepAmount1());

        Double discount = OperationalUtils.doubleString(voucher.getDiscount());
        Double newPrice = OperationalUtils.doubleString(claimItem.getNewPriceSP());
        Double calculatedVoucherPrice = OperationalUtils.getCashValueRD(newPrice, discount);
        Double newPriceWithoutDepr = OperationalUtils.getCashValueRD(newPrice, OperationalUtils.doubleString(claimItem.getDepAmount1()));
        Double calculatedVoucherPriceWithoutDepr = OperationalUtils.getCashValueRD(calculatedVoucherPrice, OperationalUtils.doubleString(claimItem.getDepAmount1()));
        Double faceValue = pages.ME_SET_DIALOG.getVoucherFaceValueFieldText();
        Double cashValue = pages.ME_SET_DIALOG.getVoucherCashValueFieldText();

        Assert.assertTrue((Math.abs(faceValue-newPriceWithoutDepr)<0.0001),
                "Voucher face value "+faceValue+"should be equal to depreciated new Price "+newPriceWithoutDepr);
        Assert.assertTrue((Math.abs(cashValue-calculatedVoucherPriceWithoutDepr)<0.0001),
                "Voucher cash value "+cashValue+"should be equal to depreciated voucher cash value "+calculatedVoucherPriceWithoutDepr);
        pages.ME_SET_DIALOG.saveAndCloseAddEditSetDialog();
        Assert.assertTrue((Math.abs(OperationalUtils.toNumber(pages.ME_SETTLEMENT.getFaceTooltipValue()) - newPriceWithoutDepr) < 0.0001),
                "Tooltip face value " + OperationalUtils.toNumber(pages.ME_SETTLEMENT.getFaceTooltipValue()) + "should be equal to depreciated new price " + newPriceWithoutDepr);
        flows.CLAIM.completeWithMailAndLoginToShop(client, mail, credentials);
        Assert.assertTrue((Math.abs(pages.SHOP_WELCOME.getProductCashValue()-calculatedVoucherPriceWithoutDepr)<0.0001),
                "Voucher cash value "+pages.SHOP_WELCOME.getProductCashValue()+"should be equal to depreciated voucher cash value "+calculatedVoucherPriceWithoutDepr);
        Assert.assertTrue((Math.abs(pages.SHOP_WELCOME.getProductFaceValue()-newPriceWithoutDepr)<0.0001),
                "Voucher face value "+pages.SHOP_WELCOME.getProductFaceValue()+"should be equal to depreciated new price "+newPriceWithoutDepr);
        shopMenu.logoutWithoutRedirectToME();
        Driver.get().get(DataProvider.getLinks().getStartPage());
        pages.LOGIN.loginToME(credentials.getMyAdmin10Login(), credentials.getMyAdmin10Pass());
        pages.ME_MY_PAGE.selectLatestClient();
        pages.ME_CUSTOMER_DETAILS.showHideClaimInfo();
        Assert.assertTrue((Math.abs(pages.ME_CUSTOMER_DETAILS.getCashValue()-calculatedVoucherPriceWithoutDepr)<0.0001),
                "Voucher cash value "+pages.ME_CUSTOMER_DETAILS.getCashValue()+"should be equal to depreciated voucher cash value "+calculatedVoucherPriceWithoutDepr);
        Assert.assertTrue((Math.abs(pages.ME_CUSTOMER_DETAILS.getFaceValue()-newPriceWithoutDepr)<0.0001),
                "Voucher face value "+pages.ME_CUSTOMER_DETAILS.getFaceValue()+"should be equal to depreciated new price "+newPriceWithoutDepr);
        pages.ME_CUSTOMER_DETAILS.selectReopenClaimOptionWithAlert();
        flows.CLAIM.selectCompleteClaimAndFillCustInfo(client);
        pages.ME_BASE_INFO.openReplacementDialog();
        Assert.assertTrue((Math.abs(pages.ME_REP_WIZARD.getVoucherFaceValue()-newPriceWithoutDepr)<0.0001),
                "Voucher face value "+pages.ME_REP_WIZARD.getVoucherFaceValue()+"should be equal to depreciated new price "+newPriceWithoutDepr);
        Assert.assertTrue((Math.abs(pages.ME_REP_WIZARD.getItemPriceValue()-calculatedVoucherPriceWithoutDepr)<0.0001),
                "Voucher cash value "+pages.ME_REP_WIZARD.getItemPriceValue()+"should be equal to depreciated voucher cash value "+calculatedVoucherPriceWithoutDepr);
        pages.ME_REP_WIZARD.closeRepWizardDialog();
        flows.CLAIM.logout();
    }

    *//**
     * GIVEN: FT "Display voucher value with depreciation deducted" ON
     * WHEN: ClaimHandler(CH) created claim
     * AND:  "Compare discount and depreciation" ON
     * AND:"Compare discount and depreciation" ON
     * AND:"Combine discount and depreciation" OFF
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

    @Test(dataProvider = "testDataProvider", description = "ECC-3288 Display voucher value with depreciation deducted (off)")
    public void ecc3288_verifyDandD2AndFTRealationCombineDDOff() {
        pages.LOGIN.loginToME(credentials.getMyAdmin10Login(), credentials.getMyAdmin10Pass());
        flows.CLAIM.createClient(client);
        pages.ME_SETTLEMENT.openAddEditDialog();
        pages.ME_SET_DIALOG.disablecombineDD();
        pages.ME_SET_DIALOG.fillCategoriesVoucherPrice(claimItem.getTextFieldSP(), claimItem.getExistingCat1(), claimItem.getExistingSubCat1(), claimItem.getNewPriceSP(), claimItem.getExistingVoucher_10());
        pages.ME_SET_DIALOG.setDepreciationAmount(claimItem.getDepAmount1());
        Double discount = OperationalUtils.doubleString(voucher.getDiscount());
        Double newPrice = OperationalUtils.doubleString(claimItem.getNewPriceSP());
        Double calculatedVoucherPrice = OperationalUtils.getCashValueRD(newPrice, discount);
        Double faceValue = pages.ME_SET_DIALOG.getVoucherFaceValueFieldText();
        Double cashValue = pages.ME_SET_DIALOG.getVoucherCashValueFieldText();
        Assert.assertTrue((Math.abs(faceValue-newPrice)<0.0001),
                "Voucher face value "+faceValue+"should be equal to not depreciated new Price "+newPrice);
        Assert.assertTrue((Math.abs(cashValue-calculatedVoucherPrice)<0.0001),
                "Voucher cash value "+cashValue+"should be equal to not depreciated voucher cash value "+calculatedVoucherPrice);
        pages.ME_SET_DIALOG.saveAndCloseAddEditSetDialog();
        Assert.assertTrue((Math.abs(OperationalUtils.toNumber(pages.ME_SETTLEMENT.getFaceTooltipValue())-newPrice)<0.0001),
                "Tooltip face value "+OperationalUtils.toNumber(pages.ME_SETTLEMENT.getFaceTooltipValue())+"should be equal to not  depreciated new price "+newPrice);
        flows.CLAIM.completeWithMailAndLoginToShop(client, mail, credentials);
        Assert.assertTrue((Math.abs(pages.SHOP_WELCOME.getProductCashValue()-calculatedVoucherPrice)<0.0001),
                "Voucher cash value "+pages.SHOP_WELCOME.getProductCashValue()+"should be equal to not depreciated voucher cash value "+calculatedVoucherPrice);
        Assert.assertTrue((Math.abs(pages.SHOP_WELCOME.getProductFaceValue() - newPrice) < 0.0001),
                "Voucher face value " + pages.SHOP_WELCOME.getProductFaceValue() + "should be equal to not depreciated new price " + newPrice);
        shopMenu.logoutWithoutRedirectToME();
        Driver.get().get(DataProvider.getLinks().getStartPage());
        pages.LOGIN.loginToME(credentials.getMyAdmin10Login(), credentials.getMyAdmin10Pass());
        pages.ME_MY_PAGE.selectLatestClient();
        pages.ME_CUSTOMER_DETAILS.showHideClaimInfo();
        Assert.assertTrue((Math.abs(pages.ME_CUSTOMER_DETAILS.getCashValue()-calculatedVoucherPrice)<0.0001),
                "Voucher cash value "+pages.ME_CUSTOMER_DETAILS.getCashValue()+"should be equal to not depreciated voucher cash value "+calculatedVoucherPrice);
        Assert.assertTrue((Math.abs(pages.ME_CUSTOMER_DETAILS.getFaceValue()-newPrice)<0.0001),
                "Voucher face value "+pages.ME_CUSTOMER_DETAILS.getFaceValue()+"should be equal to not depreciated new price "+newPrice);
        pages.ME_CUSTOMER_DETAILS.selectReopenClaimOptionWithAlert();
        flows.CLAIM.selectCompleteClaimAndFillCustInfo(client);
        pages.ME_BASE_INFO.openReplacementDialog();
        Assert.assertTrue((Math.abs(pages.ME_REP_WIZARD.getVoucherFaceValue()-newPrice)<0.0001),
                "Voucher face value "+pages.ME_REP_WIZARD.getVoucherFaceValue()+"should be equal to not depreciated new price "+newPrice);
        Assert.assertTrue((Math.abs(pages.ME_REP_WIZARD.getItemPriceValue()-calculatedVoucherPrice)<0.0001),
                "Voucher cash value "+pages.ME_REP_WIZARD.getItemPriceValue()+"should be equal to not depreciated voucher cash value "+calculatedVoucherPrice);
        pages.ME_REP_WIZARD.closeRepWizardDialog();
        flows.CLAIM.logout();
    }

    *//**
     * GIVEN: FT "Display voucher value with depreciation deducted" ON
     * WHEN: ClaimHandler(CH) created claim
     * AND: Add manual line in Category (C1) with voucher (V1) with discount(VD1) assigned based on New Price (NP)
     * AND: Depreciation D1 >0%
     * AND:  "Compare discount and depreciation" ON
     * AND:"Combine discount and depreciation" ON
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
    @Test(dataProvider = "testDataProvider", description = "ECC-3288 Display voucher value with depreciation deducted (on)")
    public void ecc3288_3281_verifyDandD2AndFTRealationCombineDDOn() {
        pages.LOGIN.loginToME(credentials.getMyAdmin10Login(), credentials.getMyAdmin10Pass());
        meMenu.selectAdminItem();
        flows.ADMIN.enableDisplayVVWithDD(insuranceCompany.getSpFTName());
        meAdminMenu.selectMELink();
        flows.CLAIM.createClient(client);
        pages.ME_SETTLEMENT.openAddEditDialog();
        pages.ME_SET_DIALOG.enablecombineDD();
        pages.ME_SET_DIALOG.fillCategoriesVoucherPrice(claimItem.getTextFieldSP(), claimItem.getExistingCat1(), claimItem.getExistingSubCat1(), claimItem.getNewPriceSP(), claimItem.getExistingVoucher_10());
        pages.ME_SET_DIALOG.setDepreciationAmount(claimItem.getDepAmount1());
        Double discount = OperationalUtils.doubleString(voucher.getDiscount());
        Double newPrice = OperationalUtils.doubleString(claimItem.getNewPriceSP());
        Double calculatedVoucherPrice = OperationalUtils.getCashValueRD(newPrice, discount);
        Double newPriceWithoutDepr = OperationalUtils.getCashValueRD(newPrice, OperationalUtils.doubleString(claimItem.getDepAmount1()));
        Double calculatedVoucherPriceWithoutDepr = OperationalUtils.getCashValueRD(calculatedVoucherPrice, OperationalUtils.doubleString(claimItem.getDepAmount1()));
        Double faceValue = pages.ME_SET_DIALOG.getVoucherFaceValueFieldText();
        Double cashValue = pages.ME_SET_DIALOG.getVoucherCashValueFieldText();
        Assert.assertTrue((Math.abs(faceValue-newPriceWithoutDepr)<0.0001),
                "Voucher face value "+faceValue+" should be equal to depreciated new Price "+newPriceWithoutDepr);
        Assert.assertTrue((Math.abs(cashValue-calculatedVoucherPriceWithoutDepr)<0.0001),
                "Voucher cash value "+cashValue+" should be equal to depreciated voucher cash value "+calculatedVoucherPriceWithoutDepr);
        pages.ME_SET_DIALOG.saveAndCloseAddEditSetDialog();
        Assert.assertTrue((Math.abs(OperationalUtils.toNumber(pages.ME_SETTLEMENT.getFaceTooltipValue())-newPriceWithoutDepr)<0.0001),
                "Tooltip face value "+OperationalUtils.toNumber(pages.ME_SETTLEMENT.getFaceTooltipValue())+"should be equal to depreciated new price "+newPriceWithoutDepr);
        flows.CLAIM.completeWithMailAndLoginToShop(client, mail, credentials);
        Assert.assertTrue((Math.abs(pages.SHOP_WELCOME.getProductCashValue()-calculatedVoucherPriceWithoutDepr)<0.0001),
                "Voucher cash value "+pages.SHOP_WELCOME.getProductCashValue()+" should be equal to depreciated voucher cash value "+calculatedVoucherPriceWithoutDepr);
        Assert.assertTrue((Math.abs(pages.SHOP_WELCOME.getProductFaceValue()-newPriceWithoutDepr)<0.0001),
                "Voucher face value "+pages.SHOP_WELCOME.getProductFaceValue()+" should be equal to depreciated new price "+newPriceWithoutDepr);        shopMenu.logoutWithoutRedirectToME();
        Driver.get().get(DataProvider.getLinks().getStartPage());
        pages.LOGIN.loginToME(credentials.getMyAdmin10Login(), credentials.getMyAdmin10Pass());
        pages.ME_MY_PAGE.selectLatestClient();
        pages.ME_CUSTOMER_DETAILS.showHideClaimInfo();
        Assert.assertTrue((Math.abs(pages.ME_CUSTOMER_DETAILS.getCashValue()-calculatedVoucherPriceWithoutDepr)<0.0001),
                "Voucher cash value "+pages.ME_CUSTOMER_DETAILS.getCashValue()+"should be equal to depreciated voucher cash value "+calculatedVoucherPriceWithoutDepr);
        Assert.assertTrue((Math.abs(pages.ME_CUSTOMER_DETAILS.getFaceValue()-newPriceWithoutDepr)<0.0001),
                "Voucher face value "+pages.ME_CUSTOMER_DETAILS.getFaceValue()+"should be equal to depreciated new price "+newPriceWithoutDepr);
        pages.ME_CUSTOMER_DETAILS.selectReopenClaimOptionWithAlert();
        flows.CLAIM.selectCompleteClaimAndFillCustInfo(client);
        pages.ME_BASE_INFO.openReplacementDialog();
        Assert.assertTrue((Math.abs(pages.ME_REP_WIZARD.getVoucherFaceValue()-newPriceWithoutDepr)<0.0001),
                "Voucher face value "+pages.ME_REP_WIZARD.getVoucherFaceValue()+"should be equal to depreciated new price "+newPriceWithoutDepr);
        Assert.assertTrue((Math.abs(pages.ME_REP_WIZARD.getItemPriceValue()-calculatedVoucherPriceWithoutDepr)<0.0001),
                "Voucher cash value "+pages.ME_REP_WIZARD.getItemPriceValue()+"should be equal to depreciated voucher cash value "+calculatedVoucherPriceWithoutDepr);
        pages.ME_REP_WIZARD.closeRepWizardDialog();
        flows.CLAIM.logout();
    }*/
}
