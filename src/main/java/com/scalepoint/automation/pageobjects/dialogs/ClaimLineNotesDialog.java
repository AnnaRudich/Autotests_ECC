package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Condition.not;
import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;
import static com.scalepoint.automation.utils.Wait.waitForPageLoaded;
import static org.assertj.core.api.Assertions.assertThat;

public class ClaimLineNotesDialog extends BaseDialog {

    private NotesTreePanel notesTreePanel = new NotesTreePanel();
    private AddNotePanel addNotePanel = new AddNotePanel();
    private ClaimLineNotesGrid claimLineNotesGrid = new ClaimLineNotesGrid();

    @FindBy(id = "closeNotesButton")
    private WebElement closeNotesButton;

    @Override
    protected void ensureWeAreAt() {
        waitForAjaxCompletedAndJsRecalculation();
        waitForPageLoaded();
        $(closeNotesButton).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
    }

    public SettlementPage clickCloseWindow() {
        hoverAndClick($(closeNotesButton));
        return Page.at(SettlementPage.class);
    }

    public ClaimLineNotesDialog clickAddNoteButton() {
        addNotePanel
                .clickAddNoteButton();
        return this;
    }

    public ClaimLineNotesDialog clickClaimLine(String name) {
        notesTreePanel
                .getNotesTreeLeafByName(name)
                .click();
        return this;
    }

    public ClaimLineNotesDialog clickCopyNoteTextButton(String noteText) {
        claimLineNotesGrid
                .getClaimLineNotesGridRowByName(noteText)
                .clickCopyNoteTextButton();
        return this;
    }

    public ClaimLineNotesDialog pasteClipboardInNoteWindow() {
        addNotePanel
                .clickNoteText()
                .pasteText();
        return this;
    }

    public ClaimLineNotesDialog enterClaimLineNote(String note) {
        enterNote(note).
                clickAddNoteButton();
        return this;
    }

    public ClaimLineNotesDialog enterVisibleClaimLineNote(String note){
        enterNote(note);
        addNotePanel
                .clickVisibleNoteButton();
        clickAddNoteButton();
        return this;
    }

    public EditClaimLineNoteDialog editClaimLineNote(String noteText){
        claimLineNotesGrid
                .getClaimLineNotesGridRowByName(noteText)
                .clickEditNoteButtonElement();
        return at(EditClaimLineNoteDialog.class);
    }

    public ClaimLineNotesDialog enterNote(String note) {
        addNotePanel
                .clickNoteText()
                .setText(note);
        return this;
    }

    public RemoveClaimLineNoteWarningDialog removeNote(String noteText){
        return claimLineNotesGrid
                .getClaimLineNotesGridRowByName(noteText)
                .removeNote();
    }

    public ClaimLineNotesDialog doAssert(Consumer<Asserts> assertFunc) {
        assertFunc.accept(new Asserts());
        return ClaimLineNotesDialog.this;
    }

    public class Asserts {

        public Asserts assertNoteText(String originalNoteText) {
            String noteText = claimLineNotesGrid.getClaimLineNotesGridRowByName(originalNoteText).getNoteText();
            assertThat(originalNoteText).
                    as("Note text is: " + noteText + "but should be the same as original note text: " + originalNoteText).isEqualTo(noteText);
            return this;
        }

        public Asserts assertNoteVisible(String noteText) {
            boolean noteVisible = claimLineNotesGrid.getClaimLineNotesGridRowByName(noteText).isNoteVisible();
            assertThat(noteVisible).
                    as("Note visible icon is not displayed").isTrue();
            return this;
        }

        public Asserts assertNoteInvisible(String noteText) {
            boolean noteVisible = claimLineNotesGrid.getClaimLineNotesGridRowByName(noteText).isNoteVisible();
            assertThat(noteVisible).
                    as("Note visible icon is not displayed").isFalse();
            return this;
        }

        public Asserts assertClaimLineNotesGridRowsSize(int expectedSize) {
            int actualSize = claimLineNotesGrid.getClaimLineNotesGridRows().size();
            assertThat(expectedSize ).
                    as("ClaimLine grid rows size is: " + actualSize + "but should be :" + expectedSize).isEqualTo(expectedSize);
            return this;
        }

