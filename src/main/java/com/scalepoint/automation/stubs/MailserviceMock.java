package com.scalepoint.automation.stubs;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.scalepoint.automation.services.externalapi.DatabaseApi;
import com.scalepoint.automation.shared.TestMobileNumber;
import com.scalepoint.automation.utils.data.request.Mail;
import com.scalepoint.automation.utils.data.request.MailListItem;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

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
                .stubDateOfFirstSelfServiceWelcome()
                .stubEmail()
                .test();
    }

    public class MailserviceStub {

        private static final String SMS_URL = "/api/.*/sms";

        public MailserviceStub() {

            WireMock.configureFor(wireMock);
        }

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
                                    .withBody(String.format("{content=\"TOKEN:%s;STATUS:OK\", binary=false, json=false}", UUID.randomUUID().toString()))));

            return this;
        }

        public MailserviceStub stubSMS(int responseCode) {

            wireMock.stubFor(
                    any(urlMatching(SMS_URL))
                            .withRequestBody(containing(getTestMobileNumberForStatusCode(responseCode)))
                            .atPriority(1)
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

        public MailserviceStub test() {

//            wireMock.stubFor(
//                    post(urlMatching("/api/v1/email"))
//                            .atPriority(2)
//                            .willReturn(aResponse().withStatus(200).withBody("TOKEN:{{randomValue type='UUID'}}STATUS:OK")));
            return this;
        }

        public MailserviceStub forCase(List<MailListItem> mailListItems) {

            mailListItems.get(0).getToken();
            String body =  null;
            try
            {
                body = new ObjectMapper().writeValueAsString(mailListItems);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            wireMock.stubFor(
                    get(urlMatching("/api/v1/email/forCase/".concat(mailListItems.get(0).getToken())))
                            .atPriority(3)
                            .willReturn(aResponse()
                                    .withHeader("Content-Type", "application/json;charset=utf-8")
                                    .withStatus(200)
                                    .withBody(body)));
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

        public List<LoggedRequest> findSentEmails(){

            return wireMock
                    .find(postRequestedFor(urlMatching("/api/v1/email")));
        }
    }


}

