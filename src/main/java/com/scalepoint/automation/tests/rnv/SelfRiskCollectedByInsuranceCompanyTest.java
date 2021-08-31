package com.scalepoint.automation.tests.rnv;

import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.utils.NumberFormatUtils;
import com.scalepoint.automation.utils.RandomUtils;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.TestDataActions;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.entity.input.ServiceAgreement;
import com.scalepoint.automation.utils.data.entity.input.Translations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class SelfRiskCollectedByInsuranceCompanyTest extends RnVBase {

    private static final String SELR_RISK_LOWER_THAN_REPAIR_PRICE_DATA_PROVIDER = "selfRiskLowerThanRepairPriceDataProvider";
    private static final String SELF_RISK_EQUAL_TO_REPAIR_PRICE_DATA_PROVIDER = "selfRiskEqualToRepairPriceDataProvider";
    private static final String SELF_RISK_HIGHER_THAN_REPAIR_PRICE_DATA_PROVIDER = "selfRiskHigherThanRepairPriceDataProvider";

    @BeforeMethod
    public void toSettlementPage(Object[] objects) {

        List parameters = Arrays.asList(objects);

        User user = getObjectByClass(parameters, User.class).get(0);

        setSelfRiskCollectedByInsuranceCompany(user);
    }

    @RequiredSetting(type = FTSetting.ENABLE_AUTOMATIC_RV_INVOICE_PAYMENT)
    @RequiredSetting(type = FTSetting.DEFAULT_AUTOMATIC_INVOICE_PAYMENTS, value = "Insurance company")
    @Test(groups = {TestGroups.RNV, TestGroups.RNV_SMOKE}, dataProvider = SELR_RISK_LOWER_THAN_REPAIR_PRICE_DATA_PROVIDER,
            description = "RnV1. SendLine to RnV, send Service Partner feedback")
    public void selfRiskLowerThanRepairPriceTest(User user, Claim claim, ServiceAgreement agreement,
                                                 Translations translations, String lineDescription,
                                                 BigDecimal selfRiskByServicePartner, BigDecimal repairPrice,
                                                 BigDecimal selfRisk) {

        final BigDecimal selfRiskTakenByInsuranceCompany = repairPrice.subtract(selfRisk);

        sendRnVAndFeedbackWithTakenSelfRisk(user, claim, agreement, translations, lineDescription,
                selfRisk.setScale(0).toString(), repairPrice, selfRiskByServicePartner);

        SettlementPage settlementPage = verifyPanelView(agreement.getFeedbackReceivedStatus(), ZERO)
                .clickEvaluateAssignment()
                .acceptFeedback()
                .toSettlementPage();

        verifyRepairPanel(settlementPage,
                repairPrice,
                ZERO,
                selfRisk,
                ZERO,
                ZERO,
                selfRiskTakenByInsuranceCompany);

        settlementPage = settlementPage
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail(claim, databaseApi, true)
                .openRecentClaim()
                .startReopenClaimWhenViewModeIsEnabled()
                .reopenClaim();

        verifyRepairPanel(settlementPage,
                repairPrice,
                ZERO,
                selfRisk,
                ZERO,
                selfRiskTakenByInsuranceCompany,
                ZERO);
    }

    @RequiredSetting(type = FTSetting.ENABLE_AUTOMATIC_RV_INVOICE_PAYMENT)
    @RequiredSetting(type = FTSetting.DEFAULT_AUTOMATIC_INVOICE_PAYMENTS, value = "Insurance company")
    @Test(groups = {TestGroups.RNV, TestGroups.RNV_SMOKE}, dataProvider = SELF_RISK_EQUAL_TO_REPAIR_PRICE_DATA_PROVIDER,
            description = "RnV1. SendLine to RnV, send Service Partner feedback")
    public void selfRiskEqualToRepairPriceTest(User user, Claim claim, ServiceAgreement agreement,
                                               Translations translations, String lineDescription,
                                               BigDecimal selfRiskByServicePartner, BigDecimal repairPrice,
                                               BigDecimal selfRisk) {

        final BigDecimal selfRiskTakenByInsuranceCompany = repairPrice.subtract(selfRisk);

        sendRnVAndFeedbackWithTakenSelfRisk(user, claim, agreement, translations, lineDescription,
                selfRisk.setScale(0).toString(), repairPrice, selfRiskByServicePartner);

        SettlementPage settlementPage = verifyPanelView(agreement.getFeedbackReceivedStatus(), ZERO)
                .clickEvaluateAssignment()
                .acceptFeedback()
                .toSettlementPage();

        verifyRepairPanel(settlementPage,
                repairPrice,
                ZERO,
                selfRiskTakenByInsuranceCompany,
                ZERO,
                ZERO,
                repairPrice);

        settlementPage = settlementPage
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail(claim, databaseApi, true)
                .openRecentClaim()
                .startReopenClaimWhenViewModeIsEnabled()
                .reopenClaim();

        verifyRepairPanel(settlementPage,
                repairPrice,
                ZERO,
                ZERO,
                ZERO,
                selfRisk,
                ZERO);
    }

    @RequiredSetting(type = FTSetting.ENABLE_AUTOMATIC_RV_INVOICE_PAYMENT)
    @RequiredSetting(type = FTSetting.DEFAULT_AUTOMATIC_INVOICE_PAYMENTS, value = "Insurance company")
    @Test(groups = {TestGroups.RNV, TestGroups.RNV_SMOKE}, dataProvider = SELF_RISK_HIGHER_THAN_REPAIR_PRICE_DATA_PROVIDER,
            description = "RnV1. SendLine to RnV, send Service Partner feedback")
    public void selfRiskHigherThanRepairPriceTest(User user, Claim claim, ServiceAgreement agreement,
                                                  Translations translations, String lineDescription,
                                                  BigDecimal selfRiskByServicePartner, BigDecimal repairPrice,
                                                  BigDecimal selfRisk) {

        sendRnVAndFeedbackWithTakenSelfRisk(user, claim, agreement, translations, lineDescription,
                selfRisk.setScale(0).toString(), repairPrice, selfRiskByServicePartner);

        SettlementPage settlementPage = verifyPanelView(agreement.getFeedbackReceivedStatus(), ZERO)
                .clickEvaluateAssignment()
                .acceptFeedback()
                .toSettlementPage();

        verifyRepairPanel(settlementPage,
                repairPrice,
                ZERO,
                ZERO,
                ZERO,
                ZERO,
                repairPrice);

        settlementPage = settlementPage
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail(claim, databaseApi, true)
                .openRecentClaim()
                .startReopenClaimWhenViewModeIsEnabled()
                .reopenClaim();

        verifyRepairPanel(settlementPage,
                repairPrice,
                ZERO,
                ZERO,
                ZERO,
                repairPrice,
                ZERO);
    }

    @DataProvider(name = SELR_RISK_LOWER_THAN_REPAIR_PRICE_DATA_PROVIDER)
    public static Object[][] selfRiskLowerThanRepairPriceDataProvider(Method method) {

        String lineDescription = RandomUtils.randomName(RV_LINE_DESCRIPTION);
        BigDecimal selfRiskByServicePartner = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(10.00);
        BigDecimal repairPrice = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(30.00);
        BigDecimal selfRisk = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(15.00);

        return addNewParameters(TestDataActions.getTestDataParameters(method), lineDescription,
                selfRiskByServicePartner, repairPrice, selfRisk);
    }

    @DataProvider(name = SELF_RISK_EQUAL_TO_REPAIR_PRICE_DATA_PROVIDER)
    public static Object[][] selfRiskEqualToRepairPriceDataProvider(Method method) {

        String lineDescription = RandomUtils.randomName(RV_LINE_DESCRIPTION);
        BigDecimal selfRiskByServicePartner = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(10.00);
        BigDecimal repairPrice = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(15.00);
        BigDecimal selfRisk = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(15.00);

        return addNewParameters(TestDataActions.getTestDataParameters(method), lineDescription, selfRiskByServicePartner,
                repairPrice, selfRisk);
    }

    @DataProvider(name = SELF_RISK_HIGHER_THAN_REPAIR_PRICE_DATA_PROVIDER)
    public static Object[][] selfRiskHigherThanRepairPriceDataProvider(Method method) {

        String lineDescription = RandomUtils.randomName(RV_LINE_DESCRIPTION);
        BigDecimal selfRiskByServicePartner = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(10.00);
        BigDecimal repairPrice = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(15.00);
        BigDecimal selfRisk = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(20.00);

        return addNewParameters(TestDataActions.getTestDataParameters(method), lineDescription, selfRiskByServicePartner,
                repairPrice, selfRisk);
    }
}

