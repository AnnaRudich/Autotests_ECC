package com.scalepoint.automation.tests.fraudAlert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.scalepoint.automation.model.CaseData;
import com.scalepoint.automation.model.Item;
import com.scalepoint.automation.pageobjects.pages.MailsPage;
import com.scalepoint.automation.services.externalapi.EventApiService;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.restService.UnifiedIntegrationService;
import com.scalepoint.automation.stubs.FraudAlertMock;
import com.scalepoint.automation.stubs.FraudAlertMock.FraudAlertStubs;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.annotations.RunOn;
import com.scalepoint.automation.utils.annotations.UserCompany;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import com.scalepoint.automation.utils.data.request.InsertSettlementItem;
import com.scalepoint.automation.utils.driver.DriverType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.scalepoint.automation.services.usersmanagement.CompanyCode.TOPDANMARK;
import static com.scalepoint.automation.utils.DateUtils.ISO8601;
import static com.scalepoint.automation.utils.DateUtils.format;
import static org.assertj.core.api.Assertions.assertThat;

public class FraudAlertTest extends BaseTest {

    static final String TENANT = "topdanmark";
    static final String COUNTRY = "dk";
    FraudAlertStubs fraudAlertStubs;

    @BeforeClass
    public void startWireMock() throws IOException {
        WireMock.configureFor(wireMock);
        wireMock.resetMappings();
        fraudAlertStubs = new FraudAlertMock(wireMock).addStub(TENANT);
        wireMock.allStubMappings()
                .getMappings()
                .stream()
                .forEach(m -> log.info(String.format("Registered stubs: %s",m.getRequest())));
        new EventApiService().scheduleSubscription("3");
    }

    private String excelImportPath = "C:\\ExcelImport\\DK_NYT ARK(3)(a).xls";

    @Test(dataProvider = "testDataProvider", description = "Add")
    public void manualClaimHandlingAddNoFraud(@UserCompany(TOPDANMARK)User user, ClaimRequest claimRequest, ClaimItem claimItem, Claim claim) throws IOException {

        claimRequest.setAccidentDate(format(LocalDateTime.now().minusDays(2L), ISO8601));
        String token = createCwaClaimAndGetClaimToken(claimRequest);
        loginAndOpenUnifiedIntegrationClaimByToken(user, token)
                .openSid()
                .setBaseData(claimItem)
                .disableAge()
                .closeSidWithOk();

        fraudAlertStubs
                .waitForClaimUpdatedEvent(token, "0");

        String response = new UnifiedIntegrationService()
                .getCaseEndpointByToken(COUNTRY, TENANT, token)
                .getBody()
                .print();

        log.info("----------------" + response);

        CaseData caseData = new ObjectMapper().readValue(response, CaseData.class);

        log.info("-------------------" + caseData);

        Item item = caseData.getLoss().getContent().getItems().get(0);

        assertThat(item.getDescription()).isEqualTo(claimItem.getTextFieldSP());
        assertThat(item.getCategory()).isEqualTo(claimItem.getCategoryBabyItems().getGroupName());
        assertThat(item.getValuationByType("CUSTOMER_DEMAND").getPrice()).isEqualTo(claimItem.getCustomerDemand());
        assertThat(item.getValuationByType("NEW_PRICE").getPrice()).isEqualTo(claimItem.getNewPriceSP());
    }

    @Test(dataProvider = "testDataProvider", description = "Edit")
    public void manualClaimHandlingEditNoFraud(@UserCompany(TOPDANMARK)User user, Claim claim, ClaimItem claimItem, ClaimRequest claimRequest) throws IOException {

        claimRequest.setAccidentDate(format(LocalDateTime.now().minusDays(2L), ISO8601));
        String token = createCwaClaimAndGetClaimToken(claimRequest);
        loginAndOpenUnifiedIntegrationClaimByToken(user, token)
                .openSid()
                .setBaseData(claimItem)
                .disableAge()
                .closeSidWithOk()
                .findClaimLine(claimItem.getTextFieldSP())
                .editLine()
                .setNewPrice(2000.00)
                .disableAge()
                .closeSidWithOk();

        fraudAlertStubs
                .waitForClaimUpdatedEvent(token, "1");

        String response = new UnifiedIntegrationService()
                .getCaseEndpointByToken(COUNTRY, TENANT, token)
                .getBody()
                .print();

        log.info("----------------" + response);

        CaseData caseData = new ObjectMapper().readValue(response, CaseData.class);

        log.info("-------------------" + caseData);

        Item item = caseData.getLoss().getContent().getItems().get(0);

        assertThat(item.getDescription()).isEqualTo(claimItem.getTextFieldSP());
        assertThat(item.getCategory()).isEqualTo(claimItem.getCategoryBabyItems().getGroupName());
        assertThat(item.getValuationByType("CUSTOMER_DEMAND").getPrice()).isEqualTo(claimItem.getCustomerDemand());
        assertThat(item.getValuationByType("NEW_PRICE").getPrice()).isEqualTo(claimItem.getNewPriceSP());
    }

