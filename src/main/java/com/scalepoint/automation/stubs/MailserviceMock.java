package com.scalepoint.automation.stubs;


import com.github.tomakehurst.wiremock.client.WireMock;
import com.scalepoint.automation.services.externalapi.DatabaseApi;
import com.scalepoint.automation.shared.TestMobileNumber;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;

import java.util.List;
import java.util.NoSuchElementException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class MailserviceMock extends EccMock {

    MailserviceStub stub;
    List<TestMobileNumber> testMobileNumber;
    String response = "Mock %d";
    DatabaseApi databaseApi;

    public MailserviceMock(WireMock wireMock, DatabaseApi databaseApi){

        super(wireMock);
        log = LogManager.getLogger(MailserviceMock.class);
        testMobileNumber = databaseApi.findTestMobileNumbers();
        this.databaseApi = databaseApi;
    }

    public MailserviceStub addStub(){

        return stub = new MailserviceStub()
                .stubForInternalServerError()
                .stubForNotFound()
                .stubMissingToken()
                .stubDateOfFirstSelfServiceWelcome()
                .stubEmail();
    }

    public class MailserviceStub {

        private static final String SMS_URL = "/api/.*/sms";

        public MailserviceStub stubForInternalServerError() {

            stubSMS(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            return this;
        }

        public MailserviceStub stubForNotFound() {
            stubSMS(HttpStatus.SC_NOT_FOUND);
            return this;
        }

        public MailserviceStub stubMissingToken() {
            stubSMS(HttpStatus.SC_OK);
            return this;
        }

        public MailserviceStub stubDateOfFirstSelfServiceWelcome(){

            wireMock.stubFor(
                    get(urlMatching("/api/v1/email/dateOfFirstSelfServiceWelcome/.*"))
                            .atPriority(1)
                            .willReturn(aResponse()
                                    .withStatus(204)
                                    .withBody("{content=null, binary=true, json=false")));

            return this;
        }

        public MailserviceStub stubEmail(){

            wireMock.stubFor(
                    post(urlEqualTo("/api/v1/email"))
                            .atPriority(1)
                            .willReturn(aResponse()
                                    .withStatus(200)
                                    .withTransformers("mailServiceTransformer")));

            return this;
        }

        public MailserviceStub stubSMS(int responseCode) {

            wireMock.stubFor(
                    any(urlMatching(SMS_URL))
                            .atPriority(1)
                            .withRequestBody(containing(getTestMobileNumberForStatusCode(responseCode)))
                            .willReturn(aResponse().withStatus(responseCode).withBody(String.format(response, responseCode))));
            return this;
        }

        public String getTestMobileNumberForStatusCode(int responseCode){

            return  testMobileNumber
                    .stream()
                    .filter(testMobile -> testMobile.getTestMobileOwner().contains(String.valueOf(responseCode)))
                    .findFirst()
                    .orElseThrow(NoSuchElementException::new)
                    .getTestMobileNumber();
        }
    }
}

