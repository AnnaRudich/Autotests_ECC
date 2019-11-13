package com.scalepoint.automation.stubs;


import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.scalepoint.automation.utils.data.entity.rnv.serviceTask.ServiceTasksExport;
import lombok.Getter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.StringReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class RnVMock {

    private static WireMock wireMock;
    RnvStub stub;

    public static final int POLL_INTERVAL = 10;
    public static final int TIMEOUT = 60;

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

        public ServiceTasksExport waitForServiceTask(String claimNumber) {

            return
                    await()
                            .pollInterval(POLL_INTERVAL, TimeUnit.MILLISECONDS)
                            .timeout(TIMEOUT, TimeUnit.SECONDS)
                            .until(() ->
                                            wireMock.find(postRequestedFor(urlPathEqualTo(baseUrl)))
                                                    .stream()
                                                    .map(loggedRequest ->loggedRequestToServiceTasksExport(loggedRequest))
                                                    .filter(serviceTasksExport ->
                                                            serviceTasksExport
                                                                    .getServiceTasks()
                                                                    .stream().filter(serviceTask ->
                                                                    serviceTask
                                                                            .getClaim()
                                                                            .getClaimNumber()
                                                                            .equals(claimNumber)
                                                            ).count() != 0)
                                                    .findFirst()
                                                    .get()
                                    , is(notNullValue()));
        }



        private ServiceTasksExport loggedRequestToServiceTasksExport(LoggedRequest loggedRequest){

            try {

                String form = URLDecoder
                        .decode(
                                loggedRequest
                                        .getBodyAsString()
                                        .replace("+", " "), StandardCharsets.UTF_8.toString()
                        );

                Matcher body = Pattern
                        .compile("xmlString=.*", Pattern.DOTALL).matcher(form);
                body.find();

                String xml = body
                        .group()
                        .replace("xmlString=", "");

                return (ServiceTasksExport) JAXBContext
                        .newInstance(ServiceTasksExport.class)
                        .createUnmarshaller()
                        .unmarshal(new StringReader(xml));

            } catch (IOException | JAXBException e) {

                throw new RuntimeException(e);
            }
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

