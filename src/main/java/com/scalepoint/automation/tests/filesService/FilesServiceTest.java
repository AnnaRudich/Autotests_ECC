package com.scalepoint.automation.tests.filesService;

import com.scalepoint.automation.pageobjects.pages.MailsPage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.annotations.UserCompany;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.attachmentUpdated.Change;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

import static com.scalepoint.automation.services.usersmanagement.CompanyCode.TOPDANMARK;
import static com.scalepoint.automation.utils.DateUtils.ISO8601;
import static com.scalepoint.automation.utils.DateUtils.format;

public class FilesServiceTest extends BaseTest {

    private String[] lineDescriptions = new String[]{"item1", "item2"};
    private File attachment1 = new File("src\\main\\resources\\attachments\\bw.jpg");
    private static final String IPHONE = "iPhone";

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
                .uploadAttachment(attachment1)
                .doAssert(attachmentDialog ->
                        attachmentDialog
                                .attachmentHasLink(attachment1.getName(), 1))
                .getListpanelAttachmentView()
                .linkAttachment(attachment1.getName(), lineDescriptions[1])
                .doAssert(attachmentDialog ->
                        attachmentDialog
                                .attachmentHasLink(attachment1.getName(), 1)
                                .attachmentHasLink(attachment1.getName(), 2));

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
                .uploadAttachment(attachment1)
                .doAssert(attachmentDialog ->
                        attachmentDialog
                                .attachmentExists(attachment1.getName()))
                .getListpanelAttachmentView()
                .linkAttachment(attachment1.getName(), lineDescriptions[0])
                .doAssert(attachmentDialog ->
                        attachmentDialog
                                .attachmentExists(attachment1.getName())
                                .attachmentHasLink(attachment1.getName(), 1));

        eventDatabaseApi
                .assertNumberOfAttachmentsUpdatedEventsThatWasCreatedForClaim(claimRequest,
                        Change.Property.ATTACHMENT_ADDED_TO_CLAIM_LEVEL,1)
                .assertNumberOfAttachmentsUpdatedEventsThatWasCreatedForClaim(claimRequest,
                        Change.Property.ATTACHMENT_ADDED_FROM_CLAIM_LEVEL_TO_CLAIM_LINE_LEVEL, 1);
    }


    @Test(dataProvider = "fraudAlertDataProvider", description = "test3")
    public void attachmenDeletedFromClaimLevel(@UserCompany(TOPDANMARK) User user, ClaimRequest claimRequest, ClaimItem claimItem){
        claimRequest.setAccidentDate(format(LocalDateTime.now().minusDays(2L), ISO8601));
        String token = createCwaClaimAndGetClaimToken(claimRequest);
        loginAndOpenUnifiedIntegrationClaimByToken(user, token)
                .addLines(claimItem, lineDescriptions[0], lineDescriptions[1])
                .getToolBarMenu()
                .openAttachmentsDialog()
                .uploadAttachment(attachment1)
                .doAssert(attachmentDialog ->
                        attachmentDialog
                                .attachmentExists(attachment1.getName()))
                .getListpanelAttachmentView()
                .deleteAttachment(attachment1.getName())
                .doAssert(attachmentDialog ->
                        attachmentDialog
                                .attachmentNotExists(attachment1.getName()));

        eventDatabaseApi
                .assertNumberOfAttachmentsUpdatedEventsThatWasCreatedForClaim(claimRequest,
                        Change.Property.ATTACHMENT_ADDED_TO_CLAIM_LEVEL,1)
                .assertNumberOfAttachmentsUpdatedEventsThatWasCreatedForClaim(claimRequest,
                        Change.Property.ATTACHMENT_DELETED_FROM_CLAIM_LEVEL, 1);
    }

    @Test(dataProvider = "fraudAlertDataProvider", description = "test4")
    public void attachmentUnlinkedFromClaimLineLevel(@UserCompany(TOPDANMARK) User user, ClaimRequest claimRequest, ClaimItem claimItem){
        claimRequest.setAccidentDate(format(LocalDateTime.now().minusDays(2L), ISO8601));
        String token = createCwaClaimAndGetClaimToken(claimRequest);
        loginAndOpenUnifiedIntegrationClaimByToken(user, token)
                .addLines(claimItem, lineDescriptions[0], lineDescriptions[1])
                .getToolBarMenu()
                .openAttachmentsDialog()
                .getTreepanelAttachmentView()
                .selectLine(lineDescriptions[0])
                .uploadAttachment(attachment1)
                .doAssert(attachmentDialog ->
                        attachmentDialog
                                .attachmentExists(attachment1.getName()))
                .getListpanelAttachmentView()
                .unlinkAttachment(attachment1.getName())
                .doAssert(attachmentDialog ->
                        attachmentDialog
                                .attachmentNotExists(attachment1.getName()));

        eventDatabaseApi
                .assertNumberOfAttachmentsUpdatedEventsThatWasCreatedForClaim(claimRequest,
                        Change.Property.ATTACHMENT_ADDED_TO_CLAIM_LINE_LEVEL,1)
                .assertNumberOfAttachmentsUpdatedEventsThatWasCreatedForClaim(claimRequest,
                        Change.Property.ATTACHMENT_UNLINKED_FROM_CLAIM_LINE_LEVEL, 1);
    }

    @Test(dataProvider = "fraudAlertDataProvider",
            description = "SelfService")
    @RequiredSetting(type = FTSetting.SHOW_POLICY_TYPE, enabled = false)
    @RequiredSetting(type = FTSetting.INCLUDE_PURCHASE_PRICE_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_NEW_PRICE_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_USED_NEW_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_CUSTOMER_DEMAND_COLUMN_IN_SELF_SERVICE)
    public void attachmentImportedFromSelfService(@UserCompany(TOPDANMARK) User user, Claim claim, ClaimRequest claimRequest) throws IOException {

        claimRequest.setAccidentDate(format(LocalDateTime.now().minusDays(2L), ISO8601));
        String token = createCwaClaimAndGetClaimToken(claimRequest);
        loginAndOpenUnifiedIntegrationClaimByToken(user, token)
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

        eventDatabaseApi
                .assertNumberOfAttachmentsUpdatedEventsThatWasCreatedForClaim(claimRequest,
                        Change.Property.ATTACHMENT_IMPORTED_FROM_SELFSERVICE,1);
    }

    @Test(dataProvider = "fraudAlertDataProvider", description = "test4")
    public void attachmentImportedFromFNOL(@UserCompany(TOPDANMARK) User user, Claim claim){

        ClaimRequest itemizationRequest = TestData.getClaimRequestItemizationCaseTopdanmarkFNOL();
        ClaimRequest createClaimRequest = TestData.getClaimRequestCreateClaimTopdanmarkFNOL();

        String token = createFNOLClaimAndGetClaimToken(itemizationRequest, createClaimRequest);
        loginAndOpenUnifiedIntegrationClaimByToken(user, token).requestSelfService(claim, Constants.DEFAULT_PASSWORD)
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

        eventDatabaseApi
                .assertNumberOfAttachmentsUpdatedEventsThatWasCreatedForClaim(createClaimRequest,
                        Change.Property.ATTACHMENT_IMPORTED_FROM_FNOL,1);
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
