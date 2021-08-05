package com.scalepoint.automation.tests.api.unifiedpayments.v3;

import com.scalepoint.automation.services.restService.*;
import com.scalepoint.automation.tests.api.BaseApiTest;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.settled.EventClaimSettled;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import com.scalepoint.automation.utils.data.request.InsertSettlementItem;
import com.scalepoint.automation.utils.data.request.Valuation;
import lombok.Builder;
import lombok.Data;

import static com.scalepoint.automation.services.restService.SettlementClaimService.CloseCaseReason.CLOSE_EXTERNAL;
import static com.scalepoint.automation.services.restService.common.BaseService.loginAndOpenClaimWithItems;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.testng.Assert.assertTrue;


public class BaseUnifiedPaymentsApiTest extends BaseApiTest {

    //    ClaimRequest claimRequest;
    SettlementClaimService settlementClaimService;
    EccSettlement eccSettlementSummaryService;
    ClaimSettlementItemsService claimSettlementItemsService;


    public enum PartyReference {
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

        public static PartyReference getByValue(int value) {
            for (PartyReference partyReference : PartyReference.values()) {
                if (value == partyReference.getValue()) {
                    return partyReference;
                }
            }
            return null;
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


    static void initClaimRequest(ClaimRequest claimRequest) {
        claimRequest.setTenant("topdanmark");
        claimRequest.setCompany("topdanmark");
        claimRequest.setCheckForFraud(false);
    }


    void createClaim(User user, ClaimRequest claimRequest,  int selfRisk, int manualReduction, InsertSettlementItem... items) {
        createClaimWithItems(user, claimRequest, items);

        if (selfRisk > 0)
            setSelfRisk(selfRisk);
        if (manualReduction > 0)
            setManualReduction(manualReduction);
    }

    void setSelfRisk(int selfRisk) {
        new OwnRiskService().setSelfRiskForClaim(selfRisk + "");
    }

    void setManualReduction(int manualReduction) {
        new ManualReductionService().setManualReductionForClaim(manualReduction + "");
    }

    SettlementClaimService closeExternally(ClaimRequest claimRequest) {
        return settlementClaimService.close(claimRequest, CLOSE_EXTERNAL);
    }

    void validateJsonSchema(EventClaimSettled event) {
        assertTrue(matchesJsonSchemaInClasspath("schema/case_settled.schema.json").matches(event.getJsonString()));
    }

    void reopenClaim() {
        new ReopenClaimService().reopenClaim();
    }

    EventClaimSettled getEventClaimSettled(ClaimRequest claimRequest) {
        return eventDatabaseApi.getEventClaimSettled(claimRequest);
    }

    EventClaimSettled getSecondEventClaimSettled(ClaimRequest claimRequest) {
        return eventDatabaseApi.getEventClaimSettled(claimRequest, 1);
    }

    EventClaimSettled getThirdEventClaimSettled(ClaimRequest claimRequest) {
        return eventDatabaseApi.getEventClaimSettled(claimRequest, 2);
    }

    EventClaimSettled getFourthEventClaimSettled(ClaimRequest claimRequest) {
        return eventDatabaseApi.getEventClaimSettled(claimRequest, 3);
    }

    SettlementClaimService close(ClaimRequest claimRequest, SettlementClaimService.CloseCaseReason closeCaseReason) {
        return settlementClaimService.close(claimRequest, closeCaseReason);
    }

    void assertThatCloseCaseEventWasCreated(ClaimRequest claimRequest) {
        eventDatabaseApi.assertThatCloseCaseEventWasCreated(claimRequest);
    }

    void assertThatSecondCloseCaseEventWasCreated(ClaimRequest claimRequest) {
        eventDatabaseApi.assertThatCloseCaseEventWasCreated(claimRequest, 1);
    }

    void assertThatThirdCloseCaseEventWasCreated(ClaimRequest claimRequest) {
        eventDatabaseApi.assertThatCloseCaseEventWasCreated(claimRequest, 2);
    }

    void assertThatFourthCloseCaseEventWasCreated(ClaimRequest claimRequest) {
        eventDatabaseApi.assertThatCloseCaseEventWasCreated(claimRequest, 3);
    }

    SettlementClaimService createClaimWithItems(User user,ClaimRequest claimRequest,  InsertSettlementItem... items) {
        settlementClaimService =
                loginAndOpenClaimWithItems(user, claimRequest, items).closeCase();
        eccSettlementSummaryService = new EccSettlement()
                .getSummaryTotals();
        claimSettlementItemsService = new ClaimSettlementItemsService();
        return settlementClaimService;
    }


    static void setPrice(InsertSettlementItem item, int price, int depreciation) {
        item.getSettlementItem().setPostDepreciation(depreciation + "");
        for (Valuation valuation : item.getSettlementItem().getValuations().getValuation()) {
            if ("NEW_PRICE".equals(valuation.getValuationType())) {
                valuation.setActive("true");
                valuation.getPrice()[0].setAmount(price + "");
            } else {
                valuation.setActive("false");
            }
        }
    }

    @Data
    @Builder
    static class CreateClaimInput{

        private int selfRisk;
        private int manualReduction;
    }
}
