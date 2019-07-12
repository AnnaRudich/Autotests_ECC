package com.scalepoint.automation.schemaValidation;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.scalepoint.automation.stubs.CommunicationDesignerStubs;
import com.scalepoint.automation.utils.JsonUtils;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.assertj.core.api.Assertions.assertThat;

public class SchemaValidation {

    JsonSchema templateGenerateManual;
    JsonSchema templateGenerateAutomatic;
    WireMock wireMock;

    public SchemaValidation(WireMock wireMock){

        this.wireMock = wireMock;

        try {
            templateGenerateManual = JsonSchemaFactory
                    .byDefault()
                    .getJsonSchema(JsonUtils.getJSONfromResources("schema/outputManagement/templateGenerateSchemaManual.json"));
            templateGenerateAutomatic= JsonSchemaFactory
                    .byDefault()
                    .getJsonSchema(JsonUtils.getJSONfromResources("schema/outputManagement/templateGenerateSchemaAutomatic.json"));
        } catch (ProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void validateTemplateGenerateSchema(String claimNumber, JsonSchema jsonSchema){


        String json = wireMock.find(postRequestedFor(urlPathEqualTo(CommunicationDesignerStubs.TEMPLATES_GENERATE)))
                .stream()
                .filter(loggedRequest ->
                        JsonUtils.stringToJsonNode(loggedRequest.getBodyAsString())
                                .path("data")
                                .path("claim")
                                .path("claimNumber")
                                .asText()
                                .equals(claimNumber)
                )
                .findFirst()
                .get()
                .getBodyAsString();

        ProcessingReport report;

        try {
            report = jsonSchema.validate(JsonUtils.stringToJsonNode(json));
        } catch (ProcessingException e) {
            throw new RuntimeException(e);
        }
        assertThat(report.isSuccess())
                .as(String.format("Template generate schema validation: %s", report))
                .isTrue();
    }

    public void validateTemplateGenerateSchemaManual(String claimNumber){

        validateTemplateGenerateSchema(claimNumber, templateGenerateManual);
    }

    public void validateTemplateGenerateSchemaAutomatic(String claimNumber){

        validateTemplateGenerateSchema(claimNumber, templateGenerateAutomatic);
    }
}
