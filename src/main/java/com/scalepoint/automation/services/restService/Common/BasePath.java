package com.scalepoint.automation.services.restService.Common;


public interface BasePath {

    String UNIFIED_INTEGRATION = "/Integration/UnifiedIntegration";
    String OPEN_CLAIM = "Integration/Open";
    String CREATE_CLAIM = "Integration/CreateClaim";
    String CREATE_AND_OPEN_CLAIM = "Integration/CreateAndOpen";
    String INSERT_SETTLEMENT_ITEM = "/{userId}/InsertSettlementItem";
    String UPDATE_SETTLEMENT_ITEM = "/{userId}/UpdateSettlementItem";
    String REMOVE_SETTLEMENT_ITEM = "/{userId}/rest/settlement/removeSelected.json";
    String SAVE_CLAIM = "/{userId}/SaveUserInfo";
    String CLOSE_EXTERNAL = "/{userId}/CloseCaseExternal";
    String CLOSE_WITH_MAIL = "/{userId}/SettleCustomer?sendMailAndSms=true";
    String CLOSE_WITHOUT_MAIL = "/{userId}/SettleCustomer?sendMailAndSms=false";
    String REPLACEMENT = "{userId}/PostReplacedFromME";
    String CASE_GET_REVISION = "case/{tenant}/revision/{revisionToken}";
    String CANCEL_CLAIM = "/CancelClaim";
    String SETTLEMENT = "/{userId}/webshop/jsp/matching_engine/settlement.jsp";
    String OWN_RISK = "/{userId}/rest/settlement/ownRisk";
    String MANUAL_REDUCTION = "/{userId}/rest/settlement/manualReduction";
    String FRAUD_STATUS = "/{userId}/rest/settlement/fraudStatus.json";
    String REOPEN = "ReopenCustomer";
    String SETTLEMENT_TOTALS = "/{userId}/rest/settlement/settlementSummaryTotals.json";
    String SELF_SERVICE_REQUEST = "/{userId}/rest/settlement/selfServiceRequest.json";
    String SELF_SERVICE_LOGIN = "/self-service/dk/login";
    String EXCEL = "/{userId}/rest/lossImport/excel";
    String SELF_SERVICE_LOSS_ITEMS = "/self-service/dk/lossItems/";
    String SELF_SERVICE_SUBMITTED = "/self-service/dk/submitted";
}