    @Test(dataProvider = "testDataProvider", description = "Remove")
    public void manualClaimHandlingRemoveNoFraud(@UserCompany(TOPDANMARK)User user, Claim claim, ClaimItem claimItem, ClaimRequest claimRequest) throws IOException {

        claimRequest.setAccidentDate(format(LocalDateTime.now().minusDays(2L), ISO8601));
        String token = createCwaClaimAndGetClaimToken(claimRequest);
        loginAndOpenUnifiedIntegrationClaimByToken(user, token)
                .openSid()
                .setBaseData(claimItem)
                .disableAge()
                .closeSidWithOk()
                .selectLinesByDescriptions(claimItem.getTextFieldSP())
                .delete();

        fraudAlertStubs
                .waitForClaimUpdatedEvent(token, "1");

        String response = new UnifiedIntegrationService()
                .getCaseEndpointByToken(COUNTRY, TENANT, token)
                .getBody()
                .print();

        log.info("----------------" + response);

        CaseData caseData = new ObjectMapper().readValue(response, CaseData.class);

        log.info("-------------------" + caseData);

        List<Item> items = caseData.getLoss().getContent().getItems();

        assertThat(items.size()).isEqualTo(0);
    }

    @Test(dataProvider = "testDataProvider",
            description = "SelfService")
    @RequiredSetting(type = FTSetting.USE_SELF_SERVICE2, enabled = false)
    @RequiredSetting(type = FTSetting.SHOW_POLICY_TYPE, enabled = false)
    @RequiredSetting(type = FTSetting.INCLUDE_PURCHASE_PRICE_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_NEW_PRICE_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_USED_NEW_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_CUSTOMER_DEMAND_COLUMN_IN_SELF_SERVICE)
    public void selfServiceAddNoFraud(@UserCompany(TOPDANMARK)User user, Claim claim, ClaimRequest claimRequest) throws IOException {

        claimRequest.setAccidentDate(format(LocalDateTime.now().minusDays(2L), ISO8601));
        String token = createCwaClaimAndGetClaimToken(claimRequest);
        loginAndOpenUnifiedIntegrationClaimByToken(user, token)
                .requestSelfService(claim, Constants.DEFAULT_PASSWORD)
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceLinkAndOpenIt()
                .login(Constants.DEFAULT_PASSWORD)
                .getSelfServiceGrid()
                .getRows()
                .get(1)
                .addDescription("test")
                .selectRandomCategory()
                .selectRandomAcquired()
                .selectRandomPurchaseDate()
                .addPurchasePrice("1500")
                .addNewPrice("2500")
                .addCustomerDemandPrice("2000")
                .uploadDocumentation(false)
                .selfServiceGrid()
                .selfServicePage()
                .selectSubmitOption();

        fraudAlertStubs
                .waitForClaimUpdatedEvent(token, "0");

        String response = new UnifiedIntegrationService()
                .getCaseEndpointByToken(COUNTRY, TENANT, token)
                .getBody()
                .print();

        log.info("----------------" + response);

        CaseData caseData = new ObjectMapper().readValue(response, CaseData.class);

        log.info("-------------------" + caseData);

        Item item = caseData.getLoss().getContent().getItems().get(0);

        assertThat(item.getValuationByType("PURCHASE_PRICE").getPrice()).isEqualTo(1500.0);
        assertThat(item.getValuationByType("CUSTOMER_DEMAND").getPrice()).isEqualTo(2000.0);
        assertThat(item.getValuationByType("NEW_PRICE").getPrice()).isEqualTo(2500.0);
    }

