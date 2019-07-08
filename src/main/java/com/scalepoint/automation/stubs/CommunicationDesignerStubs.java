package com.scalepoint.automation.stubs;

import com.scalepoint.automation.utils.JsonUtils;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class CommunicationDesignerStubs {

    private static final String BASE_URL = "/outputManagementUrl/future60";
    public static final String TEMPLATES_QUERY = BASE_URL.concat("/api/v1.1/Templates/Query");
    public static final String TEMPLATES_GENERATE = BASE_URL.concat("/api/v1.1/Templates/Generate");
    private static final String CONTENT_TYPE = "application/json";

    public static void templatesQueryStub() throws IOException {

        stubFor(get(urlPathEqualTo(TEMPLATES_QUERY))
                .withQueryParam("templateName", matching(".*"))
                .withQueryParam("contextType", matching(".*"))
                .withQueryParam("modelSchemaKey", matching(".*"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", CONTENT_TYPE)
                        .withBody(JsonUtils.getJSONasString("__files/communicationDesignerMock/findTemplateResponse.json"))
                        .withStatus(200)));
    }

    public static void templatesGenerateStub() throws IOException {

        stubFor(post(urlPathEqualTo(TEMPLATES_GENERATE))
                .willReturn(aResponse()
                        .withHeader("Content-Type", CONTENT_TYPE)
                        .withBody(JsonUtils.getJSONasString("__files/communicationDesignerMock/selfServiceCustomerWelcomTemplate.json"))
                        .withStatus(200)));
    }
}
