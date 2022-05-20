package com.scalepoint.automation.pageobjects.modules;

import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.dialogs.AttachmentDialog;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.ClaimLineNotesDialog;
import com.scalepoint.automation.pageobjects.dialogs.UpdateCategoriesDialog;
import com.scalepoint.automation.pageobjects.pages.TextSearchPage;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.pageobjects.pages.Page.at;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompleted;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

public class ToolBarMenu extends Module {

    @FindBy(xpath = "//span[contains(@style, 'sendToRepairIcon')]/ancestor::a")
    private SelenideElement sendToRnVButton;

    private Button getKeepSelection(){

        return new Button($(By.xpath("//span[contains(@style, 'keepSelectionIcon')]/ancestor::a")));
    }

    private Button getSelectAll(){

        return new Button($(By.xpath("//span[contains(@style, 'selectAllIcon')]/ancestor::a")));
    }

    private Button getMarkAsReviewed(){

        return new Button($(By.xpath("//span[contains(@style, 'markIcon')]/ancestor::a")));
    }

    private Button getExcludeFromClaim(){

        return new Button($(By.xpath("//span[contains(@style, 'includeIcon')]/ancestor::a")));
    }

    private Button getRemoveSelected(){

        return new Button($(By.xpath("//span[contains(@style, 'deleteIcon')]/ancestor::a")));
    }

    private Button getAddChangeVoucher(){

        return new Button($(By.xpath("//span[contains(@style, 'changeVoucherIcon')]/ancestor::a")));
    }

    private Button getRemoveVoucher(){

        return new Button($(By.xpath("//span[contains(@style, 'removeVoucherIcon')]/ancestor::a")));
    }

    private Button getSentToRepairValuation(){

        return new Button($(By.xpath("//span[contains(@style, 'sendToRepairIcon')]/ancestor::a")));
    }

    private Button getReject(){

        return new Button($(By.xpath("//span[contains(@style, 'rejectIcon')]/ancestor::a")));
    }

    private Button getCreateGroupOfSelectedLines(){

        return new Button($(By.xpath("//span[contains(@style, 'groupIcon')]/ancestor::a")));
    }

    private Button getUngroup(){

        return new Button($(By.xpath("//span[contains(@style, 'ungroupIcon')]/ancestor::a")));
    }

    private Button getProductMatch(){

        return new Button($(By.xpath("//span[contains(@style, 'productMatchIcon')]/ancestor::a")));
    }

    private Button getAttachments(){

        return new Button($(By.xpath("//span[contains(@style, 'attachmentsIcon')]/ancestor::a")));
    }

    private Button getClaimLineNote(){

        return new Button($(By.xpath("//span[contains(@style, 'claimLineNotesIcon')]/ancestor::a")));
    }

    private Button getUpdateCategory(){

        return new Button($(By.xpath("//span[contains(@style, 'updateCategory')]/ancestor::a")));
    }

    public TextSearchPage toProductMatchPage() {

        getProductMatch().click();
        return at(TextSearchPage.class);
    }

    public ToolBarMenu removeSelected() {

        getRemoveSelected().click();
        if (isAlertPresent()) {

            acceptAlert();
        }
        return this;
    }

    public ToolBarMenu sendToRepairAndValuation() {

        sendToRnVButton.click();
        return this;
    }

    public ToolBarMenu selectAll() {

        getSelectAll().click();
        return this;
    }

    public ToolBarMenu openClaimLineNotes() {

        getClaimLineNote().click();
        return this;
    }

    public ToolBarMenu openUpdateCategoriesDialog() {

        getUpdateCategory().click();
        return this;
    }

    public AttachmentDialog openAttachmentsDialog(){

        getAttachments().click();
        return BaseDialog.at(AttachmentDialog.class);
    }

    public ClaimLineNotesDialog toClaimLineNotesPage() {

        waitForAjaxCompleted();
        return BaseDialog.at(ClaimLineNotesDialog.class);
    }

    public UpdateCategoriesDialog toUpdateCategoriesDialog() {

        waitForAjaxCompletedAndJsRecalculation();
        return BaseDialog.at(UpdateCategoriesDialog.class);
    }
}
