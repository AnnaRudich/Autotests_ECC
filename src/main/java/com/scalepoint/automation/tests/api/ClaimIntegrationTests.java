package com.scalepoint.automation.tests.api;


import com.scalepoint.automation.services.restService.LoginProcessService;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static com.scalepoint.automation.utils.Configuration.getEccUrl;
import static io.restassured.RestAssured.given;

public class ClaimIntegrationTests extends BaseApiTest {

    @Test(dataProvider = "testDataProvider", dataProviderClass = BaseTest.class)
    public void xmlIntegration(User user){

        String claimNumber = "a3";

        String xml = "<?xml version=\"1.0\"?>\n" +
                "<ECCIntegration updateAction=\"UPDATE\">\n" +
                "\t<Claim claimNumber=\""+claimNumber+"\" allowAutoClose=\"false\">\n" +
                "\t\t<ClaimedItems>\n" +
                "\t\t\t<ClaimedItem description=\"Nokia E71\" quantity=\"1\" depreciation=\"10\" ageMonths=\"3\" itemId=\"NOK_E71_BLK\" brand=\"Nokia\" model=\"E71\" status=\"ACTIVE\" activeValuation=\"CUSTOMER_DEMAND\">\n" +
                "\t\t\t\t<Valuations>\n" +
                "\t\t\t\t\t<Valuation price=\"2100\" type=\"CUSTOMER_DEMAND\"/>\n" +
                "\t\t\t\t</Valuations>\n" +
                "\t\t\t\t<CompanySpecificItemData/>\n" +
                "\t\t\t</ClaimedItem>\n" +
                "\t\t</ClaimedItems>\n" +
                "\t</Claim>\n" +
                "\t<Claimant firstName=\"Jennifer\" lastName=\"Reid\" address1=\"818 Edgefield Hwy\" postalCode=\"1017\" city=\"Leander\" phone=\"29162857\" email=\"bza@scalepoint.com\"/>\n" +
                "</ECCIntegration>\n";

        Response response = new LoginProcessService()
                .login(user).getResponse();

        given().baseUri(getEccUrl()).log().all()
                .sessionId(response.getSessionId())
                .basePath("Integration/CreateClaim")
                .formParam("xml",xml)
                .post()
                .then().log().all().extract().response();

        given().baseUri(getEccUrl()).log().all()
                .sessionId(response.getSessionId())
                .redirects().follow(false)
                .basePath("Integration/Open")
                .queryParam("shnbr", databaseApi.getUserIdByClaimNumber(claimNumber))
                .get()
                .then().log().all().extract().response();

        Response response1 = given().baseUri(getEccUrl()).log().all()
                .sessionId(response.getSessionId())
                .redirects().follow(false)
                .queryParam("shnbr", databaseApi.getUserIdByClaimNumber(claimNumber))
                .basePath("webshop/jsp/matching_engine/customer_details.jsp")
                .get()
                .then().log().all().extract().response();

        //location header
        //no location header



    }
}
