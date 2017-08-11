package com.scalepoint.automation.services.restService.Common;


public interface BasePath {

    String UNIFIED_INTEGRATION = "/Integration/UnifiedIntegration";
    String OPEN_CLAIM = "Integration/Open";
    String CREATE_CLAIM = "Integration/CreateClaim";
    String CREATE_AND_OPEN_CLAIM = "Integration/CreateAndOpen";
    String INSERT_SETTLEMENT = "/{userId}/InsertSettlementItem";
    String SAVE_CLAIM = "/{userId}/SaveUserInfo";
    String CLOSE_EXTERNAL = "/{userId}/CloseCaseExternal";
    String CLOSE_WITH_MAIL = "/{userId}/SettleCustomer";
    String REPLACEMENT = "{userId}/PostReplacedFromME";
    String CASE_GET_REVISION = "case/{tenant}/revision/{revisionToken}";
    String CANCEL_CLAIM = "/CancelClaim";
    String SETTLEMENT = "/{userId}/webshop/jsp/matching_engine/settlement.jsp";
    String OWN_RISK = "/{userId}/rest/settlement/ownRisk";
    String REOPEN = "ReopenCustomer";
    String SETTLEMENT_TOTALS = "/{userId}/rest/settlement/settlementSummaryTotals.json";
}
