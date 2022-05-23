package com.scalepoint.automation.services.restService;

import com.scalepoint.automation.services.externalapi.ftoggle.FeatureId;
import com.scalepoint.automation.services.restService.common.BaseService;
import com.scalepoint.automation.services.usersmanagement.UsersManager;
import com.scalepoint.automation.utils.Configuration;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;


public class FeaturesToggleAdministrationService extends BaseService {

    private String sessionId = loginUser(UsersManager.getSystemUser()).getResponse().getSessionId();

    public FeaturesToggleAdministrationService updateToggle(ActionsOnToggle expectedActionOnToggle, FeatureId featureId) {

        given().param("op", expectedActionOnToggle.name()).
                param("uid", featureId.name()).
                sessionId(sessionId).
                when().
                get(Configuration.getFeatureToggleAdminUrl()).
                then().
                statusCode(200);

        assertToggleStatus(featureId, expectedActionOnToggle.getEnableParameterValue());
        return this;
    }

    private void assertToggleStatus(FeatureId featureId, Boolean expectedStatus) {
        assertThat(getToggleStatus(featureId.name())).
                as("toggle for featureId: " + featureId + "should be enable: " + expectedStatus.toString()).isEqualTo(expectedStatus);
    }

    public Boolean getToggleStatus(String featureId) {

        ValidatableResponse response =
                given().
                        sessionId(sessionId).
                        get(Configuration.getFeaturesApiUrl() + featureId).then().statusCode(200);
        return response.extract().jsonPath().getBoolean("enable");
    }

    public enum ActionsOnToggle {

        ENABLE(true),
        DISABLE(false);

        private Boolean enableParameterValue;

        ActionsOnToggle(boolean toggleStatusInJson) {
            this.enableParameterValue = toggleStatusInJson;
        }

        public Boolean getEnableParameterValue() {
            return enableParameterValue;
        }

        public static ActionsOnToggle of(boolean enable) {
            return enable ? ENABLE : DISABLE;
        }
    }
}
