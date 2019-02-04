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

        new OwnRiskService().setSelfRiskForClaim(selfRisk + "");
        new ManualReductionService().setManualReductionForClaim(manualReduction + "");
    }

    SettlementClaimService closeExternally() {
        return settlementClaimService.close(claimRequest, CLOSE_EXTERNAL);
    }

    void validateAgainstSchema(EventClaimSettled event) {
        assertTrue(matchesJsonSchemaInClasspath("schema/case_settled.schema.json").matches(event.getJsonString()));
    }

    void reopenClaim() {
        new ReopenClaimService().reopenClaim();
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
