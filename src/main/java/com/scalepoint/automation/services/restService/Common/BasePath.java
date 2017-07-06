package com.scalepoint.automation.services.restService.Common;


public interface BasePath {

    String UNIFIED_INTEGRATION = "/Integration/UnifiedIntegration";
    String OPEN_CLAIM = "Integration/Open";
    String INSERT_SETTLEMENT = "/{userId}/InsertSettlementItem";
    String SAVE_CLAIM = "/{userId}/SaveUserInfo";
    String CLOSE_EXTERNAL = "/{userId}/CloseCaseExternal";
    String CLOSE_WITH_MAIL = "/{userId}/SettleCustomer";
    String REPLACEMENT = "{userId}/PostReplacedFromME";
    String CASE_GET_REVISION = "case/{tenant}/revision/{revisionToken}";

}
