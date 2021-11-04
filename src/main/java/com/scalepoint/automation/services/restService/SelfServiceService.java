package com.scalepoint.automation.services.restService;

import com.scalepoint.automation.pageobjects.pages.MailsPage;
import com.scalepoint.automation.services.restService.common.BaseService;
import com.scalepoint.automation.utils.Configuration;
import com.scalepoint.automation.utils.data.request.CustomerMailListItem;
import com.scalepoint.automation.utils.data.request.SelfServiceInitData;
import com.scalepoint.automation.utils.data.request.SelfServiceLossItems;
import com.scalepoint.automation.utils.data.request.SelfServiceRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.scalepoint.automation.pageobjects.pages.MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME;
import static com.scalepoint.automation.services.restService.common.BasePath.*;
import static com.scalepoint.automation.utils.Configuration.*;
import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static org.assertj.core.api.Assertions.assertThat;

public class SelfServiceService extends BaseService {

    private Response response;
    private String linkToSS;


    public SelfServiceService(){

        super();
    }
    public SelfServiceService(String ssToken){

        data.setSelfServiceAccessToken(ssToken);
    }

    public Response getResponse() {
        return this.response;
    }

    public SelfServiceService requestSelfService(SelfServiceRequest selfServiceRequest) {

        this.response = given()
                .baseUri(getEccUrl())
                .sessionId(data.getEccSessionId())
                .pathParam("userId", data.getUserId())
                .contentType("application/json")
                .body(selfServiceRequest)
                .post(SELF_SERVICE_REQUEST)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().response();

        this.linkToSS = getLinkToSS(getSelfServiceEmailToken());

        return this;
    }

    public String getSelfServiceEmailToken(){

        this.response = given()
                .baseUri(getEccUrl())
                .sessionId(data.getEccSessionId())
                .pathParam("userId", data.getUserId())
                .contentType("application/json")
                .get(CUSTOMER_MAIL_LIST)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().response();

        CustomerMailListItem[] mailListItems = response.getBody().as(CustomerMailListItem[].class);

        return Arrays.stream(mailListItems)
                .filter(customerMailListItem ->
                        MailsPage.MailType.findByText(customerMailListItem.getType()).equals(SELFSERVICE_CUSTOMER_WELCOME))
                .findFirst()
                .orElseThrow(NoSuchElementException::new)
                .getToken();
    }

    public String getLinkToSS(String selfServiceEmailToken){

        this.response = given().log().all()
                .baseUri(getEccUrl())
                .sessionId(data.getEccSessionId())
                .pathParam("userId", data.getUserId())
                .pathParam("emailToken", selfServiceEmailToken)
                .contentType("application/json")
                .get(CUSTOMER_MAIL_CONTENT)
                .then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .extract().response();

        String body = response.getBody().asString();

        Matcher matcher = Pattern.compile("<a href=\\\\\"(http://.+?/shop/LoginToShop\\?selfService=true.+?)\\\\\"")
                .matcher(body);

        matcher.find();

        return matcher.group(1).replace("amp;", "");
    }

    public SelfServiceService getCaseWidget(String caseToken, String ssoToken) {

        this.response = given().log().all()
                .baseUri(getServerUrl())
                .header("Referer", Configuration.getTestWidgetProtocol().concat(String.format(Configuration.getDomainTestWidget(), "01")))
                .pathParam("caseToken", caseToken)
                .param("sso_token", ssoToken)
                .param("widget", "true")
                .param("target", "scalepoint_widget_id")
                .contentType("application/json")
                .get(SELF_SERVICE_CASE_WIDGET)
                .then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .extract().response();

        data.setSelfServiceAccessToken(response.getHeader("Access-Token"));

        return this;
    }

