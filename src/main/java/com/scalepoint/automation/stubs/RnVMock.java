package com.scalepoint.automation.stubs;


import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.scalepoint.automation.utils.data.entity.rnv.serviceTask.ServiceTasksExport;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class RnVMock extends EccMock{

    RnvStub stub;

    public static final int POLL_INTERVAL = 10;
    public static final int TIMEOUT = 60;

    public static final double UNAUTHORIZED_PRICE = 122.00;
    public static final double OK_PRICE = 100.00;

    public RnVMock(WireMock wireMock){
        super(wireMock);
        log = LogManager.getLogger(RnVMock.class);
    }

    public RnvStub addStub(){

        return stub = new RnvStub()
                .rvTaskWebServiceUrlOkStub()
                .rvTaskWebServiceUrlUnauthorizedStub()
                .rvStatusMessageWebServiceUrlStub()
                .rvFreeTextMessageWebServiceUrlStub();
    }

    public class RnvStub {

        @Getter
        private final String baseUrl = "/rnv";
        private final String rvTaskWebServiceUrl = baseUrl.concat("/rvTaskWebServiceUrl");
        private final String unauthorizedRegex = String.format(".*valuations.*newPrice.*%s.*", UNAUTHORIZED_PRICE);

        public RnvStub() {

            WireMock.configureFor(wireMock);
        }

        public RnvStub rvTaskWebServiceUrlOkStub(){
            wireMock.stubFor(post(urlPathEqualTo(rvTaskWebServiceUrl))
                    .withRequestBody(notMatching(unauthorizedRegex))
                    .willReturn(aResponse().withStatus(200)));

            return this;
        }

        public RnvStub rvTaskWebServiceUrlUnauthorizedStub(){

            wireMock.stubFor(post(urlPathEqualTo(rvTaskWebServiceUrl))
                    .withRequestBody(matching(unauthorizedRegex))
                    .willReturn(aResponse().withStatus(401)));

            return this;
        }

        public RnvStub rvStatusMessageWebServiceUrlStub() {

            stubFor(post(urlPathEqualTo(baseUrl.concat("/rvStatusMessageWebServiceUrl")))
                    .willReturn(aResponse()
                            .withStatus(200)));

            return this;
        }

        public RnvStub rvFreeTextMessageWebServiceUrlStub() {

            stubFor(post(urlPathEqualTo(baseUrl.concat("/rvFreeTextMessageWebServiceUrl")))
                    .willReturn(aResponse()
                            .withStatus(200)));
            return this;
        }

        public synchronized ServiceTasksExport waitForServiceTask(String claimNumber) {

            return await()
                    .pollInterval(POLL_INTERVAL, TimeUnit.MILLISECONDS)
                    .timeout(TIMEOUT, TimeUnit.SECONDS)
                    .until(() ->
                                    wireMock.find(postRequestedFor(urlPathEqualTo(rvTaskWebServiceUrl)))
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
                                            .orElse(null)
                            , is(notNullValue()));
        }



        private ServiceTasksExport loggedRequestToServiceTasksExport(LoggedRequest loggedRequest){

            log.info("Mock request: {}", loggedRequest);

            try {
                String form = URLDecoder
                        .decode(loggedRequest.getBodyAsString(), StandardCharsets.UTF_8.toString());

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

            } catch (UnsupportedEncodingException | JAXBException e) {

                throw new RuntimeException(e);
            }
        }
    }
}

