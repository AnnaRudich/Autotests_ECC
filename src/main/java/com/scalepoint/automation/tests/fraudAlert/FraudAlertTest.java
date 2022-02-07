package com.scalepoint.automation.tests.fraudAlert;

import com.scalepoint.automation.pageobjects.pages.MailsPage;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.services.externalapi.EventApiService;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.testGroups.UserCompanyGroups;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.annotations.UserAttributes;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.fraudStatus.ClaimLineChanged;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.entity.input.ClaimItem;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

import static com.scalepoint.automation.services.usersmanagement.CompanyCode.TOPDANMARK;

public class FraudAlertTest extends FraudAlertBase {

    @Test(groups = {TestGroups.FRAUD_ALERT, TestGroups.ECC, UserCompanyGroups.TOPDANMARK}, dataProvider = "topdanmarkDataProvider",
            description = "Add")
    public void productSearchFraudTest(@UserAttributes(company = TOPDANMARK) User user, ClaimRequest claimRequest, ClaimItem claimItem) throws IOException {

        productSearch(claimRequest, user, claimItem, EventApiService.FraudStatus.FRAUDULENT)
                .getSettlementSummary()
                .doAssert(settlementSummary -> settlementSummary.assertFraudulent());
    }

    @Test(groups = {TestGroups.FRAUD_ALERT, TestGroups.ECC, UserCompanyGroups.TOPDANMARK}, dataProvider = "topdanmarkDataProvider",
            description = "Add")
    public void productSearchNoFraudTest(@UserAttributes(company = TOPDANMARK) User user, ClaimRequest claimRequest, ClaimItem claimItem) throws IOException {

        productSearch(claimRequest, user, claimItem, EventApiService.FraudStatus.NOT_FRAUDULENT)
                .getSettlementSummary()
                .doAssert(settlementSummary -> settlementSummary.assertNotFraudulent());
    }

    @Test(groups = {TestGroups.FRAUD_ALERT, TestGroups.ECC, UserCompanyGroups.TOPDANMARK}, dataProvider = "topdanmarkDataProvider",
            description = "Add")
    public void manualClaimHandlingAddFraudTest(@UserAttributes(company = TOPDANMARK) User user, ClaimRequest claimRequest, ClaimItem claimItem) throws IOException {

        manualClaimHandlingAdd(claimRequest, user, claimItem, EventApiService.FraudStatus.FRAUDULENT)
                .getSettlementSummary()
                .doAssert(settlementSummary -> settlementSummary.assertFraudulent());
    }

    @Test(groups = {TestGroups.FRAUD_ALERT, TestGroups.ECC, UserCompanyGroups.TOPDANMARK}, dataProvider = "topdanmarkDataProvider",
            description = "Add")
    public void manualClaimHandlingAddNoFraudTest(@UserAttributes(company = TOPDANMARK) User user, ClaimItem claimItem, ClaimRequest claimRequest) throws IOException {

        manualClaimHandlingAdd(claimRequest, user, claimItem, EventApiService.FraudStatus.NOT_FRAUDULENT)
                .getSettlementSummary()
                .doAssert(settlementSummary -> settlementSummary.assertNotFraudulent());
    }

    @Test(groups = {TestGroups.FRAUD_ALERT, TestGroups.ECC, UserCompanyGroups.TOPDANMARK}, dataProvider = "topdanmarkDataProvider",
            description = "Remove")
    public void manualClaimHandlingRemoveFraudTest(@UserAttributes(company = TOPDANMARK) User user, ClaimItem claimItem, ClaimRequest claimRequest) throws IOException {

        manualClaimHandlingRemove(claimRequest, user, claimItem, EventApiService.FraudStatus.FRAUDULENT)
                .getSettlementSummary()
                .doAssert(settlementSummary -> settlementSummary.assertFraudulent());
    }

