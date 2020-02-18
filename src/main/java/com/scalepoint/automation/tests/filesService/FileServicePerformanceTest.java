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

    private static String[] test = new String[]{"DCCECF45-153B-4CD0-9B44-D2BF8995F009",
            "10297A95-931A-45C4-B7B7-9B2B9D5BF825",
            "423072E6-357E-470C-B9A0-95411C48FB06",
            "14710970-E2AB-4A4A-9E9C-BBDB66D5209B",
            "064B1282-276A-4033-96AC-5C3BF63C4911",
            "E82FF3EC-E326-41AB-9E1C-3C06F6F659EA",
            "AB56485A-498E-4A11-9D9E-40E5C8F08725",
            "90A006FF-5BF9-46EB-82E0-0AA7C05DF7C3",
            "6DD26004-D7B2-4C4D-B460-22EAC069CA77",
            "AC77FC2D-0F34-4553-9F76-C026C86ABD8E",
            "505113CF-9D96-46D0-A9FA-50D5C7A94F06",
            "E223CC91-06D0-4EC5-8238-530D69BBA3AF",
            "FC954A64-4495-4677-ADAB-CD74F4419E17",
            "0BD116CF-CEF8-464A-B2C2-4BC33264E2A0",
            "48B38E96-C046-4337-8A48-C39CBC38443B",
            "12484D1D-A250-4B4B-BD04-B6A093359F74",
            "049BD1FE-40D3-42C2-87F5-0846F30756FE",
            "97C0BB82-EEA8-405A-AA7F-DF7A341B8908",
            "1A0947D0-1754-4D95-96B2-EE6960D8FB21",
            "0E4B62CE-EDBA-42C9-BD67-9AF8A471B3D6",
            "CDAB9EBD-F0F6-44A7-B75A-4FC53607FDE6",
            "425F7827-6626-42AF-84EF-305523F37E05",
            "DD9FF30D-CDA7-4170-9D5F-9A2484304EB7",
            "7CD4F6EC-6ABC-4905-B229-CE2CF2CCAE8A",
            "2D25FDEA-6DB8-40A2-8A3D-7B010CD495A0",
            "883E2A1B-6732-41DA-905F-2F133F1F224D",
            "C84F8988-9CD2-40AA-8093-9BCC8828C1B4",
            "0EDBF0C0-6927-4FC2-833E-4206EB7603FC",
            "DB2FCCF2-D72D-4F5F-A844-64B823A1F50B",
            "553AC882-984C-486F-A241-6C70FCCC22B6",
            "357D2750-F98C-42D3-8124-7D3510D2B986",
            "2681C70B-2E74-4F7B-9036-B5A4CB48792D",
            "C2F479CF-B908-4FB7-A6D2-8FA3C22D71A2",
            "26D535ED-85FB-470B-8FA1-A5016F517AF7",
            "9DC8316D-399B-4036-89AD-89F9A59D432D",
            "5939553D-9648-4943-B987-68D9FB303C9E",
            "FE96BE80-7E33-4025-86FA-D1111DA80934",
            "94ACFE68-42C4-4222-8E44-67E95DE6E1AD",
            "7C880339-7D42-4E9F-BD18-68F5F63C428D",
            "53B88A29-500B-4619-8762-3715F0216A94",
            "7F3C2D96-6246-4447-97B5-5545A3181EF3",
            "6F758A80-61F1-4574-8811-0A46C87A83C8",
            "1FC91C99-B0D9-41F1-BD88-BEA761042E07",
            "65DB35E7-9B26-4020-A237-1B7C5DD78E07",
            "99CE7B2D-FC0B-41D3-B16E-ACD511124E88",
            "B45736FD-39B4-4C5D-AF29-C423A4975293",
            "61EDE7BF-63B8-42B9-A16A-330100196E50",
            "7BC5DE58-B059-4DF7-9634-D2C36616D6AD",
            "B5A81FA2-68E4-4838-8EB8-D5EBEE629EF8",
            "DB468CDA-43C4-4666-82A6-352DD28DBCE4",
            "759FF2FD-0001-4812-9784-7D8539C02CD0",
            "E44D4819-CAF6-4A54-A3B4-5916BE8F7820",
            "550E4598-D02C-4DEF-BB98-08A923E88E5D",
            "7B324B9A-248A-4770-AD8F-F9DE73786626",
            "37250AEB-0D99-400A-98CE-4C680EBE6C73",
            "C2C070D1-6E1C-403B-B507-FC71894D5A7B",
            "4A364E2E-3DD3-4713-A696-C62C6B12C526",
            "94555316-40DB-459C-9CEC-609BC9DB6459",
            "8D562D8A-C76D-40B4-9439-C9BC2E549B7B",
            "23622BF7-BECA-4DA2-9E0B-A9B826EBABD7",
            "A72DA809-69A5-4EB0-A5EF-A8C1D53DC578",
            "084EF47D-1536-4ED1-9B30-C73DF9289DF8",
            "7B9F156A-87E6-4C63-AB65-2703A77F62E9",
            "D038E947-CC70-49C7-AF42-8752B090BCCF",
            "0788C665-5403-43B2-88FD-701A78E6A4AF",
            "C2543E68-BE69-4843-BB80-E276C05CB110",
            "F82C36BF-15A5-4832-8726-2078902C4BD3",
            "AD7914D6-BFB0-4361-B5DF-E872F8947C38",
            "D784FBA6-7362-4BC2-8A99-6A9BF582949F",
            "F4EF837C-26F8-4BD8-AA69-58BC95789CFC",
            "D3ABAF49-13DD-4065-B89C-5EDEE348FEA0",
            "C1A2FD94-AB2C-442A-9ACB-463E3933F107",
            "364C65BB-05AA-46DE-ACBE-768D7D35B4D3",
            "992698F5-AF5F-4B93-86EA-2D73E8D0B353",
            "6EF26731-A9B4-41F9-9D58-94C5EB23748C",
            "3465C1C7-C094-4713-990A-33629FCE54C9",
            "10D3439B-1E03-47B1-ADC0-BA46CEA4DBCF",
            "3723888A-8FAA-4ED0-9354-712373D8C52E",
            "0B08AAD8-C8D7-4F90-AD6B-031309365BCF",
            "37335499-6F2F-464F-AD52-837220C8BADD",
            "D9655B01-8C45-4066-94BC-5A729B09D15B",
            "059F2479-9043-414F-A541-D9FF601D4435",
            "655421EB-77BC-467A-A276-052641173C68",
            "E01A52B4-AC3A-4469-9479-629C6EA58839",
            "F1D4D306-F392-4BED-B015-413E9E1FF2FE",
            "6845CAD3-303F-47CE-8EC6-35CD45569524",
            "90EDC8B8-9B3A-42F7-8440-87700A0D3F19",
            "12CC009C-9EE5-4152-8605-3BB886E9DD13",
            "A860B11C-77B7-435E-80DE-72BE63926C97",
            "D33684EC-7E5D-4529-8B2D-30E4E99C29CD",
            "129FB789-8FB5-4987-9B2C-60122FE6430C",
            "F5E33C0C-CB70-41BC-9568-20C1F4A55EBB",
            "3253BFD2-EEE3-44E0-BC3B-182FCD8895D0",
            "827D8368-6A17-496D-B96A-2DEDE3BC389E",
            "82B1F20F-49F5-4335-9336-CA1A83548665",
            "BDB68F5D-96F0-4FD7-BA0F-8C3D8F9F40FD",
            "3CC750CE-3319-4A4D-B672-A2A677FAC0A1",
            "550C07F5-6ED3-4F0D-9DD3-4D268CE4A650",
            "7AEA3D21-10AD-4E98-87F4-7DB8728ABF46",
            "B60EE0EE-70D7-476A-A28E-F876C1722234"};
}
