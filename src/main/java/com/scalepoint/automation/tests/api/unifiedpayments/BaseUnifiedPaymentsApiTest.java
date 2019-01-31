package com.scalepoint.automation.tests.api.unifiedpayments;

import com.scalepoint.automation.services.restService.EccSettlementSummaryService;
import com.scalepoint.automation.services.restService.ManualReductionService;
import com.scalepoint.automation.services.restService.OwnRiskService;
import com.scalepoint.automation.services.restService.SettlementClaimService;
import com.scalepoint.automation.tests.api.BaseApiTest;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.settled.Expense;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.settled.Obligation;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.settled.PartyRef;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.settled.Payment;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import com.scalepoint.automation.utils.data.request.InsertSettlementItem;
import com.scalepoint.automation.utils.data.request.Valuation;

import static com.scalepoint.automation.services.restService.Common.BaseService.loginAndOpenClaimWithItems;
import static java.util.Arrays.asList;
import static org.testng.Assert.assertEquals;

public class BaseUnifiedPaymentsApiTest extends BaseApiTest {

    ClaimRequest claimRequest;
    SettlementClaimService settlementClaimService;
    EccSettlementSummaryService eccSettlementSummaryService;



    enum PartyReference {
        SCALEPOINT(0),
        INSURANCE_COMPANY(1),
        CLAIMANT(2),
        SERVICE_PARTNER(3);

        private int value;
        PartyReference(int s) {
            value = s;
        }
        public int getValue() {
            return value;
        }
    }


    enum ExpenseType {
        INVOICE("invoice"),
        CREDIT_NOTE("creditNote"),
        REPAIR_INVOICE("repairInvoice"),
        ASSESSMENT_INVOICE("assessmentInvoice"),
        REJECTION_INVOICE("rejectionInvoice"),
        CASH_COMPENSATION("cashCompensation");

        private String value;
        ExpenseType(String s) {
            value = s;
        }
        public String getValue() {
            return value;
        }
    }

    enum ObligationType {
        DEDUCTIBLE("deductible"),
        DEPRECIATION("depreciation"),
        MANUAL_REDUCTION("manualReduction"),
        COMPENSATION("compensation"),
        CREDIT_NOTE("creditNote");

        private String value;
        ObligationType(String s) {
            value = s;
        }
        public String getValue() {
            return value;
        }
    }


    void createClaim(User user, int selfRisk, int manualReduction, InsertSettlementItem... items) {
        createClaimWithItems(user, items);

        new OwnRiskService().setSelfRiskForClaim(selfRisk + "");
        new ManualReductionService().setManualReductionForClaim(manualReduction + "");
    }


    SettlementClaimService createClaimWithItems(User user, InsertSettlementItem... items){
        settlementClaimService =
                loginAndOpenClaimWithItems(user, claimRequest, asList(items)).closeCase();
        eccSettlementSummaryService = new EccSettlementSummaryService()
                .getSummaryTotals();
        return settlementClaimService;
    }


    void setPrice(InsertSettlementItem item, int price, int depreciation) {
        item.getSettlementItem().setPostDepreciation(depreciation + "");
        for (Valuation valuation : item.getSettlementItem().getValuations().getValuation()) {
            if ("NEW_PRICE".equals(valuation.getValuationType())) {
                valuation.setActive("true");
                valuation.getPrice()[0].setAmount(price + "");
            }
            else{
                valuation.setActive("false");
            }
        }
    }



    void assertObligation(Obligation actualObligation, ObligationType expectedObligationType, double expectedTotal, PartyReference expectedPayerParty, PartyReference expectedPayeeParty){
        assertEquals(actualObligation.getObligationType(), expectedObligationType.getValue());
        assertEquals(actualObligation.getTotal(), expectedTotal);
        assertPartyRef(actualObligation.getPayerParty(), expectedPayerParty);
        assertPartyRef(actualObligation.getPayeeParty(), expectedPayeeParty);
    }

    void assertExpense(Expense actualExpense, ExpenseType expectedExpenseType, double expectedTotal, PartyReference expectedPayerParty, PartyReference expectedPayeeParty){
        assertEquals(actualExpense.getExpenseType(), expectedExpenseType.getValue());
        assertEquals(actualExpense.getTotal(), expectedTotal);
        assertPartyRef(actualExpense.getPayerParty(), expectedPayerParty);
        assertPartyRef(actualExpense.getPayeeParty(), expectedPayeeParty);
    }

    void assertPayment(Payment actualPayment, double expectedTotal, PartyReference expectedPayerParty, PartyReference expectedPayeeParty){
        assertEquals(actualPayment.getTotal(), expectedTotal);
        assertPartyRef(actualPayment.getPayerParty(), expectedPayerParty);
        assertPartyRef(actualPayment.getPayeeParty(), expectedPayeeParty);
    }

    void assertPartyRef(PartyRef partyRef, PartyReference partyNum){
        assertEquals(partyRef.get$ref(), "/parties/" + partyNum.getValue());
    }

}
