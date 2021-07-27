package com.scalepoint.automation.tests.fraudAlert;

import com.scalepoint.automation.pageobjects.pages.CustomerDetailsPage;
import com.scalepoint.automation.pageobjects.pages.MailsPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.services.externalapi.EventApiService;
import com.scalepoint.automation.services.restService.UnifiedIntegrationService;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.testGroups.UserCompanyGroups;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.annotations.UserCompany;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.changed.Case;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.changed.Item;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.fraudStatus.ClaimLineChanged;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.entity.input.ClaimItem;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

import static com.scalepoint.automation.pageobjects.pages.Page.at;
import static com.scalepoint.automation.services.usersmanagement.CompanyCode.TOPDANMARK;
import static org.assertj.core.api.Assertions.assertThat;

public class FraudAlertSystemTest extends FraudAlertBase {

    @Test(groups = {TestGroups.FRAUD_ALERT, TestGroups.UNI, UserCompanyGroups.TOPDANMARK}, dataProvider = "topdanmarkDataProvider",
            description = "Add")
    public void productSearchFraudSystemTest(@UserCompany(TOPDANMARK) User user, ClaimRequest claimRequest, ClaimItem claimItem) throws IOException {

        productSearch(claimRequest, user, claimItem, EventApiService.FraudStatus.FRAUDULENT)
                .getSettlementSummary()
                .doAssert(settlementSummary -> settlementSummary.assertFraudulent());
    }

    @Test(groups = {TestGroups.FRAUD_ALERT, TestGroups.UNI, UserCompanyGroups.TOPDANMARK}, dataProvider = "topdanmarkDataProvider",
            description = "Add")
    public void productSearchNoFraudSystemTest(@UserCompany(TOPDANMARK) User user, ClaimRequest claimRequest, ClaimItem claimItem) throws IOException {

        productSearch(claimRequest, user, claimItem, EventApiService.FraudStatus.NOT_FRAUDULENT)
                .getSettlementSummary()
                .doAssert(settlementSummary -> settlementSummary.assertNotFraudulent());
    }

    @Test(groups = {TestGroups.FRAUD_ALERT, TestGroups.UNI, UserCompanyGroups.TOPDANMARK}, dataProvider = "topdanmarkDataProvider",
            description = "Add")
    public void manualClaimHandlingAddFraudSystemTest(@UserCompany(TOPDANMARK) User user, ClaimRequest claimRequest, ClaimItem claimItem) throws IOException {

        manualClaimHandlingAdd(claimRequest, user, claimItem, EventApiService.FraudStatus.FRAUDULENT)
                .getSettlementSummary()
                .doAssert(settlementSummary -> settlementSummary.assertFraudulent());
    }

    @Test(groups = {TestGroups.FRAUD_ALERT, TestGroups.UNI, UserCompanyGroups.TOPDANMARK}, dataProvider = "topdanmarkDataProvider",
            description = "Add")
    public void manualClaimHandlingAddNoFraudSystemTest(@UserCompany(TOPDANMARK) User user, ClaimItem claimItem, ClaimRequest claimRequest) throws IOException {

        manualClaimHandlingAdd(claimRequest, user, claimItem, EventApiService.FraudStatus.NOT_FRAUDULENT)
                .getSettlementSummary()
                .doAssert(settlementSummary -> settlementSummary.assertNotFraudulent());
    }

    @Test(groups = {TestGroups.FRAUD_ALERT, TestGroups.UNI, UserCompanyGroups.TOPDANMARK}, dataProvider = "topdanmarkDataProvider",
            description = "Remove")
    public void manualClaimHandlingRemoveFraudSystemTest(@UserCompany(TOPDANMARK) User user, ClaimItem claimItem, ClaimRequest claimRequest) throws IOException {

        manualClaimHandlingRemove(claimRequest, user, claimItem, EventApiService.FraudStatus.FRAUDULENT)
                .getSettlementSummary()
                .doAssert(settlementSummary -> settlementSummary.assertFraudulent());
    }

    @Test(groups = {TestGroups.FRAUD_ALERT, TestGroups.UNI, UserCompanyGroups.TOPDANMARK}, dataProvider = "topdanmarkDataProvider",
            description = "Remove")
    public void manualClaimHandlingRemoveNoFraudSystemTest(@UserCompany(TOPDANMARK) User user, ClaimItem claimItem, ClaimRequest claimRequest) throws IOException {

        manualClaimHandlingRemove(claimRequest, user, claimItem, EventApiService.FraudStatus.NOT_FRAUDULENT)
                .getSettlementSummary()
                .doAssert(settlementSummary -> settlementSummary.assertNotFraudulent());
    }

    @Test(groups = {TestGroups.FRAUD_ALERT, TestGroups.UNI, UserCompanyGroups.TOPDANMARK}, dataProvider = "topdanmarkDataProvider",
            description = "Edit")
    public void manualClaimHandlingEditFraudSystemTest(@UserCompany(TOPDANMARK) User user, ClaimItem claimItem, ClaimRequest claimRequest) throws IOException {

        manualClaimHandlingEdit(claimRequest, user, claimItem, EventApiService.FraudStatus.FRAUDULENT)
                .getSettlementSummary()
                .doAssert(settlementSummary -> settlementSummary.assertFraudulent());
    }

