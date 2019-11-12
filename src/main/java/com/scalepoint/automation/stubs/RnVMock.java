package com.scalepoint.automation.stubs;


import com.github.tomakehurst.wiremock.client.WireMock;
import lombok.Getter;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class RnVMock {

    private static WireMock wireMock;
    RnvStub stub;

    public RnVMock(WireMock wireMock){
        this.wireMock = wireMock;
    }

    public RnvStub addStub() {

        return stub = new RnvStub().rvTaskWebServiceUrlStub();
    }

    public class RnvStub {
        @Getter
        private final String baseUrl;
//        private final String tenant;
//        @Getter
//        public final String templatesQuery;
//        @Getter
//        public final String templatesGenerate;
//        private final String CONTENT_TYPE = "application/json";



        public RnvStub() {
            WireMock.configureFor(wireMock);
            baseUrl = "/rnv/rvTaskWebServiceUrl";
        }

        public RnvStub rvTaskWebServiceUrlStub() {

            stubFor(post(urlPathEqualTo(baseUrl))
                    .willReturn(aResponse()
                            .withStatus(200)));
            return this;
        }

//        public RnvStub templatesGenerateStub() throws IOException {
//
//            stubFor(post(urlPathEqualTo(templatesGenerate))
//                    .willReturn(aResponse()
//                            .withHeader("Content-Type", CONTENT_TYPE)
//                            .withBody(JsonUtils.getJSONasString("__files/communicationDesignerMock/selfServiceCustomerWelcomTemplate.json"))
//                            .withStatus(200)));
//            return this;
//        }

//        public RnvStub doValidation(Consumer<SchemaValidation> schemaValidationFunc) {
//            schemaValidationFunc.accept(new SchemaValidation());
//            return CommunicationDesignerStub.this;
//        }

//        public class SchemaValidation {
//
//            JsonSchema templateGenerate;
//            CommunicationDesignerStub communicationDesignerStubs;
//
//            public SchemaValidation() {
//                try {
//                    templateGenerate = JsonSchemaFactory
//                            .byDefault()
//                            .getJsonSchema(JsonUtils.getJSONfromResources("schema/outputManagement/templateGenerateSchema.json"));
//                } catch (ProcessingException e) {
//                    throw new RuntimeException(e);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//
//            public SchemaValidation validateTemplateGenerateSchema(String claimNumber) {
//                List jsonBodies = wireMock.find(postRequestedFor(urlPathEqualTo(CommunicationDesignerStub.this.getTemplatesGenerate())))
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
//        }
    }
}

