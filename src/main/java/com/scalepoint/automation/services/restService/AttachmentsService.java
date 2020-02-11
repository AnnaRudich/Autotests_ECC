package com.scalepoint.automation.services.restService;

import com.google.common.io.CharStreams;
import com.scalepoint.automation.services.restService.Common.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.UUID;

import static com.scalepoint.automation.utils.Configuration.getEccUrl;
import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;

public class AttachmentsService extends BaseService {

    private Response response;
    Integer userId = data.getUserId();

    public Response getResponse() {
        return this.response;
    }

    private AttachmentsTree getTree(){

        return given().baseUri(getEccUrl()).log().all()
                .sessionId(data.getEccSessionId())
                .queryParam("_dc", LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond())
                .queryParam("shnbr", userId)
                .pathParam("userId", userId)
                .get(BasePath.ATTACHMENTS + "/tree")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .log().all()
                .extract()
                .as(AttachmentsTree[].class)[0];
    }

    public AttachmentsService addAttachmentToClaimLevel() throws IOException {

        File file = new File("C:\\Users\\bna\\Desktop\\rnv_issue.png");

        String boundary = "132425116134";
        given().baseUri(getEccUrl())
                .sessionId(data.getEccSessionId())
                .config(config()
                        .encoderConfig(encoderConfig()
                                .encodeContentTypeAs("multipart/form-data", ContentType.TEXT)))
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Accept-Language", "pl-PL,pl;q=0.9,en-US;q=0.8,en;q=0.7")
                .queryParam("fname", "test")
                .queryParam("customer_id", userId)
                .pathParam("userId", userId)
                .contentType("multipart/form-data; boundary=".concat(boundary))
                .body("--"
                        .concat(boundary)
                        .concat("\r\nContent-Disposition: form-data; name=\"attachmentName\"; filename=\"rnv_issue.png\"\r\nContent-Type: image/png\r\n\r\n")
                        .concat(CharStreams.toString(new InputStreamReader(new FileInputStream(file), Charset.forName("ISO-8859-1")))))
                .post(BasePath.ATTACHMENTS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .log().all();

        return this;
    }

    public AttachmentsService addAttachmentToClaimLineLevel(int lineId) throws IOException {

        File file = new File("C:\\Users\\bna\\Desktop\\rnv_issue.png");

        String boundary = "132425116134";
        given().baseUri(getEccUrl())
                .sessionId(data.getEccSessionId())
                .config(config()
                        .encoderConfig(encoderConfig()
                                .encodeContentTypeAs("multipart/form-data", ContentType.TEXT)))
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Accept-Language", "pl-PL,pl;q=0.9,en-US;q=0.8,en;q=0.7")
                .queryParam("fname", "test")
                .queryParam("customer_id", userId)
                .queryParam("matchId", getTree().getChildren().get(lineId).getId())
                .pathParam("userId", userId)
                .contentType("multipart/form-data; boundary=".concat(boundary))
                .body("--"
                        .concat(boundary)
                        .concat("\r\nContent-Disposition: form-data; name=\"attachmentName\"; filename=\"rnv_issue.png\"\r\nContent-Type: image/png\r\n\r\n")
                        .concat(CharStreams.toString(new InputStreamReader(new FileInputStream(file), Charset.forName("ISO-8859-1")))))
                .post(BasePath.ATTACHMENTS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .log().all();

        return this;
    }

    public AttachmentsService linkAttachmentToClaimLineLevel(int lineTo, int lineFrom){


        AttachmentsMapPayload attachmentsMapPayload = AttachmentsMapPayload
                .builder()
                .matchIdToMap(getTree().getChildren().get(lineTo).getId().toString())
                .attachments(Arrays.asList(AttachmentsPayload.builder()
                        .name("rnv_issue.png")
                        .type("IMAGE")
                        .level("root")
                        .coveredIds("1")
                        .matchId(getTree().getChildren().get(lineFrom).getId())
                        .guid(UUID.randomUUID().toString())
                        .id("Settlement.AttachmentModel-6")
                        .build()))
                .build();

        given().baseUri(getEccUrl()).log().all()
                .sessionId(data.getEccSessionId())
                .header("Content-Type", "application/json")
                .queryParam("customer_id", userId)
                .pathParam("userId", userId)
                .body(attachmentsMapPayload)
                .post(BasePath.ATTACHMENTS + "/mapped")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .log().all();

        return this;
    }

    public AttachmentsService linkAttachmentToClaimLineLevel(int lineTo){


        AttachmentsMapPayload attachmentsMapPayload = AttachmentsMapPayload
                .builder()
                .matchIdToMap(getTree().getChildren().get(lineTo).getId().toString())
                .attachments(Arrays.asList(AttachmentsPayload.builder()
                        .name("rnv_issue.png")
                        .type("IMAGE")
                        .level("root")
                        .coveredIds("")
                        .matchId(0)
                        .guid(UUID.randomUUID().toString())
                        .id("Settlement.AttachmentModel-1")
                        .build()))
                .build();

        given().baseUri(getEccUrl()).log().all()
                .sessionId(data.getEccSessionId())
                .header("Content-Type", "application/json")
                .queryParam("customer_id", userId)
                .pathParam("userId", userId)
                .body(attachmentsMapPayload)
                .post(BasePath.ATTACHMENTS + "/mapped")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .log().all();

        return this;
    }
}
