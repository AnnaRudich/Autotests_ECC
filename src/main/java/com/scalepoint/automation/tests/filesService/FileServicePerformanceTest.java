package com.scalepoint.automation.tests.filesService;

import com.scalepoint.automation.services.restService.AttachmentsService;
import com.scalepoint.automation.services.restService.Common.BaseService;
import com.scalepoint.automation.tests.api.BaseApiTest;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.attachmentUpdated.Change;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Method;

public class FileServicePerformanceTest extends BaseApiTest {

    @Test(dataProvider = "usersDataProvider", enabled = true)
    public void addAttachmentToClaimLevel(User user) throws IOException {

        ClaimRequest claimRequest = TestData.getClaimRequestFraudAlert();

        AttachmentsService attachmentsService = BaseService
                .loginAndOpenClaimWithItems(user, claimRequest, TestData.getInsertSettlementItem(), TestData.getInsertSettlementItem())
                .toAttachments();

        eventDatabaseApi
                .assertNumberOfClaimLineChangedEventsThatWasCreatedForClaim(claimRequest, 2);

        attachmentsService
                .addAttachmentToClaimLevel();

        eventDatabaseApi
                .assertNumberOfAttachmentsUpdatedEventsThatWasCreatedForClaim(claimRequest,
                        Change.Property.ATTACHMENT_ADDED_TO_CLAIM_LEVEL,1);
    }

    @Test(dataProvider = "usersDataProvider", enabled = true)
    public void addAttachmentToClaimLineLevel(User user) throws IOException {

        ClaimRequest claimRequest = TestData.getClaimRequestFraudAlert();

        AttachmentsService attachmentsService = BaseService
                .loginAndOpenClaimWithItems(user, claimRequest, TestData.getInsertSettlementItem(), TestData.getInsertSettlementItem())
                .toAttachments();

        eventDatabaseApi
                .assertNumberOfClaimLineChangedEventsThatWasCreatedForClaim(claimRequest, 2);

        attachmentsService
                .addAttachmentToClaimLineLevel(0);

        eventDatabaseApi
                .assertNumberOfAttachmentsUpdatedEventsThatWasCreatedForClaim(claimRequest,
                        Change.Property.ATTACHMENT_ADDED_TO_CLAIM_LINE_LEVEL,1);
    }

    @Test(dataProvider = "usersDataProvider", enabled = true)
    public void linkAttachmentFromClaimLevel(User user) throws IOException {

        ClaimRequest claimRequest = TestData.getClaimRequestFraudAlert();

        AttachmentsService attachmentsService = BaseService
                .loginAndOpenClaimWithItems(user, claimRequest, TestData.getInsertSettlementItem(), TestData.getInsertSettlementItem())
                .toAttachments();

        eventDatabaseApi
                .assertNumberOfClaimLineChangedEventsThatWasCreatedForClaim(claimRequest, 2);

        attachmentsService
                .addAttachmentToClaimLevel();

        eventDatabaseApi
                .assertNumberOfAttachmentsUpdatedEventsThatWasCreatedForClaim(claimRequest,
                        Change.Property.ATTACHMENT_ADDED_TO_CLAIM_LEVEL,1);

        attachmentsService
                .linkAttachmentToClaimLineLevel(0);

        eventDatabaseApi
                .assertNumberOfAttachmentsUpdatedEventsThatWasCreatedForClaim(claimRequest,
                        Change.Property.ATTACHMENT_ADDED_FROM_CLAIM_LEVEL_TO_CLAIM_LINE_LEVEL, 1);
    }

    @Test(dataProvider = "usersDataProvider", enabled = true)
    public void linkAttachmentFromClaimLineLevel(User user) throws IOException {

        ClaimRequest claimRequest = TestData.getClaimRequestFraudAlert();

        AttachmentsService attachmentsService = BaseService
                .loginAndOpenClaimWithItems(user, claimRequest, TestData.getInsertSettlementItem(), TestData.getInsertSettlementItem())
                .toAttachments()
                .addAttachmentToClaimLineLevel(0);

        eventDatabaseApi
                .assertNumberOfAttachmentsUpdatedEventsThatWasCreatedForClaim(claimRequest,
                        Change.Property.ATTACHMENT_ADDED_TO_CLAIM_LINE_LEVEL,1);

        attachmentsService
                .linkAttachmentToClaimLineLevel(1,0);

        eventDatabaseApi
                .assertNumberOfAttachmentsUpdatedEventsThatWasCreatedForClaim(claimRequest,
                        Change.Property.ATTACHMENT_ADDED_FROM_CLAIM_LINE_LEVEL_TO_CLAIM_LINE_LEVEL, 1);
    }

    @DataProvider(name = "usersDataProvider", parallel = true)
    public static Object[][] usersDataProvider(Method method) {

        int users = 1;

        Object[][] objects = new Object[users][1];

        for(int i =0;i<users;i++){

            objects[i][0] = new User("autotest-topdanmark".concat(new Integer(i + 1).toString()), "12341234");
        }

        return objects;
    }
}
