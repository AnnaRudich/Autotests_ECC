package com.scalepoint.automation.services.restService;

import com.scalepoint.automation.services.restService.Common.BaseService;
import com.scalepoint.automation.services.usersmanagement.UsersManager;
import com.scalepoint.automation.utils.Configuration;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.sessionId;
import static javafx.scene.input.KeyCode.SLASH;
import static org.assertj.core.api.Assertions.assertThat;


public class FeaturesToggleAdministrationService extends BaseService {

    private final static String toggleUrl_1 = Configuration.getEccAdminUrl()+"/ff4j-console/features";
    //http://qa14.scalepoint.com/webapp/ScalePoint/dk/ff4j-console/features?op=enable&uid=NEW_SETTLE_WITHOUT_MAIL_BUTTON

    private final static String toggleUrl_2 = Configuration.getEccAdminUrl()+"/ff4j-console/api/features";
    //http://qa09.scalepoint.com/webapp/ScalePoint/dk/ff4j-console/api/features/EVOUCHER_BACKEND_SYNCHRONOUS_CALLS

    public FeaturesToggleAdministrationService updateToggle(String expectedState, String featureId){

        String sessionId = loginUser(UsersManager.getSystemUser()).getResponse().getSessionId();

        given().param("op", expectedState).
                param("uid", featureId).
                sessionId(sessionId).
                when().
                get(toggleUrl_1).
                then().
                statusCode(200).
                log().all();
        return this;
    }

    //{"uid":"EVOUCHER_BACKEND_SYNCHRONOUS_CALLS","enable":true,
    // "description":"Setting to make evoucher issues/redeem sync or async","group":"CountrySettings","permissions":[],"flippingStrategy":null,"customProperties":{}}

    public FeaturesToggleAdministrationService isToggleEnabled(String featureId) {
        assertThat(getToggleStatus(featureId)).as("toggle with id" + featureId + "should be enabled").isTrue();
        return this;
    }

    public FeaturesToggleAdministrationService isToggleDisabled(String featureId) {
        assertThat(getToggleStatus(featureId)).as("toggle with id" + featureId + "should be disabled").isFalse();
        return this;
    }

    private Boolean getToggleStatus(String featureId){
        ValidatableResponse response = given().sessionId(sessionId).get(toggleUrl_2 + SLASH + featureId).then().statusCode(200).log().all();
        return response.extract().jsonPath().getBoolean("enable");
    }

}
