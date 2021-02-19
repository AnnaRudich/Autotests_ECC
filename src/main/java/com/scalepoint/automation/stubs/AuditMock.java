package com.scalepoint.automation.stubs;


import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class AuditMock {

    protected Logger logger = LogManager.getLogger(AuditMock.class);

    private static WireMock wireMock;
    AuditStub stub;

    public AuditMock(WireMock wireMock){
        this.wireMock = wireMock;
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

        public AuditStub auditSelfServiceSubmitApprovedStub(){
            wireMock.stubFor(post(urlPathEqualTo(baseUrl))
                    .withRequestBody(equalToJson("{ \"triggerPoint\": \"selfServiceSubmit\"}", true, true))
                    .willReturn(aResponse().withStatus(200).withBody("{\"success\":true,\"errorDescription\":null,\"payload\":{\"evaluationResult\":\"Approve\",\"newAssigneeType\":\"\",\"newAssigneeId\":\"\",\"isSelectedForTraining\":false,\"triggeredRules\":[]}}")));
            return this;
        }

        public AuditStub auditCloseClaimEventApprovedStub(){
            wireMock.stubFor(post(urlPathEqualTo(baseUrl))
                    .withRequestBody(equalToJson("{ \"triggerPoint\": \"closeClaimEvent\"}", true, true))
                    .willReturn(aResponse().withStatus(200).withBody("{\"success\":true,\"errorDescription\":null,\"payload\":{\"evaluationResult\":\"Approve\",\"newAssigneeType\":\"\",\"newAssigneeId\":\"\",\"isSelectedForTraining\":false,\"triggeredRules\":[]}}")));
            return this;
        }

        public AuditStub auditFnolSubmitEventApprovedStub(){
            wireMock.stubFor(post(urlPathEqualTo(baseUrl))
                    .withRequestBody(equalToJson("{ \"triggerPoint\": \"fnolSubmit\"}", true, true))
                    .willReturn(aResponse().withStatus(200).withBody("{\"success\":true,\"errorDescription\":null,\"payload\":{\"evaluationResult\":\"Approve\",\"newAssigneeType\":\"\",\"newAssigneeId\":\"\",\"isSelectedForTraining\":false,\"triggeredRules\":[]}}")));
            return this;
        }
    }
}

