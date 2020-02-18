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

    private static String[] test = new String[]{"06A36051-AD42-4249-9631-70B77133B94E",
            "787EDCD8-DCF3-4752-9495-71FC9CA43ED9",
            "6477373B-24F4-4CB7-BA4E-F247D2AC5CCA",
            "0F3D9B1B-549F-4A0A-8244-75DE4060E9BF",
            "48B7F761-BD7F-464A-9A1F-91AA815AA217",
            "B0CDBF41-2DCE-4E6B-BBDE-12475AFD0E12",
            "645913FE-7767-41D5-9BE5-0EC6F083F69F",
            "CA1EDF20-DD49-4B3A-BD64-10F98A7517D3",
            "4614B5EF-4294-4D2A-A9D8-76BE74A156B9",
            "66BB2E4A-05E1-40A2-B6EF-76A800B3289E",
            "80E618C0-DDC2-4CBF-A5CC-94B4F8397186",
            "AB5612C5-1254-4956-A68E-3DFD04D0821A",
            "7372BDED-D6F2-439F-82AA-6A953EB1AA97",
            "CE54E3E2-4D5A-4244-8A6E-247F5BCBFAD8",
            "CFF75599-0A73-47CB-B922-EFBF9D902A81",
            "081ED7B1-1C52-49AC-861A-574A834786DE",
            "38BE81BB-25C1-4123-84EA-1C85246BF2EE",
            "2E7444D2-05B1-4A63-A550-53CCC913CE52",
            "05008596-25A8-437F-B010-96ABB75BFD50",
            "4C932798-08C0-4DA9-B6A3-0551F3E62CF5",
            "A0707F4A-5C54-4AD0-B79D-EBA99A54D802",
            "B327B4CF-A4B4-4627-8D47-CE3C4D52B383",
            "39BB4022-8026-43ED-8880-A53C8BFF8E10",
            "B6032C78-BCE6-4F20-98D4-B552A0385714",
            "2DF346E4-AF92-4560-822E-8785DDC44F3C",
            "7A003FD1-DDE9-417F-8CE9-332568B8D706",
            "D4C8A3C6-C6DB-42D5-93F8-3FDF2F9FF613",
            "80EE0EFB-67FB-42D9-99E6-1F3EC4693BC0",
            "8F33A30D-8587-4BF6-B35B-717DBE27ABC9",
            "21B53DF3-2230-437B-9A7C-E0F6C31C1536",
            "52ECD28D-B047-4F20-AD0C-AC6E65408888",
            "F24EF33A-DBFB-4576-980D-078DC7AB4185",
            "FCCDC388-08D9-4CBD-ACDE-E73DB24B7B46",
            "685397FB-07AB-4B52-8638-111B8FA10078",
            "8B6685CD-5F0C-4B06-935A-CF1C4B38CE29",
            "6118FCFA-8B7B-4AF9-B7F5-289794CACBE9",
            "9EB33C66-02C9-4D9D-BAD4-1163C643E7D7",
            "6B76E739-962B-4006-9E99-B6337E7FA18A",
            "957C4226-BE6E-49E2-ADF4-5863C8B7B95E",
            "98E21B00-44B8-4BFE-8315-B30A627D549B",
            "12B3C411-8D3F-4D37-B4DB-4700B5443514",
            "DBCA294B-C7CB-44CB-A441-4776CA847C80",
            "C2EE0867-B70A-4B63-AC3C-00D62CE5E5F3",
            "90C4BE07-138D-4F97-AAFD-C977E60152D8",
            "ED5118B6-2CD3-429F-BCE9-E7F31C1BB314",
            "309F3C59-FDB3-4102-BA06-15361896EB04",
            "3C4C0E55-4C66-43E1-9F3A-1DD305F07F87",
            "33BE04CE-47E4-4B11-9452-87C7FAA94165",
            "A4D57A59-C65C-4F72-8681-470A35F54E16",
            "6261E598-D98E-40D9-BFD1-FCBA222DF4BE",
            "D79F2DB5-31DD-43B6-9106-842361841D78",
            "25A4F8E3-6372-4D96-A6E2-53C58471292C",
            "6A112BC8-16F6-42F3-A14C-219F99101106",
            "04FD37B4-97D3-4896-B1E5-8D6B1B5AB3F4",
            "9199973F-B311-43D5-AA1C-04DB93349D54",
            "AA283F02-8FBB-449C-98C3-D55B2C6B1CFE",
            "3605ADC9-D654-4310-8B2E-8D6EEA3A0A49",
            "8536AE4D-B2B5-4E79-8134-6D7C0964D80D",
            "97126785-E3E6-4655-8F84-1DB9D6577AFC",
            "ED5D4DD0-B5DD-4677-A4DE-E1A1A114D001",
            "7FD05123-BD9C-44C0-98A7-1BE97BF87BD1",
            "17C4A564-3D38-4682-9B91-D8D1C3198AD0",
            "41D7CBBE-2186-4498-9E14-3FB6EFADB155",
            "C4F753DF-422C-4F87-ADB0-4E83DC8ECCE9",
            "CD3FFE04-B588-4933-85BB-C2F8DD23D2A5",
            "E60FA133-E9C6-4380-8AB0-BA36032B6DDA",
            "04C88F92-FF32-477E-9891-829813FBAAEC",
            "39B735F0-3858-4C5A-A694-2A327AF3E3C5",
            "AEA1ECF9-68C6-47FB-9C04-D24703FDD950",
            "FF6F0D80-6662-45F9-A523-260A571C598E",
            "49D3F285-162F-49F1-B9BB-087D991BC0F8",
            "E83F13BA-94C2-47C5-BFB9-93FFE908846D",
            "1D8AB3FB-FF66-48B6-BC10-F8AB932E2674",
            "A12701CF-30F4-45B3-9E21-D95A11311147",
            "48D574F6-5F4D-480B-A18E-389CABA51D5E",
            "236C3ADB-C278-49A8-B94A-6509EAD8C121",
            "CBB5B165-D9A5-4DC3-846F-0374D5CF0A9D",
            "AAE0AEE0-99FD-40E0-B4D0-E55628CBE05D",
            "DD323844-0FDD-4A62-BE59-E7DDF2EDA2F2",
            "2422F094-409D-4D9D-9AB2-B6A521E5D3C7",
            "E4B31AFF-89BB-4AD3-881A-310CF6FE5103",
            "BF249F90-1209-464F-B7D9-E318D9663C98",
            "002EA697-1D25-4282-BDFE-A1C049150059",
            "CF782CDE-8E7A-4CB4-A0DE-B5ABA34BF95B",
            "6798C556-7B1D-45EB-BB8E-25C26578714D",
            "5962B909-A23D-4FEF-8C31-1896FDE0703C",
            "2C60849D-183B-4D94-8911-D8888A076176",
            "555BC401-EA56-4E4B-92BC-1BC884F91850",
            "19E3EFB0-0A0B-47D9-8385-E13E4310DB08",
            "C3E5BEB3-05A6-4B25-9A40-27C8D86110E5",
            "D2AA2238-22BC-47EB-9E57-9C336114950D",
            "0F6450D9-7561-48B5-AC61-ECC9C12FC2EA",
            "C473F2D6-2B92-4D78-B015-B7AEFF046214",
            "78A302A2-3F54-44F2-9429-737B5BD83E7F",
            "85AD26FB-3297-4FC1-B5D4-25CD821F98B1",
            "6402CA54-BD29-4DAA-9319-86182EB4B4D5",
            "1A95A50A-4D03-4C20-8801-4B75ABC1774E",
            "16331E7A-BBDC-4518-9D34-199CAFDAEEE7",
            "2805FDA3-0E3B-4BC8-A53E-25FD18ACFF7B",
            "09AD0CDD-0B8A-47E4-883D-C448BAEC63F6"};
}
