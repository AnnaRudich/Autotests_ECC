package com.scalepoint.automation.stubs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.scalepoint.automation.utils.JsonUtils;
import lombok.Builder;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class EVBMock {

    protected Logger log = LogManager.getLogger(EVBMock.class);

    private static WireMock wireMock;

    public EVBMock(WireMock wireMock){
        this.wireMock = wireMock;
    }

    public synchronized EVBStubs addStub() throws IOException {

            return new EVBStubs()
                    .issuedStub();
    }

    public class EVBStubs {

        private static final String CONTENT_TYPE = "application/json";
        public static final String ISSUED = "/evb/issue";


//        public EVBStubs evbStubs() {
//            WireMock.configureFor(wireMock);
//        }

        public EVBStubs issuedStub() throws IOException {
            stubFor(post(urlMatching(ISSUED))
                    .willReturn(aResponse()
                            .withHeader("Content-Type", CONTENT_TYPE)
                            .withBody(new ObjectMapper().writeValueAsString(buildIssueResponse()))
                            .withStatus(200)));

            return this;
        }

        private IssueResponse buildIssueResponse(){

            IssueResponseData issueResponseData = new IssueResponseData();
            issueResponseData.setCardNumber("1234");

            IssueResponse issueResponse = new IssueResponse();
            issueResponse.setVoucherPublicId(UUID.randomUUID().toString());
            issueResponse.setData(issueResponseData);

            log.info("EVB voucher Public ID: " + issueResponse.getVoucherPublicId());

            return issueResponse;
        }

        @Data
        public class IssueResponse {

            @JsonProperty("VoucherPublicId")
            private String voucherPublicId;

            @JsonProperty("Data")
            private IssueResponseData data;
        }

        @Data
        public class IssueResponseData {
            @JsonProperty("CardNumber")
            private String cardNumber;
        }
    }
}
