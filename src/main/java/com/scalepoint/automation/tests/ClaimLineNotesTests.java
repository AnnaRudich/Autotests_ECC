package com.scalepoint.automation.tests;

import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.entity.input.ClaimItem;
import org.testng.annotations.Test;

public class ClaimLineNotesTests extends BaseTest {

    String firstItemName = "item1";
    String secondItemName = "item2";
    String firstNoteText = "noteText1";
    String secondNoteText = "noteText2";
    String thirdNoteText = "noteText3";

    @Test(groups = {TestGroups.CLAIM_LINES_NOTES}, dataProvider = "testDataProvider")
    public void editClaimLineNoteTest(User user, Claim claim, ClaimItem claimItem) {

        loginAndCreateClaim(user, claim)
                .addLines(claimItem, firstItemName)
                .addLines(claimItem, secondItemName)
                .getToolBarMenu()
                .openClaimLineNotes()
                .toClaimLineNotesPage()
                .clickClaimLine(firstItemName)
                .enterClaimLineNote(firstNoteText)
                .doAssert(notesPage -> {
                    notesPage.assertClaimLineNotesGridRowsSize(1);
                    notesPage.assertNoteText(firstNoteText);
                    notesPage.assertClaimLineNotesIconPresent(firstItemName);
                    notesPage.assertClaimLineNotesIconMissing(secondItemName);
                })
                .editClaimLineNote(firstNoteText)
                .doAssert(editNoteDialog -> editNoteDialog.assertNoteText(firstNoteText))
                .enterNote(secondNoteText)
                .doAssert(editNoteDialog -> editNoteDialog.assertNoteText(secondNoteText))
                .cancel()
                .doAssert(notesPage -> {
                    notesPage.assertClaimLineNotesGridRowsSize(1);
                    notesPage.assertNoteText(firstNoteText);
                    notesPage.assertClaimLineNotesIconPresent(firstItemName);
                    notesPage.assertClaimLineNotesIconMissing(secondItemName);
                })
                .editClaimLineNote(firstNoteText)
                .doAssert(editNoteDialog -> editNoteDialog.assertNoteText(firstNoteText))
                .enterNote(secondNoteText)
                .doAssert(editNoteDialog -> editNoteDialog.assertNoteText(secondNoteText))
                .confirm()
                .doAssert(notesPage -> {
                    notesPage.assertClaimLineNotesGridRowsSize(1);
                    notesPage.assertNoteText(secondNoteText);
                    notesPage.assertClaimLineNotesIconPresent(firstItemName);
                    notesPage.assertClaimLineNotesIconMissing(secondItemName);
                })
                .clickCloseWindow()
                .findClaimLine(firstItemName)
                .toClaimLineNote()
                .editClaimLineNote(secondNoteText)
                .enterNote(thirdNoteText)
                .doAssert(editNoteDialog -> editNoteDialog.assertNoteText(thirdNoteText))
                .confirm()
                .doAssert(notesPage -> {
                    notesPage.assertClaimLineNotesGridRowsSize(1);
                    notesPage.assertNoteText(thirdNoteText);
                    notesPage.assertClaimLineNotesIconPresent(firstItemName);
                    notesPage.assertClaimLineNotesIconMissing(secondItemName);
                });
    }

    @Test(groups = {TestGroups.CLAIM_LINES_NOTES}, dataProvider = "testDataProvider")
    public void addClaimLineNoteTest(User user, Claim claim, ClaimItem claimItem) {

        loginAndCreateClaim(user, claim)
                .addLines(claimItem, firstItemName)
                .addLines(claimItem, secondItemName)
                .getToolBarMenu()
                .openClaimLineNotes()
                .toClaimLineNotesPage()
                .clickClaimLine(firstItemName)
                .enterClaimLineNote(firstNoteText)
                .doAssert(notesPage -> {
                    notesPage.assertClaimLineNotesGridRowsSize(1);
                    notesPage.assertNoteText(firstNoteText);
                    notesPage.assertClaimLineNotesIconPresent(firstItemName);
                    notesPage.assertClaimLineNotesIconMissing(secondItemName);
                    notesPage.assertNoteInvisible(firstNoteText);
                })
                .enterVisibleClaimLineNote(secondNoteText)
                .doAssert(notesPage -> {
                    notesPage.assertClaimLineNotesGridRowsSize(2);
                    notesPage.assertNoteText(secondNoteText);
                    notesPage.assertNoteText(firstNoteText);
                    notesPage.assertClaimLineNotesIconPresent(firstItemName);
                    notesPage.assertClaimLineNotesIconMissing(secondItemName);
                    notesPage.assertNoteInvisible(firstNoteText);
                    notesPage.assertNoteVisible(secondNoteText);
                })
                .clickClaimLine(secondItemName)
                .doAssert(notesPage -> {
                    notesPage.assertClaimLineNotesGridRowsSize(0);
                    notesPage.assertClaimLineNotesIconPresent(firstItemName);
                    notesPage.assertClaimLineNotesIconMissing(secondItemName);
                })
                .clickCloseWindow()
                .findClaimLine(secondItemName)
                .doAssert(claimLine -> {
                    claimLine.assertClaimLineNotesIconMissing();
                })
                .toSettlementPage()
                .findClaimLine(firstItemName)
                .doAssert(claimLine -> claimLine.assertClaimLineNotesIconPresent());
    }