    @Test(dataProvider = "testDataProvider", description = "Add")
    public void manualClaimHandlingAddFraud(@UserCompany(TOPDANMARK)User user, ClaimRequest claimRequest, ClaimItem claimItem, Claim claim) throws IOException {

        claimRequest.setAccidentDate(format(LocalDateTime.now().minusDays(2L), ISO8601));
        String token = createCwaClaimAndGetClaimToken(claimRequest);
        loginAndOpenUnifiedIntegrationClaimByToken(user, token)
                .openSid()
                .setBaseData(claimItem)
                .disableAge()
                .closeSidWithOk();

        fraudAlertStubs
                .waitForClaimUpdatedEvent(token, "0");

        String response = new UnifiedIntegrationService()
                .getCaseEndpointByToken(COUNTRY, TENANT, token)
                .getBody()
                .print();

        log.info("----------------" + response);

        CaseData caseData = new ObjectMapper().readValue(response, CaseData.class);

        log.info("-------------------" + caseData);

        Item item = caseData.getLoss().getContent().getItems().get(0);

        assertThat(item.getDescription()).isEqualTo(claimItem.getTextFieldSP());
        assertThat(item.getCategory()).isEqualTo(claimItem.getCategoryBabyItems().getGroupName());
        assertThat(item.getValuationByType("CUSTOMER_DEMAND").getPrice()).isEqualTo(claimItem.getCustomerDemand());
        assertThat(item.getValuationByType("NEW_PRICE").getPrice()).isEqualTo(claimItem.getNewPriceSP());
    }

    @Test(dataProvider = "testDataProvider", description = "Edit")
    public void manualClaimHandlingEditFraud(@UserCompany(TOPDANMARK)User user, Claim claim, ClaimItem claimItem, ClaimRequest claimRequest) throws IOException {

        claimRequest.setAccidentDate(format(LocalDateTime.now().minusDays(2L), ISO8601));
        String token = createCwaClaimAndGetClaimToken(claimRequest);
        loginAndOpenUnifiedIntegrationClaimByToken(user, token)
                .openSid()
                .setBaseData(claimItem)
                .disableAge()
                .closeSidWithOk()
                .findClaimLine(claimItem.getTextFieldSP())
                .editLine()
                .setNewPrice(2000.00)
                .disableAge()
                .closeSidWithOk();

        fraudAlertStubs
                .waitForClaimUpdatedEvent(token, "1");

        String response = new UnifiedIntegrationService()
                .getCaseEndpointByToken(COUNTRY, TENANT, token)
                .getBody()
                .print();

        log.info("----------------" + response);

        CaseData caseData = new ObjectMapper().readValue(response, CaseData.class);

        log.info("-------------------" + caseData);

        Item item = caseData.getLoss().getContent().getItems().get(0);

        assertThat(item.getDescription()).isEqualTo(claimItem.getTextFieldSP());
        assertThat(item.getCategory()).isEqualTo(claimItem.getCategoryBabyItems().getGroupName());
        assertThat(item.getValuationByType("CUSTOMER_DEMAND").getPrice()).isEqualTo(claimItem.getCustomerDemand());
        assertThat(item.getValuationByType("NEW_PRICE").getPrice()).isEqualTo(claimItem.getNewPriceSP());
    }

    @Test(dataProvider = "testDataProvider", description = "Remove")
    public void manualClaimHandlingRemoveFraud(@UserCompany(TOPDANMARK)User user, Claim claim, ClaimItem claimItem, ClaimRequest claimRequest) throws IOException {

        claimRequest.setAccidentDate(format(LocalDateTime.now().minusDays(2L), ISO8601));
        String token = createCwaClaimAndGetClaimToken(claimRequest);
        loginAndOpenUnifiedIntegrationClaimByToken(user, token)
                .openSid()
                .setBaseData(claimItem)
                .disableAge()
                .closeSidWithOk()
                .selectLinesByDescriptions(claimItem.getTextFieldSP())
                .delete();

        fraudAlertStubs
                .waitForClaimUpdatedEvent(token, "1");

        String response = new UnifiedIntegrationService()
                .getCaseEndpointByToken(COUNTRY, TENANT, token)
                .getBody()
                .print();

        log.info("----------------" + response);

        CaseData caseData = new ObjectMapper().readValue(response, CaseData.class);

        log.info("-------------------" + caseData);

        List<Item> items = caseData.getLoss().getContent().getItems();

        assertThat(items.size()).isEqualTo(0);
    }

