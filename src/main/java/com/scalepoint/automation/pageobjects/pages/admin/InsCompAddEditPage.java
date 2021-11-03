package com.scalepoint.automation.pageobjects.pages.admin;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.GdprConfirmationDialog;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.annotations.page.RequiredParameters;
import com.scalepoint.automation.utils.data.entity.input.InsuranceCompany;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

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

    private String byICCulturePath = "select[name='icCulture']";

    private String byAuditPath = "select[name='auditEnabled']";

    @Override
    protected void ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForAjaxCompletedAndJsRecalculation();
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

        communicationDesigner.emailList.forEach(omEmail -> omEmail.setSettings());

        $("#chkUseOutputManagement").setSelected(communicationDesigner.useOutputManagement);
        $("[name=OM_CustomerWelcome_uCommerceEmployee]").setSelected(communicationDesigner.omCustomerWelcomeUcommerceEmployee);
        $("[name=OM_NotificationToClaimant]").setSelected(communicationDesigner.omNotificationToClaimant);
        $("[name=OM_ReminderMail]").setSelected(communicationDesigner.omReminderMail);
        $("[name=OM_BlockedAccount]").setSelected(communicationDesigner.omBlockedAccount);
        $("[name=OM_ItemizationReminderLossItems]").setSelected(communicationDesigner.omItemizationReminderLossItems);
//        $("[name=OM_SettlementNotification]").setSelected(communicationDesigner.omSettlementNotification);
//        $("[name=OM_SettlementNotificationClosedExternal]").setSelected(communicationDesigner.omSettlementNotificationClosedExternal);
//        $("[name=OM_SettlementPreview]").setSelected(communicationDesigner.omSettlementPreview);
        $("[name=OM_OrderConfirmationEmployee]").setSelected(communicationDesigner.omOrderConfirmationEmployee);
        $("[name=OM_OrderConfirmation_uCommerceEmployee]").setSelected(communicationDesigner.omOrderConfirmation_uCommerceEmployee);