        public Asserts assertNoteIsCopied(String originalNoteText) {
            String textFromClipboard = addNotePanel.getNoteText();
            assertThat(originalNoteText).
                    as("Pasted text is: " + textFromClipboard + "but should be the same as original note text: " + originalNoteText).isEqualTo((textFromClipboard));
            return this;
        }

        public Asserts assertClaimLineNotesIconPresent(String lineName) {
            assertThat(notesTreePanel
                    .getNotesTreeLeafByName(lineName)
                    .claimLineNotesIconElementShouldBe(Condition.visible)
                    .isDisplayed())
                    .as("Claim line notes icon is missing for lineName: " + lineName).isTrue();
            return this;
        }

        public Asserts assertClaimLineNotesIconMissing(String lineName) {
            assertThat(notesTreePanel
                    .getNotesTreeLeafByName(lineName)
                    .claimLineNotesIconElementShouldBe(not(Condition.visible))
                    .isDisplayed())
                    .as("Claim line notes icon exists for ineName: " + lineName).isFalse();
            return this;
        }
    }

    private class ClaimLineNotesGrid{

        private By claimLineNotesGridPath = By.id("claimLineNotesGrid");
        private By claimLineNotesGridRowPath = By.cssSelector("[role=row]");

        private SelenideElement claimLineNotesGridElement;

        ClaimLineNotesGrid() {
            claimLineNotesGridElement = $(claimLineNotesGridPath);
        }

        List<ClaimLineNotesGridRow> getClaimLineNotesGridRows(){
            return claimLineNotesGridElement
                    .findAll(claimLineNotesGridRowPath)
                    .stream()
                    .map(ClaimLineNotesGridRow::new)
                    .collect(Collectors.toList());
        }

        ClaimLineNotesGridRow getClaimLineNotesGridRowByName(String name){

            return getClaimLineNotesGridRows()
                    .stream()
                    .filter(claimLineNotesGridRow -> claimLineNotesGridRow.getNoteText().equals(name))
                    .findFirst()
                    .orElseThrow(NoSuchElementException::new);
        }


        private class ClaimLineNotesGridRow{

            private By copyNoteTextButtonPath = By.cssSelector(".copy-note-text");
            private By editNoteButtonPath = By.cssSelector(".edit-note");
            private By noteTextPath = By.cssSelector("[role=gridcell] div");
            private By removeNoteButtonPath = By.cssSelector(".remove-note");
            private By visibleNotePath = By.cssSelector("[class*=visible-note]");

            private SelenideElement claimLineNotesGridRowElement;
            private SelenideElement copyNoteTextButtonElement;
            private SelenideElement editNoteButtonElement;
            private SelenideElement removeNoteButtonElement;

            String noteText;

            ClaimLineNotesGridRow(SelenideElement element){
                claimLineNotesGridRowElement = element;
                copyNoteTextButtonElement = claimLineNotesGridRowElement.find(copyNoteTextButtonPath);
                removeNoteButtonElement = claimLineNotesGridRowElement.find(removeNoteButtonPath);
                editNoteButtonElement = claimLineNotesGridRowElement.find(editNoteButtonPath);

                Pattern pattern = Pattern.compile("(--.*--)\n\n(.*)", Pattern.MULTILINE | Pattern.DOTALL);
                String text = claimLineNotesGridRowElement.find(noteTextPath).getText();
                Matcher matcher = pattern.matcher(text);
                matcher.find();
                noteText = matcher.group(2);
            }

            boolean isNoteVisible(){

                String attr = claimLineNotesGridRowElement.find(visibleNotePath).attr("class");
                boolean bool = attr.contains("not-visible-note");
                return !bool;
            }

            String getNoteText(){
                return noteText;
            }

            ClaimLineNotesGridRow clickCopyNoteTextButton(){
                hoverAndClick(copyNoteTextButtonElement);
                return this;
            }

