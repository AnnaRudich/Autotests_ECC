package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.utils.JavascriptHelper;
import com.scalepoint.automation.utils.Wait;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;
import static com.scalepoint.automation.utils.Wait.waitForLoaded;
import static org.assertj.core.api.Assertions.*;

public class AttachmentDialog extends BaseDialog {

    @Override
    protected void ensureWeAreAt() {

        waitForAjaxCompletedAndJsRecalculation();
        $("#window-attachment-view_header-title-textEl").should(Condition.visible);
    }

    public AttachmentDialog selectNode(){

        return new TreepanelAttachmentView().selectNode();
    }

    public AttachmentDialog selectLine(String lineDescription){

        return new TreepanelAttachmentView().selectLine(lineDescription);
    }

    public AttachmentDialog deleteAttachment(String name){

        return new ListpanelAttachmentView().deleteAttachment(name);
    }

    public AttachmentDialog unlinkAttachment(String name){

        return new ListpanelAttachmentView().unlinkAttachment(name);
    }

    public AttachmentDialog uploadAttachment(File file){

        int currentSize = new ListpanelAttachmentView().attachmentsSize();

        $("input[name=attachmentName]").uploadFile(file);
        Wait.waitForJavascriptRecalculation();

        new ListpanelAttachmentView().waitForAttachmentSize(++currentSize);

        return at(AttachmentDialog.class);
    }

    public AttachmentDialog deleteDownloadAttachment(String name){

        return new DownloadAttachmentsPanel().deleteAttachment(name);
    }

    public AttachmentDialog download(){

        $("[data-componentid=button-download-attachments]").click();

        return at(AttachmentDialog.class);
    }

    public AttachmentDialog clearAttachments(){

        $("#button-clear-attachments").click();

        return at(AttachmentDialog.class);
    }

    public AttachmentDialog addToDownloading(){

        $("#button-add-all-attachments").click();
        return at(AttachmentDialog.class);
    }

    public AttachmentDialog linkAttachment(String name, String lineDescription){

        ListpanelAttachmentView.Attachment attachment = new ListpanelAttachmentView().getAttachmentByName(name);
        TreepanelAttachmentView.Line line = new TreepanelAttachmentView().getLine(lineDescription);

        dragAndDrop(attachment.getDragArea(), line.getDropArea());
        waitForLoaded();

        return at(AttachmentDialog.class);
    }

    public AttachmentDialog doAssert(Consumer<Asserts> func) {

        func.accept(new Asserts());

        return AttachmentDialog.this;
    }

    public class Asserts {

        public Asserts attachmentExists(String name) {

            assertThatCode(() -> new ListpanelAttachmentView().getAttachmentByName(name))
                    .as(String.format("Attachment named %s doesn't exist", name))
                    .doesNotThrowAnyException();

            return this;
        }

        public Asserts attachmentNotExists(String name){

            assertThatThrownBy(() -> new ListpanelAttachmentView().getAttachmentByName(name))
                    .as(String.format("Attachment named %s exists", name))
                    .isInstanceOf(NoSuchElementException.class);

            return this;
        }

        public Asserts attachmentHasLink(String name, int id){

            String links = new ListpanelAttachmentView().getAttachmentByName(name).getLinks();

            assertThat(links)
                    .as(String.format("Attachment named: %s doesn't have link to line id: %s", name, id))
                    .containsIgnoringCase(String.valueOf(id));

            return this;
        }

        public Asserts attachmentExistsOnDownloadList(String name) {

            assertThatCode(() -> new DownloadAttachmentsPanel().getAttachmentByName(name))
                    .as(String.format("Attachment named %s doesn't exist", name))
                    .doesNotThrowAnyException();

            return this;
        }

        public Asserts attachmentNotExistsOnDownloadList(String name){

            assertThatThrownBy(() -> new DownloadAttachmentsPanel().getAttachmentByName(name))
                    .as(String.format("Attachment named %s exists", name))
                    .isInstanceOf(NoSuchElementException.class);

            return this;
        }

        public Asserts attachmentDownloaded(String name){

            try {

                assertThat(JavascriptHelper.getZipEntries(JavascriptHelper.getFileContent(JavascriptHelper.getDownloadedFiles().get(0)))
                        .stream()
                        .anyMatch(zipEntry -> zipEntry.getName().equals(name)))
                        .as(String.format("Attachment %s not found", name))
                        .isTrue();

            } catch (IOException e) {

                throw new RuntimeException(e);
            }

            return this;
        }
    }

    class TreepanelAttachmentView{

        private SelenideElement node;
        private List<Line> claimLines;