//        $("[name=OM_InvoiceToIC]").setSelected(communicationDesigner.omInvoiceToIC);
//        $("[name=OM_PayoutNotification]").setSelected(communicationDesigner.omPayoutNotification);
//        $("[name=OM_ProcuraApprovalRequest]").setSelected(communicationDesigner.omProcuraApprovalRequest);
//        $("[name=OM_ProcuraDecisionNotification]").setSelected(communicationDesigner.omProcuraDecisionNotification);
//        $("[name=OM_ItemizationCustomerMail]").setSelected(communicationDesigner.omItemizationCustomerMail);

        return this;
    }

    @Accessors(chain = true)
    @Setter
    public static class CommunicationDesigner{

        boolean useOutputManagement;
        List<OMEmail> emailList = new ArrayList<>();

        boolean omCustomerWelcomeUcommerceEmployee;
        boolean omNotificationToClaimant;
        boolean omReminderMail;
        boolean omBlockedAccount;
        boolean omItemizationReminderLossItems;
        //        boolean omSettlementNotification;
//        boolean omSettlementNotificationClosedExternal;
//        boolean omSettlementPreview;
        boolean omOrderConfirmationEmployee;
        boolean omOrderConfirmation_uCommerceEmployee;
//        boolean omInvoiceToIC;
//        boolean omPayoutNotification;
//        boolean omProcuraApprovalRequest;
//        boolean omProcuraDecisionNotification;
//        boolean omItemizationCustomerMail;

        public CommunicationDesigner setSelfServiceCustomerWelcome(boolean omPDF, String omPDFText) {
            return addToEmails(new SelfServiceCustomerWelcome(), omPDF, omPDFText);
        }

        public CommunicationDesigner setCustomerWelcome(boolean omPDF, String omPDFText) {
            return addToEmails(new CustomerWelcome(), omPDF, omPDFText);
        }

        public CommunicationDesigner setItemizationSubmitLossItems(boolean omPDF, String omPDFText) {
            return addToEmails(new ItemizationSubmitLossItems(), omPDF, omPDFText);
        }

        public CommunicationDesigner setItemizationSaveLossItems(boolean omPDF, String omPDFText) {
            return addToEmails(new ItemizationSaveLossItems(), omPDF, omPDFText);
        }

        public CommunicationDesigner setCustomerWelcomeRejectionMail(boolean omPDF, String omPDFText) {
            return addToEmails(new CustomerWelcomeRejectionMail(), omPDF, omPDFText);
        }

        public CommunicationDesigner setCustomerWelcomeWithOutstanding(boolean omPDF, String omPDFText) {
            return addToEmails(new CustomerWelcomeWithOutstanding(), omPDF, omPDFText);
        }

        public CommunicationDesigner setOrderConfirmation(boolean omPDF, String omPDFText) {
            return addToEmails(new OrderConfirmation(), omPDF, omPDFText);
        }

        public CommunicationDesigner setReplacementMail(boolean omPDF, String omPDFText) {
            return addToEmails(new ReplacementMail(), omPDF, omPDFText);
        }

        public CommunicationDesigner setAutomaticCustomerWelcome(boolean omPDF, String omPDFText) {
            return addToEmails(new AutomaticCustomerWelcome(), omPDF, omPDFText);
        }

        public static CommunicationDesigner reset(){

            CommunicationDesigner reset = new CommunicationDesigner()
                    .setUseOutputManagement(false)
                    .setOmCustomerWelcomeUcommerceEmployee(false)
                    .setOmNotificationToClaimant(false)
                    .setOmReminderMail(false)
                    .setOmBlockedAccount(false)
                    .setOmOrderConfirmationEmployee(false)
                    .setOmOrderConfirmation_uCommerceEmployee(false)
                    .resetEmails(new SelfServiceCustomerWelcome())
                    .resetEmails(new CustomerWelcome())
                    .resetEmails(new ItemizationSubmitLossItems())
                    .resetEmails(new ItemizationSaveLossItems())
                    .resetEmails(new CustomerWelcomeRejectionMail())
                    .resetEmails(new CustomerWelcomeWithOutstanding())
                    .resetEmails(new OrderConfirmation())
                    .resetEmails(new ReplacementMail())
                    .resetEmails(new AutomaticCustomerWelcome());
            return reset;
        }

        private CommunicationDesigner addToEmails(OMEmail omEmail, boolean omPDF, String omPDFText){
            omEmail = omEmail
                    .setOmEmail(true)
                    .setOmPDF(omPDF)
                    .setOmPDFText(omPDFText);
            emailList.add(omEmail);
            return this;
        }

        private CommunicationDesigner resetEmails(OMEmail omEmail){
            omEmail = omEmail
                    .setOmEmail(false);
            emailList.add(omEmail);
            return this;
        }
    }

    public static class SelfServiceCustomerWelcome extends OMEmail{

        public SelfServiceCustomerWelcome() {
            super("[name=OM_SelfServiceCustomerWelcome]",
                    "[name=OM_PDF_SelfServiceCustomerWelcome]",
                    "#OM_PDF_TEXT_SelfServiceCustomerWelcome");
        }
    }

    public static class CustomerWelcome extends OMEmail{

        public CustomerWelcome() {
            super("[name=OM_CustomerWelcome]",
                    "[name=OM_PDF_CustomerWelcome]",
                    "#OM_PDF_TEXT_CustomerWelcome");
        }
    }

    public static class ItemizationSubmitLossItems extends OMEmail{

        public ItemizationSubmitLossItems() {
            super("[name=OM_ItemizationSubmitLossItems]",
                    "[name=OM_PDF_ItemizationSubmitLossItems]",
                    "#OM_PDF_TEXT_ItemizationSubmitLossItems");
        }
    }

    public static class ItemizationSaveLossItems extends OMEmail{

        public ItemizationSaveLossItems() {
            super("[name=OM_ItemizationSaveLossItems]",
                    "[name=OM_PDF_ItemizationSaveLossItems]",
                    "#OM_PDF_TEXT_ItemizationSaveLossItems");
        }
    }

    public static class CustomerWelcomeRejectionMail extends OMEmail{

        public CustomerWelcomeRejectionMail() {
            super("[name=OM_CustomerWelcomeRejectionMail]",
                    "[name=OM_PDF_CustomerWelcomeRejectionMail]",
                    "#OM_PDF_TEXT_CustomerWelcomeRejectionMail");
        }
    }

    public static class CustomerWelcomeWithOutstanding extends OMEmail{

        public CustomerWelcomeWithOutstanding() {
            super("[name=OM_CustomerWelcomeWithOutstanding]",
                    "[name=OM_PDF_CustomerWelcomeWithOutstanding]",
                    "#OM_PDF_TEXT_CustomerWelcomeWithOutstanding");
        }
    }

    public static class ReplacementMail extends OMEmail{

        public ReplacementMail() {
            super("[name=OM_ReplacementMail]",
                    "[name=OM_PDF_ReplacementMail]",
                    "#OM_PDF_TEXT_ReplacementMail");
        }
    }

    public static class OrderConfirmation extends OMEmail{

        public OrderConfirmation() {
            super("[name=OM_OrderConfirmation]",
                    "[name=OM_PDF_OrderConfirmation]",
                    "#OM_PDF_TEXT_OrderConfirmation");
        }
    }

    public static class AutomaticCustomerWelcome extends OMEmail{

        public AutomaticCustomerWelcome() {
            super("[name=OM_AutomaticCustomerWelcome]",
                    "[name=OM_PDF_AutomaticCustomerWelcome]",
                    "#OM_PDF_TEXT_AutomaticCustomerWelcome");
        }
    }

    @RequiredArgsConstructor
    @Accessors(chain = true)
    public static class OMEmail{
        @NonNull
        protected String omEmailPath;
        @NonNull
        protected String omPDFPath;
        @NonNull
        protected String omPDFTextPath;
        @Setter
        protected boolean omEmail;
        @Setter
        protected boolean omPDF;
        @Setter
        protected String omPDFText;

        public void setSettings(){
            $(omEmailPath).setSelected(omEmail);
            $(omPDFPath).setSelected(omPDF);
            if(omEmail && omPDF) {
                $(omPDFTextPath).setValue(omPDFText);
            }
        }
    }
}
