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

    @Test(dataProvider = "performanceDataProvider", enabled = false)
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

            objects[i][0] = test[i];
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

    private static String[] test = new String[]{"4D05F821-9289-47AC-83FE-0031784707F4",
            "C2EE0867-B70A-4B63-AC3C-00D62CE5E5F3",
            "A418197A-87DB-4655-A89B-029D834B9CE7",
            "CBB5B165-D9A5-4DC3-846F-0374D5CF0A9D",
            "0B5CCDF3-A349-4D99-9E54-03AC0EE11A72",
            "9199973F-B311-43D5-AA1C-04DB93349D54",
            "4C932798-08C0-4DA9-B6A3-0551F3E62CF5",
            "F24EF33A-DBFB-4576-980D-078DC7AB4185",
            "D7BCAB5D-A1A9-430C-B6E5-08452CE6B8F9",
            "49D3F285-162F-49F1-B9BB-087D991BC0F8",
            "0BD6372A-41E8-42A0-A2E0-0A8B1CC73EDD",
            "E0ED12D4-74D1-481F-BB3B-0AB60927B10C",
            "552DF777-14E1-4AF7-BE56-0B1EFAD3CE55",
            "D4AA4A66-5100-4BD0-B617-0B662654DD1C",
            "2953CD78-017A-46AE-B511-0C9FF3A651FD",
            "A07D70E5-8E3F-4E59-B7EA-0D8E2DF39D3C",
            "19B24ADD-6D32-49FD-930E-0E1BC5BD363A",
            "645913FE-7767-41D5-9BE5-0EC6F083F69F",
            "00E311AA-D86E-493E-9CDB-0F1339107071",
            "F1603704-EAEA-4F93-A8A8-0F24243CF639",
            "7936D213-D452-4BF2-A96B-0F8DB9B847A5",
            "CA1EDF20-DD49-4B3A-BD64-10F98A7517D3",
            "685397FB-07AB-4B52-8638-111B8FA10078",
            "9EB33C66-02C9-4D9D-BAD4-1163C643E7D7",
            "B0CDBF41-2DCE-4E6B-BBDE-12475AFD0E12",
            "309F3C59-FDB3-4102-BA06-15361896EB04",
            "A0AAC6FA-0AA1-4972-ACDC-15E814108D04",
            "C48EC8B3-04F0-45FF-B773-173EDE8E3475",
            "5962B909-A23D-4FEF-8C31-1896FDE0703C",
            "16331E7A-BBDC-4518-9D34-199CAFDAEEE7",
            "6F0EF4EC-A2C7-4AE1-9C85-1AB60B621001",
            "E4040879-B3B1-40E9-8B0D-1B2E85BF6541",
            "555BC401-EA56-4E4B-92BC-1BC884F91850",
            "7FD05123-BD9C-44C0-98A7-1BE97BF87BD1",
            "38BE81BB-25C1-4123-84EA-1C85246BF2EE",
            "0F2D1212-AD2A-42F0-9069-1D434947F266",
            "97126785-E3E6-4655-8F84-1DB9D6577AFC",
            "3C4C0E55-4C66-43E1-9F3A-1DD305F07F87",
            "42248ACC-0D91-4C40-825E-1E89AC73D8F8",
            "95E22081-5852-46FD-ACDA-1F05E4437031",
            "26A36BC6-4BC8-402B-9DE9-1F1BF624BA32",
            "80EE0EFB-67FB-42D9-99E6-1F3EC4693BC0",
            "B71F02E7-3B1B-4F8A-B3BF-20969A5E76D4",
            "6A112BC8-16F6-42F3-A14C-219F99101106",
            "FACA015C-EF3D-4E18-A51B-21B101B9E743",
            "538060F3-68CC-4222-BB0C-22AC6D9198F6",
            "CE54E3E2-4D5A-4244-8A6E-247F5BCBFAD8",
            "0E4A365D-5563-4F17-86BB-24E3BBDEE0CF",
            "5594419C-5DC8-4AB3-96FB-250F81BE8A0F",
            "6798C556-7B1D-45EB-BB8E-25C26578714D",
            "85AD26FB-3297-4FC1-B5D4-25CD821F98B1",
            "2805FDA3-0E3B-4BC8-A53E-25FD18ACFF7B",
            "FF6F0D80-6662-45F9-A523-260A571C598E",
            "A6FACAC6-6727-44F6-BA1F-2750DC62C8F3",
            "C3E5BEB3-05A6-4B25-9A40-27C8D86110E5",
            "6118FCFA-8B7B-4AF9-B7F5-289794CACBE9",
            "363C4F0B-B698-4EEC-8F1B-293C2406ADC9",
            "50120010-33BA-4E8A-AF2F-29B61B96D1DF",
            "49D10CD2-E325-42D8-9A44-29FBAA26D4E6",
            "39B735F0-3858-4C5A-A694-2A327AF3E3C5",
            "AE3C8AC8-2B00-4B4F-8A77-2A484BEAAA6C",
            "7CCAFA51-6B97-494F-A70A-2A99198282E6",
            "6D8E4DC8-CF98-4B3A-9307-2B04EEE05BDB",
            "694E0AE3-45A5-4BCA-BE38-2E7E85993231",
            "805D9DE2-7AFD-4D45-948B-3043C7929CF2",
            "215673AD-0819-4180-AE55-306C9D2410BE",
            "92089B24-9CE7-4DFC-A044-30ACE2E5FD8E",
            "E4B31AFF-89BB-4AD3-881A-310CF6FE5103",
            "5EE7345D-D177-40D6-A4C7-31206F72103E",
            "7A003FD1-DDE9-417F-8CE9-332568B8D706",
            "EECCE911-281C-41DF-BE02-342F1B0C1194",
            "673C167A-39D7-4034-88FE-3469FD81BDCC",
            "FCD9EB08-10FB-4B3C-B1AC-35447A47D6B4",
            "20824D63-D854-4120-A0E1-37E62BA29324",
            "48D574F6-5F4D-480B-A18E-389CABA51D5E",
            "02BE2D56-278A-414B-BC99-3BB5F140DA69",
            "6466662F-7BE2-48F3-A64E-3C39A11538AC",
            "48D68654-99F3-4D12-A243-3D951F3DA6E8",
            "AB5612C5-1254-4956-A68E-3DFD04D0821A",
            "572783B0-900F-4040-947A-3EE341FD1857",
            "41D7CBBE-2186-4498-9E14-3FB6EFADB155",
            "D4C8A3C6-C6DB-42D5-93F8-3FDF2F9FF613",
            "ED15DB83-5891-4948-814A-4188BCF69F66",
            "D25355C8-3358-49C3-946E-424B0A78EFFD",
            "E9125FE8-C44B-4F10-B7B3-426799C24987",
            "1CFB9AA2-6D74-4656-BAB2-426AEB8841AF",
            "C6582386-4662-4581-B73D-42E6E2AE59FF",
            "68EF4046-F018-4B8C-A2CC-456CF81CE2A6",
            "12B3C411-8D3F-4D37-B4DB-4700B5443514",
            "A4D57A59-C65C-4F72-8681-470A35F54E16",
            "FA333F96-A8C9-4329-81A6-47670C5A98A7",
            "DBCA294B-C7CB-44CB-A441-4776CA847C80",
            "D00105D8-F143-4C4B-997F-4A09A2180B1E",
            "1A95A50A-4D03-4C20-8801-4B75ABC1774E",
            "7561AD72-26F2-4E53-8D4B-4CC76AE2ADDD",
            "A0DEFD28-F892-45E3-8D94-4D919ED347A3",
            "C4F753DF-422C-4F87-ADB0-4E83DC8ECCE9",
            "F1FB2EE2-E644-439D-BD31-4FF7964A4932",
            "102E72CA-3801-4CC7-9174-50AF3C0C550F",
            "9F8AC1A6-D39C-40A9-87E4-50B84AED5FE8"};
}
