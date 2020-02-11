package com.scalepoint.automation.tests.filesService;

import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.annotations.RunOn;
import com.scalepoint.automation.utils.annotations.UserCompany;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.attachmentUpdated.Change;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import com.scalepoint.automation.utils.driver.DriverType;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

import static com.scalepoint.automation.services.usersmanagement.CompanyCode.TOPDANMARK;
import static com.scalepoint.automation.utils.DateUtils.ISO8601;
import static com.scalepoint.automation.utils.DateUtils.format;

public class FilesServiceTest extends BaseTest {

    private String[] lineDescriptions = new String[]{"item1", "item2"};
    private File attachment1 = new File("C:\\Users\\bna\\Desktop\\rnv_issue.png");

    @Test(dataProvider = "fraudAlertDataProvider", description = "test1")
    public void attachmentAddedFromClaimLineLevelToClaimLineLevel(@UserCompany(TOPDANMARK) User user, ClaimRequest claimRequest, ClaimItem claimItem){
        claimRequest.setAccidentDate(format(LocalDateTime.now().minusDays(2L), ISO8601));
        String token = createCwaClaimAndGetClaimToken(claimRequest);
        loginAndOpenUnifiedIntegrationClaimByToken(user, token)
                .addLines(claimItem, lineDescriptions[0], lineDescriptions[1])
                .getToolBarMenu()
                .openAttachmentsDialog()
                .getTreepanelAttachmentView()
                .selectLine(lineDescriptions[0])
                .uploadAttachment(attachment1.getAbsolutePath())
                .getListpanelAttachmentView()
                .linkAttachment(attachment1.getName(), lineDescriptions[1]);

        eventDatabaseApi
                .assertNumberOfAttachmentsUpdatedEventsThatWasCreatedForClaim(claimRequest,
                        Change.Property.ATTACHMENT_ADDED_TO_CLAIM_LINE_LEVEL,1)
                .assertNumberOfAttachmentsUpdatedEventsThatWasCreatedForClaim(claimRequest,
                        Change.Property.ATTACHMENT_ADDED_FROM_CLAIM_LINE_LEVEL_TO_CLAIM_LINE_LEVEL, 1);
    }

    @Test(dataProvider = "fraudAlertDataProvider", description = "test2")
    public void attachmentAddedFromClaimLevelToClaimLineLevel(@UserCompany(TOPDANMARK) User user, ClaimRequest claimRequest, ClaimItem claimItem){
        claimRequest.setAccidentDate(format(LocalDateTime.now().minusDays(2L), ISO8601));
        String token = createCwaClaimAndGetClaimToken(claimRequest);
        loginAndOpenUnifiedIntegrationClaimByToken(user, token)
                .addLines(claimItem, lineDescriptions[0], lineDescriptions[1])
                .getToolBarMenu()
                .openAttachmentsDialog()
                .uploadAttachment(attachment1.getAbsolutePath())
                .getListpanelAttachmentView()
                .linkAttachment(attachment1.getName(), lineDescriptions[0]);

        eventDatabaseApi
                .assertNumberOfAttachmentsUpdatedEventsThatWasCreatedForClaim(claimRequest,
                        Change.Property.ATTACHMENT_ADDED_TO_CLAIM_LEVEL,1)
                .assertNumberOfAttachmentsUpdatedEventsThatWasCreatedForClaim(claimRequest,
                        Change.Property.ATTACHMENT_ADDED_FROM_CLAIM_LEVEL_TO_CLAIM_LINE_LEVEL, 1);
    }


    @Test(dataProvider = "fraudAlertDataProvider", description = "test3")
    public void AttachmenDeletedFromClaimLevel(@UserCompany(TOPDANMARK) User user, ClaimRequest claimRequest, ClaimItem claimItem){
        claimRequest.setAccidentDate(format(LocalDateTime.now().minusDays(2L), ISO8601));
        String token = createCwaClaimAndGetClaimToken(claimRequest);
        loginAndOpenUnifiedIntegrationClaimByToken(user, token)
                .addLines(claimItem, lineDescriptions[0], lineDescriptions[1])
                .getToolBarMenu()
                .openAttachmentsDialog()
                .uploadAttachment(attachment1.getAbsolutePath())
                .getListpanelAttachmentView()
                .deleteAttachment(attachment1.getName());

        eventDatabaseApi
                .assertNumberOfAttachmentsUpdatedEventsThatWasCreatedForClaim(claimRequest,
                        Change.Property.ATTACHMENT_ADDED_TO_CLAIM_LEVEL,1)
                .assertNumberOfAttachmentsUpdatedEventsThatWasCreatedForClaim(claimRequest,
                        Change.Property.ATTACHMENT_DELETED_FROM_CLAIM_LEVEL, 1);
    }

    @Test(dataProvider = "fraudAlertDataProvider", description = "test4")
    public void AttachmentUnlinkedFromClaimLineLevel(@UserCompany(TOPDANMARK) User user, ClaimRequest claimRequest, ClaimItem claimItem){
        claimRequest.setAccidentDate(format(LocalDateTime.now().minusDays(2L), ISO8601));
        String token = createCwaClaimAndGetClaimToken(claimRequest);
        loginAndOpenUnifiedIntegrationClaimByToken(user, token)
                .addLines(claimItem, lineDescriptions[0], lineDescriptions[1])
                .getToolBarMenu()
                .openAttachmentsDialog()
                .getTreepanelAttachmentView()
                .selectLine(lineDescriptions[0])
                .uploadAttachment(attachment1.getAbsolutePath())
                .getListpanelAttachmentView()
                .unlinkAttachment(attachment1.getName());

        eventDatabaseApi
                .assertNumberOfAttachmentsUpdatedEventsThatWasCreatedForClaim(claimRequest,
                        Change.Property.ATTACHMENT_ADDED_TO_CLAIM_LINE_LEVEL,1)
                .assertNumberOfAttachmentsUpdatedEventsThatWasCreatedForClaim(claimRequest,
                        Change.Property.ATTACHMENT_UNLINKED_FROM_CLAIM_LINE_LEVEL, 1);
    }

    @DataProvider(name = "fraudAlertDataProvider")
    public static Object[][] fraudAlertDataProvider(Method method) {

        Object[][] testDataProvider = provide(method);

        for (int i = 0; i < testDataProvider[0].length; i++) {
            if (testDataProvider[0][i].getClass().equals(ClaimRequest.class)) {

                testDataProvider[0][i] = TestData.getClaimRequestFraudAlert();
            }
        }

        return testDataProvider;
    }
}
