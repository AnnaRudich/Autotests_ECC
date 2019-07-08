package com.scalepoint.automation.tests.communicationDesigner;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.scalepoint.automation.pageobjects.pages.MailsPage;
import com.scalepoint.automation.pageobjects.pages.admin.InsCompAddEditPage.CommunicationDesigner;
import com.scalepoint.automation.pageobjects.pages.admin.InsCompaniesPage;
import com.scalepoint.automation.schemaValidation.SchemaValidation;
import com.scalepoint.automation.stubs.CommunicationDesignerStubs;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.annotations.UserCompany;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;

import static com.scalepoint.automation.services.usersmanagement.CompanyCode.FUTURE60;

public class CommunicationDesignerTests extends BaseTest {

    @BeforeClass
    public void startWireMock() throws IOException {
        WireMock.configureFor(wireMock);
        wireMock.resetMappings();
        CommunicationDesignerStubs.templatesQueryStub();
        CommunicationDesignerStubs.templatesGenerateStub();
        wireMock.allStubMappings()
                .getMappings()
                .stream()
                .forEach(m -> log.info(String.format("Registered stubs: %s",m.getRequest())));
    }

    @Test(dataProvider = "testDataProvider", description = "Use communication designer to prepare SelfService Customer welcome email")
    public void SelfServiceCustomerWelcomeTest(@UserCompany(FUTURE60) User user, Claim claim) {
        CommunicationDesigner communicationDesigner = CommunicationDesigner.builder()
                .useOutputManagement(true)
                .omSelfServiceCustomerWelcome(true)
                .build();

        login(user)
                .to(InsCompaniesPage.class)
                .editCompany(user.getCompanyName())
                .setCommunicationDesignerSection(communicationDesigner)
                .selectSaveOption();

        loginAndCreateClaim(user, claim)
                .requestSelfService(claim, Constants.DEFAULT_PASSWORD)
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .doAssert(mailViewDialog -> {
                    mailViewDialog.isSelfServiceLinkVisible();
                    mailViewDialog.isTextVisible("[CommunicationDesignerTest]");
                    mailViewDialog.isTextVisible(claim.getFirstName());
                    mailViewDialog.isTextVisible(claim.getLastName());
                });

        new SchemaValidation(wireMock)
                .validateTemplateGenerateSchema(claim.getClaimNumber());
    }
}
