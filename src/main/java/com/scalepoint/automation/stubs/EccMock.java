package com.scalepoint.automation.stubs;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.apache.logging.log4j.Logger;

public class EccMock {

    protected Logger log;

    protected WireMock wireMock;

    public EccMock(WireMock wireMock) {

        this.wireMock = wireMock;
    }

    public void printAllUnmatchedRequests(){
        wireMock
                .findNearMissesForAllUnmatchedRequests()
                .stream()
                .forEach(loggedRequest -> log.info(loggedRequest));
    }
}
