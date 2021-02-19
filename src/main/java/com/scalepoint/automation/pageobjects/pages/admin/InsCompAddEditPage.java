package com.scalepoint.automation.pageobjects.pages.admin;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.GdprConfirmationDialog;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.annotations.page.RequiredParameters;
import com.scalepoint.automation.utils.data.entity.input.InsuranceCompany;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForPageLoaded;

@EccPage
@RequiredParameters("icrfnbr=%s")
public class InsCompAddEditPage extends AdminBasePage {

    @FindBy(name = "id")
    private WebElement companyIDField;

    @FindBy(name = "CompanyCode")
    private WebElement companyCodeField;

    @FindBy(name = "icname")
    private WebElement companyNameField;

    @FindBy(name = "tenant")
    private WebElement tenant;

    @FindBy(name = "omTenantAlias")
    private WebElement omTenantAlias;

    @FindBy(name = "unifiedCompanyCode")
    private WebElement unifiedCompanyCode;

    @FindBy(name = "icaddr1")
    private WebElement addressField;

    @FindBy(name = "iczipc")
    private WebElement zipCodeField;

    @FindBy(name = "iccity")
    private WebElement cityField;

    @FindBy(name = "iccommail")
    private WebElement icCommonMailField;

    @FindBy(tagName = "option")
    private List<WebElement> options;

    @FindBy(xpath = "//input[contains(@id,'icContactNo')]")
    private WebElement companyContactNumberField;

    @FindBy(xpath = "//textarea[contains(@id,'icOfficeHour')]")
    private WebElement companyOfficeHoursField;

    @FindBy(id = "btnOk")
    private WebElement saveButton;

    @FindBy(name = "auditSendTimeFrom")
    private WebElement sendTimeFromField;

    @FindBy(name = "auditSendTimeTo")
    private WebElement sendTimeToField;

    @FindBy(xpath = "//input[contains(@id, 'localizedName')]")
    private WebElement localizedNameInput;

    private String byFTPath = "select[name='icftnbr']";

    private String byGUIPath = "select[name='icgtnbr']";

    private String byICCulturePath = "select[name='icCulture']";

    private String byAuditPath = "select[name='auditEnabled']";

