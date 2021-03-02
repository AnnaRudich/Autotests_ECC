package com.scalepoint.automation.stubs;


import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import com.scalepoint.automation.utils.JsonUtils;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class AuditMock extends EccMock {

    AuditStub stub;

    public AuditMock(WireMock wireMock){
        super(wireMock);
        log = LogManager.getLogger(AuditMock.class);
    }

    public AuditStub addStub(){

        return stub = new AuditStub()
                .auditCloseClaimEventApprovedStub()
                .auditSelfServiceSubmitApprovedStub()
                .auditFnolSubmitEventApprovedStub();
    }

    public class AuditStub {

        @Getter
        private final String baseUrl = "/auditEndpointURL/integration-api/reports";

        public AuditStub() {

            WireMock.configureFor(wireMock);
        }

        public AuditStub auditSelfServiceSubmitApprovedStub() {
            wireMock.stubFor(post(urlPathEqualTo(baseUrl))
                    .withRequestBody(getTriggerPointPattern("selfServiceSubmit"))
                    .willReturn(aResponse().withStatus(200).withBody(getAuditResponse())));
            return this;
        }

        public AuditStub auditCloseClaimEventApprovedStub() {
            wireMock.stubFor(post(urlPathEqualTo(baseUrl))
                    .withRequestBody(getTriggerPointPattern("closeClaimEvent"))
                    .willReturn(aResponse().withStatus(200).withBody(getAuditResponse())));
            return this;
        }

        public AuditStub auditFnolSubmitEventApprovedStub() {
            wireMock.stubFor(post(urlPathEqualTo(baseUrl))
                    .withRequestBody(getTriggerPointPattern("fnolSubmit"))
                    .willReturn(aResponse().withStatus(200).withBody(getAuditResponse())));
            return this;
        }

        private StringValuePattern getTriggerPointPattern(String value){

            return equalToJson(String.format("{ \"triggerPoint\": \"%s\"}", value), true, true);
        }

        private String getAuditResponse() {
            try {
                return  JsonUtils.getJSONasString("__files/auditMock/auditResponse.json");
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        }
    }
}

