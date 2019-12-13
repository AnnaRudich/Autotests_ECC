package com.scalepoint.automation.stubs;


import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.scalepoint.automation.utils.data.entity.rnv.serviceTask.ServiceTasksExport;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class RnVMock {

    protected Logger logger = LogManager.getLogger(RnVMock.class);

    private static WireMock wireMock;
    RnvStub stub;

    public static final int POLL_INTERVAL = 10;
    public static final int TIMEOUT = 60;

    public RnVMock(WireMock wireMock){
        this.wireMock = wireMock;
    }

    public RnvStub addStub() throws IOException {

        return stub = new RnvStub()
                .rvTaskWebServiceUrlStub()
                .rvStatusMessageWebServiceUrlStub()
                .rvFreeTextMessageWebServiceUrlStub();
    }

    public class RnvStub {

        @Getter
        private final String baseUrl = "/rnv";
        private final String rvTaskWebServiceUrl = baseUrl.concat("/rvTaskWebServiceUrl");

        public RnvStub() {

            WireMock.configureFor(wireMock);
        }

       public RnvStub rvTaskWebServiceUrlStub(){

           WireMockRule wireMockRule = new WireMockRule();
           wireMockRule.stubFor(post(urlPathEqualTo(rvTaskWebServiceUrl))
                   .andMatching(new CustomRnvMatcher())
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

            logger.info("Mock request: {}", loggedRequest);

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

