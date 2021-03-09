package com.scalepoint.automation.tests.headers;

import com.scalepoint.automation.services.restService.ClaimSettlementItemsService;
import com.scalepoint.automation.services.restService.CreateClaimService;
import com.scalepoint.automation.services.restService.SelfServiceService;
import com.scalepoint.automation.services.restService.common.BaseService;
import com.scalepoint.automation.services.usersmanagement.CompanyCode;
import com.scalepoint.automation.tests.api.BaseApiTest;
import com.scalepoint.automation.utils.annotations.UserCompany;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.rnv.sendToRepairAndValuation.SelectedLinesAndCategories;
import com.scalepoint.automation.utils.data.entity.rnv.sendToRepairAndValuation.SendToRepairAndValuation;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import com.scalepoint.automation.utils.data.request.InsertSettlementItem;
import com.scalepoint.automation.utils.data.request.SelfServiceRequest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.hamcrest.Matcher;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

import static com.scalepoint.automation.services.restService.common.BaseService.*;
import static com.scalepoint.automation.tests.BaseTest.provide;
import static com.scalepoint.automation.utils.Configuration.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class HeadersTest extends BaseApiTest {

    static String CACHE_CONTROL = "max-age=2592000";
    static String CACHE_CONTROL2 = "no-cache, no-store, max-age=0, must-revalidate";
    static String CONTENT_SECURITY_POLICY_LONG = "default-src 'none'; img-src 'self' *.getbeamer.com data:; script-src 'self' 'unsafe-inline' 'unsafe-eval' *.getbeamer.com; style-src 'self' 'unsafe-inline' *.getbeamer.com; frame-src 'self' *.getbeamer.com; connect-src 'self' *.getbeamer.com";
    static String CONTENT_SECURITY_POLICY_SHORT = "default-src 'none'; style-src 'self' 'unsafe-inline'; script-src 'self' 'unsafe-inline' 'unsafe-eval'; img-src 'self' data:; frame-src 'self'; connect-src 'self'";
    static String CONTENT_SECURITY_POLICY_SS = "script-src 'self' https://www.google.com/recaptcha/api.js https://www.gstatic.com http://*.scalepoint.com 'nonce-";
    static String EXPIRES = "0";
    static String PRAGMA = "no-cache";


    @Test(dataProvider = "staticFilesECCDataProvider")
    public void staticFilesECCTest(User user, String url){

        ValidatableResponse validatableResponse = login(user, url);

        assertMaxAgeAndPolicyLong(validatableResponse);
    }

    @Test(dataProvider = "staticNoCacheECCDataProvider")
    public void staticNoCacheECCTest(User user, ClaimRequest claimRequest, String url){

        ValidatableResponse validatableResponse = claim(user, claimRequest, url);

        assertExpiresPragmaNoCasheAndPolicyLong(validatableResponse);
    }

    @Test(dataProvider = "staticFilesAdminDataProvider")
    public void staticFilesAdminTest(User user, String url){

        ValidatableResponse validatableResponse = admin(user , url);

        assertMaxAgeAndPolicyShort(validatableResponse);
    }

    @Test(dataProvider = "noCacheAdminDataProvider")
    public void noCacheAdminTest(User user, String url){

        ValidatableResponse validatableResponse = admin(user , url);

        assertExpiresPragmaNoCasheAndPolicyShort(validatableResponse);
    }

    @Test(dataProvider = "staticFilesRnVDataProvider")
    public void staticFilesRnVTest(User user, ClaimRequest claimRequest, InsertSettlementItem insertSettlementItem,  String url){

        ValidatableResponse validatableResponse = rnv(user, claimRequest, insertSettlementItem, url);

        assertMaxAgeAndPolicyShort(validatableResponse);
    }

    @Test(dataProvider = "staticFilesSelfServiceDataProvider")
    public void staticFilesSelfServiceTest(@UserCompany(CompanyCode.TOPDANMARK) User user, String url){

        ValidatableResponse validatableResponse = selfService(user, url);

        assertMaxAgeAndPolicySelfService(validatableResponse);
    }

    @Test(dataProvider = "noCacheSelfServiceDataProvider")
    public void noCacheSelfServiceTest(@UserCompany(CompanyCode.TOPDANMARK) User user, String url){

        ValidatableResponse validatableResponse = selfService(user, url);

        assertExpiresPragmaNoCasheAndPolicySelfService(validatableResponse);
    }

    private ValidatableResponse login(User user, String url){

        String sessionID = loginUser(user).getData().getEccSessionId();

        return given()
                .baseUri(getEccUrl())
                .sessionId(sessionID)
                .get(url)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    private ValidatableResponse claim(User user, ClaimRequest claimRequest, String url){

        String pathParam = "userId";

        CreateClaimService loginProcessService = loginAndOpenClaim(user, claimRequest);

        RequestSpecification requestSpecification = given()
                .baseUri(getEccUrl())
                .sessionId(loginProcessService.getData().getEccSessionId());

        if(url.contains(pathParam)) {

            requestSpecification
                    .pathParam(pathParam, loginProcessService.getData().getUserId());
        }

        return requestSpecification
                .get(url)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    private ValidatableResponse admin(User user , String url){

        String sessionID = loginUser(user).getResponse().getSessionId();

        Response response = given()
                .baseUri(getEccUrl()).redirects().follow(false)
                .sessionId(sessionID)
                .get("RedirectToSupplyManagement")
                .then()
                .statusCode(HttpStatus.SC_MOVED_TEMPORARILY).extract().response();

        response = given()
                .redirects().follow(false)
                .sessionId(sessionID)
                .get(response.getHeader("Location"))
                .then()
                .statusCode(HttpStatus.SC_MOVED_TEMPORARILY).extract().response();

        return given()
                .baseUri(getEccAdminUrl()).redirects().follow(false)
                .sessionId(response.getSessionId())
                .get(url)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    private ValidatableResponse selfService(User user, String url){

        SelfServiceRequest selfServiceRequest = TestData.getSelfServiceRequest();
        ClaimRequest claimRequest = TestData.getClaimRequestFraudAlert();
        selfServiceRequest.setClaimsNo(claimRequest.getCaseNumber());

        SelfServiceService selfServiceService = BaseService
                .loginAndOpenClaim(user, claimRequest)
                .requestSelfService(selfServiceRequest)
                .loginToSS(selfServiceRequest.getPassword());

        return given()
                .baseUri(getSelfServiceUrl())
                .header("Access-Token", selfServiceService.getData().getSelfServiceAccessToken())
                .get(url)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat();
    }

    private ValidatableResponse rnv(User user, ClaimRequest claimRequest, InsertSettlementItem insertSettlementItem, String url){

        ClaimSettlementItemsService loginProcessService = loginAndOpenClaimWithItems(user, claimRequest, insertSettlementItem);

        String sessionID = loginProcessService.getData().getEccSessionId();

        SelectedLinesAndCategories selectedLineCategory = SelectedLinesAndCategories.builder()
                .claimLineId(1)
                .description(insertSettlementItem.getSettlementItem().getClaim().getDescription())
                .isIsRvRepairTaskSentOrApproved(false)
                .itemId(insertSettlementItem.getEccItemId().toString())
                .build();

        SendToRepairAndValuation body = SendToRepairAndValuation.builder()
                .selectedGroups(new ArrayList<>())
                .selectedLines(Arrays.asList(insertSettlementItem.getEccItemId().toString()))
                .selectedLinesAndCategories(Arrays.asList(selectedLineCategory)).build();

        Response response = given()
                .baseUri(getEccUrl())
                .sessionId(sessionID)
                .pathParam("userId", loginProcessService.getData().getUserId())
                .contentType(ContentType.JSON)
                .body(body)
                .post("{userId}/rest/settlement/sendToRepairAndValuation.json")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().response();

        String data = response.getBody().jsonPath().get("data");

        response = given().redirects().follow(false)
                .get(data)
                .then()
                .statusCode(HttpStatus.SC_MOVED_TEMPORARILY)
                .extract().response();

        sessionID = response.getSessionId();

        ValidatableResponse validatableResponse = given().redirects().follow(false)
                .sessionId(sessionID)
                .get(response.getHeader("Location"))
                .then()
                .statusCode(HttpStatus.SC_OK);

        assertExpiresPragmaNoCasheAndPolicyShort(validatableResponse);

        return given()
                .baseUri(getEccRnvUrl())
                .sessionId(sessionID)
                .get(url)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    public void assertMaxAgeAndPolicyLong(ValidatableResponse validatableResponse){

        assertCacheControlAndContentSecurityPolicy(validatableResponse, equalTo(CACHE_CONTROL), equalTo(CONTENT_SECURITY_POLICY_LONG));
    }

    public void assertMaxAgeAndPolicyShort(ValidatableResponse validatableResponse){

        assertCacheControlAndContentSecurityPolicy(validatableResponse, equalTo(CACHE_CONTROL), equalTo(CONTENT_SECURITY_POLICY_SHORT));
    }


    public void assertMaxAgeAndPolicySelfService(ValidatableResponse validatableResponse){

        assertCacheControlAndContentSecurityPolicy(validatableResponse, equalTo(CACHE_CONTROL), containsString(CONTENT_SECURITY_POLICY_SS));
    }

    public void assertExpiresPragmaNoCasheAndPolicySelfService(ValidatableResponse validatableResponse){

        assertCacheControlAndContentSecurityPolicy(assertExpiresPragma(validatableResponse), equalTo(CACHE_CONTROL2), containsString(CONTENT_SECURITY_POLICY_SS));
    }

    private void assertExpiresPragmaNoCasheAndPolicyShort(ValidatableResponse validatableResponse){

        assertCacheControlAndContentSecurityPolicy(assertExpiresPragma(validatableResponse), equalTo(CACHE_CONTROL2), equalTo(CONTENT_SECURITY_POLICY_SHORT));
    }

    private void assertExpiresPragmaNoCasheAndPolicyLong(ValidatableResponse validatableResponse){

        assertCacheControlAndContentSecurityPolicy(assertExpiresPragma(validatableResponse), equalTo(CACHE_CONTROL2), equalTo(CONTENT_SECURITY_POLICY_LONG));
    }

    private ValidatableResponse assertExpiresPragma(ValidatableResponse validatableResponse){

        return validatableResponse
                .assertThat()
                .header("Expires", EXPIRES)
                .header("Pragma", PRAGMA);
    }

    private ValidatableResponse assertCacheControlAndContentSecurityPolicy(ValidatableResponse validatableResponse, Matcher cacheControl, Matcher contentSecurityPolicy){

        return validatableResponse
                .header("Cache-Control", cacheControl)
                .header("Content-Security-Policy", contentSecurityPolicy);
    }

    @DataProvider(name = "staticFilesECCDataProvider")
    public static Object[][] staticFilesECCDataProvider(Method method) {

        return dataProvider(method, staticFilesECC);
    }

    @DataProvider(name = "staticFilesAdminDataProvider")
    public static Object[][] staticFilesAdminDataProvider(Method method) {

        return dataProvider(method, staticFilesAdmin);
    }

    @DataProvider(name = "staticFilesRnVDataProvider")
    public static Object[][] staticFilesRnVDataProvider(Method method) {

        return dataProvider(method, staticFilesRnV);
    }

    @DataProvider(name = "staticNoCacheECCDataProvider")
    public static Object[][] staticNoCacheECCDataProvider(Method method) {

        return dataProvider(method, noCacheECC);
    }

    @DataProvider(name = "noCacheAdminDataProvider")
    public static Object[][] noCacheAdminDataProvider(Method method) {

        return dataProvider(method, noCacheAdmin);
    }

    @DataProvider(name = "staticFilesSelfServiceDataProvider")
    public static Object[][] staticFilesSelfServiceDataProvider(Method method) {

        return dataProvider(method, staticFilesSelfService);
    }

    @DataProvider(name = "noCacheSelfServiceDataProvider")
    public static Object[][] noCacheSelfServiceDataProvider(Method method) {

        return dataProvider(method, noCacheSelfService);
    }

    public static Object[][] dataProvider(Method method, String[] request){

        Object[] baseObjects = provide(method)[0];

        Object[][] objects = new Object[request.length][baseObjects.length + 1];

        for(int i = 0; i < request.length; i++){
            for (int j = 0; j < baseObjects.length; j++){

                objects[i][j] = baseObjects[j];
            }

            objects[i][baseObjects.length] = request[i];
        }
        return objects;
    }

    public static String[] staticFilesECC= {
            "wwwroot/shop/scripts/ExtJS6/classic/theme-classic/resources/theme-classic-all_1.css",
            "wwwroot/shop/scripts/ExtJS4/ext-all-custom.js?build=2.14.0.26063-(Tech-Jan2014)",
            "wwwroot/scripts/matching_engine/menu/app/controller/MenuController.js?dc=BUILDNUMBER_",
            "wwwroot/images/common/matching_engine/images/scalepoint_logo_small_transparent.png"
    };

    public static String[] staticFilesAdmin= {
            "repository/content/common/scripts/extjs/resources/css/ext-all.css",
            "repository/content/common/css/base.css",
            "repository/content/common/scripts/extjs/extjs-4.2.1/ext-all-debug.js",
            "repository/content/common/scripts/multiselect/multiselect.js"
    };

    public static String[] staticFilesRnV= {
            "static/js/extjs/ext-all.js",
            "static/js/RepairAndValuation.js",
            "static/js/extjs/ext-overrides.css"
    };

    public static String[] noCacheECC= {
            "webshop/jsp/matching_engine/my_page.jsp",
            "{userId}/webshop/jsp/matching_engine/settlement.jsp",
            "{userId}/rest/settlement/getChangesInMenu.json?_dc=1586253195681",
            "{userId}/rest/settlement/settlementSummaryTotals.json?_dc=1586253195848"

    };

    public static String[] staticFilesSelfService= {
            "resources/self-service-site-topdanmark.css?v=3.0.0.local",
            "resources/self-service-widget-topdanmark.css?v=3.0.0.local",
            "static/js/app.min.js?v=3.0.0.local",
            "static/favicons/topdanmark.ico",
            "static/logo/topdanmark.png",
            "static/images/no_pic.png"

    };

    public static String[] noCacheSelfService= {
            "categories/",
            "initdata"

    };

    public static String[] noCacheAdmin= {
            ""
    };
}
