package com.scalepoint.automation.services.restService;

import com.scalepoint.automation.services.restService.Common.BaseService;
import com.scalepoint.automation.utils.data.response.Token;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.SystemDefaultHttpClient;

import java.time.Duration;
import java.time.LocalDateTime;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;
import static io.restassured.config.HttpClientConfig.httpClientConfig;
import static io.restassured.config.RestAssuredConfig.newConfig;

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

        LocalDateTime start = LocalDateTime.now();
        this.response = given()
                .baseUri("https://dev-ecc-01.spcph.test")
                .basePath("/api/fs/files/{locale}/{tenant}/{fileGUID}")
                .log().all()
                .pathParam("locale", "dk")
                .pathParam("tenant", "topdanmark")
                .pathParam("fileGUID", fileGUID)
                .contentType(ContentType.JSON)
                .accept("*/*")
                .headers("Accept-Encoding", "gzip, deflate, br")
                .headers("Cache-Control", "no-cache")
                .headers("Connection", "keep-alive")
                .header(token.getAuthorizationHeader())
                .when()
                .get()
                .then()
//                .log().all()
                .statusCode(200)
                .extract().response();
        LocalDateTime end = LocalDateTime.now();
        Duration duration = Duration.between(start, end);
        logger.info("Duration" + duration);
        return this;
    }
}





