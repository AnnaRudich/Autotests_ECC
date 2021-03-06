package com.scalepoint.automation.utils;

import org.openqa.selenium.remote.SessionId;

import static io.restassured.RestAssured.given;

/**
 * Created by bza on 8/28/2017.
 */
public class GridInfoUtils {

    public static String getGridNodeName(SessionId sessionId) {
        String nodeAddress;
        try {
            nodeAddress = given().baseUri(Configuration.getHubRemote().replace("/wd/hub", ""))
                    .basePath("/grid/api/testsession")
                    .queryParam("session", sessionId.toString())
                    .when().get()
                    .then().extract().jsonPath().get("proxyId");
        } catch (Exception e) {
            nodeAddress = e.getMessage();
        }
        return nodeAddress == null ? "localhost" : nodeAddress;
    }
}
