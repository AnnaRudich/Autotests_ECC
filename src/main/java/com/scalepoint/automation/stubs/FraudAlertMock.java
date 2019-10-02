package com.scalepoint.automation.stubs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.scalepoint.automation.utils.JsonUtils;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.fraudStatus.ClaimLineChanged;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.equalTo;

public class FraudAlertMock {

    protected Logger log = LogManager.getLogger(FraudAlertMock.class);

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
        public static final int POLL_INTERVAL = 10;
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

        public List<ClaimLineChanged> waitForClaimUpdatedEvents(String token, int count) {
            await()
                    .pollInterval(POLL_INTERVAL, TimeUnit.MILLISECONDS)
                    .timeout(TIMEOUT, TimeUnit.SECONDS)
                    .until(() -> getClaimEventsByToken(token)
                                    .size(),
                            equalTo(count));

            return getClaimEventsByToken(token);
        }

        public List<ClaimLineChanged> getClaimEventsByToken(String token){

            return wireMock.find(postRequestedFor(urlPathEqualTo(ROUTE_CLAIM_UPDATED)))
                    .stream()
                    .map(loggedRequest -> {
                        try {
                            return new ObjectMapper().readValue(loggedRequest.getBodyAsString(), ClaimLineChanged.class);

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .filter(claimLineChanged ->
                            claimLineChanged.getCaseClaimLineChanged().getToken().equals(token))
                    .collect(Collectors.toList());
        }

        public FraudAlertStubs printAllUnmatchedRequests(){
            wireMock
                    .findAllUnmatchedRequests()
                    .stream()
                    .forEach(loggedRequest -> log.info(loggedRequest.getBodyAsString()));
            return this;
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
        }
    }
}
