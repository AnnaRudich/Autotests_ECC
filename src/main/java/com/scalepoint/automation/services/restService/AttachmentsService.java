package com.scalepoint.automation.services.restService;

import com.google.common.io.CharStreams;
import com.scalepoint.automation.services.restService.Common.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
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

    private static final String ACCEPT = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9";
    private static final String ACCEPT_ENCODING = "gzip, deflate, br";
    private static final String ACCEPT_LANGUAGE = "pl-PL,pl;q=0.9,en-US;q=0.8,en;q=0.7";
    private static final String BOUNDARY = "132425116134";
    private static final String CHARSET = "ISO-8859-1";
    private static final String ATTACHMENT_NAME = "attachmentName";
    private static final String CONTENT_TYPE = "image/jpeg";
    private static final String MULTIPART_FORM_DATA_PATTERN = "--%s\r\nContent-Disposition: form-data; name=\"%s\"; filename=\"%s\"\r\nContent-Type: %s\r\n\r\n";
    private static final String TYPE = "IMAGE";
    private static final String LEVEL = "root";

    private final Integer userId;
    private final File file;
    private final String fileName;
    private final String attachmentStream;

    public AttachmentsService() throws IOException {

        this.userId = data.getUserId();
        this.file = new File("src\\main\\resources\\attachments\\5mb_attachment.jpg");
        this.fileName = file.getName();
        this.attachmentStream = CharStreams.toString(new InputStreamReader(new FileInputStream(file), Charset.forName(CHARSET)));
    }

    public Response getResponse() {
        return this.response;
    }

    public AttachmentsService addAttachmentToClaimLevel() throws IOException {

        addAttachment(getAddAttachmentRequestSpecification());

        return this;
    }

    public AttachmentsService addAttachmentToClaimLineLevel(int lineId) throws IOException {

        RequestSpecification requestSpecification = getAddAttachmentRequestSpecification()
                .queryParam("matchId", getTree().getChildren().get(lineId).getId());

        addAttachment(requestSpecification);

        return this;
    }

    public AttachmentsService linkAttachmentToClaimLineLevel(int lineTo, int lineFrom){

        AttachmentsPayload attachmentsPayload = getAttachmentPayloadBuilder()
                .coveredIds("1")
                .matchId(getTree().getChildren().get(lineFrom).getId())
                .id("Settlement.AttachmentModel-6")
                .build();

        linkAttachment(getAttachmentsMapPayload(lineTo, attachmentsPayload));

        return this;
    }

    public AttachmentsService linkAttachmentToClaimLineLevel(int lineTo){

        AttachmentsPayload attachmentsPayload = getAttachmentPayloadBuilder()
                .coveredIds("")
                .matchId(0)
                .id("Settlement.AttachmentModel-1")
                .build();

        linkAttachment(getAttachmentsMapPayload(lineTo, attachmentsPayload));

        return this;
    }

    private AttachmentsTree getTree(){

        return given().baseUri(getEccUrl())
//                .log().all()
                .sessionId(data.getEccSessionId())
                .queryParam("_dc", LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond())
                .queryParam("shnbr", userId)
                .pathParam("userId", userId)
                .get(BasePath.ATTACHMENTS.concat("/tree"))
                .then()
                .statusCode(HttpStatus.SC_OK)
//                .log().all()
                .extract()
                .as(AttachmentsTree[].class)[0];
    }

    private RequestSpecification getAddAttachmentRequestSpecification() throws IOException {

        return given().baseUri(getEccUrl())
//                .log().all()
                .sessionId(data.getEccSessionId())
                .config(config()
                        .encoderConfig(encoderConfig()
                                .encodeContentTypeAs("multipart/form-data", ContentType.TEXT)))
                .header("Accept", ACCEPT)
                .header("Accept-Encoding", ACCEPT_ENCODING)
                .header("Accept-Language", ACCEPT_LANGUAGE)
                .queryParam("fname", "C:\\fakepath\\".concat(fileName))
                .queryParam("customer_id", userId)
                .pathParam("userId", userId)
                .contentType("multipart/form-data; boundary=".concat(BOUNDARY))
                .body(String.format(MULTIPART_FORM_DATA_PATTERN, BOUNDARY, ATTACHMENT_NAME, fileName, CONTENT_TYPE)
                        .concat(CharStreams.toString(new InputStreamReader(new FileInputStream(file), Charset.forName(CHARSET)))));
    }

    private AttachmentsPayload.AttachmentsPayloadBuilder getAttachmentPayloadBuilder(){

        return AttachmentsPayload.builder()
                .name(fileName)
                .type(TYPE)
                .level(LEVEL)
                .guid(UUID.randomUUID().toString());
    }

    private AttachmentsMapPayload getAttachmentsMapPayload(int lineTo, AttachmentsPayload attachmentsPayload){

        return AttachmentsMapPayload
                .builder()
                .matchIdToMap(getTree().getChildren().get(lineTo).getId().toString())
                .attachments(Arrays.asList(attachmentsPayload))
                .build();
    }

    private void addAttachment(RequestSpecification requestSpecification){

        requestSpecification
                .post(BasePath.ATTACHMENTS)
                .then()
                .statusCode(HttpStatus.SC_OK);
//                .log().all();
    }
    private void linkAttachment(AttachmentsMapPayload attachmentsMapPayload){

        given().baseUri(getEccUrl())
//                .log().all()
                .sessionId(data.getEccSessionId())
                .header("Content-Type", "application/json")
                .queryParam("customer_id", userId)
                .pathParam("userId", userId)
                .body(attachmentsMapPayload)
                .post(BasePath.ATTACHMENTS + "/mapped")
                .then()
                .statusCode(HttpStatus.SC_OK);
//                .log().all();
    }
}
