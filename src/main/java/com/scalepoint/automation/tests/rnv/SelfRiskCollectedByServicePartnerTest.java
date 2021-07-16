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

public class SelfRiskCollectedByServicePartnerTest extends RnVBase {

    private static final String SELF_RISK_LOWER_THAN_TOTAL_AMOUNT_OF_COMPENSATION_DATA_PROVIDER = "selfRiskLowerThanTotalAmountOfCompensationDataProvider";
    private static final String SELF_RISK_EQUAL_TO_TOTAL_AMOUNT_OF_COMPENSATION_DATA_PROVIDER = "selfRiskEqualToTotalAmountOfCompensationDataProvider";
    private static final String SELF_RISK_HIGHER_THAN_TOTAL_AMOUNT_OF_COMPENSATION_DATA_PROVIDER = "selfRiskHigherThanTotalAmountOfCompensationDataProvider";

    @BeforeMethod
    public void toSettlementPage(Object[] objects) {

        List parameters = Arrays.asList(objects);

        User user = getObjectByClass(parameters, User.class).get(0);

        setSelfRiskCollectedByServicePartner(user);
    }

    @RequiredSetting(type = FTSetting.ENABLE_AUTOMATIC_RV_INVOICE_PAYMENT)
    @RequiredSetting(type = FTSetting.DEFAULT_AUTOMATIC_INVOICE_PAYMENTS, value = "Insurance company")
    @Test(groups = {TestGroups.RNV, TestGroups.RNV_SMOKE},
            dataProvider = SELF_RISK_LOWER_THAN_TOTAL_AMOUNT_OF_COMPENSATION_DATA_PROVIDER,
            description = "RnV1. SendLine to RnV, send Service Partner feedback")
    public void selfRiskLowerThanTotalAmountOfCompensationTest(User user, Claim claim, ServiceAgreement agreement,
                                                               Translations translations, String lineDescription,
                                                               BigDecimal selfRiskByServicePartner, BigDecimal repairPrice,
                                                               BigDecimal selfRisk) {

        final BigDecimal selfRiskTakenByInsuranceCompany = repairPrice.subtract(selfRiskByServicePartner).subtract(selfRisk);

        sendRnVAndFeedbackWithTakenSelfRisk(user, claim, agreement, translations, lineDescription,
                selfRisk.setScale(0).toString(), repairPrice, selfRiskByServicePartner);

        SettlementPage settlementPage = verifyPanelView(agreement.getFeedbackReceivedStatus(), selfRiskByServicePartner)
                .clickEvaluateAssignment()
                .acceptFeedback()
                .toSettlementPage();

        verifyRepairPanel(settlementPage,
                repairPrice,
                selfRiskByServicePartner,
                selfRisk,
                ZERO,
                ZERO,
                selfRiskTakenByInsuranceCompany);

        settlementPage = settlementPage
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail(claim, databaseApi, true)
                .openRecentClaim()
                .reopenClaim();

        verifyRepairPanel(settlementPage,
                repairPrice,
                selfRiskByServicePartner,
                selfRisk,
                ZERO,
                selfRiskTakenByInsuranceCompany,
                ZERO);
    }

    @RequiredSetting(type = FTSetting.ENABLE_AUTOMATIC_RV_INVOICE_PAYMENT)
    @RequiredSetting(type = FTSetting.DEFAULT_AUTOMATIC_INVOICE_PAYMENTS, value = "Insurance company")
    @Test(groups = {TestGroups.RNV, TestGroups.RNV_SMOKE},
            dataProvider = SELF_RISK_EQUAL_TO_TOTAL_AMOUNT_OF_COMPENSATION_DATA_PROVIDER,
            description = "RnV1. SendLine to RnV, send Service Partner feedback")
    public void selfRiskEqualToTotalAmountOfCompensationTest(User user, Claim claim, ServiceAgreement agreement,
                                                             Translations translations, String lineDescription,
                                                             BigDecimal selfRiskByServicePartner, BigDecimal repairPrice,
                                                             BigDecimal selfRisk) {

        final BigDecimal selfRiskTakenByInsuranceCompany = repairPrice.subtract(selfRiskByServicePartner).subtract(selfRisk);

        sendRnVAndFeedbackWithTakenSelfRisk(user, claim, agreement, translations, lineDescription,
                selfRisk.setScale(0).toString(), repairPrice, selfRiskByServicePartner);

        SettlementPage settlementPage = verifyPanelView(agreement.getFeedbackReceivedStatus(), selfRiskByServicePartner)
                .clickEvaluateAssignment()
                .acceptFeedback()
                .toSettlementPage();

        verifyRepairPanel(settlementPage,
                repairPrice,
                selfRiskByServicePartner,
                selfRisk,
                ZERO,
                ZERO,
                selfRiskTakenByInsuranceCompany);

        settlementPage = settlementPage
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail(claim, databaseApi, true)
                .openRecentClaim()
                .reopenClaim();

        verifyRepairPanel(settlementPage,
                repairPrice,
                selfRiskByServicePartner,
                selfRisk,
                ZERO,
                selfRiskTakenByInsuranceCompany,
                ZERO);
    }