    @Test(groups = {TestGroups.FRAUD_ALERT, TestGroups.UNI, UserCompanyGroups.TOPDANMARK}, dataProvider = "topdanmarkDataProvider",
            description = "Edit")
    public void manualClaimHandlingEditNoFraudSystemTest(@UserCompany(TOPDANMARK) User user, ClaimItem claimItem, ClaimRequest claimRequest) throws IOException {

        manualClaimHandlingEdit(claimRequest, user, claimItem, EventApiService.FraudStatus.NOT_FRAUDULENT)
                .getSettlementSummary()
                .doAssert(settlementSummary -> settlementSummary.assertNotFraudulent());
    }

    @Test(groups = {TestGroups.FRAUD_ALERT, TestGroups.UNI, UserCompanyGroups.TOPDANMARK}, dataProvider = "topdanmarkDataProvider",
            description = "SelfService")
    public void selfServiceAddFraudSystemTest(@UserCompany(TOPDANMARK) User user, Claim claim, ClaimRequest claimRequest) throws IOException {

        selfService(claimRequest, user, claim, EventApiService.FraudStatus.FRAUDULENT, SettlementPage.class)
                .getSettlementSummary()
                .doAssert(settlementSummary -> settlementSummary.assertFraudulent());
    }

    @Test(groups = {TestGroups.FRAUD_ALERT, TestGroups.UNI, UserCompanyGroups.TOPDANMARK}, dataProvider = "topdanmarkDataProvider",
            description = "SelfService")
    public void selfServiceAddNoFraudSystemTest(@UserCompany(TOPDANMARK) User user, Claim claim, ClaimRequest claimRequest) throws IOException {

        selfService(claimRequest, user, claim, EventApiService.FraudStatus.NOT_FRAUDULENT, CustomerDetailsPage.class)
                .reopenClaim()
                .getSettlementSummary()
                .doAssert(settlementSummary -> settlementSummary.assertNotFraudulent());
    }

    @Test(groups = {TestGroups.FRAUD_ALERT, TestGroups.UNI, UserCompanyGroups.TOPDANMARK}, dataProvider = "topdanmarkDataProvider",
            description = "CHARLIE-508 Verify that after importing excel with discretionary valuation" +
                    " drop-down for choosing reason is enabled")
    public void importExcelNoFraudSystemTest(@UserCompany(TOPDANMARK) User user,
                                             ClaimRequest claimRequest) throws IOException {

        importExcel(claimRequest, user, EventApiService.FraudStatus.NOT_FRAUDULENT)
                .getSettlementSummary()
                .doAssert(settlementSummary -> settlementSummary.assertNotFraudulent());
    }

    @Test(groups = {TestGroups.FRAUD_ALERT, TestGroups.UNI, UserCompanyGroups.TOPDANMARK}, dataProvider = "topdanmarkDataProvider",
            description = "CHARLIE-508 Verify that after importing excel with discretionary valuation" +
                    " drop-down for choosing reason is enabled")
    public void importExcelFraudSystemTest(@UserCompany(TOPDANMARK) User user,
                                           ClaimRequest claimRequest) throws IOException {

        importExcel(claimRequest, user, EventApiService.FraudStatus.FRAUDULENT)
                .getSettlementSummary()
                .doAssert(settlementSummary -> settlementSummary.assertFraudulent());
    }

    @Test(groups = {TestGroups.FRAUD_ALERT, TestGroups.UNI, UserCompanyGroups.TOPDANMARK}, dataProvider = "topdanmarkDataProvider",
            description = "FNOL")
    public void fnolFraudSystemTest(@UserCompany(TOPDANMARK) User user, Claim claim) throws IOException {

        fnol(user, claim, EventApiService.FraudStatus.FRAUDULENT)
                .getSettlementSummary()
                .doAssert(settlementSummary -> settlementSummary.assertFraudulent());
    }

    @Test(groups = {TestGroups.FRAUD_ALERT, TestGroups.UNI, UserCompanyGroups.TOPDANMARK}, dataProvider = "topdanmarkDataProvider",
            description = "FNOL")
    public void fnolNoFraudSystemTest(@UserCompany(TOPDANMARK) User user, Claim claim) throws IOException {

        fnol(user, claim, EventApiService.FraudStatus.NOT_FRAUDULENT);
        at(CustomerDetailsPage.class)
                .reopenClaim()
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
        assertThat(item.getValuationByType("CATALOG_PRICE").getPrice()).isEqualTo(2324.07);
        assertThat(item.getValuationByType("MARKET_PRICE").getPrice()).isEqualTo(2499.00);

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

    private  <T extends Page> T selfService(ClaimRequest claimRequest, User user, Claim claim, EventApiService.FraudStatus fraudStatus, Class<T> returnPageClass) throws IOException {

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

        return loginAndOpenUnifiedIntegrationClaimByToken(user, token, returnPageClass);
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

        fraudStatus(events.get(0), createClaimRequest.getCaseNumber(),fraudStatus);

        return loginAndOpenUnifiedIntegrationClaimByToken(user, token);
    }
}