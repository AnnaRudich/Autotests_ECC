package com.scalepoint.automation.tests.api.fnol;

import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.utils.Configuration;
import com.scalepoint.automation.utils.data.TestDataActions;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import io.restassured.response.ValidatableResponse;
import lombok.Builder;
import lombok.Data;
import org.apache.http.HttpStatus;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class FnolCorsStaticGetTest extends FnolBaseTest {

    private static final String GET_DATA_PROVIDER = "getDataProvider";

    @Test(groups = {TestGroups.FNOL, TestGroups.FNOL_CORS_GET}, dataProvider = GET_DATA_PROVIDER,
            description = "Verify flow when request is sent from an unauthenticated domain")
    public void nonAuthTest(User user, FnolCorsGetTestData fnolCorsGetTestData) {

        String body = getResponse(nonAuthOrigin, fnolCorsGetTestData)
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .extract().asString();

        assertThat(body).isEqualTo("Invalid CORS request");
    }

    @Test(groups = {TestGroups.FNOL, TestGroups.FNOL_CORS_GET}, dataProvider = GET_DATA_PROVIDER,
            description = "Verify flow when request is sent from an authenticated domain")
    public void authTest(User user, FnolCorsGetTestData fnolCorsGetTestData) {

        getResponse(authOrigin, fnolCorsGetTestData)
                .statusCode(HttpStatus.SC_OK);
    }

    private ValidatableResponse getResponse(String origin, FnolCorsGetTestData fnolCorsGetTestData){

        return given().log().all()
                .baseUri(Configuration.getServerUrl())
                .header(ACCESS_TOKEN, fnolCorsGetTestData.getSsToken())
                .header(ORIGIN, origin)
                .contentType(APPLICATION_JSON)
                .get(fnolCorsGetTestData.getUri())
                .then().log().all();
    }

    @DataProvider(name = GET_DATA_PROVIDER)
    public static Object[][] getDataProvider(Method method) {

        return new Object[][]{

                TestDataActions.getTestDataWithExternalParameters(method, FnolCorsGetTestData.builder().uri("/self-service/dk/categories/").build()).toArray(),
                TestDataActions.getTestDataWithExternalParameters(method, FnolCorsGetTestData.builder().uri("/self-service/dk/initdata").build()).toArray(),
                TestDataActions.getTestDataWithExternalParameters(method, FnolCorsGetTestData.builder().uri("/self-service/dk/resources/self-service-widget-future.css").build()).toArray(),
                TestDataActions.getTestDataWithExternalParameters(method, FnolCorsGetTestData.builder().uri("/self-service/dk/fonts/open-sans.woff").build()).toArray(),
        };
    }

    @Data
    @Builder
    static class FnolCorsGetTestData extends FnolCorsTestData{

        String uri;
    }
}
