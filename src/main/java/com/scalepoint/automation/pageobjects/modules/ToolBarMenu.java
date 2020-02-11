package com.scalepoint.automation.pageobjects.modules;

import com.scalepoint.automation.pageobjects.dialogs.AttachmentDialog;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.UpdateCategoriesDialog;
import com.scalepoint.automation.pageobjects.pages.ClaimLineNotesPage;
import com.scalepoint.automation.pageobjects.pages.TextSearchPage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;

import static com.scalepoint.automation.pageobjects.pages.Page.at;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompleted;

public class ToolBarMenu extends Module {

    @FindBy(xpath = "//span[contains(@style, 'keepSelectionIcon')]/ancestor::a")
    private Button keepSelection;

    @FindBy(xpath = "//span[contains(@style, 'selectAllIcon')]/ancestor::a")
    private Button selectAll;

    @FindBy(xpath = "//span[contains(@style, 'markIcon')]/ancestor::a")
    private Button markAsReviewed;

    @FindBy(xpath = "//span[contains(@style, 'includeIcon')]/ancestor::a")
    private Button excludeFromClaim;

    @FindBy(xpath = "//span[contains(@style, 'deleteIcon')]/ancestor::a")
    private Button removeSelected;

    @FindBy(xpath = "//span[contains(@style, 'changeVoucherIcon')]/ancestor::a")
    private Button addChangeVoucher;

    @FindBy(xpath = "//span[contains(@style, 'removeVoucherIcon')]/ancestor::a")
    private Button removeVoucher;

    @FindBy(xpath = "//span[contains(@style, 'sendToRepairIcon')]/ancestor::a")
    private Button sentToRepairValuation;

    @FindBy(xpath = "//span[contains(@style, 'rejectIcon')]/ancestor::a")
    private Button reject;

    @FindBy(xpath = "//span[contains(@style, 'groupIcon')]/ancestor::a")
    private Button createGroupOfSelectedLines;

    @FindBy(xpath = "//span[contains(@style, 'ungroupIcon')]/ancestor::a")
    private Button ungroup;

    @FindBy(xpath = "//span[contains(@style, 'productMatchIcon')]/ancestor::a")
    private Button productMatch;

    @FindBy(xpath = "//span[contains(@style, 'attachmentsIcon')]/ancestor::a")
    private Button attachments;

    @FindBy(xpath = "//span[contains(@style, 'claimLineNotesIcon')]/ancestor::a")
    private Button claimLineNote;

    @FindBy(xpath = "//span[contains(@style, 'sendToRepairIcon')]/ancestor::a")
    private WebElement sendToRnVButton;

    @FindBy(xpath = "//span[contains(@style, 'updateCategory')]/ancestor::a")
    private Button updateCategory;


    public TextSearchPage toProductMatchPage() {
        productMatch.click();
        return at(TextSearchPage.class);
    }

    public ToolBarMenu removeSelected() {
        removeSelected.click();
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
        selectAll.click();
        return this;
    }

    public ToolBarMenu openClaimLineNotes() {
        claimLineNote.click();
        return this;
    }

    public ToolBarMenu openUpdateCategoriesDialog() {
        updateCategory.click();
        return this;
    }

    public AttachmentDialog openAttachmentsDialog(){
        attachments.click();
        return BaseDialog.at(AttachmentDialog.class);
    }

    public ClaimLineNotesPage toClaimLineNotesPage() {
        waitForAjaxCompleted();
        return at(ClaimLineNotesPage.class);
    }

    public UpdateCategoriesDialog toUpdateCategoriesDialog() {
        waitForAjaxCompleted();
        return BaseDialog.at(UpdateCategoriesDialog.class);
    }
}