    @Test(dataProvider = "testDataProvider",
            description = "SelfService")
    @RequiredSetting(type = FTSetting.USE_SELF_SERVICE2, enabled = false)
    @RequiredSetting(type = FTSetting.SHOW_POLICY_TYPE, enabled = false)
    @RequiredSetting(type = FTSetting.INCLUDE_PURCHASE_PRICE_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_NEW_PRICE_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_USED_NEW_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_CUSTOMER_DEMAND_COLUMN_IN_SELF_SERVICE)
    public void selfServiceAddFraud(@UserCompany(TOPDANMARK)User user, Claim claim, ClaimRequest claimRequest) throws IOException {

        claimRequest.setAccidentDate(format(LocalDateTime.now().minusDays(2L), ISO8601));
        String token = createCwaClaimAndGetClaimToken(claimRequest);
        loginAndOpenUnifiedIntegrationClaimByToken(user, token)
                .requestSelfService(claim, Constants.DEFAULT_PASSWORD)
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceLinkAndOpenIt()
                .login(Constants.DEFAULT_PASSWORD)
                .getSelfServiceGrid()
                .getRows()
                .get(1)
                .addDescription("test")
                .selectRandomCategory()
                .selectRandomAcquired()
                .selectRandomPurchaseDate()
                .addPurchasePrice("1500")
                .addNewPrice("2500")
                .addCustomerDemandPrice("2000")
                .uploadDocumentation(false)
                .selfServiceGrid()
                .selfServicePage()
                .selectSubmitOption();;

        fraudAlertStubs
                .waitForClaimUpdatedEvent(token, "0");

        String response = new UnifiedIntegrationService()
                .getCaseEndpointByToken(COUNTRY, TENANT, token)
                .getBody()
                .print();

        log.info("----------------" + response);

        CaseData caseData = new ObjectMapper().readValue(response, CaseData.class);

        log.info("-------------------" + caseData);

        Item item = caseData.getLoss().getContent().getItems().get(0);

        assertThat(item.getValuationByType("PURCHASE_PRICE").getPrice()).isEqualTo(1500.0);
        assertThat(item.getValuationByType("CUSTOMER_DEMAND").getPrice()).isEqualTo(2000.0);
        assertThat(item.getValuationByType("NEW_PRICE").getPrice()).isEqualTo(2500.0);
    }

    @RequiredSetting(type = FTSetting.SHOW_DISCREATIONARY_REASON)
    @RequiredSetting(type = FTSetting.SHOW_POLICY_TYPE, enabled = false)
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 Verify that after importing excel with discretionary valuation" +
            " drop-down for choosing reason is enabled")
    public void importExcelNoFraud(@UserCompany(TOPDANMARK)User user,
                                   Claim claim, ClaimRequest claimRequest) throws IOException {
        claimRequest.setAccidentDate(format(LocalDateTime.now().minusDays(2L), ISO8601));
        String token = createCwaClaimAndGetClaimToken(claimRequest);
        loginAndOpenUnifiedIntegrationClaimByToken(user, token)
                .importExcelFile(excelImportPath);

        fraudAlertStubs
                .waitForClaimUpdatedEvent(token, "0");

        String response = new UnifiedIntegrationService()
                .getCaseEndpointByToken(COUNTRY, TENANT, token)
                .getBody()
                .print();

        log.info("----------------" + response);

        CaseData caseData = new ObjectMapper().readValue(response, CaseData.class);

        log.info("-------------------" + caseData);

        List<Item> items = caseData.getLoss().getContent().getItems();

        assertThat(items.size()).isEqualTo(50);
    }

    @RequiredSetting(type = FTSetting.SHOW_DISCREATIONARY_REASON)
    @RequiredSetting(type = FTSetting.SHOW_POLICY_TYPE, enabled = false)
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 Verify that after importing excel with discretionary valuation" +
            " drop-down for choosing reason is enabled")
    public void importExcelFraud(@UserCompany(TOPDANMARK)User user,
                                 Claim claim, ClaimRequest claimRequest) throws IOException {
        claimRequest.setAccidentDate(format(LocalDateTime.now().minusDays(2L), ISO8601));
        String token = createCwaClaimAndGetClaimToken(claimRequest);
        loginAndOpenUnifiedIntegrationClaimByToken(user, token)
                .importExcelFile(excelImportPath);

        fraudAlertStubs
                .waitForClaimUpdatedEvent(token, "0");

        String response = new UnifiedIntegrationService()
                .getCaseEndpointByToken(COUNTRY, TENANT, token)
                .getBody()
                .print();

        log.info("----------------" + response);

        CaseData caseData = new ObjectMapper().readValue(response, CaseData.class);

        log.info("-------------------" + caseData);

        List<Item> items = caseData.getLoss().getContent().getItems();

        assertThat(items.size()).isEqualTo(50);
    }

    @Test(enabled = false, dataProvider = "testDataProvider", description = "Create claim using unified integration")
    public void charlie_554_createClaimUsingUnifiedIntegration(User user, ClaimRequest claimRequest, InsertSettlementItem settlementItem) {
        claimRequest.setAccidentDate(format(LocalDateTime.now().minusDays(2L), ISO8601));

        String token = createCwaClaim(claimRequest)
                .getClaimTokenWithoutPrefix();
        loginAndOpenUnifiedIntegrationClaimByToken(user, token)
                .toCustomerDetails()
                .doAssert(
                        asserts -> asserts.assertDamageDateIs(LocalDate.now().minusDays(2L))
                );
    }
}