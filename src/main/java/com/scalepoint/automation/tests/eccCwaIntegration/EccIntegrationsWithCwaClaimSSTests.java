package com.scalepoint.automation.tests.eccCwaIntegration;

import com.scalepoint.automation.pageobjects.pages.MailsPage;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.CwaTaskLog;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import com.scalepoint.automation.utils.data.request.ExtraModifier;
import com.scalepoint.ecc.thirdparty.integrations.model.cwa.TaskType;
import com.scalepoint.ecc.thirdparty.integrations.model.enums.EventType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Year;

import static com.scalepoint.automation.utils.Constants.JANUARY;
import static org.assertj.core.api.Assertions.assertThat;

public class EccIntegrationsWithCwaClaimSSTests extends BaseTest {

    private String claimToken;
    private Integer userIdByClaimToken;

    @BeforeMethod
    public void setUpForEccIntegrationsTest(){
        ClaimRequest claimRequest = TestData.getClaimRequest();
        claimRequest.getExtraModifiers().add(new ExtraModifier().withType("cwaServiceId").withValue("1234"));
        claimToken = createCwaClaimAndGetClaimToken(claimRequest);
        userIdByClaimToken = databaseApi.getUserIdByClaimToken(claimToken.replace("c.",""));
    }

    @RequiredSetting(type = FTSetting.USE_SELF_SERVICE2)
    @RequiredSetting(type = FTSetting.ENABLE_SELF_SERVICE)
    @Test(dataProvider = "testDataProvider")
    public void selfServiceImport(User user, ClaimItem claimItem){
        SettlementPage settlementPage = loginAndOpenUnifiedIntegrationClaimByToken(user, claimToken)
                .requestSelfService(Constants.PASSWORD);

        assertThat(databaseApi.getCwaTaskLogsForClaimId(userIdByClaimToken).stream().anyMatch((CwaTaskLog cwa) ->
            cwa.getTaskType().equals(TaskType.SELF_SERVICE_OTHER) && cwa.getTaskStatus().equals(EventType.TASK_CREATED)
        )).isTrue();

        settlementPage
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceNewLinkAndOpenIt()
                .enterPassword(Constants.PASSWORD)
                .login()
                .addDescription(claimItem.getSetDialogTextMatch())
                .selectPurchaseYear(String.valueOf(Year.now().getValue()))
                .selectPurchaseMonth(JANUARY)
                .saveItem()
                .sendResponseToEcc();

        assertThat(databaseApi.getCwaTaskLogsForClaimId(userIdByClaimToken).stream().anyMatch((CwaTaskLog cwa) ->
                cwa.getTaskType().equals(TaskType.SELF_SERVICE_OTHER) && cwa.getTaskStatus().equals(EventType.TASK_COMPLETED)
        )).isTrue();
        assertThat(databaseApi.getCwaTaskLogsForClaimId(userIdByClaimToken).stream().anyMatch((CwaTaskLog cwa) ->
                cwa.getTaskType().equals(TaskType.SELF_SERVICE) && cwa.getTaskStatus().equals(EventType.TASK_CREATED)
        )).isTrue();
    }
}