    @Test(groups = {TestGroups.CLAIM_LINES_NOTES}, dataProvider = "testDataProvider")
    public void removeClaimLineNoteTest(User user, Claim claim, ClaimItem claimItem) {

        loginAndCreateClaim(user, claim)
                .addLines(claimItem, firstItemName)
                .addLines(claimItem, secondItemName)
                .getToolBarMenu()
                .openClaimLineNotes()
                .toClaimLineNotesPage()
                .clickClaimLine(firstItemName)
                .enterClaimLineNote(firstNoteText)
                .enterClaimLineNote(secondNoteText)
                .enterClaimLineNote(thirdNoteText)
                .clickClaimLine(secondItemName)
                .enterClaimLineNote(firstNoteText)
                .enterClaimLineNote(secondNoteText)
                .enterClaimLineNote(thirdNoteText)
                .doAssert(notesPage -> {
                    notesPage.assertClaimLineNotesGridRowsSize(3);
                    notesPage.assertNoteText(firstNoteText);
                    notesPage.assertNoteText(secondNoteText);
                    notesPage.assertNoteText(thirdNoteText);
                    notesPage.assertClaimLineNotesIconPresent(firstItemName);
                    notesPage.assertClaimLineNotesIconPresent(secondItemName);
                })
                .removeNote(firstNoteText)
                .cancel()
                .doAssert(notesPage -> {
                    notesPage.assertClaimLineNotesGridRowsSize(3);
                    notesPage.assertNoteText(firstNoteText);
                    notesPage.assertNoteText(secondNoteText);
                    notesPage.assertNoteText(thirdNoteText);
                    notesPage.assertClaimLineNotesIconPresent(firstItemName);
                    notesPage.assertClaimLineNotesIconPresent(secondItemName);
                })
                .removeNote(secondNoteText)
                .confirm()
                .doAssert(notesPage -> {
                    notesPage.assertClaimLineNotesGridRowsSize(2);
                    notesPage.assertNoteText(firstNoteText);
                    notesPage.assertNoteText(thirdNoteText);
                    notesPage.assertClaimLineNotesIconPresent(firstItemName);
                    notesPage.assertClaimLineNotesIconPresent(secondItemName);
                })
                .removeNote(thirdNoteText)
                .confirm()
                .doAssert(notesPage -> {
                    notesPage.assertClaimLineNotesGridRowsSize(1);
                    notesPage.assertNoteText(firstNoteText);
                    notesPage.assertClaimLineNotesIconPresent(firstItemName);
                    notesPage.assertClaimLineNotesIconPresent(secondItemName);
                })
                .removeNote(firstNoteText)
                .confirm()
                .doAssert(notesPage -> {
                    notesPage.assertClaimLineNotesGridRowsSize(0);
                    notesPage.assertClaimLineNotesIconPresent(firstItemName);
                    notesPage.assertClaimLineNotesIconMissing(secondItemName);
                })
                .clickClaimLine(firstItemName)
                .doAssert(notesPage -> {
                    notesPage.assertClaimLineNotesGridRowsSize(3);
                    notesPage.assertNoteText(firstNoteText);
                    notesPage.assertNoteText(secondNoteText);
                    notesPage.assertNoteText(thirdNoteText);
                    notesPage.assertClaimLineNotesIconPresent(firstItemName);
                    notesPage.assertClaimLineNotesIconMissing(secondItemName);
                })
                .clickCloseWindow()
                .findClaimLine(firstItemName)
                .doAssert(claimLine -> claimLine.assertClaimLineNotesIconPresent())
                .toSettlementPage()
                .findClaimLine(secondItemName)
                .doAssert(claimLine -> claimLine.assertClaimLineNotesIconMissing());
    }
}
