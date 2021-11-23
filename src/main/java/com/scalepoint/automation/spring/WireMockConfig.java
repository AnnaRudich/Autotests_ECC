package com.scalepoint.automation.spring;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.client.WireMockBuilder;
import com.scalepoint.automation.shared.WiremockServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
@EnableAutoConfiguration
public class WireMockConfig {

    @Value("${wiremock.host}")
    private String host;

    @Value("${wiremock.urlPathPrefix}")
    private String urlPathPrefix;

    @Value("${wiremock.port}")
    String port;

    @Bean
    public WireMock wireMock(){

        WireMock wireMock =  new WireMockBuilder()
                .https()
                .host(WiremockServer.findByDomain(host).getIpAddress())
                .urlPathPrefix(urlPathPrefix)
                .port(Integer.valueOf(port))
                .build();

        WireMock.configureFor(wireMock);
//        wireMock.resetMappings();

        return wireMock;
    }
}
