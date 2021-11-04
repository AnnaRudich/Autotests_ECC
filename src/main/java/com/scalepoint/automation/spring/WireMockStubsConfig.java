package com.scalepoint.automation.spring;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.scalepoint.automation.services.externalapi.DatabaseApi;
import com.scalepoint.automation.stubs.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@EnableAutoConfiguration
public class WireMockStubsConfig {

    @Autowired
    public DatabaseApi databaseApi;

    @Autowired
    public WireMock wireMock;

    @Bean
    public RnVMock.RnvStub rnvStub() {

        return new RnVMock(wireMock)
                .addStub();
    }

    @Bean
    public AuditMock.AuditStub auditStub() {

        return new AuditMock(wireMock)
                .addStub();
    }

    @Bean
    public CommunicationDesignerMock communicationDesignerMock() {

        return new CommunicationDesignerMock(wireMock);
    }

    @Bean
    public EVBMock.EVBStubs evbMock() throws IOException {

        return new EVBMock(wireMock).addStub();
    }

    @Bean
    public FraudAlertMock fraudAlertMock(){

        return new FraudAlertMock(wireMock);
    }

    @Bean
    public PostalCodeMock.PostalCodeStubs postalCodeMock() throws IOException {

        return new PostalCodeMock(wireMock)
                .addStub();
    }

    @Bean
    public MailserviceMock.MailserviceStub mailserviceStub() throws IOException {

        return new MailserviceMock(wireMock, databaseApi)
                .addStub();
    }
}
