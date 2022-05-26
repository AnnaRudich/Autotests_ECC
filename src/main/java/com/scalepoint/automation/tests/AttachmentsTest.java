package com.scalepoint.automation.tests;

import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.AttachmentFiles;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.entity.input.ClaimItem;
import org.testng.annotations.Test;

import java.io.File;

public class AttachmentsTest extends BaseUITest {

    private String[] lineDescriptions = new String[]{"item1", "item2"};
    private static final String IPHONE = "iPhone";

    @Test(groups = {TestGroups.ATTACHMENTS}, dataProvider = TEST_DATA_PROVIDER,
            description = "Verifies attachments downloading from different lines")
    public void differentLinesAttachmentDownloadingTest(User user, Claim claim, ClaimItem claimItem, AttachmentFiles attachmentFiles){

        File attachment1 = new File(attachmentFiles.getJpgFile1Loc());
        File attachment2 = new File(attachmentFiles.getJpgFile2Loc());

        loginFlow.loginAndCreateClaim(user, claim)
                .addLines(claimItem, lineDescriptions[0], lineDescriptions[1])
                .getToolBarMenu()
                .openAttachmentsDialog()
                .selectLine(lineDescriptions[0])
                .uploadAttachment(attachment1)
                .doAssert(attachmentDialog ->
                        attachmentDialog
                                .attachmentHasLink(attachment1.getName(), 1))
                .addToDownloading()
                .doAssert(attachmentDialog ->
                        attachmentDialog
                                .attachmentExistsOnDownloadList(attachment1.getName()))
                .selectLine(lineDescriptions[1])
                .uploadAttachment(attachment2)
                .doAssert(attachmentDialog ->
                        attachmentDialog
                                .attachmentHasLink(attachment2.getName(), 2))
                .addToDownloading()
                .doAssert(attachmentDialog ->
                        attachmentDialog
                                .attachmentExistsOnDownloadList(attachment1.getName())
                                .attachmentExistsOnDownloadList(attachment2.getName()))
                .download()
                .doAssert(attachmentDialog ->
                        attachmentDialog
                                .attachmentDownloaded(attachment1.getName())
                                .attachmentDownloaded(attachment2.getName()));
    }

    @Test(groups = {TestGroups.ATTACHMENTS}, dataProvider = TEST_DATA_PROVIDER,
            description = "Verifies attchment downloading from one line")
    public void oneLineAttachmentDownloadingTest(User user, Claim claim, ClaimItem claimItem, AttachmentFiles attachmentFiles){

        File attachment1 = new File(attachmentFiles.getJpgFile1Loc());
        File attachment2 = new File(attachmentFiles.getJpgFile2Loc());

        loginFlow.loginAndCreateClaim(user, claim)
                .addLines(claimItem, lineDescriptions[0], lineDescriptions[1])
                .getToolBarMenu()
                .openAttachmentsDialog()
                .selectLine(lineDescriptions[0])
                .uploadAttachment(attachment1)
                .doAssert(attachmentDialog ->
                        attachmentDialog
                                .attachmentHasLink(attachment1.getName(), 1))
                .addToDownloading()
                .doAssert(attachmentDialog ->
                        attachmentDialog
                                .attachmentExistsOnDownloadList(attachment1.getName()))
                .uploadAttachment(attachment2)
                .doAssert(attachmentDialog ->
                        attachmentDialog
                                .attachmentHasLink(attachment1.getName(), 1)
                                .attachmentHasLink(attachment2.getName(), 1))
                .addToDownloading()
                .doAssert(attachmentDialog ->
                        attachmentDialog
                                .attachmentExistsOnDownloadList(attachment1.getName())
                                .attachmentExistsOnDownloadList(attachment2.getName()))
                .download()
                .doAssert(attachmentDialog ->
                        attachmentDialog
                                .attachmentDownloaded(attachment1.getName())
                                .attachmentDownloaded(attachment2.getName()));
    }

    @Test(groups = {TestGroups.ATTACHMENTS}, dataProvider = TEST_DATA_PROVIDER,
            description = "Verifies attachment downloading from node")
    public void nodeAttachmentDownloadingTest(User user, Claim claim, ClaimItem claimItem, AttachmentFiles attachmentFiles){

        File attachment1 = new File(attachmentFiles.getJpgFile1Loc());
        File attachment2 = new File(attachmentFiles.getJpgFile2Loc());

        loginFlow.loginAndCreateClaim(user, claim)
                .addLines(claimItem, lineDescriptions[0], lineDescriptions[1])
                .getToolBarMenu()
                .openAttachmentsDialog()
                .selectLine(lineDescriptions[0])
                .uploadAttachment(attachment1)
                .doAssert(attachmentDialog ->
                        attachmentDialog
                                .attachmentHasLink(attachment1.getName(), 1))
                .selectLine(lineDescriptions[1])
                .uploadAttachment(attachment2)
                .doAssert(attachmentDialog ->
                        attachmentDialog
                                .attachmentHasLink(attachment2.getName(), 2))
                .selectNode()
                .doAssert(attachmentDialog ->
                        attachmentDialog
                                .attachmentHasLink(attachment1.getName(), 1)
                                .attachmentHasLink(attachment2.getName(), 2))
                .addToDownloading()
                .doAssert(attachmentDialog ->
                        attachmentDialog
                                .attachmentExistsOnDownloadList(attachment1.getName())
                                .attachmentExistsOnDownloadList(attachment2.getName()))
                .download()
                .doAssert(attachmentDialog ->
                        attachmentDialog
                                .attachmentDownloaded(attachment1.getName())
                                .attachmentDownloaded(attachment2.getName()));
    }

