package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ex.ElementShould;
import com.scalepoint.automation.Actions;
import com.scalepoint.automation.utils.Wait;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.io.File;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

import static com.codeborne.selenide.Condition.not;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.assertj.core.api.Assertions.*;

public class AttachmentDialog extends BaseDialog implements Actions {

    @FindBy(id = "window-attachment-view_header-title-textEl")
    private WebElement dialogHeader;

    @FindBy(id = ".x-toolbar-footer a[role=button]")
    private WebElement button;


    @Override
    protected BaseDialog ensureWeAreAt() {

        Wait.waitForJavascriptRecalculation();
        $(dialogHeader).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);

        return this;
    }

    public TreepanelAttachmentView getTreepanelAttachmentView(){
        return new TreepanelAttachmentView();
    }

    public ListpanelAttachmentView getListpanelAttachmentView(){
        return new ListpanelAttachmentView();
    }

    public AttachmentDialog uploadAttachment(File file){

        int currentSize = new ListpanelAttachmentView().attachmentsSize();

        enterToHiddenUploadFileField($("input[name=attachmentName]").getWrappedElement(), file.getAbsolutePath());

        Wait.waitForJavascriptRecalculation();
        Wait.waitForAjaxCompleted();

        ElementsCollection elements = new ListpanelAttachmentView().attachments;
        elements.shouldHave(CollectionCondition.size(++currentSize));
        elements.get(currentSize - 1).find(".file-name").waitUntil(Condition.matchesText(file.getName()), TIME_OUT_IN_MILISECONDS);

        return at(AttachmentDialog.class);
    }

    public AttachmentDialog doAssert(Consumer<Asserts> func) {
        func.accept(new Asserts());
        return AttachmentDialog.this;
    }

    public class Asserts {

        public Asserts attachmentExists(String name) {

            assertThatCode(() -> new ListpanelAttachmentView().getAttachmentByName(name))
                    .doesNotThrowAnyException();

            return this;
        }

        public Asserts attachmentNotExists(String name){

            assertThatThrownBy(() -> new ListpanelAttachmentView().getAttachmentByName(name))
                    .isInstanceOf(NoSuchElementException.class);

            return this;
        }

        public Asserts attachmentHasLink(String name, int id){

            String title = new ListpanelAttachmentView()
                    .getAttachmentByName(name)
                    .find("td > .file-name span")
                    .getAttribute("title");

            assertThat(title)
                    .as(String.format("Attachment named: %s doesn't have link to line id: %s", name, id))
                    .containsIgnoringCase(String.valueOf(id));

            return this;
        }

        public Asserts attachmentHasNotLink(String name, int id){

            String title = new ListpanelAttachmentView()
                    .getAttachmentByName(name)
                    .find("td > .file-name span")
                    .getAttribute("title");

            assertThat(title)
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

            SelenideElement element = getLine(lineDescription);

            int tryCounter = 2;
            ElementShould elementShould;
            do {
                try {

                    $(element).click();
                    element.waitUntil(Condition.attribute("aria-selected", "true"), 3000);

                    return AttachmentDialog.this;
                } catch (ElementShould e) {

                    elementShould = e;
                }
            }while (--tryCounter > 0);

                throw elementShould;
        }

        public SelenideElement getLine(String lineDescription){

            return claimLines
                    .stream()
                    .filter(claimLine -> claimLine.find("span").getText().contains(lineDescription))
                    .findFirst()
                    .orElseThrow(NoSuchElementException::new);
        }
    }

    public class ListpanelAttachmentView{

        private ElementsCollection attachments;

        ListpanelAttachmentView(){

            attachments = $$(".thumb-wrap");
        }

        public AttachmentDialog linkAttachment(String name, String lineDescription){

            SelenideElement attachment = getAttachmentByName(name);

            SelenideElement line = new TreepanelAttachmentView().getLine(lineDescription);

            dragAndDrop(attachment.find(".thumb-view img"), line.find("td"));

            String id = line.find(".x-tree-node-text ").getText().substring(0,1);

            attachment.find("tr > td >.file-name span").waitUntil(Condition.text(id), TIME_OUT_IN_MILISECONDS);

            return at(AttachmentDialog.class);
        }


        public AttachmentDialog deleteAttachment(String name){

            int currentSize = new ListpanelAttachmentView().attachmentsSize();

            new ListpanelAttachmentView().attachments
                    .stream()
                    .filter(e -> e.find("div > .file-name").getText().equals(name))
                    .findFirst()
                    .orElseThrow(NoSuchElementException::new)
                    .find("img[id^=\"img_delete\"]")
                    .waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS)
                    .click();

            $$("div[role=alertdialog] a[role=button][aria-hidden=false]").get(0).click();

            ElementsCollection elements = new ListpanelAttachmentView().attachments;
            elements.shouldHave(CollectionCondition.size(--currentSize));

            return AttachmentDialog.this;
        }

        public AttachmentDialog unlinkAttachment(String name){

            SelenideElement element = attachments
                    .stream()
                    .filter(e -> e.find(".file-name").getText().equals(name))
                    .findFirst()
                    .orElseThrow(NoSuchElementException::new)
                    .find("img[id^=\"img_link\"]")
                    .waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);

            element.click();

            element.waitUntil(not(Condition.exist), TIME_OUT_IN_MILISECONDS);

            return AttachmentDialog.this;
        }

        private SelenideElement getAttachmentByName(String name){

            return attachments
                    .stream()
                    .filter(e -> e.find("div > .file-name").getText().equals(name))
                    .findFirst()
                    .orElseThrow(NoSuchElementException::new);
        }

        public int attachmentsSize(){

            return attachments.size();
        }
    }

}
