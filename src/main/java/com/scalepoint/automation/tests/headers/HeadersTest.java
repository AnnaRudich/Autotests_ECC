package com.scalepoint.automation.tests.headers;

import com.scalepoint.automation.services.restService.ClaimSettlementItemsService;
import com.scalepoint.automation.services.restService.CreateClaimService;
import com.scalepoint.automation.tests.api.BaseApiTest;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import com.scalepoint.automation.utils.data.request.InsertSettlementItem;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import lombok.Data;
import org.apache.http.HttpStatus;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.scalepoint.automation.services.restService.Common.BaseService.*;
import static com.scalepoint.automation.tests.BaseTest.provide;
import static com.scalepoint.automation.utils.Configuration.*;
import static io.restassured.RestAssured.given;

public class HeadersTest extends BaseApiTest {

    static String CACHE_CONTROL = "max-age=2592000";
    static String CACHE_CONTROL2 = "no-cache, no-store, max-age=0, must-revalidate";
    static String CONTENT_SECURITY_POLICY = "default-src 'none'; script-src 'self' 'unsafe-inline' 'unsafe-eval' https://code.jquery.com/qunit/qunit-2.0.0.js; style-src 'self' 'unsafe-inline' https://code.jquery.com/qunit/qunit-2.0.0.css; img-src 'self' data:; frame-src 'self'; connect-src 'self'";
    static String CONTENT_SECURITY_POLICY2 = "default-src 'none'; style-src 'self' 'unsafe-inline'; script-src 'self' 'unsafe-inline' 'unsafe-eval'; img-src 'self' data:; frame-src 'self'; connect-src 'self'";
    static String EXPIRES = "0";
    static String PRAGMA = "no-cache";


    @Test(dataProvider = "testproviderstaticFilesECC")
    public void test(User user, String url){

        String sessionID = loginUser(user).getData().getEccSessionId();

        ValidatableResponse validatableResponse = given()
                .baseUri(getEccUrl())
                .sessionId(sessionID)
                .get(url)
                .then()
                .statusCode(HttpStatus.SC_OK);

        assert3(validatableResponse);
    }

    @Test(dataProvider = "testproviderstaticNoCacheECC")
    public void test3(User user, ClaimRequest claimRequest, String url){

        CreateClaimService loginProcessService = loginAndOpenClaim(user, claimRequest);

        RequestSpecification requestSpecification = given()
                .baseUri(getEccUrl())
                .sessionId(loginProcessService.getData().getEccSessionId());

        if(url.contains("userId")) {

            requestSpecification
                    .pathParam("userId", loginProcessService.getData().getUserId());
        }

        ValidatableResponse validatableResponse = requestSpecification
                .get(url)
                .then()
                .statusCode(HttpStatus.SC_OK);

        assert2(validatableResponse);
    }

    @Test(dataProvider = "testproviderstaticFilesAdmin")
    public void test1(User user, String url){

        ValidatableResponse validatableResponse = admin(user , url);

        assert4(validatableResponse);
    }

    @Test(dataProvider = "testprovidernoCacheAdmin")
    public void test4(User user, String url){

        ValidatableResponse validatableResponse = admin(user , url);

        assert1(validatableResponse);
    }

    @Test(dataProvider = "testproviderstaticFilesRnV")
    public void test2(User user, ClaimRequest claimRequest, InsertSettlementItem insertSettlementItem,  String url){

        ValidatableResponse validatableResponse = rnv(user, claimRequest, insertSettlementItem, url);

        assert4(validatableResponse);
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

        return given().log().all()
                .baseUri(getEccAdminUrl()).redirects().follow(false)
                .sessionId(response.getSessionId())
                .get(url)
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK);
    }

    private ValidatableResponse rnv(User user, ClaimRequest claimRequest, InsertSettlementItem insertSettlementItem, String url){

        ClaimSettlementItemsService loginProcessService = loginAndOpenClaimWithItems(user, claimRequest, insertSettlementItem);

        String sessionID = loginProcessService.getData().getEccSessionId();


        Body body = new Body();
        body.setSelectedGroups(new ArrayList<>());
        body.setSelectedLines(Arrays.asList(insertSettlementItem.getEccItemId().toString()));
        SelectedLinesAndCategories selectedLineCategory = new SelectedLinesAndCategories();
        selectedLineCategory.setItemId(insertSettlementItem.getEccItemId().toString());
        selectedLineCategory.setClaimLineId(1);
        selectedLineCategory.setPseudocatId(209083);
        selectedLineCategory.setDescription(insertSettlementItem.getSettlementItem().getClaim().getDescription());
        selectedLineCategory.setIsRvRepairTaskSentOrApproved(false);
        body.setSelectedLinesAndCategories(Collections.singletonList(selectedLineCategory));

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

        assert1(validatableResponse);

        return given()
                .baseUri(getEccRnvUrl())
                .sessionId(sessionID)
                .get(url)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    private void assert1(ValidatableResponse validatableResponse){

        validatableResponse
                .assertThat()
                .header("Cache-Control", CACHE_CONTROL2)
                .header("Expires", EXPIRES)
                .header("Pragma", PRAGMA)
                .header("Content-Security-Policy", CONTENT_SECURITY_POLICY2);
    }

    public void assert2(ValidatableResponse validatableResponse){

        validatableResponse
                .assertThat()
                .header("Cache-Control", CACHE_CONTROL2)
                .header("Expires", EXPIRES)
                .header("Pragma", PRAGMA)
                .header("Content-Security-Policy", CONTENT_SECURITY_POLICY);
    }

    public void assert3(ValidatableResponse validatableResponse){

        validatableResponse
                .assertThat()
                .header("Cache-Control", CACHE_CONTROL)
                .header("Content-Security-Policy", CONTENT_SECURITY_POLICY);
    }

    public void assert4(ValidatableResponse validatableResponse){

        validatableResponse
                .assertThat()
                .header("Cache-Control", CACHE_CONTROL)
                .header("Content-Security-Policy", CONTENT_SECURITY_POLICY2);
    }

    @DataProvider(name = "testproviderstaticFilesECC")
    public static Object[][] testproviderstaticFilesECC(Method method) {

        return test(method, staticFilesECC);
    }

    @DataProvider(name = "testproviderstaticFilesAdmin")
    public static Object[][] testproviderstaticFilesAdmin(Method method) {

        return test(method, staticFilesAdmin);
    }

    @DataProvider(name = "testproviderstaticFilesRnV")
    public static Object[][] testproviderstaticFilesRnV(Method method) {

        return test(method, staticFilesRnV);
    }

    @DataProvider(name = "testproviderstaticNoCacheECC")
    public static Object[][] testproviderstaticNoCacheECC(Method method) {

        return test(method, noCacheECC);
    }

    @DataProvider(name = "testprovidernoCacheAdmin")
    public static Object[][] testprovidernoCacheAdmin(Method method) {

        return test(method, noCacheAdmin);
    }

    public static Object[][] test(Method method, String[] request){

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

    public static String[] noCacheAdmin= {
            ""
    };

    @Data
    class Body{
        List<String> selectedGroups;
        List<String> selectedLines;
        List<SelectedLinesAndCategories> selectedLinesAndCategories;
    }

    @Data
    class SelectedLinesAndCategories{

        String itemId;
        int claimLineId;
        int pseudocatId;
        String description;
        boolean isIsRvRepairTaskSentOrApproved;
    }
}
