package com.scalepoint.automation.services.restService;

import com.scalepoint.automation.services.restService.Common.BaseService;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import com.scalepoint.automation.utils.data.response.Token;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import org.apache.http.params.CoreConnectionPNames;

import java.time.Duration;
import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;

public class FilesServiceService extends BaseService {

    private Token token;
    private Response response;

    public FilesServiceService(Token token) {
        this.token = token;
    }

    public Response getResponse() {
        return this.response;
    }

    public FilesServiceService setToken(Token token) {
        this.token = token;
        return this;
    }

    public FilesServiceService getFile(String fileGUID) {

        RestAssuredConfig config = RestAssured.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam(CoreConnectionPNames.CONNECTION_TIMEOUT, 60000)
                        .setParam(CoreConnectionPNames.SO_TIMEOUT, 60000));

        LocalDateTime start = LocalDateTime.now();
        this.response = given().config(config)
                .baseUri("https://dev-ecc-01.spcph.test")
                .basePath("/api/fs/files/{locale}/{tenant}/{fileGUID}")
//                .log().all()
                .pathParam("locale", "dk")
                .pathParam("tenant", "topdanmark")
                .pathParam("fileGUID", fileGUID)
                .header(token.getAuthorizationHeader())
                .when()
                .get()
                .then()
//                .log().all()
                .statusCode(200)
                .extract().response();
        LocalDateTime end = LocalDateTime.now();
        Duration duration = Duration.between(start, end);
        logger.info(duration);
        return this;
    }
}





