package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.Actions;
import com.scalepoint.automation.utils.Wait;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.NoSuchElementException;

import static com.codeborne.selenide.Condition.not;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

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

    public AttachmentDialog uploadAttachment(String path){
        int currentSize = new ListpanelAttachmentView().attachments.size();
        enterToHiddenUploadFileField($("input[name=attachmentName]").getWrappedElement(), path);
        Wait.waitForJavascriptRecalculation();
        Wait.waitForAjaxCompleted();
        new ListpanelAttachmentView().attachments.shouldHave(CollectionCondition.size(++currentSize));
        return at(AttachmentDialog.class);
    }

//    public AttachmentDialog doAssert(Consumer<Asserts> func) {
//        func.accept(new Asserts());
//        return AttachmentDialog.this;
//    }

//    public class Asserts {
//
//        public Asserts assertDiscretionaryReasonValuePresent(String expectedValue) {
//            List<String> options = discretionaryReason.getComboBoxOptions();
//            Assert.assertTrue(options.stream().anyMatch(i -> i.contains(expectedValue)));
//            return this;
//        }
//    }

    public class TreepanelAttachmentView{

        SelenideElement node;
        ElementsCollection claimLines;

        TreepanelAttachmentView(){

            node = $(".x-grid-tree-node-expanded");
            claimLines = $$(".x-grid-tree-node-leaf");
        }

        public TreepanelAttachmentView selectNode(){
            node.click();
            return this;
        }

        public AttachmentDialog selectLine(String lineDescription){

            getLine(lineDescription)
                    .click();

            return AttachmentDialog.this;
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

        ElementsCollection attachments;

        ListpanelAttachmentView(){

            attachments = $$(".thumb-wrap");
        }

        public AttachmentDialog linkAttachment(String name, String lineDescription){

            SelenideElement attachment = attachments
                    .stream()
                    .filter(e -> e.find(".file-name").getText().equals(name))
                    .findFirst()
                    .orElseThrow(NoSuchElementException::new);

            SelenideElement line = new TreepanelAttachmentView().getLine(lineDescription);

            dragAndDrop(attachment.find(".thumb-view img"), line.find("td"));

            String id = line.find(".x-tree-node-text ").getText().substring(0,1);

            attachment.find("tr > td >.file-name span").waitUntil(Condition.text(id), TIME_OUT_IN_MILISECONDS);

            return at(AttachmentDialog.class);
        }


        public AttachmentDialog deleteAttachment(String name){
            attachments
                    .stream()
                    .filter(e -> e.find(".file-name").getText().equals(name))
                    .findFirst()
                    .orElseThrow(NoSuchElementException::new)
                    .find("img[id^=\"img_delete\"]")
                    .waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS)
                    .click();

            $$("div[role=alertdialog] a[role=button][aria-hidden=false]").get(0).click();

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
    }

}
