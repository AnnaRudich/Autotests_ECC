package com.scalepoint.automation.stubs;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.scalepoint.automation.services.externalapi.DatabaseApi;
import com.scalepoint.automation.shared.TestMobileNumber;
import com.scalepoint.automation.utils.data.request.Mail;
import com.scalepoint.automation.utils.data.request.MailContent;
import com.scalepoint.automation.utils.data.request.MailListItem;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.scalepoint.automation.utils.Configuration.getEccUrl;

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
                .test();
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

//            wireMock.stubFor(
//                    post(urlMatching("/api/v1/email"))
//                            .atPriority(2)
//                            .willReturn(aResponse().withStatus(200).withBody("TOKEN:{{randomValue type='UUID'}}STATUS:OK")));
            return this;
        }

        public MailserviceStub test3(List<MailListItem> mailListItems) {

//            mailListItems.get(0).getToken();
//            String body =  null;
//            try
//            {
//                body = new ObjectMapper().writeValueAsString(mailListItems);
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//            }
//
//            wireMock.stubFor(
//                    get(urlMatching("/api/v1/email/forCase/".concat(mailListItems.get(0).getToken())))
//                            .atPriority(3)
//                            .willReturn(aResponse()
//                                    .withHeader("Content-Type", "application/json;charset=utf-8")
//                                    .withStatus(200)
//                                    .withBody(body)));
            return this;
        }

        public MailserviceStub test4(Mail mail, DatabaseApi databaseApi) {

//            String itemization = databaseApi.getItemizationCaseReferenceByClaimNumber(mail.getClaimNumber());
//
//            MailContent mailContent = MailContent.builder()
//                                                .output(String.format("<html><body>test<a href=\"%sshop/LoginToShop?selfService=true&amp;login=%s\" style=\"color: #447198\">Selvbetjening</a></body></html>",getEccUrl(), itemization))
//                                                .caseId(mail.getCaseId())
//                                                .company(mail.getCompanyCode())
//                                                .country(mail.getCountryCode())
//                                                .date(LocalDateTime.now().toString())
//                                                .eventId(mail.getEventId())
//                                                .eventType(mail.getEventType())
//                                                .from(mail.getFrom())
//                                                .replyTo(mail.getReplyTo())
//                                                .status(3)
//                                                .subject(mail.getSubject())
//                                                .token(mail.getCaseId())
//                                                .type(mail.getMailType())
//                                                .addresses(null)
//                                                .build();
//            String body =  null;
//            try
//            {
//                body = new ObjectMapper().writeValueAsString(mailContent);
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//            }
//
//            wireMock.stubFor(
//                    get(urlMatching("/api/v1/email/".concat(mail.getCaseId())))
//                            .atPriority(3)
//                            .willReturn(aResponse()
//                                    .withHeader("Content-Type", "application/json;charset=utf-8")
//                                    .withStatus(200)
//                                    .withBody(body)));
            return this;
        }

        public List<LoggedRequest> test2(){

            return wireMock
                    .find(postRequestedFor(urlMatching("/api/v1/email")));
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

