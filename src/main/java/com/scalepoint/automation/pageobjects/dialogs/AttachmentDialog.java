package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

import java.io.File;
import java.time.Duration;
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

    private TreepanelAttachmentView treepanelAttachmentView = new TreepanelAttachmentView();
    private ListpanelAttachmentView listpanelAttachmentView = new ListpanelAttachmentView();

    @Override
    protected void ensureWeAreAt() {

        waitForAjaxCompletedAndJsRecalculation();
        $("#window-attachment-view_header-title-textEl").should(Condition.visible);
    }

    public AttachmentDialog selectLine(String lineDescription){

        return treepanelAttachmentView.selectLine(lineDescription);
    }

    public AttachmentDialog deleteAttachment(String name){

        return listpanelAttachmentView.deleteAttachment(name);
    }

    public AttachmentDialog unlinkAttachment(String name){

        return listpanelAttachmentView.unlinkAttachment(name);
    }

    public AttachmentDialog uploadAttachment(File file){

        int currentSize = listpanelAttachmentView.attachmentsSize();

        $("input[name=attachmentName]").uploadFile(file);

        listpanelAttachmentView.waitForAttachmentSize(++currentSize);

        return at(AttachmentDialog.class);
    }

    public AttachmentDialog linkAttachment(String name, String lineDescription){

        ListpanelAttachmentView.Attachment attachment = listpanelAttachmentView.getAttachmentByName(name);
        TreepanelAttachmentView.Line line = treepanelAttachmentView.getLine(lineDescription);

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

            assertThatCode(() -> listpanelAttachmentView.getAttachmentByName(name))
                    .as(String.format("Attachment named %s doesn't exist", name))
                    .doesNotThrowAnyException();

            return this;
        }

        public Asserts attachmentNotExists(String name){

            assertThatThrownBy(() -> listpanelAttachmentView.getAttachmentByName(name))
                    .as(String.format("Attachment named %s exists", name))
                    .isInstanceOf(NoSuchElementException.class);

            return this;
        }

        public Asserts attachmentHasLink(String name, int id){

            String links = listpanelAttachmentView.getAttachmentByName(name).getLinks();

            assertThat(links)
                    .as(String.format("Attachment named: %s doesn't have link to line id: %s", name, id))
                    .containsIgnoringCase(String.valueOf(id));

            return this;
        }
    }

    class TreepanelAttachmentView{

        private SelenideElement node;
        private List<Line> claimLines;

        TreepanelAttachmentView(){

            node = $(".x-grid-tree-node-expanded");
            claimLines = $$(".x-grid-tree-node-leaf").stream().map(Line::new).collect(Collectors.toList());
        }

        public TreepanelAttachmentView selectNode(){

            node.click();
            return this;
        }

        public AttachmentDialog selectLine(String lineDescription){

            getLine(lineDescription)
                    .select();

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

            private final int TIMEOUT = 3;

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
                element.should(Condition.attribute("aria-selected", "true"), Duration.ofSeconds(TIMEOUT));
            }
        }
    }

    class ListpanelAttachmentView{

        private ElementsCollection attachmentCollection;

        private List<Attachment> attachments;

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
}
