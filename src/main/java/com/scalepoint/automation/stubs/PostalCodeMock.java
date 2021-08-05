package com.scalepoint.automation.stubs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import lombok.Builder;
import lombok.Data;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class PostalCodeMock extends EccMock{

    public PostalCodeMock(WireMock wireMock){

        super(wireMock);
        log = LogManager.getLogger(PostalCodeMock.class);
    }

    public synchronized PostalCodeStubs addStub() throws IOException {

        return new PostalCodeStubs()
                .healthCheckStub();
    }

    public class PostalCodeStubs {

        public static final String HEALTH_CHECK = "/actuator/health";

        public PostalCodeStubs healthCheckStub() throws IOException {
            stubFor(get(urlMatching(HEALTH_CHECK))
                    .willReturn(aResponse()
                            .withBody(new ObjectMapper().writeValueAsString(buildHealthCheckResponse()))
                            .withStatus(200)));

            return this;
        }

        private HealthCheckResponse buildHealthCheckResponse(){

            return HealthCheckResponse
                    .builder()
                    .status("UP")
                    .build();
        }

        @Data
        @Builder
        public class HealthCheckResponse {

            @JsonProperty("status")
            private String status;
        }
    }
}
