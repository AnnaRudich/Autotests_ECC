package com.scalepoint.automation.tests.eccIntegrations;

import com.scalepoint.automation.pageobjects.pages.MailsPage;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.services.externalapi.DatabaseApi;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.CwaTaskLog;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.request.*;
import com.scalepoint.automation.utils.data.response.Token;
import com.scalepoint.automation.utils.threadlocal.Browser;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.scalepoint.automation.utils.Configuration.getEccUrl;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertTrue;

/**
 * Created by bza on 5/25/2017.
 */
public class EccIntegrationsWithCwaSS extends BaseTest {

    @Autowired
    private DatabaseApi databaseApi;

    @RequiredSetting(type = FTSetting.USE_SELF_SERVICE2)
    @RequiredSetting(type = FTSetting.ENABLE_SELF_SERVICE)
    @Test(dataProvider = "testDataProvider")
    public void selfServiceImport(User user, Claim claim, ClaimItem claimItem){

        Token token = given().baseUri("https://test-accounts.scalepoint.com").basePath("/connect/token").log().all()
                .formParam("grant_type", "client_credentials")
                .formParam("client_id", "test_integration_all_tenants")
                .formParam("client_secret", "-N64TJmEy5konAWGy7fSo7CbZ6sDdUJhHrXBIbJlE-Y")
                .formParam("scope", "case_integration")
                .when()
                .post()
                .then().log().all().statusCode(200).extract().as(Token.class);

        List<ExtraModifier> modifiersList = new ArrayList<>();
        modifiersList.add(new ExtraModifier().withType("postItemizationCompletedUrl").withValue("http://www.google.com"));
        modifiersList.add(new ExtraModifier().withType("cwaServiceId").withValue("666"));
        modifiersList.add(new ExtraModifier().withType("replyToCaseEmail").withValue("bza@scalepoint.com"));

        UUID caseNUmber = UUID.randomUUID();
        ClaimRequest claimRequest = new ClaimRequest()
                .withTenant("scalepoint")
                .withCompany("scalepoint")
                .withCountry("dk")
                .withCaseType("contentClaim")
                .withCaseNumber(caseNUmber.toString())
                .withItemizationCaseReference("")
                .withExternalReference("")
                .withAllowAutoClose(false)
                .withPolicy(new Policy()
                        .withNumber(""))
                .withCustomer(new Customer()
                        .withFirstName("john")
                        .withLastName("doe")
                        .withEmail("bza@scalepoint.com")
                        .withMobile("88 88 80 80")
                        .withAddress(new Address()
                                .withStreet1("")
                                .withPostalCode("")
                                .withCity("")))
                .withExtraModifiers(modifiersList);

        String claimToken = given().baseUri(getEccUrl()).port(80).basePath("/Integration/UnifiedIntegration").log().all()
                .body(claimRequest)
                .header("Authorization", token.getTokenType() + " " + token.getAccessToken())
                .when()
                .post()
                .then().log().all().statusCode(200).extract().jsonPath().get("token");

        int UserId = databaseApi.getUserIdByClaimToken(claimToken);

        login(user);
        Browser.open(getEccUrl()+ "Integration/Open?token=" + claimToken);
        SettlementPage settlementPage = new SettlementPage()
                .requestSelfService(claim, Constants.PASSWORD);

        assertTrue(databaseApi.getCwaTaskLogsForClaimId(UserId).stream().anyMatch((CwaTaskLog cwa) ->
            cwa.getTaskType().equals("OTHER") && cwa.getTaskStatus().equals("TASK_CREATED")
        ));

        settlementPage
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceNewLinkAndOpenIt()
                .enterPassword(Constants.PASSWORD)
                .login()
                .addDescription(claimItem.getSetDialogTextMatch())
                .selectPurchaseYear(String.valueOf(Year.now().getValue()))
                .selectPurchaseMonth("Apr")
                .saveItem()
                .sendResponseToEcc();

        assertTrue(databaseApi.getCwaTaskLogsForClaimId(UserId).stream().anyMatch((CwaTaskLog cwa) ->
                cwa.getTaskType().equals("OTHER") && cwa.getTaskStatus().equals("TASK_COMPLETED")
        ));
        assertTrue(databaseApi.getCwaTaskLogsForClaimId(UserId).stream().anyMatch((CwaTaskLog cwa) ->
                cwa.getTaskType().equals("SELF_SERVICE") && cwa.getTaskStatus().equals("TASK_CREATED")
        ));

    }
}
