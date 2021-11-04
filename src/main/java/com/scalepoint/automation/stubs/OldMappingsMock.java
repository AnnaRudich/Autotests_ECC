package com.scalepoint.automation.stubs;

import com.github.tomakehurst.wiremock.client.WireMock;

import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class OldMappingsMock extends EccMock{

    public OldMappingsMock(WireMock wireMock) {
        super(wireMock);
    }

    public OldMappingsMock registerStubs(){

        claimAutComplete();
        cwaEventSink();
        ecbTenantsDiagnosticPing();
        ecbTenantsHealthCheck();
        ecbTenantsInfo();
        healthCheck();
        ip2();
        mailServiceProxyHealthCheck();
        mailServiceProxyMail();
        rnvFeedbackApprove();
        rnvFeedbackManual();
        rnvFeedbackReject();
        sms();

        return this;
    }

    private void claimAutComplete(){

        wireMock.stubFor(
                post(urlEqualTo("/auditEndpointURL/integration-api/reports"))
                        .withRequestBody(equalToJson("{ \"triggerPoint\": \"rvAutoAccept\"}", true, true))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withBody("{\"success\":true,\"errorDescription\":null,\"payload\":{\"evaluationResult\":\"Approve\",\"newAssigneeType\":\"\",\"newAssigneeId\":\"\",\"isSelectedForTraining\":false,\"triggeredRules\":[]}}")));
    }

    private void cwaEventSink(){

        wireMock.stubFor(
                head(urlEqualTo("/ecx/codan/api/v1/EventSink/Create"))
                        .willReturn(aResponse()
                                .withStatus(200)));
    }

    private void ecbTenantsDiagnosticPing(){

        wireMock.stubFor(
                get(urlMatching("/ecx/.*/diagnostics/ping"))
                        .willReturn(aResponse()
                                .withStatus(200)));
    }

    private void ecbTenantsHealthCheck(){

        wireMock.stubFor(
                get(urlMatching("/ecx/.*/healthCheck"))
                        .willReturn(aResponse()
                                .withStatus(200)));
    }

    private void ecbTenantsInfo(){

        wireMock.stubFor(
                get(urlEqualTo("/ecx/host/api/tenantsinfo"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json; charset=utf-8")
                                .withBody("{\"tenants\": {\n        \"codan\": {\n            \"countryCode\": \"DK\"\n        },\n        \"gjensidige\": {\n            \"countryCode\": \"DK\"\n        },\n        \"trygghansa\": {\n            \"countryCode\": \"SE\"\n        },\n        \"topdanmark\": {\n            \"countryCode\": \"DK\"\n        },\n        \"storebrand\": {\n            \"countryCode\": \"NO\"\n        },\n        \"codanhealth\": {\n            \"countryCode\": \"DK\"\n        },\n        \"thisted\": {\n            \"countryCode\": \"DK\"\n        }\n    }\n}")));
    }

    private void healthCheck(){

        wireMock.stubFor(
                get(urlEqualTo("/auditEndpointURL/healthCheck"))
                        .willReturn(aResponse()
                                .withStatus(200)));
    }

    private void ip2(){

        wireMock.stubFor(
                post(urlEqualTo("/externalIntegration/testic"))
                        .willReturn(aResponse()
                                .withStatus(200)));
    }

    private void mailServiceProxyHealthCheck(){

        wireMock.stubFor(
                any(urlMatching("/mailservice.asmx"))
                        .atPriority(10)
                        .willReturn(aResponse()
                                .proxiedFrom("https://qa-shr-ms.spcph.local")));
    }

    private void mailServiceProxyMail(){

        wireMock.stubFor(
                any(urlMatching("/api/v1/email.*"))
                        .atPriority(10)
                        .willReturn(aResponse()
                                .proxiedFrom("https://qa-shr-ms.spcph.local")));
    }

    private void rnvFeedbackApprove(){

        wireMock.stubFor(
                post(urlEqualTo("/auditEndpointURL/integration-api/reports"))
                        .withRequestBody(matchingJsonPath("$.serviceLinesReceived[?(@.repairPrice=='50')]"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withBody("{\"success\":true,\"errorDescription\":null,\"payload\":{\"evaluationResult\":\"Approve\",\"newAssigneeType\":\"\",\"newAssigneeId\":\"\",\"isSelectedForTraining\":false,\"triggeredRules\":[]}}")));
    }

    private void rnvFeedbackManual(){

        wireMock.stubFor(
                post(urlEqualTo("/auditEndpointURL/integration-api/reports"))
                        .withRequestBody(matchingJsonPath("$.serviceLinesReceived[?(@.repairPrice=='100')]"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withBody("{\"success\":true,\"errorDescription\":null,\"payload\":{\"evaluationResult\":\"ManualEvaluation\",\"newAssigneeType\":\"\",\"newAssigneeId\":\"\",\"isSelectedForTraining\":false,\"triggeredRules\":[]}}")));
    }

    private void rnvFeedbackReject(){

        wireMock.stubFor(
                post(urlEqualTo("/auditEndpointURL/integration-api/reports"))
                        .withRequestBody(matchingJsonPath("$.serviceLinesReceived[?(@.repairPrice=='10')]"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withBody("{\"success\":true,\"errorDescription\":null,\"payload\":{\"evaluationResult\":\"Reject\",\"newAssigneeType\":\"\",\"newAssigneeId\":\"\",\"isSelectedForTraining\":false,\"triggeredRules\":[]}}")));
    }

    private void sms(){

        wireMock.stubFor(
                post(urlMatching("/api/.*/sms"))
                        .atPriority(9)
                        .withRequestBody(matchingJsonPath("$.serviceLinesReceived[?(@.repairPrice=='10')]"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withBody("TOKEN:".concat(UUID.randomUUID().toString()))));
    }
}