    @Test(groups = {TestGroups.ATTACHMENTS}, dataProvider = TEST_DATA_PROVIDER,
            description = "Verifies deletion from attachment list")
    public void deletionFromAttachmentListTest(User user, Claim claim, ClaimItem claimItem, AttachmentFiles attachmentFiles){

        File attachment1 = new File(attachmentFiles.getJpgFile1Loc());
        File attachment2 = new File(attachmentFiles.getJpgFile2Loc());

        loginFlow.loginAndCreateClaim(user, claim)
                .addLines(claimItem, lineDescriptions[0], lineDescriptions[1])
                .getToolBarMenu()
                .openAttachmentsDialog()
                .selectLine(lineDescriptions[0])
                .uploadAttachment(attachment1)
                .doAssert(attachmentDialog ->
                        attachmentDialog
                                .attachmentHasLink(attachment1.getName(), 1))
                .selectLine(lineDescriptions[1])
                .uploadAttachment(attachment2)
                .doAssert(attachmentDialog ->
                        attachmentDialog
                                .attachmentNotExists(attachment1.getName())
                                .attachmentHasLink(attachment2.getName(), 2))
                .selectNode()
                .doAssert(attachmentDialog ->
                        attachmentDialog
                                .attachmentHasLink(attachment1.getName(), 1)
                                .attachmentHasLink(attachment2.getName(), 2))
                .addToDownloading()
                .doAssert(attachmentDialog ->
                        attachmentDialog
                                .attachmentExistsOnDownloadList(attachment1.getName())
                                .attachmentExistsOnDownloadList(attachment2.getName()))
                .deleteDownloadAttachment(attachment1.getName())
                .doAssert(attachmentDialog ->
                        attachmentDialog
                                .attachmentNotExistsOnDownloadList(attachment1.getName())
                                .attachmentExistsOnDownloadList(attachment2.getName()))
                .download()
                .doAssert(attachmentDialog ->
                        attachmentDialog
                                .attachmentDownloaded(attachment2.getName()));
    }

    @Test(groups = {TestGroups.ATTACHMENTS}, dataProvider = TEST_DATA_PROVIDER,
            description = "Verifies clearing the list of attachment")
    public void clearAttachmentListTest(User user, Claim claim, ClaimItem claimItem, AttachmentFiles attachmentFiles){

        File attachment1 = new File(attachmentFiles.getJpgFile1Loc());
        File attachment2 = new File(attachmentFiles.getJpgFile2Loc());

        loginFlow.loginAndCreateClaim(user, claim)
                .addLines(claimItem, lineDescriptions[0], lineDescriptions[1])
                .getToolBarMenu()
                .openAttachmentsDialog()
                .selectLine(lineDescriptions[0])
                .uploadAttachment(attachment1)
                .doAssert(attachmentDialog ->
                        attachmentDialog
                                .attachmentHasLink(attachment1.getName(), 1))
                .selectLine(lineDescriptions[1])
                .uploadAttachment(attachment2)
                .doAssert(attachmentDialog ->
                        attachmentDialog
                                .attachmentNotExists(attachment1.getName())
                                .attachmentHasLink(attachment2.getName(), 2))
                .selectNode()
                .doAssert(attachmentDialog ->
                        attachmentDialog
                                .attachmentHasLink(attachment1.getName(), 1)
                                .attachmentHasLink(attachment2.getName(), 2))
                .addToDownloading()
                .doAssert(attachmentDialog ->
                        attachmentDialog
                                .attachmentExistsOnDownloadList(attachment1.getName())
                                .attachmentExistsOnDownloadList(attachment2.getName()))
                .clearAttachments()
                .doAssert(attachmentDialog ->
                        attachmentDialog
                                .attachmentNotExistsOnDownloadList(attachment1.getName())
                                .attachmentNotExistsOnDownloadList(attachment2.getName()))
                .selectLine(lineDescriptions[0])
                .addToDownloading()
                .doAssert(attachmentDialog ->
                        attachmentDialog
                                .attachmentExistsOnDownloadList(attachment1.getName()))
                .download()
                .doAssert(attachmentDialog ->
                        attachmentDialog
                                .attachmentDownloaded(attachment1.getName()));
    }
}
