package com.scalepoint.automation.tests.performance;

import com.scalepoint.automation.services.restService.common.BaseService;
import com.scalepoint.automation.tests.api.BaseApiTest;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Method;

import static io.restassured.RestAssured.given;

public class PerformanceTest extends BaseApiTest {

    String baseUri = "http://localhost:88/databot";
    String eccAdminUrl = "https://qa14.scalepoint.com/eccAdmin/dk";
    String selfService = "https://qa14.scalepoint.com/self-service/dk";
    static int users;

    @Parameters({"users"})
    @BeforeClass
    public void startWireMock(String users) throws IOException {

        this.users = Integer.valueOf(users);
    }

    @Test(dataProvider = "usersDataProvider")
    public void testAdd(User user) {

//        ClaimRequest claimRequest = TestData.getClaimRequest();
//
        BaseService
                .loginUser(user);

//        String token = given()
//                .baseUri(baseUri)
//                .log().all()
//                .redirects().follow(false)
//                .queryParam("eccAdminUrl", eccAdminUrl)
//                .queryParam("login", user.getLogin())
//                .queryParam("pass", user.getPassword())
//                .queryParam("selfServiceUrl", selfService)
//                .get("/login/login")
//                .then().log().all()
//                .statusCode(HttpStatus.SC_OK)
//                .extract().response().getBody().asString();

//        given()
//                .baseUri(baseUri)
//                .log().all()
//                .redirects().follow(false)
//                .queryParam("token", token)
//                .post("/rv-emulator/createAndSendToRV")
//                .then().log().all()
//                .statusCode(HttpStatus.SC_OK)
//                .extract().response();
    }

    @DataProvider(name = "usersDataProvider", parallel = true)
    public static Object[][] usersDataProvider(Method method) {

        Object[][] objects = new Object[users][1];

        for(int i =0;i<users;i++){

            objects[i][0] = new User("autotest-future50".concat(new Integer(i + 1).toString()), "12341234");
        }

        return objects;
    }
}
