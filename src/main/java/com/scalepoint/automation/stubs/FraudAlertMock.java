package com.scalepoint.automation.stubs;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.scalepoint.automation.utils.JsonUtils;
import lombok.Getter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.*;

public class FraudAlertMock {

    private static WireMock wireMock;
    private static Map<String, FraudAlertStubs> stubs = new HashMap<>();

    public FraudAlertMock(WireMock wireMock){
        this.wireMock = wireMock;
    }

    public FraudAlertStubs getStub(String tenant){
        return stubs.get(tenant);
    }

    public synchronized FraudAlertStubs addStub(String tenant) throws IOException {
        if(!stubs.containsKey(tenant)) {
            FraudAlertStubs fraudAlertStubs = new FraudAlertStubs(tenant)
                    .routeClaimUpdatedStub()
                    .tokenEndPointStub();
            stubs.put(tenant, fraudAlertStubs);
        }
        return stubs.get(tenant);
    }

    public class FraudAlertStubs {

        private static final String CONTENT_TYPE = "application/json";
        public static final String ROUTE_CLAIM_UPDATED = "/fraudAlert";
        public static final String TOKEN_ENDPOINT = "/token";
        public static final int POLL_INTERVAL = 1;
        public static final int TIMEOUT = 60;

        @Getter
        private final String tenant;

        public FraudAlertStubs(String tenant) {
            WireMock.configureFor(wireMock);
            this.tenant = tenant;
        }

        public FraudAlertStubs routeClaimUpdatedStub() throws IOException {
            stubFor(post(urlPathEqualTo(TOKEN_ENDPOINT))
                    .willReturn(aResponse()
                            .withHeader("Content-Type", CONTENT_TYPE)
                            .withBody(JsonUtils.getJSONasString("__files.fraudAlert/routClaimUpdated.json"))
                            .withStatus(200)));

            return this;
        }
        public FraudAlertStubs tokenEndPointStub(){
            stubFor(post(urlPathEqualTo(ROUTE_CLAIM_UPDATED))
                    .willReturn(aResponse()
                            .withStatus(202)));

            return this;
        }

        public JsonNode waitForClaimUpdatedEvent(String token, String eventId) {
            return await()
                    .pollInterval(POLL_INTERVAL, TimeUnit.SECONDS)
                    .timeout(TIMEOUT, TimeUnit.SECONDS)
                    .until(() -> {
                        try {
                            return wireMock.find(postRequestedFor(urlPathEqualTo(ROUTE_CLAIM_UPDATED)))
                                    .stream()
                                    .map(loggedRequest -> JsonUtils.stringToJsonNode(loggedRequest.getBodyAsString()))
                                    .filter(jsonNode -> jsonNode.path("case").path("token").textValue().equals(token))
                                    .filter(jsonNode -> jsonNode
                                            .path("eventId")
                                            .asText()
                                            .equals(eventId))
                                    .findFirst()
                                    .get();
                        } catch (NoSuchElementException e) {
                            return null;
                        }
                    }, is(not(nullValue())));
        }
        public FraudAlertStubs doValidation(Consumer<SchemaValidation> schemaValidationFunc) {
            schemaValidationFunc.accept(new SchemaValidation());
            return FraudAlertStubs.this;
        }

        public class SchemaValidation {

            JsonSchema templateGenerate;
            FraudAlertStubs fraudAlertStubs;

            public SchemaValidation() {
                try {
                    templateGenerate = JsonSchemaFactory
                            .byDefault()
                            .getJsonSchema(JsonUtils.getJSONfromResources("schema/outputManagement/templateGenerateSchema.json"));
                } catch (ProcessingException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

//            public SchemaValidation validateTemplateGenerateSchema(String claimNumber) {
//                List jsonBodies = wireMock.find(postRequestedFor(urlPathEqualTo(ROUTE_CLAIM_UPDATED)))
//                        .stream()
//                        .filter(loggedRequest ->
//                                JsonUtils.stringToJsonNode(loggedRequest.getBodyAsString())
//                                        .path("data")
//                                        .path("claim")
//                                        .path("claimNumber")
//                                        .asText()
//                                        .equals(claimNumber)
//                        )
//                        .map(loggedRequest -> loggedRequest.getBodyAsString())
//                        .collect(Collectors.toList());
//
//                jsonBodies.stream().forEach(body -> {
//                    ProcessingReport report;
//                    try {
//                        report = templateGenerate.validate(JsonUtils.stringToJsonNode((String) body));
//                    } catch (ProcessingException e) {
//                        throw new RuntimeException(e);
//                    }
//                    assertThat(report.isSuccess())
//                            .as(String.format("Template generate schema validation: %s", report))
//                            .isTrue();
//                });
//
//                return this;
//            }
        }
    }
}
