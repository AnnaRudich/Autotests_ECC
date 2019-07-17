package com.scalepoint.automation.schemaValidation;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.scalepoint.automation.stubs.CommunicationDesignerStubs;
import com.scalepoint.automation.utils.JsonUtils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.assertj.core.api.Assertions.assertThat;

public class SchemaValidation {

    JsonSchema templateGenerate;
    WireMock wireMock;

    public SchemaValidation(WireMock wireMock){

        this.wireMock = wireMock;

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

    public void validateTemplateGenerateSchema(String claimNumber){
        List jsonBodies = wireMock.find(postRequestedFor(urlPathEqualTo(CommunicationDesignerStubs.TEMPLATES_GENERATE)))
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
    }
}