    @RequiredSetting(type = FTSetting.ENABLE_AUTOMATIC_RV_INVOICE_PAYMENT)
    @RequiredSetting(type = FTSetting.DEFAULT_AUTOMATIC_INVOICE_PAYMENTS, value = "Insurance company")
    @Test(groups = {TestGroups.RNV, TestGroups.RNV_SMOKE},
            dataProvider = SELF_RISK_HIGHER_THAN_TOTAL_AMOUNT_OF_COMPENSATION_DATA_PROVIDER,
            description = "RnV1. SendLine to RnV, send Service Partner feedback", enabled = false)
    public void selfRiskHigherThanTotalAmountOfCompensationTest(User user, Claim claim, ServiceAgreement agreement,
                                                                Translations translations, String lineDescription,
                                                                BigDecimal selfRiskByServicePartner, BigDecimal repairPrice,
                                                                BigDecimal selfRisk) {

        sendRnVAndFeedbackWithTakenSelfRisk(user, claim, agreement, translations, lineDescription,
                selfRisk.toString(), repairPrice, selfRiskByServicePartner)
                .doAssert(rnvService -> rnvService.assertTakenSelfRiskNotWithinAllowedRange());

        verifyPanelView(agreement.getWaitingStatus(), ZERO);
    }

    @DataProvider(name = SELF_RISK_LOWER_THAN_TOTAL_AMOUNT_OF_COMPENSATION_DATA_PROVIDER)
    public static Object[][] selfRiskLowerThanTotalAmountOfCompensationDataProvider(Method method) {

        String lineDescription = RandomUtils.randomName(RV_LINE_DESCRIPTION);
        BigDecimal selfRiskByServicePartner = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(10.00);
        BigDecimal repairPrice = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(30.00);
        BigDecimal selfRisk = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(15.00);

        return addNewParameters(TestDataActions.getTestDataParameters(method), lineDescription, selfRiskByServicePartner,
                repairPrice, selfRisk);
    }

    @DataProvider(name = SELF_RISK_EQUAL_TO_TOTAL_AMOUNT_OF_COMPENSATION_DATA_PROVIDER)
    public static Object[][] selfRiskEqualToTotalAmountOfCompensationDataProvider(Method method) {

        String lineDescription = RandomUtils.randomName(RV_LINE_DESCRIPTION);
        BigDecimal selfRiskByServicePartner = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(15.00);
        BigDecimal repairPrice = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(30.00);
        BigDecimal selfRisk = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(15.00);

        return addNewParameters(TestDataActions.getTestDataParameters(method), lineDescription, selfRiskByServicePartner,
                repairPrice, selfRisk);
    }

    @DataProvider(name = SELF_RISK_HIGHER_THAN_TOTAL_AMOUNT_OF_COMPENSATION_DATA_PROVIDER)
    public static Object[][] selfRiskHigherThanTotalAmountOfCompensationDataProvider(Method method) {

        String lineDescription = RandomUtils.randomName(RV_LINE_DESCRIPTION);
        BigDecimal selfRiskByServicePartner = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(20.00);
        BigDecimal repairPrice = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(30.00);
        BigDecimal selfRisk = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(15.00);

        return addNewParameters(TestDataActions.getTestDataParameters(method), lineDescription, selfRiskByServicePartner,
                repairPrice, selfRisk);
    }
}

