package com.scalepoint.automation.services.restService;

import com.scalepoint.automation.services.externalapi.ftoggle.FeatureIds;
import com.scalepoint.automation.services.restService.Common.BaseService;
import com.scalepoint.automation.services.usersmanagement.UsersManager;
import com.scalepoint.automation.utils.Configuration;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.sessionId;
import static org.assertj.core.api.Assertions.assertThat;


public class FeaturesToggleAdministrationService extends BaseService {

    public FeaturesToggleAdministrationService updateToggle(ActionsOnToggle expectedActionOnToggle, FeatureIds featureId){

        String sessionId = loginUser(UsersManager.getSystemUser()).getResponse().getSessionId();

        given().param("op", expectedActionOnToggle.name()).
                param("uid", featureId.name()).
                sessionId(sessionId).
                when().
                get(Configuration.getFeatureToggleAdminUrl()).
                then().
                statusCode(200).
                log().all();

        if (expectedActionOnToggle.getEnableParameterValue()) {
            assertThat(getToggleStatus(featureId.name())).as("toggle with id" + featureId + "should be enabled").isTrue();
        } else {
            assertThat(getToggleStatus(featureId.name())).as("toggle with id" + featureId + "should be disabled").isFalse();
        }
        return this;
    }

    public Boolean getToggleStatus(String featureId){
        ValidatableResponse response = given().sessionId(sessionId).get(Configuration.getFf4jFeaturesApiUrl()+ featureId).then().statusCode(200).log().all();
        return response.extract().jsonPath().getBoolean("enable");
    }

    public enum ActionsOnToggle {
        enable(true),
        disable(false);

        private Boolean enableParameterValue;

        ActionsOnToggle(boolean toggleStatusInJson){
            this.enableParameterValue =toggleStatusInJson;
        }

        public Boolean getEnableParameterValue(){
            return enableParameterValue;
        }
    }
}
