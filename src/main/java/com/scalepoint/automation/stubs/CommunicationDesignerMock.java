package com.scalepoint.automation.stubs;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.scalepoint.automation.utils.JsonUtils;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
public class CommunicationDesignerMock extends EccMock{

    private static Map<String, CommunicationDesignerStub> stubs = new HashMap<>();

    public CommunicationDesignerMock(WireMock wireMock){

        super(wireMock);
        log = LogManager.getLogger(CommunicationDesignerMock.class);
    }

    public CommunicationDesignerStub getStub(String tenant){

        return stubs.get(tenant);
    }

    public synchronized CommunicationDesignerStub addStub(String tenant) throws IOException {

        if(!stubs.containsKey(tenant)) {

            CommunicationDesignerStub communicationDesignerStubs = new CommunicationDesignerStub(tenant)
                    .templatesQueryStub()
                    .templatesGenerateStub()
                    .pdfAttachmentStub();

            stubs.put(tenant, communicationDesignerStubs);
        }

        return stubs.get(tenant);
    }

    public class CommunicationDesignerStub {

        @Getter
        private final String baseUrl;
        private final String tenant;
        @Getter
        public final String templatesQuery;
        @Getter
        public final String templatesGenerate;
        @Getter
        public final String pdfAttachment;
        private final String CONTENT_TYPE = "application/json";



        public CommunicationDesignerStub(String tenant) {

            WireMock.configureFor(wireMock);
            this.tenant = tenant;
            baseUrl = String.format("/outputManagementUrl/%s", tenant);
            templatesQuery = baseUrl.concat("/api/v1.1/Templates/Query");
            templatesGenerate = baseUrl.concat("/api/v1.1/Templates/Generate");
            pdfAttachment = baseUrl.concat("/api/v1.1/Templates/GeneratePdf");
        }

        public CommunicationDesignerStub templatesQueryStub() throws IOException {

            stubFor(get(urlPathEqualTo(templatesQuery))
                    .withQueryParam("templateName", matching(".*"))
                    .withQueryParam("contextType", matching(".*"))
                    .withQueryParam("modelSchemaKey", matching(".*"))
                    .willReturn(aResponse()
                            .withTransformers("findTemplateTransformer")
                            .withStatus(200)));

            return this;
        }

        public CommunicationDesignerStub templatesGenerateStub() throws IOException {

            stubFor(post(urlPathEqualTo(templatesGenerate))
                    .willReturn(aResponse()
                            .withTransformers("omMailTemplateTransformer")
                            .withStatus(200)));

            return this;
        }

        public CommunicationDesignerStub pdfAttachmentStub() throws IOException {

            stubFor(post(urlPathEqualTo(pdfAttachment))
                    .willReturn(aResponse()
                            .withHeader("Content-Type", CONTENT_TYPE)
                            .withBody(JsonUtils.getJSONasString("__files/communicationDesignerMock/pdfAttachment.json"))
                            .withStatus(200)));

            return this;
        }

        public CommunicationDesignerStub doValidation(Consumer<SchemaValidation> schemaValidationFunc) {

            schemaValidationFunc.accept(new SchemaValidation());

            return CommunicationDesignerStub.this;
        }

        public class SchemaValidation {

            JsonSchema templateGenerate;

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

            public SchemaValidation validateTemplateGenerateSchema(String claimNumber) {

                List jsonBodies = wireMock.find(postRequestedFor(urlPathEqualTo(CommunicationDesignerStub.this.getTemplatesGenerate())))
                        .stream()
                        .filter(loggedRequest ->
                                JsonUtils.stringToJsonNode(loggedRequest.getBodyAsString())
                                        .path("data")
                                        .path("claim")
                                        .path("claimNumber")
                                        .asText()
                                        .equals(claimNumber)
                        )
                        .map(loggedRequest -> loggedRequest.getBodyAsString())
                        .collect(Collectors.toList());

                jsonBodies.stream().forEach(body -> {
                    ProcessingReport report;
                    try {

                        report = templateGenerate.validate(JsonUtils.stringToJsonNode((String) body));
                    } catch (ProcessingException e) {

                        throw new RuntimeException(e);
                    }

                    assertThat(report.isSuccess())
                            .as(String.format("Template generate schema validation: %s", report))
                            .isTrue();
                });

                return this;
            }
        }
    }
}