    @Override
    protected void ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForPageLoaded();
        $(companyIDField).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/Admin/insurance_company_edit.jsp";
    }

    public void selectSaveOption(boolean gdpr) {
        $("#btnOk").click();
        if(gdpr) {
            BaseDialog
                    .at(GdprConfirmationDialog.class)
                    .confirm();
        }
    }

    public InsCompaniesPage createCompany(InsuranceCompany insuranceCompany) {
        companyIDField.sendKeys(insuranceCompany.getIcID());
        tenant.sendKeys(insuranceCompany.getIcCode());
        omTenantAlias.sendKeys(insuranceCompany.getIcCode());
        companyCodeField.sendKeys(insuranceCompany.getIcCode().toUpperCase());
        unifiedCompanyCode.sendKeys(insuranceCompany.getIcCode().toUpperCase());

        companyNameField.sendKeys(insuranceCompany.getIcName());
        addressField.sendKeys(insuranceCompany.getAddress());
        zipCodeField.sendKeys(insuranceCompany.getZipCode());
        cityField.sendKeys(insuranceCompany.getIcCity());
        icCommonMailField.sendKeys(insuranceCompany.getCompanyCommonMail());
        localizedNameInput.sendKeys(insuranceCompany.getIcName());

        if (StringUtils.isEmpty(sendTimeFromField.getAttribute("value"))) {
            sendTimeFromField.sendKeys(insuranceCompany.getSendTimeFrom());
        }

        if (StringUtils.isEmpty(sendTimeFromField.getAttribute("value"))) {
            sendTimeToField.sendKeys(insuranceCompany.getSendTimeTo());
        }

        $(byFTPath).selectOption(insuranceCompany.getFunctionTemplate());
        $(byGUIPath).selectOption(insuranceCompany.getGuiTemplate());
        $(byICCulturePath).selectOption(insuranceCompany.getIcCulture());

        companyContactNumberField.sendKeys(insuranceCompany.getContactNumber());
        companyOfficeHoursField.sendKeys(insuranceCompany.getOfficeHours());
        selectSaveOption(true);
        return at(InsCompaniesPage.class);
    }

    public InsCompaniesPage updateNameAndSave(InsuranceCompany insuranceCompany) {
        companyNameField.clear();
        companyNameField.sendKeys(insuranceCompany.getIcName());
        selectSaveOption(false);
        return at(InsCompaniesPage.class);
    }

    public void enableAuditOptionAndSave() {
        $(byAuditPath).selectOption("Enabled");
        selectSaveOption(true);
    }

    public InsCompAddEditPage setCommunicationDesignerSection(CommunicationDesigner communicationDesigner){

        $("#chkUseOutputManagement").setSelected(communicationDesigner.useOutputManagement);
        $("[name=OM_SelfServiceCustomerWelcome]").setSelected(communicationDesigner.omSelfServiceCustomerWelcome);
        $("[name=OM_CustomerWelcome]").setSelected(communicationDesigner.omCustomerWelcome);
        $("[name=OM_CustomerWelcome_uCommerceEmployee]").setSelected(communicationDesigner.omCustomerWelcomeUcommerceEmployee);
        $("[name=OM_CustomerWelcomeWithOutstanding]").setSelected(communicationDesigner.omCustomerWelcomeWithOutstanding);
        $("[name=OM_CustomerWelcomeRejectionMail]").setSelected(communicationDesigner.omCustomerWelcomeRejectionMail);
        $("[name=OM_NotificationToClaimant]").setSelected(communicationDesigner.omNotificationToClaimant);
        $("[name=OM_ReminderMail]").setSelected(communicationDesigner.omReminderMail);
        $("[name=OM_BlockedAccount]").setSelected(communicationDesigner.omBlockedAccount);
        $("[name=OM_ItemizationReminderLossItems]").setSelected(communicationDesigner.omItemizationReminderLossItems);
        $("[name=OM_ItemizationSubmitLossItems]").setSelected(communicationDesigner.omItemizationSubmitLossItems);
        $("[name=OM_ItemizationSaveLossItems]").setSelected(communicationDesigner.omItemizationSaveLossItems);
//        $("[name=OM_SettlementNotification]").setSelected(communicationDesigner.omSettlementNotification);
//        $("[name=OM_SettlementNotificationClosedExternal]").setSelected(communicationDesigner.omSettlementNotificationClosedExternal);
//        $("[name=OM_SettlementPreview]").setSelected(communicationDesigner.omSettlementPreview);
        $("[name=OM_OrderConfirmation]").setSelected(communicationDesigner.omOrderConfirmation);
        $("[name=OM_OrderConfirmationEmployee]").setSelected(communicationDesigner.omOrderConfirmationEmployee);
        $("[name=OM_OrderConfirmation_uCommerceEmployee]").setSelected(communicationDesigner.omOrderConfirmation_uCommerceEmployee);
        $("[name=OM_ReplacementMail]").setSelected(communicationDesigner.omReplacementMail);
//        $("[name=OM_InvoiceToIC]").setSelected(communicationDesigner.omInvoiceToIC);
//        $("[name=OM_PayoutNotification]").setSelected(communicationDesigner.omPayoutNotification);
//        $("[name=OM_ProcuraApprovalRequest]").setSelected(communicationDesigner.omProcuraApprovalRequest);
//        $("[name=OM_ProcuraDecisionNotification]").setSelected(communicationDesigner.omProcuraDecisionNotification);
//        $("[name=OM_ItemizationCustomerMail]").setSelected(communicationDesigner.omItemizationCustomerMail);
        $("[name=OM_AutomaticCustomerWelcome]").setSelected(communicationDesigner.omAutomaticCustomerWelcome);

        return this;
    }

    @Data
    @Builder
    public static class CommunicationDesigner{

        boolean useOutputManagement;
        boolean omSelfServiceCustomerWelcome;
        boolean omCustomerWelcome;
        boolean omCustomerWelcomeUcommerceEmployee;
        boolean omCustomerWelcomeWithOutstanding;
        boolean omCustomerWelcomeRejectionMail;
        boolean omNotificationToClaimant;
        boolean omReminderMail;
        boolean omBlockedAccount;
        boolean omItemizationReminderLossItems;
        boolean omItemizationSubmitLossItems;
        boolean omItemizationSaveLossItems;
        boolean omAutomaticCustomerWelcome;
        //        boolean omSettlementNotification;
//        boolean omSettlementNotificationClosedExternal;
//        boolean omSettlementPreview;
        boolean omOrderConfirmation;
        boolean omOrderConfirmationEmployee;
        boolean omOrderConfirmation_uCommerceEmployee;
        boolean omReplacementMail;
//        boolean omInvoiceToIC;
//        boolean omPayoutNotification;
//        boolean omProcuraApprovalRequest;
//        boolean omProcuraDecisionNotification;
//        boolean omItemizationCustomerMail;
    }
}