    @Test(groups = {TestGroups.FRAUD_ALERT, TestGroups.ECC, UserCompanyGroups.TOPDANMARK}, dataProvider = "topdanmarkDataProvider",
            description = "Remove")
    public void manualClaimHandlingRemoveNoFraudTest(@UserAttributes(company = TOPDANMARK) User user, ClaimItem claimItem, ClaimRequest claimRequest) throws IOException {

        manualClaimHandlingRemove(claimRequest, user, claimItem, EventApiService.FraudStatus.NOT_FRAUDULENT)
                .getSettlementSummary()
                .doAssert(settlementSummary -> settlementSummary.assertNotFraudulent());
    }

    @Test(groups = {TestGroups.FRAUD_ALERT, TestGroups.ECC, UserCompanyGroups.TOPDANMARK}, dataProvider = "topdanmarkDataProvider",
            description = "Edit")
    public void manualClaimHandlingEditFraudTest(@UserAttributes(company = TOPDANMARK) User user, ClaimItem claimItem, ClaimRequest claimRequest) throws IOException {

        manualClaimHandlingEdit(claimRequest, user, claimItem, EventApiService.FraudStatus.FRAUDULENT)
                .getSettlementSummary()
                .doAssert(settlementSummary -> settlementSummary.assertFraudulent());
    }

    @Test(groups = {TestGroups.FRAUD_ALERT, TestGroups.ECC, UserCompanyGroups.TOPDANMARK}, dataProvider = "topdanmarkDataProvider",
            description = "Edit")
    public void manualClaimHandlingEditNoFraudTest(@UserAttributes(company = TOPDANMARK) User user, ClaimItem claimItem, ClaimRequest claimRequest) throws IOException {

        manualClaimHandlingEdit(claimRequest, user, claimItem, EventApiService.FraudStatus.NOT_FRAUDULENT)
                .getSettlementSummary()
                .doAssert(settlementSummary -> settlementSummary.assertNotFraudulent());
    }

    @Test(groups = {TestGroups.FRAUD_ALERT, TestGroups.ECC, UserCompanyGroups.TOPDANMARK}, dataProvider = "topdanmarkDataProvider",
            description = "SelfService")
    public void selfServiceAddFraudTest(@UserAttributes(company = TOPDANMARK) User user, Claim claim, ClaimRequest claimRequest) throws IOException {

        selfService(claimRequest, user, claim, EventApiService.FraudStatus.FRAUDULENT)
                .getSettlementSummary()
                .doAssert(settlementSummary -> settlementSummary.assertFraudulent());
    }

    @Test(groups = {TestGroups.FRAUD_ALERT, TestGroups.ECC, UserCompanyGroups.TOPDANMARK}, dataProvider = "topdanmarkDataProvider",
            description = "SelfService")
    public void selfServiceAddNoFraudTest(@UserAttributes(company = TOPDANMARK) User user, Claim claim, ClaimRequest claimRequest) throws IOException {

        selfService(claimRequest, user, claim, EventApiService.FraudStatus.NOT_FRAUDULENT)
                .getSettlementSummary()
                .doAssert(settlementSummary -> settlementSummary.assertNotFraudulent());
    }

    @Test(groups = {TestGroups.FRAUD_ALERT, TestGroups.ECC, UserCompanyGroups.TOPDANMARK}, dataProvider = "topdanmarkDataProvider",
            description = "CHARLIE-508 Verify that after importing excel with discretionary valuation" +
                    " drop-down for choosing reason is enabled")
    public void importExcelNoFraudTest(@UserAttributes(company = TOPDANMARK) User user,
                                       ClaimRequest claimRequest) throws IOException {

        importExcel(claimRequest, user, EventApiService.FraudStatus.NOT_FRAUDULENT)
                .getSettlementSummary()
                .doAssert(settlementSummary -> settlementSummary.assertNotFraudulent());
    }

    @Test(groups = {TestGroups.FRAUD_ALERT, TestGroups.ECC, UserCompanyGroups.TOPDANMARK}, dataProvider = "topdanmarkDataProvider",
            description = "CHARLIE-508 Verify that after importing excel with discretionary valuation" +
                    " drop-down for choosing reason is enabled")
    public void importExcelFraudTest(@UserAttributes(company = TOPDANMARK) User user,
                                     ClaimRequest claimRequest) throws IOException {

        importExcel(claimRequest, user, EventApiService.FraudStatus.FRAUDULENT)
                .getSettlementSummary()
                .doAssert(settlementSummary -> settlementSummary.assertFraudulent());
    }