        TreepanelAttachmentView(){

            ElementsCollection items = $$("#treepanel-attachment-view-body table");
            node = items.first();
            claimLines = items.stream().skip(1).map(Line::new).collect(Collectors.toList());
        }

        public AttachmentDialog selectNode(){

            node.click();

            waitForAjaxCompletedAndJsRecalculation();

            return at(AttachmentDialog.class);
        }

        public AttachmentDialog selectLine(String lineDescription){

            getLine(lineDescription)
                    .select();

            waitForAjaxCompletedAndJsRecalculation();

            return at(AttachmentDialog.class);
        }

        public Line getLine(String lineDescription){

            return claimLines
                    .stream()
                    .filter(claimLine -> claimLine.getLineDescription().contains(lineDescription))
                    .findFirst()
                    .orElseThrow(NoSuchElementException::new);
        }

        class Line{

            private SelenideElement element;

            @Getter
            private String id;
            @Getter
            private SelenideElement dropArea;
            @Getter
            private String lineDescription;

            private Line(SelenideElement element){

                this.element = element;
                id = element.find(".x-tree-node-text").getText().substring(0,1);
                dropArea = element.find("td");
                lineDescription = element.find("span").getText();
            }

            public void select(){

                element.click();
                element.should(Condition.cssClass("x-grid-item-selected"));
            }
        }
    }

    class ListpanelAttachmentView{

        private ElementsCollection attachmentCollection;

        private List<Attachment> attachments = new LinkedList<>();

        ListpanelAttachmentView(){

            attachmentCollection = $$(".thumb-wrap");

            attachments = attachmentCollection.stream()
                    .map(Attachment::new)
                    .collect(Collectors.toList());
        }

        public AttachmentDialog deleteAttachment(String name){

            getAttachmentByName(name)
                    .delete();

            return at(AttachmentDialog.class);
        }

        public AttachmentDialog unlinkAttachment(String name){

            getAttachmentByName(name)
                    .unlink();

            return at(AttachmentDialog.class);
        }

        private Attachment getAttachmentByName(String name){

            return attachments
                    .stream()
                    .filter(e -> e.getName().equals(name))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException(name));
        }

        private void waitForAttachmentSize(int newSize){

            attachmentCollection.shouldHave(size(newSize));
        }

        private int attachmentsSize(){

            return attachments.size();
        }

        class Attachment{

            private SelenideElement element;

            @Getter
            private String links;
            @Getter
            private SelenideElement dragArea;
            @Getter
            private String name;

            Attachment(SelenideElement element){

                this.element = element;
                dragArea = element.find(".thumb-view img");
                links = element.find("td > .file-name span").attr("title");
                name = element.find("div > .file-name").getText();
            }

            public ListpanelAttachmentView delete(){

                element.find("img[id^=\"img_delete\"]")
                        .should(Condition.visible)
                        .click();

                $$("div[role=alertdialog] a[role=button][aria-hidden=false]").get(0).click();

                element.shouldNot(Condition.exist);

                return new ListpanelAttachmentView();
            }

            public ListpanelAttachmentView unlink(){

                element.find("img[id^=\"img_link\"]")
                        .should(Condition.visible)
                        .click();

                element.shouldNot(Condition.exist);

                return new ListpanelAttachmentView();
            }
        }
    }

    class DownloadAttachmentsPanel{

        private List<DownloadAttachment> attachments;

        DownloadAttachmentsPanel(){

            attachments = $$("#form-panel-download-attachments table [role=row]").stream()
                    .map(DownloadAttachment::new)
                    .collect(Collectors.toList());
        }

        public AttachmentDialog deleteAttachment(String name){

            getAttachmentByName(name)
                    .delete();

            return at(AttachmentDialog.class);
        }

        private DownloadAttachmentsPanel.DownloadAttachment getAttachmentByName(String name){

            return attachments
                    .stream()
                    .filter(e -> e.getName().equals(name))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException(name));
        }

        class DownloadAttachment{

            private SelenideElement element;

            @Getter
            private String links;
            @Getter
            private String name;

            SelenideElement deleteColumn;

            DownloadAttachment(SelenideElement element){

                this.element = element;
                String[] qtip = element.find("[data-qtip]").attr("data-qtip").split("<br>ID:");
                name = qtip[0];
                links = qtip[1].trim();
            }

            public ListpanelAttachmentView delete(){

                element.find(" [data-columnid=deleteColumn]")
                        .should(Condition.visible)
                        .click();

                element.shouldNot(Condition.exist);

                return new ListpanelAttachmentView();
            }
        }
    }
}
