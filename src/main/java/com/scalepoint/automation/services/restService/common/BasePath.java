package com.scalepoint.automation.services.restService.common;


public interface BasePath {

    String UNIFIED_INTEGRATION = "/Integration/UnifiedIntegration";
    String OPEN_CLAIM = "Integration/Open";
    String CREATE_CLAIM = "Integration/CreateClaim";
    String CREATE_AND_OPEN_CLAIM = "Integration/CreateAndOpen";
    String INSERT_SETTLEMENT_ITEM = "/{userId}/InsertSettlementItem";
    String UPDATE_SETTLEMENT_ITEM = "/{userId}/UpdateSettlementItem";
    String REMOVE_SETTLEMENT_ITEM = "/{userId}/rest/settlement/removeSelected.json";
    String SAVE_CLAIM = "/{userId}/SaveUserInfo";
    String SAVE_CUSTOMER = "/{userId}/SaveCustomer";
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
    String CUSTOMER_MAIL_CONTENT = "/{userId}/rest/customer-mail/content/{emailToken}";
    String SELF_SERVICE_LOGIN = "/self-service/dk/login";
    String SELF_SERVICE_RELOAD_FT= "/rest/selfservicereload/functiontemplate";
    String EXCEL = "/{userId}/rest/lossImport/excel";
    String MATCH = "/{userId}/rest/lossImport/match";
    String SELF_SERVICE_LOSS_ITEMS = "/self-service/dk/lossItems/";
    String SELF_SERVICE_INIT_DATA = "/self-service/dk/initdata";
    String SELF_SERVICE_DELETE_LOSS_ITEMS = "/self-service/dk/lossItems/{Id}";
    String SELF_SERVICE_SUBMITTED = "/self-service/dk/submitted";
    String SELF_SERVICE_SAVED = "/self-service/dk/saved";
    String SELF_SERVICE_CASE_WIDGET = "/self-service/dk/api/case/{caseToken}";
    String SELF_SERVICE_FILES_UPLOAD = "/self-service/dk/lossItemAttachments/filesUpload";
    String SELF_SERVICE_LOSS_ITEM_ATTACHMENTS = "/self-service/dk/lossItemAttachments";
    String SELF_SERVICE_LOSS = "/self-service/dk/loss/";
    String ATTACHMENTS = "/{userId}/rest/settlement/attachments";
    String TEXT_SEARCH = "/{userId}/search/TextSearch";
    String SECURED_IMAGE = "/rest/settlement/resource/securedimage";
    String SID_STATISTICS = "/{userId}/webshop/jsp/matching_engine/dialog/get_settlement_item_dialog_statics.jsp";
    String SID_ITEM = "/{userId}/webshop/jsp/matching_engine/dialog/get_settlement_item_dialog_item.jsp";
    String CHECK_SELF_SERVICE_RESPONSES = "/{userId}/rest/settlement/checkSelfServiceResponses.json";
    String AUDIT_REQUEST_DATA = "/{userId}/rest/settlement/getAuditRequestData.json";
    String CLAIM_LINES = "/{userId}/rest/settlement/getClaimLines.json";
    String SEND_TO_REPAIR_AND_VALUATION = "/{userId}/rest/settlement/sendToRepairAndValuation.json";
    String ORDER_PREPARE_ACTION = "orderPrepare.action";
    String ORDER_PREPARE = "orderPrepare.json";
    String CLAIMANT = "claimant.json";
    String CLAIM = "claim.json";
    String ORDER_STATUS = "orderStatus.json";
    String ORDER = "order.json";
    String ORDER_ACTION = "order.action";
    String TASKS_STATUSES = "tasksStatuses.json";
    String HEALTCH_CHECK = "/health/status";
    String CUSTOMER_MAIL_LIST = "/rest/customer-mail/list/{userId}";
}
