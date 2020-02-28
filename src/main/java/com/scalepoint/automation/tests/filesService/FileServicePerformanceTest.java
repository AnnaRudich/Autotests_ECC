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
    private CSVWriter csv;

    @Parameters({"users"})
    @BeforeClass
    public void startWireMock(String users){

        this.users = Integer.valueOf(users);
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

        eventDatabaseApi
                .assertNumberOfAttachmentsUpdatedEventsThatWasCreatedForClaim(claimRequest,
                        Change.Property.ATTACHMENT_ADDED_TO_CLAIM_LEVEL,1);
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

    @Test(dataProvider = "performanceDataProvider", enabled = true)
    public void performanceTest(String fileGUID) {

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

    @DataProvider(name = "usersDataProvider", parallel = true)
    public static Object[][] usersDataProvider(Method method) {

        Object[][] objects = new Object[users][1];

        for(int i =0;i<users;i++){

            objects[i][0] = new User("autotest-topdanmark".concat(new Integer(i + 1).toString()), "12341234");
        }

        return objects;
    }

    private static String[] test = new String[]{"A07D70E5-8E3F-4E59-B7EA-0D8E2DF39D3C",
            "D7BCAB5D-A1A9-430C-B6E5-08452CE6B8F9",
            "CFC8DF7D-CD86-4980-82E1-CE7043F3127B",
            "8BBC745D-ACE4-4ADA-ADF1-66FC1DA17D44",
            "ED15DB83-5891-4948-814A-4188BCF69F66",
            "D00105D8-F143-4C4B-997F-4A09A2180B1E",
            "4D05F821-9289-47AC-83FE-0031784707F4",
            "6C6DA948-AAF9-4C24-B82B-85CE3D0A6A83",
            "B9F2D179-5B3A-445A-A710-8BA35F609D70",
            "8C7568CD-CD03-4785-BCF1-D47D7A174440",
            "F42942B4-4A99-49ED-8361-F03550092159",
            "F1603704-EAEA-4F93-A8A8-0F24243CF639",
            "ED424ACD-4C3C-4BCA-B1F0-7A8AF8551A0F",
            "7B908928-C986-4C77-A43F-ACE14698F729",
            "B4E5B5F6-02E8-4134-A59B-713D20911741",
            "6DF7A32D-BA46-4688-BF58-A6BFF5BB90FA",
            "DD31EF10-A0E1-441C-ACE8-C9C10D1820E5",
            "E4040879-B3B1-40E9-8B0D-1B2E85BF6541",
            "FA333F96-A8C9-4329-81A6-47670C5A98A7",
            "D40C0068-CCFE-48EC-95E4-A42E6E0DD103",
            "8DA45797-7813-40FA-9A29-E6604DED2B66",
            "EECCE911-281C-41DF-BE02-342F1B0C1194",
            "EAD25B48-D527-4CDA-AE65-E9D808C90FBE",
            "0E4A365D-5563-4F17-86BB-24E3BBDEE0CF",
            "9623B2AA-4064-4EE6-BC9F-FB69A5C99492",
            "BF08ECC7-2784-4745-9847-81EAE21C5A54",
            "CCA84A38-5CAF-42F2-839E-5F7441D32578",
            "97F87211-4B95-4807-B204-8AC0555078C5",
            "C3D4E753-8D95-4984-9D21-CD61EC693315",
            "4BD2C671-9620-4A90-BC7B-97425985CF8A",
            "EE3D57FC-3D43-4AFC-B0AE-B8D151A14158",
            "D8EC4CA8-F989-459E-AEBF-DABFD91B8BA2",
            "395CF4C0-C5AF-404B-975B-A32F5EC25CFB",
            "5DDC00A0-9590-4156-9F72-97555AEFAC1D",
            "F634F146-1FE1-4197-BDC8-D19904383F87",
            "08595DFC-0658-4DDB-A99B-F3E3426FD77E",
            "74A00806-68C7-4034-88CC-535FA3E0BE89",
            "214E6D5B-0049-4F73-AF5C-9DCB087E8429",
            "DD2B589D-A512-46F5-BBDA-BD78FF0651F1",
            "0BD6372A-41E8-42A0-A2E0-0A8B1CC73EDD",
            "78B69A3B-6A42-47BC-A8AB-AA83AD5E7650",
            "2914D357-1C40-43D4-A234-DB4F34176B3D",
            "572783B0-900F-4040-947A-3EE341FD1857",
            "00E311AA-D86E-493E-9CDB-0F1339107071",
            "9F8AC1A6-D39C-40A9-87E4-50B84AED5FE8",
            "FACA015C-EF3D-4E18-A51B-21B101B9E743",
            "A368DE3D-0F1E-43D7-9940-B356B81FC69E",
            "CACA4641-4CD9-4C67-901F-5CFC175B834B",
            "C6582386-4662-4581-B73D-42E6E2AE59FF",
            "9674C3F6-891D-4CBA-8CE6-688EA4902532",
            "1D4CB7F3-1354-43B3-86E4-E3F99AA1EA9F",
            "93DF1185-D98E-4A3A-9729-F53335068739",
            "E0ED12D4-74D1-481F-BB3B-0AB60927B10C",
            "5E10A84C-0A65-4D97-A57E-6807BF7A8510",
            "A418197A-87DB-4655-A89B-029D834B9CE7",
            "82244E79-E9AD-42EA-98FB-8EBC7696A384",
            "B2509365-B524-4678-8290-97F030D4293D",
            "9637C34F-DA78-4AFD-80AF-5F5340ED92A2",
            "C48EC8B3-04F0-45FF-B773-173EDE8E3475",
            "8B2DCD5F-5BBC-4707-BAB8-FA1AEB00F351",
            "B6158191-1805-4C94-8465-5C74C4B73AD7",
            "4E5B83E1-439C-477D-AE39-868BA9A7D657",
            "B3938064-F845-433D-B6F4-976D9351AB12",
            "6466662F-7BE2-48F3-A64E-3C39A11538AC",
            "DC8BBDAB-1560-48DF-937C-F2519C29622C",
            "F77DF4A3-B9DC-414C-BC83-872B4EFAB73D",
            "7E4F0BA5-FD78-4C9B-AE34-F727B811BC5C",
            "A0DEFD28-F892-45E3-8D94-4D919ED347A3",
            "A80EB3AA-9A34-4B8B-B5EF-A634B54F34A0",
            "8CE49173-B50B-46A8-B115-F687D34B7308",
            "5069E878-29E4-44F7-A1EA-A98C89223324",
            "01E0163D-4156-4591-A930-9B2CCA97EDB8",
            "6F0EF4EC-A2C7-4AE1-9C85-1AB60B621001",
            "F28EFBB3-1C71-49FD-A322-B7604DEFBAA0",
            "FEB82901-349D-4FB1-8EA4-51A4DF2E1325",
            "A39A2D4A-EE01-4FB4-904C-DFD4E577AC5F",
            "B231ED34-C091-4A2D-A5CD-941BC5A2D490",
            "75E97969-AB4B-4666-927C-7558E8072D3B",
            "6E46AE7C-D947-4664-8665-8F379E1B953E",
            "F6C1A892-56C8-4E62-B36F-C7B26B63BF83",
            "C40ABACA-1187-442F-B286-B8A1A35422E9",
            "BFB688EF-8C6F-417C-806C-8CF8E3FCB285",
            "20824D63-D854-4120-A0E1-37E62BA29324",
            "8C9B1844-7F77-4186-87DC-F209377DB1EF",
            "0F2D1212-AD2A-42F0-9069-1D434947F266",
            "91D8B9E2-C3C8-404B-8152-E368A96A045A",
            "0C486A38-4628-40CF-84A9-DE670426116D",
            "5594419C-5DC8-4AB3-96FB-250F81BE8A0F",
            "50120010-33BA-4E8A-AF2F-29B61B96D1DF",
            "0B5CCDF3-A349-4D99-9E54-03AC0EE11A72",
            "3529F203-FAE3-4A36-9D94-50E5B76A4BF5",
            "1CFB9AA2-6D74-4656-BAB2-426AEB8841AF",
            "6EA1673D-9629-4341-AE49-F4518662CDC9",
            "931B051B-AF5B-4FD3-964A-A5401C703965",
            "5E4B6948-91DD-45B3-950F-73B0CD6CAE99",
            "75D6802E-C816-4ED2-9E59-90C5175B8E1F",
            "A6FACAC6-6727-44F6-BA1F-2750DC62C8F3",
            "F90B37E4-A1AC-4417-AB6F-942DBC4B2C22",
            "825E6E11-6EFD-4A34-905A-7591BCECF5C3",
            "61BD0B4C-A012-40F0-A934-7E7CE31D3244"};
}
