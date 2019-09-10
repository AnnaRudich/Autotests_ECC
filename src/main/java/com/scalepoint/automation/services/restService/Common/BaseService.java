package com.scalepoint.automation.services.restService.Common;

import com.scalepoint.automation.services.externalapi.OauthTestAccountsApi;
import com.scalepoint.automation.services.restService.ClaimSettlementItemsService;
import com.scalepoint.automation.services.restService.CreateClaimService;
import com.scalepoint.automation.services.restService.LoginProcessService;
import com.scalepoint.automation.services.restService.SelfServiceService;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import com.scalepoint.automation.utils.data.request.InsertSettlementItem;
import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.config.DecoderConfig;
import io.restassured.config.EncoderConfig;
import io.restassured.config.MultiPartConfig;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

import static com.scalepoint.automation.services.restService.Common.BasePath.EXCEL;
import static com.scalepoint.automation.utils.Configuration.getEccAdminUrl;
import static com.scalepoint.automation.utils.Configuration.getEccUrl;
import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static io.restassured.config.MultiPartConfig.multiPartConfig;

/**
 * Created by bza on 6/29/2017.
 */
public class BaseService {

    protected Logger logger = LogManager.getLogger(getClass());
    protected Data data;

    public Data getData() {
        return data;
    }

    public BaseService() {
        this.data = ServiceData.getData();
        if (getEccAdminUrl().contains("localhost")) {
            RestAssured.port = 80;
        }
    }

    public String getClaimTokenWithoutPrefix() {
        return data.getClaimToken().replace("c.", "");
    }

    public void setUserIdByClaimToken() {
        data.setUserId(data.getDatabaseApi().getUserIdByClaimToken(getClaimTokenWithoutPrefix()));
    }

    public static ClaimSettlementItemsService loginAndOpenClaimWithItems(User user, ClaimRequest claimRequest, InsertSettlementItem... items) {
        return loginAndOpenClaim(user, claimRequest)
                .claimLines()
                .addLines(items);
    }

    public static CreateClaimService loginAndOpenClaim(User user, ClaimRequest claimRequest){
        return new LoginProcessService()
                .login(user)
                .createClaim(new OauthTestAccountsApi().sendRequest().getToken())
                .addClaim(claimRequest)
                .openClaim();
    }

    public static LoginProcessService loginUser(User user) {
        return new LoginProcessService()
                .login(user);
    }

    public SelfServiceService importExcel() throws IOException {


        String boundary = "26639617130818";
        given().baseUri(getEccUrl()).log().all()
                .sessionId(data.getEccSessionId())
//                .header("Content-Length", "199890")

                .config(config()
                        .encoderConfig(encoderConfig()
                                .encodeContentTypeAs("multipart/form-data", ContentType.TEXT))
                        .multiPartConfig(MultiPartConfig
                                .multiPartConfig()
                                .defaultControlName("upfile")
                                .defaultFileName("DK_NYT ARK(3)(a).xls")
//                                .defaultBoundary("---------------------------26639617130818")
                        )
                )
//                .multiPart(new MultiPartSpecBuilder(new File("C:\\Users\\bna\\IdeaProjects\\automatedtest\\src\\main\\resources\\excelImport\\DK_NYT ARK(3)(a).xls"))
//                        .fileName("DK_NYT ARK(3)(a).xls")
//                        .controlName("upfile")
//                        .mimeType("application/vnd.ms-excel")
//                        .build())
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Accept-Language", "pl-PL,pl;q=0.9,en-US;q=0.8,en;q=0.7")
//                .header("Cache-Control", "max-age=0")
                .pathParam("userId", data.getUserId())
                .contentType("multipart/form-data; boundary=".concat(boundary))
//                .contentType("application/vnd.ms-excel")
//                .body(new File("C:\\Users\\bna\\IdeaProjects\\automatedtest\\src\\main\\resources\\excelImport\\DK_NYT ARK(3)(a).xls"))
//               .multiPart(new MultiPartSpecBuilder(new String(Files.readAllBytes(new File("C:\\Users\\bna\\IdeaProjects\\automatedtest\\src\\main\\resources\\excelImport\\DK_NYT ARK(3)(a).xls").toPath())))
//                       .fileName("DK_NYT ARK(3)(a).xls")
//                        .controlName("upfile")
//                        .mimeType("application/vnd.ms-excel")
//                        .build())
                .body("--".concat(boundary).concat("\r\n\r\nContent-Disposition: form-data; name=\"upfile\"; filename=\"DK_NYT ARK(3)(a).xls\"\r\nContent-Type: application/vnd.ms-excel\r\n\r\n").concat(new String(Files.readAllBytes(new File("C:\\Users\\bna\\IdeaProjects\\automatedtest\\src\\main\\resources\\excelImport\\DK_NYT ARK(3)(a).xls").toPath()))))
                .post(EXCEL)
                .then().log().all().statusCode(HttpStatus.SC_OK).extract().response();

        return new SelfServiceService();
    }
}
