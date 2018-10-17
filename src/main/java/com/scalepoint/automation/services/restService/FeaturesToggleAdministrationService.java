package com.scalepoint.automation.services.restService;

import com.scalepoint.automation.services.restService.Common.BaseService;
import com.scalepoint.automation.utils.Configuration;

import static io.restassured.RestAssured.*;


public class FeaturesToggleAdministrationService extends BaseService {

    private final static String URL = Configuration.getEccAdminUrl()+"/ff4j-console/features";
    //http://qa14.scalepoint.com/webapp/ScalePoint/dk/ff4j-console/features?op=enable&uid=NEW_SETTLE_WITHOUT_MAIL_BUTTON

    public FeaturesToggleAdministrationService turnTheFeatureOn(String featureId){
        given().param("op", "enable").
                param("uid", featureId).
                when().
                get(URL).
                then().
                statusCode(200).
                log().all();
        return this;
    }

    private enum FeatureIds{
        DB_BLOCKING,
        EVOUCHER_BACKEND_SYNCHRONOUS_CALLS,
        NAVISION_MANDATORY_BANK_ACCOUNT_NUMBER,
        NAVISION_MANDATORY_BANK_NAME,
        NAVISION_MANDATORY_BANK_REG_NUMBER,
        NEW_SETTLE_WITHOUT_MAIL_BUTTON,
        SCALEPOINT_HANDLES_VOUCHER_AVAILABLE,
        SHOW_VOUCHER_RIGHT_OF_CANCELLATION,
        USE_ADDRESS2_IN_ORDER_XML_EMAIL,
        USE_BING_MAPS_FOR_DISTANCES,
        USE_CELL_PHONE_IN_ORDER_XML_EMAIL,
        USE_EXTENDED_PRODUCT_VALIDATION,
        USE_IP1_ENCODING_FIX,
        USE_NEW_CATEGORY_MASTER_MESSAGE,
        USE_REDEEM_INPUT_VALIDATION,
        USE_USERS_CACHE,
        XFEEDS_IMPORT_DISABLED
    }

}
