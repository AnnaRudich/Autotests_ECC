package com.scalepoint.automation.services.externalapi;

import com.scalepoint.automation.utils.data.request.*;
import com.scalepoint.automation.utils.data.response.Token;
import io.restassured.response.ValidatableResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.scalepoint.automation.utils.Configuration.getEccUrl;
import static io.restassured.RestAssured.given;

public class IntegrationClaimApi {

    private Token token;
    private ClaimRequest claimRequest;
    private ValidatableResponse response;

    public IntegrationClaimApi(Token token){
        this.token = token;
        setUpSampleClaimRequest();
    }

    public IntegrationClaimApi sendRequest() {
        this.response = given().baseUri(getEccUrl()).port(80).basePath("/Integration/UnifiedIntegration").log().all()
                .body(claimRequest)
                .header("Authorization", token.getTokenType() + " " + token.getAccessToken())
                .when()
                .post()
                .then().log().all().statusCode(200);
        return this;
    }

    public ValidatableResponse getResponse(){
        return this.response;
    }

    public String getClaimTokenString(){
        return this.response.extract().jsonPath().get("token");
    }

    private void setUpSampleClaimRequest(){
        List<ExtraModifier> modifiersList = new ArrayList<>();
        modifiersList.add(new ExtraModifier().withType("postItemizationCompletedUrl").withValue("http://www.google.com"));
        modifiersList.add(new ExtraModifier().withType("cwaServiceId").withValue("666"));
        modifiersList.add(new ExtraModifier().withType("replyToCaseEmail").withValue("bza@scalepoint.com"));

        UUID caseNUmber = UUID.randomUUID();
        this.claimRequest = new ClaimRequest()
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
    }
}
