package com.scalepoint.automation.tests.filesService;

import com.scalepoint.automation.pageobjects.pages.MailsPage;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.testGroups.UserCompanyGroups;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.annotations.UserAttributes;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.attachmentUpdated.Change;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.entity.input.ClaimItem;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import static com.scalepoint.automation.services.usersmanagement.CompanyCode.TOPDANMARK;
import static com.scalepoint.automation.utils.DateUtils.ISO8601;
import static com.scalepoint.automation.utils.DateUtils.format;

public class FilesServiceSystemTest extends BaseTest {

    private String[] lineDescriptions = new String[]{"item1", "item2"};
    private File attachment1 = new File("src\\main\\resources\\attachments\\bw.jpg");
    private static final String IPHONE = "iPhone";

    @Test(groups = {TestGroups.FILE_SERVICE, TestGroups.UNI, UserCompanyGroups.TOPDANMARK}, dataProvider = "topdanmarkDataProvider",
            description = "attachmentAddedFromClaimLineLevelToClaimLineLevel")
    public void attachmentAddedFromClaimLineLevelToClaimLineLevelSystemTest(@UserAttributes(company = TOPDANMARK) User user, ClaimRequest claimRequest, ClaimItem claimItem){

        claimRequest.setAccidentDate(format(LocalDateTime.now().minusDays(2L), ISO8601));
        String token = loginFlow.createCwaClaimAndGetClaimToken(claimRequest);
        loginFlow.loginAndOpenUnifiedIntegrationClaimByToken(user, token, SettlementPage.class)
                .addLines(claimItem, lineDescriptions[0], lineDescriptions[1])
                .getToolBarMenu()
                .openAttachmentsDialog()
                .getTreepanelAttachmentView()
                .selectLine(lineDescriptions[0])
                .uploadAttachment(attachment1)
                .doAssert(attachmentDialog ->
                        attachmentDialog
                                .attachmentHasLink(attachment1.getName(), 1))
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

    @Test(groups = {TestGroups.FILE_SERVICE, TestGroups.UNI, UserCompanyGroups.TOPDANMARK}, dataProvider = "topdanmarkDataProvider",
            description = "attachmentAddedFromClaimLevelToClaimLineLevel")
    public void attachmentAddedFromClaimLevelToClaimLineLevelSystemTest(@UserAttributes(company = TOPDANMARK) User user, ClaimRequest claimRequest, ClaimItem claimItem){
        claimRequest.setAccidentDate(format(LocalDateTime.now().minusDays(2L), ISO8601));
        String token = loginFlow.createCwaClaimAndGetClaimToken(claimRequest);
        loginFlow.loginAndOpenUnifiedIntegrationClaimByToken(user, token)
                .addLines(claimItem, lineDescriptions[0], lineDescriptions[1])
                .getToolBarMenu()
                .openAttachmentsDialog()
                .uploadAttachment(attachment1)
                .doAssert(attachmentDialog ->
                        attachmentDialog
                                .attachmentExists(attachment1.getName()))
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

    @Test(groups = {TestGroups.FILE_SERVICE, TestGroups.UNI, UserCompanyGroups.TOPDANMARK}, dataProvider = "topdanmarkDataProvider",
            description = "attachmentDeletedFromClaimLevel")
    public void attachmentDeletedFromClaimLevelSystemTest(@UserAttributes(company = TOPDANMARK) User user, ClaimRequest claimRequest, ClaimItem claimItem){
        claimRequest.setAccidentDate(format(LocalDateTime.now().minusDays(2L), ISO8601));
        String token = loginFlow.createCwaClaimAndGetClaimToken(claimRequest);
        loginFlow.loginAndOpenUnifiedIntegrationClaimByToken(user, token)
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

    @Test(groups = {TestGroups.FILE_SERVICE, TestGroups.UNI, UserCompanyGroups.TOPDANMARK}, dataProvider = "topdanmarkDataProvider",
            description = "attachmentUnlinkedFromClaimLineLevel")
    public void attachmentUnlinkedFromClaimLineLevelSystemTest(@UserAttributes(company = TOPDANMARK) User user, ClaimRequest claimRequest, ClaimItem claimItem){
        claimRequest.setAccidentDate(format(LocalDateTime.now().minusDays(2L), ISO8601));
        String token = loginFlow.createCwaClaimAndGetClaimToken(claimRequest);
        loginFlow.loginAndOpenUnifiedIntegrationClaimByToken(user, token)
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

    @Test(groups = {TestGroups.FILE_SERVICE, TestGroups.UNI, UserCompanyGroups.TOPDANMARK}, dataProvider = "topdanmarkDataProvider",
            description = "SelfService")
    public void attachmentImportedFromSelfServiceSystemTest(@UserAttributes(company = TOPDANMARK) User user, Claim claim, ClaimRequest claimRequest) throws IOException {

        claimRequest.setAccidentDate(format(LocalDateTime.now().minusDays(2L), ISO8601));
        String token = loginFlow.createCwaClaimAndGetClaimToken(claimRequest);
        loginFlow.loginAndOpenUnifiedIntegrationClaimByToken(user, token)
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

    @Test(enabled = false, groups = {TestGroups.FILE_SERVICE, TestGroups.UNI, UserCompanyGroups.TOPDANMARK},
            dataProvider = "topdanmarkDataProvider",
            description = "FNOL")
    public void attachmentImportedFromFNOLSystemTest(@UserAttributes(company = TOPDANMARK) User user, Claim claim){

        ClaimRequest itemizationRequest = TestData.getClaimRequestItemizationCaseTopdanmarkFNOL();
        ClaimRequest createClaimRequest = TestData.getClaimRequestCreateClaimTopdanmarkFNOL();

        String token = loginFlow.createFNOLClaimAndGetClaimToken(itemizationRequest, createClaimRequest);
        loginFlow.loginAndOpenUnifiedIntegrationClaimByToken(user, token)
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
                .assertNumberOfAttachmentsUpdatedEventsThatWasCreatedForClaim(createClaimRequest,
                        Change.Property.ATTACHMENT_IMPORTED_FROM_FNOL,1);
    }
}
