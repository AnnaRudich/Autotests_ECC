package com.scalepoint.automation.tests.filesService;

import com.opencsv.CSVWriter;
import com.scalepoint.automation.services.externalapi.OauthTestAccountsApi;
import com.scalepoint.automation.services.restService.AttachmentsService;
import com.scalepoint.automation.services.restService.Common.BaseService;
import com.scalepoint.automation.services.restService.FilesServiceService;
import com.scalepoint.automation.tests.api.BaseApiTest;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.attachmentUpdated.Change;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import com.scalepoint.automation.utils.data.response.Token;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;

public class FileServicePerformanceTest extends BaseApiTest {


    private static int users;
    private static String fileGUID;
    private CSVWriter csv;

    @Parameters({"users", "fileGUID"})
    @BeforeClass
    public void startWireMock(String users, String fileGUID) throws IOException {

        this.users = Integer.valueOf(users);
        this.fileGUID = fileGUID;
    }

    @BeforeMethod
    public void createFile(ITestContext iTestContext) throws IOException {
        String name = iTestContext.getName();

        csv = new CSVWriter(new FileWriter(new File(name.concat(".csv"))));
    }

    @AfterClass
    public void closeFile() throws IOException {

        csv.close();
    }
    @Test(dataProvider = "usersDataProvider", enabled = false)
    public void addAttachmentToClaimLevel(User user) throws IOException {

        ClaimRequest claimRequest = TestData.getClaimRequestFraudAlert();

        AttachmentsService attachmentsService = BaseService
                .loginAndOpenClaimWithItems(user, claimRequest, TestData.getInsertSettlementItem(), TestData.getInsertSettlementItem())
                .toAttachments();

        eventDatabaseApi
                .assertNumberOfClaimLineChangedEventsThatWasCreatedForClaim(claimRequest, 2);

        attachmentsService
                .addAttachmentToClaimLevel();

//        eventDatabaseApi
//                .assertNumberOfAttachmentsUpdatedEventsThatWasCreatedForClaim(claimRequest,
//                        Change.Property.ATTACHMENT_ADDED_TO_CLAIM_LEVEL,1);
    }

    @Test(dataProvider = "usersDataProvider", enabled = false)
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

    @Test(dataProvider = "usersDataProvider", enabled = false)
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

    @Test(dataProvider = "usersDataProvider", enabled = false)
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

    @Test(dataProvider = "performanceDataProvider", enabled = true)
    public void test(String fileGUID) throws IOException {

        Token token = new OauthTestAccountsApi().sendRequest(OauthTestAccountsApi.Scope.FILES_READ, "topdanmark_dk_integration", "YBaPu4TqRpE_aYg8r8n8g7qcbOps1gCFm3ATuBdWJCU").getToken();
        LocalDateTime start = LocalDateTime.now();
        new FilesServiceService(token)
                .getFile(fileGUID);
        LocalDateTime end = LocalDateTime.now();
        long duration = Duration.between(start, end).toMillis();
        log.info("Duration :", duration);

        csv.writeNext(new String[]{String.valueOf(duration)});
    }

    @DataProvider(name = "performanceDataProvider", parallel = true)
    public static Object[][] performanceDataProvider(Method method) {

        Object[][] objects = new Object[users][1];

        for(int i =0;i<users;i++){

            objects[i][0] = fileGUID;
        }

        return objects;
    }

    @DataProvider(name = "usersDataProvider", parallel = false)
    public static Object[][] usersDataProvider(Method method) {

        Object[][] objects = new Object[users][1];

        for(int i =0;i<users;i++){

            objects[i][0] = new User("autotest-topdanmark".concat(new Integer(i + 1).toString()), "12341234");
        }

        return objects;
    }
}
