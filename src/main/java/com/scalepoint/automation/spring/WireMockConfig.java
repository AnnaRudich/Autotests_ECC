package com.scalepoint.automation.spring;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.client.WireMockBuilder;
import com.scalepoint.automation.shared.WiremockServer;
import com.scalepoint.automation.stubs.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
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
        wireMock.resetMappings();

        return wireMock;
    }

    @Bean
    public RnVMock.RnvStub rnvStub() {
        return new RnVMock(wireMock())
                .addStub();
    }

    @Bean
    public AuditMock.AuditStub auditStub() {
        return new AuditMock(wireMock())
                .addStub();
    }

    @Bean
    public CommunicationDesignerMock communicationDesignerMock() {
        return new CommunicationDesignerMock(wireMock());
    }

    @Bean
    public EVBMock.EVBStubs evbMock() throws IOException {
        return new EVBMock(wireMock()).addStub();
    }

    @Bean
    public FraudAlertMock fraudAlertMcok(){
        return new FraudAlertMock(wireMock());
    }
}