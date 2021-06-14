package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ex.ElementShould;
import com.scalepoint.automation.Actions;
import com.scalepoint.automation.utils.Wait;
import lombok.Getter;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.io.File;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;
import static org.assertj.core.api.Assertions.*;

public class AttachmentDialog extends BaseDialog implements Actions {

    @FindBy(id = "window-attachment-view_header-title-textEl")
    private WebElement dialogHeader;

    @FindBy(id = ".x-toolbar-footer a[role=button]")
    private WebElement button;


    @Override
    protected void ensureWeAreAt() {
        waitForAjaxCompletedAndJsRecalculation();
        $(dialogHeader).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
    }

    public TreepanelAttachmentView getTreepanelAttachmentView(){
        return new TreepanelAttachmentView();
    }

    public ListpanelAttachmentView getListpanelAttachmentView(){
        return new ListpanelAttachmentView();
    }

    public AttachmentDialog uploadAttachment(File file){

        ListpanelAttachmentView listpanelAttachmentView = new ListpanelAttachmentView();

        int currentSize = listpanelAttachmentView.attachmentsSize();

        $("input[name=attachmentName]").uploadFile(file);

        Wait.waitForJavascriptRecalculation();
        Wait.waitForAjaxCompleted();

        listpanelAttachmentView.waitForNewAttachment(++currentSize, file.getName());

        return at(AttachmentDialog.class);
    }

    public AttachmentDialog linkAttachment(String name, String lineDescription){

        ListpanelAttachmentView.Attachment attachment = new ListpanelAttachmentView().getAttachmentByName(name);

        TreepanelAttachmentView.Line line = new TreepanelAttachmentView().getLine(lineDescription);

        dragAndDrop(attachment.getDragArea(), line.getDropArea());

        return at(AttachmentDialog.class);
    }

    public AttachmentDialog doAssert(Consumer<Asserts> func) {

        func.accept(new Asserts());

        return AttachmentDialog.this;
    }

    public class Asserts {

        ListpanelAttachmentView listpanelAttachmentView = new ListpanelAttachmentView();

        public Asserts attachmentExists(String name) {

            assertThatCode(() -> listpanelAttachmentView.getAttachmentByName(name))
                    .as(String.format("Attachement named %s doesn't exist", name))
                    .doesNotThrowAnyException();

            return this;
        }

        public Asserts attachmentNotExists(String name){

            assertThatThrownBy(() -> listpanelAttachmentView.getAttachmentByName(name))
                    .as("Attachment named %s exists", name)
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

        public Asserts attachmentHasNotLink(String name, int id){

            String links = listpanelAttachmentView.getAttachmentByName(name).getLinks();

            assertThat(links)
                    .as(String.format("Attachment named: %s doesn't have link to line id: %s", name, id))
                    .doesNotContain(String.valueOf(id));

            return this;
        }
    }

    public class TreepanelAttachmentView{

        private SelenideElement node;
        private ElementsCollection claimLines;

        TreepanelAttachmentView(){

            node = $(".x-grid-tree-node-expanded");
            claimLines = $$(".x-grid-tree-node-leaf");
        }

        public TreepanelAttachmentView selectNode(){

            node.click();

            return this;
        }

        public AttachmentDialog selectLine(String lineDescription){

            int tryCounter = 2;
            ElementShould elementShould;
            do {
                try {

                    getLine(lineDescription)
                            .select();

                    return AttachmentDialog.this;
                } catch (ElementShould e) {

                    elementShould = e;
                }
            }while (--tryCounter > 0);

            throw elementShould;
        }

        public Line getLine(String lineDescription){

            SelenideElement element = claimLines
                    .stream()
                    .filter(claimLine -> claimLine.find("span").getText().contains(lineDescription))
                    .findFirst()
                    .orElseThrow(NoSuchElementException::new);

            return new Line(element);
        }

        class Line{

            private SelenideElement element;
            private final int TIMEOUT = 3000;
            @Getter
            private String id;
            @Getter
            private SelenideElement dropArea;

            private Line(SelenideElement element){

                this.element = element;
                this.id = element.find(".x-tree-node-text").getText().substring(0,1);
                dropArea = element.find("td");
            }

            public void select(){

                element.click();
                element.waitUntil(Condition.attribute("aria-selected", "true"), TIMEOUT);
            }
        }
    }

    public class ListpanelAttachmentView{

        private static final String ATTACHMENT_NAME = "div > .file-name";

        private ElementsCollection attachments;

        ListpanelAttachmentView(){

            attachments = $$(".thumb-wrap");
        }

        public AttachmentDialog deleteAttachment(String name){

            int currentSize = attachmentsSize();

            getAttachmentByName(name)
                    .delete();

            $$("div[role=alertdialog] a[role=button][aria-hidden=false]").get(0).click();

            ElementsCollection elements = attachments;
            elements.shouldHave(CollectionCondition.size(--currentSize));

            return at(AttachmentDialog.class);
        }

        public AttachmentDialog unlinkAttachment(String name){

            int currentSize = attachmentsSize();

            getAttachmentByName(name)
                    .unlink();

            ElementsCollection elements = attachments;
            elements.shouldHave(CollectionCondition.size(--currentSize));

            return at(AttachmentDialog.class);
        }

        private Attachment getAttachmentByName(String name){

            SelenideElement element = attachments
                    .stream()
                    .filter(e -> e.find(ATTACHMENT_NAME).getText().equals(name))
                    .findFirst()
                    .orElseThrow(NoSuchElementException::new);

            return new Attachment(element);
        }

        public void waitForNewAttachment(int newSize, String name){

            ElementsCollection elements = new ListpanelAttachmentView().attachments;
            elements.shouldHave(CollectionCondition.size(newSize));
            elements.get(newSize - 1)
                    .find(ATTACHMENT_NAME)
                    .waitUntil(Condition.matchesText(name), TIME_OUT_IN_MILISECONDS);
        }

        public int attachmentsSize(){

            return attachments.size();
        }

        class Attachment{

            private SelenideElement element;
            @Getter
            private SelenideElement dragArea;
            @Getter
            private String links;

            private Attachment(SelenideElement element){

                this.element = element;
                dragArea = element.find(".thumb-view img");
                links = element.find("td > .file-name span").getAttribute("title");
            }

            public ListpanelAttachmentView delete(){

                element.find("img[id^=\"img_delete\"]")
                        .waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS)
                        .click();

                return new ListpanelAttachmentView();
            }

            public ListpanelAttachmentView unlink(){

                element.find("img[id^=\"img_link\"]")
                        .waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS)
                        .click();

                return new ListpanelAttachmentView();
            }

            public Attachment waitForLink(String id){

                element.find("tr > td >.file-name span").waitUntil(Condition.text(id), TIME_OUT_IN_MILISECONDS);

                return this;
            }
        }
    }

}
