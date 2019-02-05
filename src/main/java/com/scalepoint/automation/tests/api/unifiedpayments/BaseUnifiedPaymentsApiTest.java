package com.scalepoint.automation.tests.api.unifiedpayments;

import com.scalepoint.automation.services.restService.*;
import com.scalepoint.automation.tests.api.BaseApiTest;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.settled.EventClaimSettled;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import com.scalepoint.automation.utils.data.request.InsertSettlementItem;
import com.scalepoint.automation.utils.data.request.Valuation;


import static com.scalepoint.automation.services.restService.Common.BaseService.loginAndOpenClaimWithItems;
import static com.scalepoint.automation.services.restService.SettlementClaimService.CloseCaseReason.CLOSE_EXTERNAL;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.testng.Assert.assertTrue;


public class BaseUnifiedPaymentsApiTest extends BaseApiTest {

    ClaimRequest claimRequest;
    SettlementClaimService settlementClaimService;
    EccSettlementSummaryService eccSettlementSummaryService;
    ClaimSettlementItemsService claimSettlementItemsService;



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

        setSelfRisk(selfRisk);
        setManualReduction(manualReduction);
    }

    void setSelfRisk(int selfRisk) {
        new OwnRiskService().setSelfRiskForClaim(selfRisk + "");
    }

    void setManualReduction(int manualReduction) {
        new ManualReductionService().setManualReductionForClaim(manualReduction + "");
    }

    SettlementClaimService closeExternally() {
        return settlementClaimService.close(claimRequest, CLOSE_EXTERNAL);
    }

    void validateJsonSchema(EventClaimSettled event) {
        assertTrue(matchesJsonSchemaInClasspath("schema/case_settled.schema.json").matches(event.getJsonString()));
    }

    void reopenClaim() {
        new ReopenClaimService().reopenClaim();
    }

    EventClaimSettled getEventClaimSettled() {
        return eventDatabaseApi.getEventClaimSettled(claimRequest);
    }

    EventClaimSettled getSecondEventClaimSettled() {
        return eventDatabaseApi.getEventClaimSettled(claimRequest, 1);
    }

    EventClaimSettled getThirdEventClaimSettled() {
        return eventDatabaseApi.getEventClaimSettled(claimRequest, 2);
    }

    EventClaimSettled getFourthEventClaimSettled() {
        return eventDatabaseApi.getEventClaimSettled(claimRequest, 3);
    }

    SettlementClaimService close(SettlementClaimService.CloseCaseReason closeCaseReason) {
        return settlementClaimService.close(claimRequest, closeCaseReason);
    }

    void assertThatCloseCaseEventWasCreated() {
        eventDatabaseApi.assertThatCloseCaseEventWasCreated(claimRequest);
    }

    void assertThatSecondCloseCaseEventWasCreated() {
        eventDatabaseApi.assertThatCloseCaseEventWasCreated(claimRequest, 1);
    }

    void assertThatThirdCloseCaseEventWasCreated() {
        eventDatabaseApi.assertThatCloseCaseEventWasCreated(claimRequest, 2);
    }

    void assertThatFourthCloseCaseEventWasCreated() {
        eventDatabaseApi.assertThatCloseCaseEventWasCreated(claimRequest, 3);
    }

    SettlementClaimService createClaimWithItems(User user, InsertSettlementItem... items){
        settlementClaimService =
                loginAndOpenClaimWithItems(user, claimRequest, items).closeCase();
        eccSettlementSummaryService = new EccSettlementSummaryService()
                .getSummaryTotals();
        claimSettlementItemsService = new ClaimSettlementItemsService();
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


}
