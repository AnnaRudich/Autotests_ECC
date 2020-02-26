package com.scalepoint.automation.tests.fraudAlert;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.scalepoint.automation.pageobjects.pages.MailsPage;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.services.externalapi.EventApiService;
import com.scalepoint.automation.services.restService.UnifiedIntegrationService;
import com.scalepoint.automation.stubs.FraudAlertMock;
import com.scalepoint.automation.stubs.FraudAlertMock.FraudAlertStubs;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.annotations.UserCompany;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.changed.Case;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.changed.Item;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.fraudStatus.ClaimLineChanged;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static com.scalepoint.automation.services.usersmanagement.CompanyCode.TOPDANMARK;
import static com.scalepoint.automation.utils.DateUtils.ISO8601;
import static com.scalepoint.automation.utils.DateUtils.format;
import static org.assertj.core.api.Assertions.assertThat;

public class FraudAlertTest extends BaseTest {

    private static final String SONY_HDR_CX450 = "Sony HDR-CX450";
    private static final String IPHONE = "iPhone";

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
        new EventApiService().scheduleSubscription(claimLineChangedSubscriptionId);
        new EventApiService().scheduleSubscription(fraudStatusSubscriptionId);
    }

    private String excelImportPath = new File("src\\main\\resources\\excelImport\\DK_NYT ARK(3)(a).xls").getAbsolutePath();



    @Test(dataProvider = "topdanmarkDataProvider", description = "Add")
    public void productSearchFraud(@UserCompany(TOPDANMARK) User user, ClaimRequest claimRequest, ClaimItem claimItem) throws IOException {

        productSearch(claimRequest, user, claimItem, EventApiService.FraudStatus.FRAUDULENT)
                .getSettlementSummary()
                .doAssert(settlementSummary -> settlementSummary.assertFraudulent());
    }

    @Test(dataProvider = "topdanmarkDataProvider", description = "Add")
    public void productSearchNoFraud(@UserCompany(TOPDANMARK) User user, ClaimRequest claimRequest, ClaimItem claimItem) throws IOException {

        productSearch(claimRequest, user, claimItem, EventApiService.FraudStatus.NOT_FRAUDULENT)
                .getSettlementSummary()
                .doAssert(settlementSummary -> settlementSummary.assertNotFraudulent());
    }

    @Test(dataProvider = "topdanmarkDataProvider", description = "Add")
    public void manualClaimHandlingAddFraud(@UserCompany(TOPDANMARK) User user, ClaimRequest claimRequest, ClaimItem claimItem) throws IOException {

        manualClaimHandlingAdd(claimRequest, user, claimItem, EventApiService.FraudStatus.FRAUDULENT)
                .getSettlementSummary()
                .doAssert(settlementSummary -> settlementSummary.assertFraudulent());
    }

    @Test(dataProvider = "topdanmarkDataProvider", description = "Add")
    public void manualClaimHandlingAddNoFraud(@UserCompany(TOPDANMARK) User user, ClaimItem claimItem, ClaimRequest claimRequest) throws IOException {

        manualClaimHandlingAdd(claimRequest, user, claimItem, EventApiService.FraudStatus.NOT_FRAUDULENT)
                .getSettlementSummary()
                .doAssert(settlementSummary -> settlementSummary.assertNotFraudulent());
    }

    @Test(dataProvider = "topdanmarkDataProvider", description = "Remove")
    public void manualClaimHandlingRemoveFraud(@UserCompany(TOPDANMARK) User user, ClaimItem claimItem, ClaimRequest claimRequest) throws IOException {

        manualClaimHandlingRemove(claimRequest, user, claimItem, EventApiService.FraudStatus.FRAUDULENT)
                .getSettlementSummary()
                .doAssert(settlementSummary -> settlementSummary.assertFraudulent());
    }

    @Test(dataProvider = "topdanmarkDataProvider", description = "Remove")
    public void manualClaimHandlingRemoveNoFraud(@UserCompany(TOPDANMARK) User user, ClaimItem claimItem, ClaimRequest claimRequest) throws IOException {

        manualClaimHandlingRemove(claimRequest, user, claimItem, EventApiService.FraudStatus.NOT_FRAUDULENT)
                .getSettlementSummary()
                .doAssert(settlementSummary -> settlementSummary.assertNotFraudulent());
    }

    @Test(dataProvider = "topdanmarkDataProvider", description = "Edit")
    public void manualClaimHandlingEditFraud(@UserCompany(TOPDANMARK) User user, ClaimItem claimItem, ClaimRequest claimRequest) throws IOException {

        manualClaimHandlingEdit(claimRequest, user, claimItem, EventApiService.FraudStatus.FRAUDULENT)
                .getSettlementSummary()
                .doAssert(settlementSummary -> settlementSummary.assertFraudulent());
    }

    @Test(dataProvider = "topdanmarkDataProvider", description = "Edit")
    public void manualClaimHandlingEditNoFraud(@UserCompany(TOPDANMARK) User user, ClaimItem claimItem, ClaimRequest claimRequest) throws IOException {

        manualClaimHandlingEdit(claimRequest, user, claimItem, EventApiService.FraudStatus.NOT_FRAUDULENT)
                .getSettlementSummary()
                .doAssert(settlementSummary -> settlementSummary.assertNotFraudulent());
    }

    @Test(dataProvider = "topdanmarkDataProvider",
            description = "SelfService")
    public void selfServiceAddFraud(@UserCompany(TOPDANMARK) User user, Claim claim, ClaimRequest claimRequest) throws IOException {

        selfService(claimRequest, user, claim, EventApiService.FraudStatus.FRAUDULENT)
                .getSettlementSummary()
                .doAssert(settlementSummary -> settlementSummary.assertFraudulent());
    }

    @Test(dataProvider = "topdanmarkDataProvider",
            description = "SelfService")
    public void selfServiceAddNoFraud(@UserCompany(TOPDANMARK) User user, Claim claim, ClaimRequest claimRequest) throws IOException {

        selfService(claimRequest, user, claim, EventApiService.FraudStatus.NOT_FRAUDULENT)
                .getSettlementSummary()
                .doAssert(settlementSummary -> settlementSummary.assertNotFraudulent());
    }

    @Test(dataProvider = "topdanmarkDataProvider", description = "CHARLIE-508 Verify that after importing excel with discretionary valuation" +
            " drop-down for choosing reason is enabled")
    public void importExcelNoFraud(@UserCompany(TOPDANMARK) User user,
                                   ClaimRequest claimRequest) throws IOException {

        importExcel(claimRequest, user, EventApiService.FraudStatus.NOT_FRAUDULENT)
                .getSettlementSummary()
                .doAssert(settlementSummary -> settlementSummary.assertNotFraudulent());
    }

    @Test(dataProvider = "topdanmarkDataProvider", description = "CHARLIE-508 Verify that after importing excel with discretionary valuation" +
            " drop-down for choosing reason is enabled")
    public void importExcelFraud(@UserCompany(TOPDANMARK) User user,
                                 ClaimRequest claimRequest) throws IOException {

        importExcel(claimRequest, user, EventApiService.FraudStatus.FRAUDULENT)
                .getSettlementSummary()
                .doAssert(settlementSummary -> settlementSummary.assertFraudulent());
    }

    @Test(dataProvider = "topdanmarkDataProvider", description = "FNOL")
    public void fnolFraud(@UserCompany(TOPDANMARK) User user, Claim claim) throws IOException {

        fnol(user, claim, EventApiService.FraudStatus.FRAUDULENT)
                .getSettlementSummary()
                .doAssert(settlementSummary -> settlementSummary.assertFraudulent());
    }

    @Test(dataProvider = "topdanmarkDataProvider", description = "FNOL")
    public void fnolNoFraud(@UserCompany(TOPDANMARK) User user, Claim claim) throws IOException {

        fnol(user, claim, EventApiService.FraudStatus.NOT_FRAUDULENT)
                .getSettlementSummary()
                .doAssert(settlementSummary -> settlementSummary.assertNotFraudulent());
    }

    private SettlementPage productSearch(ClaimRequest claimRequest, User user, ClaimItem claimItem, EventApiService.FraudStatus fraudStatus) throws IOException {

        String token = getToken(claimRequest);
        SettlementPage settlementPage = loginAndOpenUnifiedIntegrationClaimByToken(user, token)
                .toTextSearchPage()
                .searchByProductName(SONY_HDR_CX450)
                .chooseCategory(claimItem.getCategoryVideoCamera())
                .openSidForFirstProduct()
                .disableAge()
                .closeSidWithOk();

        List<ClaimLineChanged> events = fraudAlertStubs
                .waitForClaimUpdatedEvents(token, 1);

        Case caseChanged = new UnifiedIntegrationService()
                .getCaseEndpointByToken(COUNTRY, TENANT, token, null);

        Item item = caseChanged
                .getLoss()
                .getContent()
                .getItems()
                .get(0);

        assertThat(item.getDescription()).contains(SONY_HDR_CX450);
        assertThat(item.getCategory()).isEqualTo(claimItem.getCategoryVideoCamera().getGroupName());
        assertThat(item.getValuationByType("CATALOG_PRICE").getPrice()).isEqualTo(2260.00);
        assertThat(item.getValuationByType("MARKET_PRICE").getPrice()).isEqualTo(2699.00);

        fraudStatus(events.get(0), claimRequest.getCaseNumber(),fraudStatus);

        return settlementPage;
    }

    private SettlementPage manualClaimHandlingAdd(ClaimRequest claimRequest, User user, ClaimItem claimItem, EventApiService.FraudStatus fraudStatus) throws IOException {

        String token = getToken(claimRequest);
        SettlementPage settlementPage = loginAndOpenUnifiedIntegrationClaimByToken(user, token)
                .openSid()
                .setBaseData(claimItem)
                .disableAge()
                .closeSidWithOk();

        List<ClaimLineChanged> events = fraudAlertStubs
                .waitForClaimUpdatedEvents(token, 1);

        Case caseChanged = new UnifiedIntegrationService()
                .getCaseEndpointByToken(COUNTRY, TENANT, token, null);

        Item item = caseChanged
                .getLoss()
                .getContent()
                .getItems()
                .get(0);

        assertThat(item.getDescription()).isEqualTo(claimItem.getTextFieldSP());
        assertThat(item.getCategory()).isEqualTo(claimItem.getCategoryBabyItems().getGroupName());
        assertThat(item.getValuationByType("CUSTOMER_DEMAND").getPrice()).isEqualTo(claimItem.getCustomerDemand());
        assertThat(item.getValuationByType("NEW_PRICE").getPrice()).isEqualTo(claimItem.getNewPriceSP());

        fraudStatus(events.get(0), claimRequest.getCaseNumber(),fraudStatus);

        return settlementPage;
    }

    private SettlementPage manualClaimHandlingRemove(ClaimRequest claimRequest, User user, ClaimItem claimItem, EventApiService.FraudStatus fraudStatus) throws IOException {

        String token = getToken(claimRequest);
        SettlementPage settlementPage = loginAndOpenUnifiedIntegrationClaimByToken(user, token)
                .openSid()
                .setBaseData(claimItem)
                .disableAge()
                .closeSidWithOk()
                .selectLinesByDescriptions(claimItem.getTextFieldSP())
                .delete();

        List<ClaimLineChanged> events = fraudAlertStubs
                .waitForClaimUpdatedEvents(token, 2);

        Case caseChanged = new UnifiedIntegrationService()
                .getCaseEndpointByToken(COUNTRY, TENANT, token, null);

        List<Item> items = caseChanged
                .getLoss()
                .getContent()
                .getItems();

        assertThat(items.size()).isEqualTo(0);

        fraudStatus(events.get(1), claimRequest.getCaseNumber(),fraudStatus);

        return settlementPage;
    }

    private SettlementPage manualClaimHandlingEdit(ClaimRequest claimRequest, User user, ClaimItem claimItem, EventApiService.FraudStatus fraudStatus) throws IOException {

        String token = getToken(claimRequest);
        SettlementPage settlementPage = loginAndOpenUnifiedIntegrationClaimByToken(user, token)
                .openSid()
                .setBaseData(claimItem)
                .disableAge()
                .closeSidWithOk()
                .findClaimLine(claimItem.getTextFieldSP())
                .editLine()
                .setNewPrice(2000.00)
                .disableAge()
                .closeSidWithOk();

        List<ClaimLineChanged> events = fraudAlertStubs
                .waitForClaimUpdatedEvents(token, 2);

        Case caseChanged = new UnifiedIntegrationService()
                .getCaseEndpointByToken(COUNTRY, TENANT, token, null);

        Item item = caseChanged
                .getLoss()
                .getContent()
                .getItems()
                .get(0);

        assertThat(item.getDescription()).isEqualTo(claimItem.getTextFieldSP());
        assertThat(item.getCategory()).isEqualTo(claimItem.getCategoryBabyItems().getGroupName());
        assertThat(item.getValuationByType("CUSTOMER_DEMAND").getPrice()).isEqualTo(claimItem.getCustomerDemand());
        assertThat(item.getValuationByType("NEW_PRICE").getPrice()).isEqualTo(2000.0);

        fraudStatus(events.get(1), claimRequest.getCaseNumber(),fraudStatus);

        return settlementPage;
    }

    private SettlementPage selfService(ClaimRequest claimRequest, User user, Claim claim, EventApiService.FraudStatus fraudStatus) throws IOException {

        String token = getToken(claimRequest);
        loginAndOpenUnifiedIntegrationClaimByToken(user, token)
                .requestSelfService(claim, Constants.DEFAULT_PASSWORD)
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceNewLinkAndOpenIt()
                .login(Constants.DEFAULT_PASSWORD)
                .addDescription(IPHONE)
                .addNewPrice(Constants.PRICE_500)
                .addCustomerDemandPrice(Constants.PRICE_50)
                .selectAge("2")
                .addDocumentation()
                .saveItem()
                .sendResponseToEcc();

        List<ClaimLineChanged> events = fraudAlertStubs
                .waitForClaimUpdatedEvents(token, 1);

        Case caseChanged = new UnifiedIntegrationService()
                .getCaseEndpointByToken(COUNTRY, TENANT, token, null);

        Item item = caseChanged
                .getLoss()
                .getContent()
                .getItems()
                .get(0);

        assertThat(item.getValuationByType("CUSTOMER_DEMAND").getPrice()).isEqualTo(50.0);
        assertThat(item.getValuationByType("NEW_PRICE").getPrice()).isEqualTo(500.0);

        fraudStatus(events.get(0), claimRequest.getCaseNumber(),fraudStatus);

        return loginAndOpenUnifiedIntegrationClaimByToken(user, token);
    }

    private SettlementPage importExcel(ClaimRequest claimRequest, User user, EventApiService.FraudStatus fraudStatus) throws IOException {

        String token = getToken(claimRequest);
        SettlementPage settlementPage = loginAndOpenUnifiedIntegrationClaimByToken(user, token)
                .importExcelFile(excelImportPath);

        List<ClaimLineChanged> events = fraudAlertStubs
                .waitForClaimUpdatedEvents(token, 1);

        Case caseChanged = new UnifiedIntegrationService()
                .getCaseEndpointByToken(COUNTRY, TENANT, token, null);

        List<Item> items = caseChanged
                .getLoss()
                .getContent()
                .getItems();

        assertThat(items.size()).isEqualTo(50);

        fraudStatus(events.get(0), claimRequest.getCaseNumber(),fraudStatus);

        return  settlementPage;
    }
    private SettlementPage fnol(User user, Claim claim, EventApiService.FraudStatus fraudStatus) throws IOException {

        ClaimRequest itemizationRequest = TestData.getClaimRequestItemizationCaseTopdanmarkFNOL();
        ClaimRequest createClaimRequest = TestData.getClaimRequestCreateClaimTopdanmarkFNOL();

        String token = createFNOLClaimAndGetClaimToken(itemizationRequest, createClaimRequest);
        loginAndOpenUnifiedIntegrationClaimByToken(user, token).requestSelfService(claim, Constants.DEFAULT_PASSWORD)
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceNewLinkAndOpenIt()
                .login(Constants.DEFAULT_PASSWORD)
                .addDescription(IPHONE)
                .addNewPrice(Constants.PRICE_500)
                .addCustomerDemandPrice(Constants.PRICE_50)
                .selectAge("2")
                .addDocumentation()
                .saveItem()
                .sendResponseToEcc();

        List<ClaimLineChanged> events = fraudAlertStubs
                .waitForClaimUpdatedEvents(token, 1);

        Case caseChanged = new UnifiedIntegrationService()
                .getCaseEndpointByToken(COUNTRY, TENANT, token, null);

        Item item = caseChanged
                .getLoss()
                .getContent()
                .getItems()
                .get(0);

        assertThat(item.getValuationByType("CUSTOMER_DEMAND").getPrice()).isEqualTo(50.0);
        assertThat(item.getValuationByType("NEW_PRICE").getPrice()).isEqualTo(500.0);

        fraudStatus(events.get(0), createClaimRequest.getCaseNumber(),fraudStatus);

        return loginAndOpenUnifiedIntegrationClaimByToken(user, token);
    }

    private void fraudStatus(ClaimLineChanged event, String caseNumber, EventApiService.FraudStatus fraudStatus){

        new EventApiService().sendFraudStatus(event, fraudStatus.name());
        databaseApi.waitForFraudStatusChange(fraudStatus.getStatus(), caseNumber);
    }

    private String getToken(ClaimRequest claimRequest){

        claimRequest.setAccidentDate(format(LocalDateTime.now().minusDays(2L), ISO8601));
        return createCwaClaimAndGetClaimToken(claimRequest);
    }
}