    public SelfServiceService loginToSS(String password){

        String body = given()
                .get(linkToSS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().response().getBody().print();

        Matcher matcher = Pattern.compile("<.+id=\"username\".+>").matcher(body);
        matcher.find();
        matcher = Pattern.compile("\\w+(-\\w+){4}").matcher(matcher.group());
        matcher.find();
        String username = matcher.group();

        this.response = given()
                .baseUri(getEnvironmentUrl())
                .formParam("username", username)
                .formParam("password", password)
                .post(SELF_SERVICE_LOGIN)
                .then()
                .statusCode(HttpStatus.SC_MOVED_TEMPORARILY)
                .extract().response();

        data.setSelfServiceAccessToken(response.getHeader("Access-Token"));

        this.response = given()
                .get(response.getHeader("Location"))
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().response();

        return this;
    }

    public SelfServiceService getInitData(String origin){

        this.response = given()
                .baseUri(getEnvironmentUrl())
                .header("Origin", origin)
                .header("Access-Token", data.getSelfServiceAccessToken())
                .contentType("application/json")
                .get(SELF_SERVICE_INIT_DATA)
                .then()
                .extract().response();

        return this;
    }

    public SelfServiceService updateLoss(SelfServiceInitData selfServiceInitData, String origin){

        this.response = given()
                .baseUri(getEnvironmentUrl())
                .header("Access-Token", data.getSelfServiceAccessToken())
                .header("Origin", origin)
                .contentType("application/json")
                .body(selfServiceInitData)
                .put(SELF_SERVICE_LOSS);

        return this;
    }

    public SelfServiceService addLossItem(SelfServiceLossItems selfServiceLossItems){

        this.response = given()
                .baseUri(getEnvironmentUrl())
                .header("Access-Token", data.getSelfServiceAccessToken())
                .contentType("application/json")
                .body(selfServiceLossItems)
                .post(SELF_SERVICE_LOSS_ITEMS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().response();

        String data = response.body().jsonPath().getString("data");

        selfServiceLossItems.setId(Integer.parseInt(data));

        return this;
    }

    public SelfServiceService addLossItem(SelfServiceLossItems selfServiceLossItems, String origin){

        this.response = given().log().all()
                .baseUri(getEnvironmentUrl())
                .header("Access-Token", data.getSelfServiceAccessToken())
                .header("Origin", origin)
                .contentType("application/json")
                .body(selfServiceLossItems)
                .post(SELF_SERVICE_LOSS_ITEMS);

        if(response.statusCode() == HttpStatus.SC_OK) {

            String data = response.body().jsonPath().getString("data");

            selfServiceLossItems.setId(Integer.parseInt(data));
        }

        return this;
    }

    public SelfServiceService updateLossItem(SelfServiceLossItems selfServiceLossItems, String origin){

        this.response = given()
                .baseUri(getEnvironmentUrl())
                .header("Access-Token", data.getSelfServiceAccessToken())
                .header("Origin", origin)
                .contentType("application/json")
                .body(selfServiceLossItems)
                .put(SELF_SERVICE_LOSS_ITEMS);

        return this;
    }

    public SelfServiceService deleteLossItem(SelfServiceLossItems selfServiceLossItems, String origin){

        this.response = given()
                .baseUri(getEnvironmentUrl())
                .pathParam("Id", selfServiceLossItems.getId())
                .header("Access-Token", data.getSelfServiceAccessToken())
                .header("Origin", origin)
                .delete(SELF_SERVICE_DELETE_LOSS_ITEMS);

        return this;
    }

    public SelfServiceService uploadAttachment(SelfServiceLossItems selfServiceLossItems, String origin, File file){

        response = given()
                .config(RestAssured
                        .config()
                        .encoderConfig(
                                encoderConfig()
                                        .encodeContentTypeAs("multipart/form-data", ContentType.TEXT))).log().all()
                .baseUri(getEnvironmentUrl())
                .queryParam("Access-Token", data.getSelfServiceAccessToken())
                .multiPart("itemId", selfServiceLossItems.getId())
                .multiPart(file)
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Origin", origin)
                .contentType("multipart/form-data; boundary=----WebKitFormBoundaryHMB5yt4LyGtoxUjZ")
                .post(SELF_SERVICE_FILES_UPLOAD).then().log().all().extract().response();

        return this;
    }

    public SelfServiceService getAttachment(SelfServiceLossItems selfServiceLossItems, String origin){

        this.response = given()
                .baseUri(getEnvironmentUrl())
                .pathParam("lossItemId", selfServiceLossItems.getId())
                .header("Access-Token", data.getSelfServiceAccessToken())
                .header("Origin", origin)
                .get(SELF_SERVICE_LOSS_ITEM_ATTACHMENTS.concat("/{lossItemId}"))
                .then()
                .extract().response();

        if(response.statusCode() == HttpStatus.SC_OK) {

            List<String> data = response.body().jsonPath().getList("data.fileGUID", String.class);

            selfServiceLossItems.setAttachments(data);
        }

        return this;
    }

    public SelfServiceService deleteAttachment(SelfServiceLossItems selfServiceLossItems, String origin){

        this.response = given()
                .baseUri(getEnvironmentUrl())
                .contentType("application/json")
                .header("Access-Token", data.getSelfServiceAccessToken())
                .header("Origin", origin)
                .body(selfServiceLossItems.getAttachments())
                .delete(SELF_SERVICE_LOSS_ITEM_ATTACHMENTS.concat("/"))
                .then()
                .extract().response();

        if(response.statusCode() == HttpStatus.SC_OK) {

            selfServiceLossItems.setAttachments(null);
        }

        return this;
    }

    public SelfServiceService submitted(){

        this.response = given()
                .baseUri(getEnvironmentUrl())
                .header("Access-Token", data.getSelfServiceAccessToken())
                .post(SELF_SERVICE_SUBMITTED);

        return this;
    }

    public SelfServiceService submitted(String origin){

        this.response = given()
                .baseUri(getEnvironmentUrl())
                .header("Access-Token", data.getSelfServiceAccessToken())
                .header("Origin", origin)
                .post(SELF_SERVICE_SUBMITTED);

        return this;
    }

    public SelfServiceService saved(String origin){

        this.response = given()
                .baseUri(getEnvironmentUrl())
                .header("Access-Token", data.getSelfServiceAccessToken())
                .header("Origin", origin)
                .post(SELF_SERVICE_SAVED)
                .then()
                .extract().response();

        return this;
    }

    public SelfServiceService reloadFunctionTemplate(){

        this.response = given()
                .baseUri(getEccUrl())
                .sessionId(data.getEccSessionId())
                .contentType("application/json")
                .post(SELF_SERVICE_RELOAD_FT)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().response();

        return this;
    }

    public SelfServiceService doAssert(Consumer<SelfServiceService.Asserts> func) {
        func.accept(new SelfServiceService.Asserts());
        return SelfServiceService.this;
    }


    public class Asserts {

        public SelfServiceService.Asserts assertStatusCodeOK() {

            response.then()
                    .statusCode(HttpStatus.SC_OK);

            return this;
        }

        public SelfServiceService.Asserts assertStatusInvalidCors() {

            String body = response.then()
                    .statusCode(HttpStatus.SC_FORBIDDEN)
                    .extract().asString();

            assertThat(body).isEqualTo("Invalid CORS request");

            return this;
        }


    }
}
