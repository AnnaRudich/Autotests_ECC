package com.scalepoint.automation.tests.api;


import com.scalepoint.automation.services.restService.LoginProcessService;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.eccIntegration.EccIntegration;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static com.scalepoint.automation.utils.Configuration.getEccUrl;
import static io.restassured.RestAssured.given;

public class ClaimIntegrationTests extends BaseApiTest {

    @Test(dataProvider = "testDataProvider", dataProviderClass = BaseTest.class)
    public void xmlIntegration(User user, EccIntegration eccIntegration){

        Response response = new LoginProcessService()
                .login(user).getResponse();

        given().baseUri(getEccUrl()).log().all()
                .sessionId(response.getSessionId())
                .basePath("Integration/CreateClaim")
                .formParam("xml", TestData.objectAsXml(eccIntegration))
                .post()
                .then().log().all().extract().response();

        given().baseUri(getEccUrl()).log().all()
                .sessionId(response.getSessionId())
                .redirects().follow(false)
                .basePath("Integration/Open")
                .queryParam("shnbr", databaseApi.getUserIdByClaimNumber(eccIntegration.getClaim().getClaimNumber()))
                .get()
                .then().log().all().extract().response();

        Response response1 = given().baseUri(getEccUrl()).log().all()
                .sessionId(response.getSessionId())
                .redirects().follow(false)
                .queryParam("shnbr", databaseApi.getUserIdByClaimNumber(eccIntegration.getClaim().getClaimNumber()))
                .basePath("webshop/jsp/matching_engine/customer_details.jsp")
                .get()
                .then().log().all().extract().response();

        //location header
        //no location header



    }
}
