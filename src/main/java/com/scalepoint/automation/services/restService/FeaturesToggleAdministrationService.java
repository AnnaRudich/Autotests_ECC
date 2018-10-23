package com.scalepoint.automation.services.restService;

import com.scalepoint.automation.services.externalapi.ftoggle.FeatureIds;
import com.scalepoint.automation.services.restService.Common.BaseService;
import com.scalepoint.automation.services.usersmanagement.UsersManager;
import com.scalepoint.automation.utils.Configuration;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;


public class FeaturesToggleAdministrationService extends BaseService {
    String sessionId = loginUser(UsersManager.getSystemUser()).getResponse().getSessionId();;


    public FeaturesToggleAdministrationService updateToggle(ActionsOnToggle expectedActionOnToggle, FeatureIds featureId) {

        given().log().all().param("op", expectedActionOnToggle.name()).
                param("uid", featureId.name()).
                sessionId(sessionId).
                when().
                get(Configuration.getFeatureToggleAdminUrl()).
                then().
                statusCode(200).
                log().all();

        assertToggleStatus(featureId, expectedActionOnToggle.getEnableParameterValue());
        return this;
    }

    private void assertToggleStatus(FeatureIds featureId, Boolean expectedStatus) {
        assertThat(getToggleStatus(featureId.name())).
                as("toggle for featureId: " + featureId + "should be enable: " + expectedStatus.toString()).isEqualTo(expectedStatus);
    }

    public Boolean getToggleStatus(String featureId) {

        ValidatableResponse response =
                given().log().all().
                        sessionId(sessionId).
                        get(Configuration.getFeaturesApiUrl() + featureId).then().statusCode(200).log().all();
        return response.extract().jsonPath().getBoolean("enable");
    }

    public enum ActionsOnToggle {
        enable(true),
        disable(false);

        private Boolean enableParameterValue;

        ActionsOnToggle(boolean toggleStatusInJson) {
            this.enableParameterValue = toggleStatusInJson;
        }

        public Boolean getEnableParameterValue() {
            return enableParameterValue;
        }
    }
}