            ClaimLineNotesGridRow clickEditNoteButtonElement(){
                hoverAndClick(editNoteButtonElement);
                return this;
            }

            RemoveClaimLineNoteWarningDialog removeNote(){
                hoverAndClick(removeNoteButtonElement);
                return at(RemoveClaimLineNoteWarningDialog.class);
            }
        }
    }

    private class AddNotePanel{

        private By addNotePanelPath = By.id("addNotePanel");
        private By noteTextPath = By.name("note");
        private By addNoteButtonPath = By.id("addNoteButton");
        private By clearNoteButtonPath = By.id("clearNoteButton");
        private By visibleNoteButtonPath = By.id("visibleNoteButton-btnIconEl");

        private SelenideElement addNotePanelElement;
        private SelenideElement noteText;
        private SelenideElement addNoteButton;
        private SelenideElement clearNoteButton;
        private SelenideElement visibleNoteButton;

        AddNotePanel() {
            addNotePanelElement = $(addNotePanelPath);
            noteText = addNotePanelElement.find(noteTextPath);
            addNoteButton = addNotePanelElement.find(addNoteButtonPath);
            clearNoteButton = addNotePanelElement.find(clearNoteButtonPath);
            visibleNoteButton = addNotePanelElement.find(visibleNoteButtonPath);
        }

        AddNotePanel pasteText(){
            noteText.sendKeys(Keys.CONTROL + "v");
            return this;
        }

        AddNotePanel setText(String text){
            noteText.setValue(text);
            return this;
        }
        AddNotePanel clickNoteText(){
            hoverAndClick(noteText);
            return this;
        }
        AddNotePanel clickAddNoteButton(){
            hoverAndClick(addNoteButton);
            return this;
        }

        AddNotePanel clickVisibleNoteButton(){
            hoverAndClick(visibleNoteButton);
            return this;
        }

        AddNotePanel clearNoteButton(){
            hoverAndClick(clearNoteButton);
            return this;
        }

        String getNoteText(){
            return noteText.getValue();
        }
    }


    private class NotesTreePanel {

        private SelenideElement notesTreePanelElement;

        private By notesTreePanelPath = By.cssSelector("#notesTreePanel-body");
        private By notesTreeRootPath = By.cssSelector("table:nth-of-type(1)");
        private By notesTreeLeafPath = By.cssSelector("table .x-grid-tree-node-leaf");

        private SelenideElement notesTreeRoot;
        private List<NotesTreeLeaf> notesTreeLeaves;

        public NotesTreeLeaf getNotesTreeLeafByName(String name){
            return notesTreeLeaves.stream()
                    .filter(notesTreeLeaf -> notesTreeLeaf.getName().equals(name))
                    .findFirst()
                    .orElseThrow(NoSuchElementException::new);
        }

        NotesTreePanel() {
            notesTreePanelElement = $(notesTreePanelPath);
            notesTreeRoot = notesTreePanelElement.find(notesTreeRootPath);
            notesTreeLeaves = notesTreePanelElement
                    .findAll(notesTreeLeafPath)
                    .stream()
                    .map(NotesTreeLeaf::new)
                    .collect(Collectors.toList());
        }

        private class NotesTreeRoot{

            SelenideElement notesTreeRootElement;

            NotesTreeRoot(SelenideElement element){
                this.notesTreeRootElement = element;
            }
        }

        private class NotesTreeLeaf{

            private SelenideElement notesTreeLeafElement;

            private By namePath = By.cssSelector("span");
            private By claimLineNotesIconPath = By.cssSelector("img[src*=notes_claim_line]");

            private String name;

            NotesTreeLeaf(SelenideElement element){
                notesTreeLeafElement = element;
                name = notesTreeLeafElement.find(namePath).getText();
            }

            String getName(){
                return name;
            }

            NotesTreeLeaf click(){
                hoverAndClick(notesTreeLeafElement);
                return this;
            }

            SelenideElement claimLineNotesIconElementShouldBe(Condition condition){
                return notesTreeLeafElement
                        .find(claimLineNotesIconPath)
                        .waitUntil(condition, 6000);
            }
        }
    }
}