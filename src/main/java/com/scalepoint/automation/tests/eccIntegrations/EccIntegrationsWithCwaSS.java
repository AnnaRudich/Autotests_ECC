package com.scalepoint.automation.tests.eccIntegrations;

import com.scalepoint.automation.pageobjects.pages.MailsPage;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.services.externalapi.DatabaseApi;
import com.scalepoint.automation.services.externalapi.IntegrationClaimApi;
import com.scalepoint.automation.services.externalapi.TestAccountsApi;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.CwaTaskLog;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import com.scalepoint.automation.utils.data.response.Token;
import com.scalepoint.ecc.thirdparty.integrations.model.cwa.TaskType;
import com.scalepoint.ecc.thirdparty.integrations.model.enums.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Year;
import java.util.UUID;

import static org.testng.Assert.assertTrue;

public class EccIntegrationsWithCwaSS extends BaseTest {

    @Autowired
    private DatabaseApi databaseApi;

    private Token token;
    private String claimToken;
    private Integer userIdByClaimToken;

    @BeforeMethod
    public void setUp(){
        ClaimRequest claimRequest = TestData.getClaimRequest().withCaseNumber(String.valueOf(UUID.randomUUID()));
        token = new TestAccountsApi().sendRequest().getToken();
        claimToken = new IntegrationClaimApi(token).sendRequest(claimRequest).getClaimTokenString();
        userIdByClaimToken = databaseApi.getUserIdByClaimToken(claimToken);
    }

    @RequiredSetting(type = FTSetting.USE_SELF_SERVICE2)
    @RequiredSetting(type = FTSetting.ENABLE_SELF_SERVICE)
    @Test(dataProvider = "testDataProvider")
    public void selfServiceImport(User user, Claim claim, ClaimItem claimItem){

        SettlementPage settlementPage = loginAndOpenCwaClaim(user, claimToken)
                .requestSelfService(claim, Constants.PASSWORD);

        assertTrue(databaseApi.getCwaTaskLogsForClaimId(userIdByClaimToken).stream().anyMatch((CwaTaskLog cwa) ->
            cwa.getTaskType().equals(TaskType.OTHER) && cwa.getTaskStatus().equals(EventType.TASK_CREATED)
        ));

        settlementPage
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceNewLinkAndOpenIt()
                .enterPassword(Constants.PASSWORD)
                .login()
                .addDescription(claimItem.getSetDialogTextMatch())
                .selectPurchaseYear(String.valueOf(Year.now().getValue()))
                .selectPurchaseMonth("Apr")
                .saveItem()
                .sendResponseToEcc();

        assertTrue(databaseApi.getCwaTaskLogsForClaimId(userIdByClaimToken).stream().anyMatch((CwaTaskLog cwa) ->
                cwa.getTaskType().equals(TaskType.OTHER) && cwa.getTaskStatus().equals(EventType.TASK_COMPLETED)
        ));
        assertTrue(databaseApi.getCwaTaskLogsForClaimId(userIdByClaimToken).stream().anyMatch((CwaTaskLog cwa) ->
                cwa.getTaskType().equals(TaskType.SELF_SERVICE) && cwa.getTaskStatus().equals(EventType.TASK_CREATED)
        ));
    }
}