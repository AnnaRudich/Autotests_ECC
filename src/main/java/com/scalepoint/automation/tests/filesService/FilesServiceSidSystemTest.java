package com.scalepoint.automation.tests.filesService;

import com.scalepoint.automation.pageobjects.dialogs.AttachmentDialog;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.testGroups.UserCompanyGroups;
import com.scalepoint.automation.tests.BaseUITest;
import com.scalepoint.automation.utils.annotations.UserAttributes;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.TestDataActions;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.attachmentUpdated.Change;
import com.scalepoint.automation.utils.data.entity.input.ClaimItem;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static com.scalepoint.automation.pageobjects.dialogs.BaseDialog.at;
import static com.scalepoint.automation.services.usersmanagement.CompanyCode.TOPDANMARK;
import static com.scalepoint.automation.utils.DateUtils.ISO8601;
import static com.scalepoint.automation.utils.DateUtils.format;

public class FilesServiceSidSystemTest extends BaseUITest {

    private static final String FILES_SERVICE_DATA_PROVIDER = "filesServiceDataProvider";

    @BeforeMethod(alwaysRun = true)
    public void setCommunicationDesignerSection(Object[] objects) {

        List parameters = Arrays.asList(objects);

        User user = getLisOfObjectByClass(parameters, User.class).get(0);
        ClaimItem claimItem = getLisOfObjectByClass(parameters, ClaimItem.class).get(0);
        ClaimRequest claimRequest = getLisOfObjectByClass(parameters, ClaimRequest.class).get(0);
        String[] lineDescriptions = getLisOfObjectByClass(parameters, String[].class).get(0);

        claimRequest.setAccidentDate(format(LocalDateTime.now().minusDays(2L), ISO8601));
        String token = loginFlow.createCwaClaimAndGetClaimToken(claimRequest);

        loginFlow.loginAndOpenUnifiedIntegrationClaimByToken(user, token)
                .addLines(claimItem, lineDescriptions[0], lineDescriptions[1])
                .getToolBarMenu()
                .openAttachmentsDialog();
    }

    @Test(groups = {TestGroups.FILE_SERVICE, TestGroups.UNI, UserCompanyGroups.TOPDANMARK}, dataProvider = FILES_SERVICE_DATA_PROVIDER,
            description = "Verifies attachment added from one claim line to another")
    public void attachmentAddedFromClaimLineLevelToClaimLineLevelSystemTest(@UserAttributes(company = TOPDANMARK) User user,
                                                                            ClaimItem claimItem, ClaimRequest claimRequest,
                                                                            File attachment1, String[] lineDescriptions){

        claimRequest.setAccidentDate(format(LocalDateTime.now().minusDays(2L), ISO8601));
        String token = loginFlow.createCwaClaimAndGetClaimToken(claimRequest);

        loginFlow.loginAndOpenUnifiedIntegrationClaimByToken(user, token)
                .addLines(claimItem, lineDescriptions[0], lineDescriptions[1])
                .getToolBarMenu()
                .openAttachmentsDialog()
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

    @Test(groups = {TestGroups.FILE_SERVICE, TestGroups.UNI, UserCompanyGroups.TOPDANMARK}, dataProvider = FILES_SERVICE_DATA_PROVIDER,
            description = "Verifies attached from claim to claim line")
    public void attachmentAddedFromClaimLevelToClaimLineLevelSystemTest(@UserAttributes(company = TOPDANMARK) User user,
                                                                        ClaimItem claimItem, ClaimRequest claimRequest,
                                                                        File attachment1, String[] lineDescriptions){

        at(AttachmentDialog.class)
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

    @Test(groups = {TestGroups.FILE_SERVICE, TestGroups.UNI, UserCompanyGroups.TOPDANMARK}, dataProvider = FILES_SERVICE_DATA_PROVIDER,
            description = "verifies attachment deleted from claim")
    public void attachmentDeletedFromClaimLevelSystemTest(@UserAttributes(company = TOPDANMARK) User user,
                                                          ClaimItem claimItem, ClaimRequest claimRequest,
                                                          File attachment1, String[] lineDescriptions){

        at(AttachmentDialog.class)
                .uploadAttachment(attachment1)
                .doAssert(attachmentDialog ->
                        attachmentDialog
                                .attachmentExists(attachment1.getName()))
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

    @Test(groups = {TestGroups.FILE_SERVICE, TestGroups.UNI, UserCompanyGroups.TOPDANMARK}, dataProvider = FILES_SERVICE_DATA_PROVIDER,
            description = "Verifies attachment unlinked from claim line")
    public void attachmentUnlinkedFromClaimLineLevelSystemTest(@UserAttributes(company = TOPDANMARK) User user,
                                                               ClaimItem claimItem, ClaimRequest claimRequest,
                                                               File attachment1, String[] lineDescriptions){

        at(AttachmentDialog.class)
                .selectLine(lineDescriptions[0])
                .uploadAttachment(attachment1)
                .doAssert(attachmentDialog ->
                        attachmentDialog
                                .attachmentExists(attachment1.getName()))
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

    @DataProvider(name = FILES_SERVICE_DATA_PROVIDER)
    public static Object[][] filesServiceDataProvider(Method method) {

        String[] lineDescriptions = new String[]{"item1", "item2"};
        File attachment1 = new File("src\\main\\resources\\attachments\\bw.jpg");

        List parameters = removeObjectByClass(TestDataActions.getTestDataParameters(method), ClaimRequest.class);

        ClaimRequest claimRequest = TestData.getClaimRequestFraudAlert();

        parameters.add(claimRequest);
        parameters.add(attachment1);
        parameters.add(lineDescriptions);

        return new Object[][]{

                parameters.toArray()
        };
    }
}
