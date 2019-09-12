package com.scalepoint.automation.services.restService;

import com.google.common.io.CharStreams;
import com.scalepoint.automation.services.restService.Common.BaseService;
import com.scalepoint.automation.utils.Configuration;
import com.scalepoint.automation.utils.data.TestData;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.scalepoint.automation.services.restService.Common.BasePath.EXCEL;
import static com.scalepoint.automation.services.restService.Common.BasePath.MATCH;
import static com.scalepoint.automation.utils.Configuration.getEccUrl;
import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;

/**
 * Created by bza on 6/29/2017.
 */
public class ImportExcelService extends BaseService {

    private Response response;
    private List<String> referenceNumbers = new LinkedList<>();
    private String importSummary;

    public Response getResponse() {
        return this.response;
    }

    public ImportExcelService excel() throws IOException {

        File file = new File("C:\\Users\\bna\\IdeaProjects\\automatedtest\\src\\main\\resources\\excelImport\\DK_NYT ARK(3)(a).xls");

        String boundary = "26639617130818";
        response = given().baseUri(getEccUrl())
                .sessionId(data.getEccSessionId())
                .config(config()
                        .encoderConfig(encoderConfig()
                                .encodeContentTypeAs("multipart/form-data", ContentType.TEXT)))
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Accept-Language", "pl-PL,pl;q=0.9,en-US;q=0.8,en;q=0.7")
                .pathParam("userId", data.getUserId())
                .contentType("multipart/form-data; boundary=".concat(boundary))
                .body("--"
                        .concat(boundary)
                        .concat("\r\nContent-Disposition: form-data; name=\"upfile\"; filename=\"DK_NYT ARK(3)(a).xls\"\r\nContent-Type: application/vnd.ms-excel\r\n\r\n")
                        .concat(CharStreams.toString(new InputStreamReader(new FileInputStream(file), Charset.forName("ISO-8859-1")))))
                .post(EXCEL)
                .then()
                .statusCode(HttpStatus.SC_OK).extract().response();

        String body = response.getBody().print();
        Matcher refNum = Pattern.compile("\"referenceNumber\":\\d*,").matcher(body);
        Matcher impSum = Pattern.compile("ImportSummary\\(\\d*,").matcher(body);
        impSum.find();

        importSummary = impSum.group().replace("ImportSummary(", "").replace(",", "");

        while (refNum.find()) {
            referenceNumbers.add(refNum.group().replace("\"referenceNumber\":", "").replace(",", ""));
        }

        return this;
    }

    public ImportExcelService match() throws IOException, ParseException {

        InputStream importLinesFile = TestData.getInputStreamFromResources(Configuration.getLocale().getValue(), "ImportLines.json");
        JSONArray importLinesArray = (JSONArray) new JSONParser().parse(new InputStreamReader(importLinesFile));
        for(int i=0;i<importLinesArray.size();i++){
            ((JSONObject)importLinesArray.get(i)).replace("id", new Long(referenceNumbers.get(i)));
        }

        InputStream importSummaryFile = TestData.getInputStreamFromResources(Configuration.getLocale().getValue(), "ImportSummary.json");
        JSONObject importSummaryObject = (JSONObject) new JSONParser().parse(new InputStreamReader(importSummaryFile));
        importSummaryObject.replace("id", new Long(importSummary));

        response = given().baseUri(getEccUrl()).log().uri()
                .sessionId(data.getEccSessionId())
                .pathParam("userId", data.getUserId())
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .formParam("importLines", importLinesArray.toString())
                .formParam("importSummary", importSummaryObject.toString())
                .post(MATCH)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().response();

        return this;
    }
}
