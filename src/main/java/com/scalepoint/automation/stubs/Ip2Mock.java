package com.scalepoint.automation.stubs;

import com.github.tomakehurst.wiremock.client.WireMock;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

public class Ip2Mock {

    protected Logger log = LogManager.getLogger(Ip2Mock.class);
    Ip2Stubs stubs;

    public Ip2Stubs add(){
        return stubs = new Ip2Stubs().ip2GjensidigeWs();
    }

    private static WireMock wireMock;

    public Ip2Mock(WireMock wireMock) {
        this.wireMock = wireMock;
    }

    public class Ip2Stubs {
        @Getter
        private final String getIp2WebServiceBaseUrl = "/externalIntegration";
        private final String ip2GjensidigeWebServiceUrl = getIp2WebServiceBaseUrl.concat("/testic");

        public Ip2Stubs() {
            WireMock.configureFor(wireMock);
        }

        //https://dev-ecc-tool03.spcph.local/mock/externalIntegration/testic
        public Ip2Stubs ip2GjensidigeWs(){
            stubFor(post(urlPathEqualTo(ip2GjensidigeWebServiceUrl))
                    .willReturn(aResponse()
                            .withStatus(202)));

            return this;
        }
    }
}
