package com.scalepoint.automation.services.restService;

import com.scalepoint.automation.services.restService.common.BaseService;
import com.scalepoint.automation.utils.data.response.Token;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

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

        this.response = given()
                .baseUri("https://dev-ecc-01.spcph.test")
                .basePath("/api/fs/files/{locale}/{tenant}/{fileGUID}")
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
                .statusCode(200)
                .extract()
                .response();

        return this;
    }
}