    private SettlementPage productSearch(ClaimRequest claimRequest, User user, ClaimItem claimItem, EventApiService.FraudStatus fraudStatus) throws IOException {

        String token = getToken(claimRequest);
        SettlementPage settlementPage = loginFlow.loginAndOpenUnifiedIntegrationClaimByToken(user, token)
                .toTextSearchPage()
                .searchByProductName(SONY_HDR_CX450)
                .chooseCategory(claimItem.getCategoryVideoCamera())
                .openSidForFirstProduct()
                .disableAge()
                .closeSidWithOk();

        List<ClaimLineChanged> events = fraudAlertStubs
                .waitForClaimUpdatedEvents(token, 1);

        fraudStatus(events.get(0), claimRequest.getCaseNumber(),fraudStatus);

        return settlementPage;
    }

    private SettlementPage manualClaimHandlingAdd(ClaimRequest claimRequest, User user, ClaimItem claimItem, EventApiService.FraudStatus fraudStatus) throws IOException {

        String token = getToken(claimRequest);
        SettlementPage settlementPage = loginFlow.loginAndOpenUnifiedIntegrationClaimByToken(user, token)
                .openSid()
                .setBaseData(claimItem)
                .disableAge()
                .closeSidWithOk();

        List<ClaimLineChanged> events = fraudAlertStubs
                .waitForClaimUpdatedEvents(token, 1);

        fraudStatus(events.get(0), claimRequest.getCaseNumber(),fraudStatus);

        return settlementPage;
    }

    private SettlementPage manualClaimHandlingRemove(ClaimRequest claimRequest, User user, ClaimItem claimItem, EventApiService.FraudStatus fraudStatus) throws IOException {

        String token = getToken(claimRequest);
        SettlementPage settlementPage = loginFlow.loginAndOpenUnifiedIntegrationClaimByToken(user, token)
                .openSid()
                .setBaseData(claimItem)
                .disableAge()
                .closeSidWithOk()
                .selectLinesByDescriptions(claimItem.getTextFieldSP())
                .delete();

        List<ClaimLineChanged> events = fraudAlertStubs
                .waitForClaimUpdatedEvents(token, 2);

        fraudStatus(events.get(1), claimRequest.getCaseNumber(),fraudStatus);

        return settlementPage;
    }

    private SettlementPage manualClaimHandlingEdit(ClaimRequest claimRequest, User user, ClaimItem claimItem, EventApiService.FraudStatus fraudStatus) throws IOException {

        String token = getToken(claimRequest);
        SettlementPage settlementPage = loginFlow.loginAndOpenUnifiedIntegrationClaimByToken(user, token)
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

        fraudStatus(events.get(1), claimRequest.getCaseNumber(),fraudStatus);

        return settlementPage;
    }

    private SettlementPage selfService(ClaimRequest claimRequest, User user, Claim claim, EventApiService.FraudStatus fraudStatus) throws IOException {

        String token = getToken(claimRequest);
        loginFlow.loginAndOpenUnifiedIntegrationClaimByToken(user, token)
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

        fraudStatus(events.get(0), claimRequest.getCaseNumber(),fraudStatus);

        return loginFlow.loginAndOpenUnifiedIntegrationClaimByToken(user, token);
    }

    private SettlementPage importExcel(ClaimRequest claimRequest, User user, EventApiService.FraudStatus fraudStatus) throws IOException {

        String token = getToken(claimRequest);
        SettlementPage settlementPage = loginFlow.loginAndOpenUnifiedIntegrationClaimByToken(user, token)
                .importExcelFile(excelImportPath);

        List<ClaimLineChanged> events = fraudAlertStubs
                .waitForClaimUpdatedEvents(token, 1);

        fraudStatus(events.get(0), claimRequest.getCaseNumber(),fraudStatus);

        return  settlementPage;
    }
}