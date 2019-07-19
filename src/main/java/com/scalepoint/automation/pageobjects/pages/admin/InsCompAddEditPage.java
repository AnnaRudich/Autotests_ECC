package com.scalepoint.automation.pageobjects.pages.admin;

import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.data.entity.InsuranceCompany;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForStaleElement;
import static com.scalepoint.automation.utils.Wait.waitForVisible;

@EccPage
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

    private String byFTXpath = "//select[@name='icftnbr']/option[contains(.,'$1')]";

    private String byGUIXpath = "//select[@name='icgtnbr']/option[contains(.,'$1')]";

    private String byICCultureXpath = "//select[@name='icCulture']/option[contains(.,'$1')]";

    private String byAuditXpath = "//select[@name='auditEnabled']/option[contains(.,'$1')]";

    @Override
    protected Page ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForVisible(companyIDField);
        return this;
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/Admin/insurance_company_edit.jsp";
    }

    public void selectSaveOption() {
        waitForStaleElement(By.id("btnOk"));
        clickAndWaitForDisplaying(saveButton, By.id("btnAdd"));
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

        WebElement option = find(byFTXpath, insuranceCompany.getFunctionTemplate());
        if (option.getText().equals(insuranceCompany.getFunctionTemplate())) {
            option.click();
        }

        WebElement option1 = find(byGUIXpath, insuranceCompany.getGuiTemplate());
        if (option1.getText().equals(insuranceCompany.getGuiTemplate())) {
            option1.click();

        }
        WebElement option2 = find(byICCultureXpath, insuranceCompany.getIcCulture());
        if (option2.getText().equals(insuranceCompany.getIcCulture())) {
            option2.click();
        }

        companyContactNumberField.sendKeys(insuranceCompany.getContactNumber());
        companyOfficeHoursField.sendKeys(insuranceCompany.getOfficeHours());
        selectSaveOption();
        return at(InsCompaniesPage.class);
    }

    public InsCompaniesPage updateNameAndSave(InsuranceCompany insuranceCompany) {
        companyNameField.clear();
        companyNameField.sendKeys(insuranceCompany.getIcName());
        selectSaveOption();
        return at(InsCompaniesPage.class);
    }

    public void enableAuditOptionAndSave() {
        WebElement option = find(byAuditXpath, "Enabled");
        if (option.getText().equals("Enabled")) {
            option.click();
        }
        selectSaveOption();
    }

    public void selectParentCompany(InsuranceCompany insuranceCompany) {
        WebElement option = find(byFTXpath, insuranceCompany.getIcName());
        if (option.getText().equals(insuranceCompany.getIcName())) {
            option.click();
        }
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
//        $("[name=OM_OrderConfirmation]").setSelected(communicationDesigner.omOrderConfirmation);
//        $("[name=OM_OrderConfirmationEmployee]").setSelected(communicationDesigner.omOrderConfirmationEmployee);
//        $("[name=OM_OrderConfirmation_uCommerceEmployee]").setSelected(communicationDesigner.omOrderConfirmation_uCommerceEmployee);
//        $("[name=OM_InvoiceToIC]").setSelected(communicationDesigner.omInvoiceToIC);
//        $("[name=OM_PayoutNotification]").setSelected(communicationDesigner.omPayoutNotification);
//        $("[name=OM_ProcuraApprovalRequest]").setSelected(communicationDesigner.omProcuraApprovalRequest);
//        $("[name=OM_ProcuraDecisionNotification]").setSelected(communicationDesigner.omProcuraDecisionNotification);
//        $("[name=OM_ItemizationCustomerMail]").setSelected(communicationDesigner.omItemizationCustomerMail);

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
//        boolean omSettlementNotification;
//        boolean omSettlementNotificationClosedExternal;
//        boolean omSettlementPreview;
//        boolean omOrderConfirmation;
//        boolean omOrderConfirmationEmployee;
//        boolean omOrderConfirmation_uCommerceEmployee;
//        boolean omInvoiceToIC;
//        boolean omPayoutNotification;
//        boolean omProcuraApprovalRequest;
//        boolean omProcuraDecisionNotification;
//        boolean omItemizationCustomerMail;
    }
}
