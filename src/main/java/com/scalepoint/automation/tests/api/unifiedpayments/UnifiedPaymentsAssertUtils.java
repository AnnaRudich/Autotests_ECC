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


    public static void assertObligation(List<Obligation> obligations, BaseUnifiedPaymentsApiTest.ObligationType expectedObligationType, double expectedTotal, BaseUnifiedPaymentsApiTest.PartyReference expectedPayerParty, BaseUnifiedPaymentsApiTest.PartyReference expectedPayeeParty){
        assertTrue(obligations.stream().anyMatch(o ->
                o.getObligationType().equals(expectedObligationType.getValue())
                        && o.getTotal().equals(expectedTotal)
                        && isEqual(o.getPayeeParty(), expectedPayeeParty)
                        && isEqual(o.getPayerParty(), expectedPayerParty)
        ));
    }

    public static void assertPayments(List<Payment> actualPayments, Object[][] expectedPayments){
        assertEquals(actualPayments.size(), expectedPayments.length);

        Arrays.stream(expectedPayments).forEach(p ->
                assertPayment(actualPayments, (Double)p[0], (BaseUnifiedPaymentsApiTest.PartyReference)p[1], (BaseUnifiedPaymentsApiTest.PartyReference)p[2])
        );

    }

    public static void assertPayment(List<Payment> payments, double expectedTotal, BaseUnifiedPaymentsApiTest.PartyReference expectedPayerParty, BaseUnifiedPaymentsApiTest.PartyReference expectedPayeeParty){
        assertTrue(payments.stream().anyMatch(p ->
                p.getTotal().equals(expectedTotal)
                        && isEqual(p.getPayeeParty(), expectedPayeeParty)
                        && isEqual(p.getPayerParty(), expectedPayerParty)
        ));
    }

    public static void assertExpenses(List<Expense> actualExpenses, Object[][] expectedExpenses){
        assertEquals(actualExpenses.size(), expectedExpenses.length);

        Arrays.stream(expectedExpenses).forEach(o ->
                assertExpense(actualExpenses, (BaseUnifiedPaymentsApiTest.ExpenseType)o[0], (Double)o[1], (BaseUnifiedPaymentsApiTest.PartyReference)o[2], (BaseUnifiedPaymentsApiTest.PartyReference)o[3])
        );

    }

    public static void assertExpense(List<Expense> expenses, BaseUnifiedPaymentsApiTest.ExpenseType expectedExpenseType, double expectedTotal, BaseUnifiedPaymentsApiTest.PartyReference expectedPayerParty, BaseUnifiedPaymentsApiTest.PartyReference expectedPayeeParty){
        assertTrue(expenses.stream().anyMatch(e ->
                e.getExpenseType().equals(expectedExpenseType.getValue())
                        && e.getTotal().equals(expectedTotal)
                        && isEqual(e.getPayeeParty(), expectedPayeeParty)
                        && isEqual(e.getPayerParty(), expectedPayerParty)
        ));
    }

    public static void assertSummary(EventClaimSettled event, double expectedDeductible, double expectedShareOfVat, double expectedDepreciation, double expectedManualReduction) {
        final Summary summary = event.getSettlement().getSummary();
        assertEquals(summary.getDeductible(), expectedDeductible);
        assertEquals(summary.getShareOfVat(), expectedShareOfVat);
        assertEquals(summary.getDepreciation(), expectedDepreciation);
        assertEquals(summary.getManualReduction(), expectedManualReduction);
    }



    private static boolean isEqual(PartyRef partyRef, BaseUnifiedPaymentsApiTest.PartyReference partyNum){
        return partyRef.get$ref().equals("/parties/" + partyNum.getValue());
    }
}
