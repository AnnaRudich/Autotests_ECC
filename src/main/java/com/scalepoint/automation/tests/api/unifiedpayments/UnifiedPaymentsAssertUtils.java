package com.scalepoint.automation.tests.api.unifiedpayments;

import com.scalepoint.automation.utils.data.entity.eventsApiEntity.settled.*;

import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class UnifiedPaymentsAssertUtils {

    public static void assertObligations(List<Obligation> actualObligations, Object[][] expectedObligations){
        assertEquals(actualObligations.size(), expectedObligations.length);

        Arrays.stream(expectedObligations).forEach(o ->
                assertObligation(actualObligations, (BaseUnifiedPaymentsApiTest.ObligationType)o[0], (Double)o[1], (BaseUnifiedPaymentsApiTest.PartyReference)o[2], (BaseUnifiedPaymentsApiTest.PartyReference)o[3])
        );

    }


    public static void assertObligation(Obligation actualObligation, BaseUnifiedPaymentsApiTest.ObligationType expectedObligationType, double expectedTotal, BaseUnifiedPaymentsApiTest.PartyReference expectedPayerParty, BaseUnifiedPaymentsApiTest.PartyReference expectedPayeeParty){
        assertEquals(actualObligation.getObligationType(), expectedObligationType.getValue());
        assertEquals(actualObligation.getTotal(), expectedTotal);
        assertPartyRef(actualObligation.getPayerParty(), expectedPayerParty);
        assertPartyRef(actualObligation.getPayeeParty(), expectedPayeeParty);
    }

    public static void assertObligation(List<Obligation> obligations, BaseUnifiedPaymentsApiTest.ObligationType expectedObligationType, double expectedTotal, BaseUnifiedPaymentsApiTest.PartyReference expectedPayerParty, BaseUnifiedPaymentsApiTest.PartyReference expectedPayeeParty){
        assertTrue(obligations.stream().anyMatch(o ->
                o.getObligationType().equals(expectedObligationType.getValue())
                        && o.getTotal().equals(expectedTotal)
                        && isEqual(o.getPayeeParty(), expectedPayeeParty)
                        && isEqual(o.getPayerParty(), expectedPayerParty)
        ));
    }

    public static void assertSummary(EventClaimSettled event, double expectedDeductible, double expectedShareOfVat, double expectedDepreciation, double expectedManualReduction) {
        final Summary summary = event.getSettlement().getSummary();
        assertEquals(summary.getDeductible(), expectedDeductible);
        assertEquals(summary.getShareOfVat(), expectedShareOfVat);
        assertEquals(summary.getDepreciation(), expectedDepreciation);
        assertEquals(summary.getManualReduction(), expectedManualReduction);
    }


    public static void assertExpense(Expense actualExpense, BaseUnifiedPaymentsApiTest.ExpenseType expectedExpenseType, double expectedTotal, BaseUnifiedPaymentsApiTest.PartyReference expectedPayerParty, BaseUnifiedPaymentsApiTest.PartyReference expectedPayeeParty){
        assertEquals(actualExpense.getExpenseType(), expectedExpenseType.getValue());
        assertEquals(actualExpense.getTotal(), expectedTotal);
        assertPartyRef(actualExpense.getPayerParty(), expectedPayerParty);
        assertPartyRef(actualExpense.getPayeeParty(), expectedPayeeParty);
    }

    public static void assertPayment(Payment actualPayment, double expectedTotal, BaseUnifiedPaymentsApiTest.PartyReference expectedPayerParty, BaseUnifiedPaymentsApiTest.PartyReference expectedPayeeParty){
        assertEquals(actualPayment.getTotal(), expectedTotal);
        assertPartyRef(actualPayment.getPayerParty(), expectedPayerParty);
        assertPartyRef(actualPayment.getPayeeParty(), expectedPayeeParty);
    }

    public static void assertPartyRef(PartyRef partyRef, BaseUnifiedPaymentsApiTest.PartyReference partyNum){
        assertEquals(partyRef.get$ref(), "/parties/" + partyNum.getValue());
    }

    private static boolean isEqual(PartyRef partyRef, BaseUnifiedPaymentsApiTest.PartyReference partyNum){
        return partyRef.get$ref().equals("/parties/" + partyNum.getValue());
    }
}
