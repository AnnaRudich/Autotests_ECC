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

    public MailserviceMock(WireMock wireMock, DatabaseApi databaseApi){
        super(wireMock);
        log = LogManager.getLogger(MailserviceMock.class);
        testMobileNumber = databaseApi.findTestMobileNumbers();
    }

    public MailserviceStub addStub(){

        return stub = new MailserviceStub()
                .stubForInternalServerError()
                .stubForNotFound()
                .stubMissingToken()
                /*.test()*/;
    }

    public class MailserviceStub {

        private static final String URL = "/api/.*/sms";

        public MailserviceStub() {

            WireMock.configureFor(wireMock);
        }

        public MailserviceStub stubForInternalServerError() {

            stub(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            return this;
        }

        public MailserviceStub stubForNotFound() {
            stub(HttpStatus.SC_NOT_FOUND);
            return this;
        }

        public MailserviceStub stubMissingToken() {
            stub(HttpStatus.SC_OK);
            return this;
        }

        public MailserviceStub stub(int responseCode) {

            wireMock.stubFor(
                    any(urlMatching(URL))
                            .withRequestBody(containing(getTestMobileNumberForStatusCode(responseCode)))
                            .atPriority(1)
                            .willReturn(aResponse().withStatus(responseCode).withBody(String.format(response, responseCode))));
            return this;
        }
        public MailserviceStub test() {

            wireMock.stubFor(
                    post(urlMatching("/api/v1/email"))
                            .atPriority(2)
                            .willReturn(aResponse().withStatus(200).withBody("TOKEN:STATUS:OK")));
            return this;
        }
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

