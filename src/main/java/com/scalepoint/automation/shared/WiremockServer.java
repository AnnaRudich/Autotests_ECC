package com.scalepoint.automation.shared;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
public enum WiremockServer {

    WIREMOCK_1("ecc-tools.spcph.local", "10.99.14.75"),
    WIREMOCK_2("dev-ecc-tool02.spcph.local", "10.99.14.100");

    @Getter
    private String domain;
    @Getter
    private String ipAddress;

    public static WiremockServer findByDomain(String domain){
        return Arrays.stream(WiremockServer.values())
                .filter(value -> value.domain.equals(domain))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Wiremock server: %s doesn't exist", domain)));
    }
}
