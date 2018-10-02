package com.scalepoint.automation.utils.data.entity;

import com.scalepoint.automation.utils.RandomUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ServiceAgreement {

    private String randomSavedTemplateName = RandomUtils.randomName("rnv_tempalate") + ".xlsm";
    private String agreementRandomName = RandomUtils.randomName("AgreementName");
    @XmlElement
    private String testAgrName;
    @XmlElement
    private String templateName;
    @XmlElement
    private String agreementEmail;
    @XmlElement
    private String locationEmail;
    @XmlElement
    private String tags;
    @XmlElement
    private String category;
    @XmlElement
    private String category2;
    @XmlElement
    private String defaultPostalCode;
    @XmlElement
    private String updPostalCode;
    @XmlElement
    private String testRnVShop;
    @XmlElement
    private String testSePa;
    @XmlElement
    private String postalCode1;
    @XmlElement
    private String postalCode2;
    @XmlElement
    private String postalCode3;
    @XmlElement
    private String postalCode4;
    @XmlElement
    private String mailBoxAddress;
    private String agreementRandomNotes = RandomUtils.randomName("SomeNotes");
    private String agreementRandomWorkflow = RandomUtils.randomName("SomeWorkflow");
    private Integer agreementRandomReminder = RandomUtils.randomInt();
    private Integer agreementRandomTimeout = RandomUtils.randomInt();
    private String randomTemplateName = RandomUtils.randomName("TestTemplate");
    @XmlElement
    private String agrIdColumnName;
    @XmlElement
    private String agrNameColumnName;
    @XmlElement
    private String agrSupplierColumnName;
    @XmlElement
    private String agrTypeColumnName;
    @XmlElement
    private String agrStatusColumnName;
    @XmlElement
    private String defaultTemplateLoc;
    @XmlElement
    private String templateSpecialName;
    @XmlElement
    private String lineCategory;
    @XmlElement
    private String lineSubCategory;
    @XmlElement
    private String newTaskMailSubj;
    @XmlElement
    private String cancelledTaskMailSubj;
    @XmlElement
    private String cancelledMessageText;
    @XmlElement
    private String claimLineName = RandomUtils.randomName("ForRnV");
    @XmlElement
    private String testAgrNameForRnV;
    @XmlElement
    private String waitingStatus;
    @XmlElement
    private String cancelledStatus;
    @XmlElement
    private String saveTemplateTo;
    @XmlElement
    private String CLSheetName;
    @XmlElement
    private String updDesc;
    @XmlElement
    private String updCat;
    @XmlElement
    private String updSubcat;
    @XmlElement
    private String updQuantity;
    @XmlElement
    private String updYear;
    @XmlElement
    private String updMonth;
    @XmlElement
    private String updNewPrice;
    @XmlElement
    private String updRepairPrice;
    @XmlElement
    private String invoiceSheetName;
    @XmlElement
    private String invoiceType;

    private Integer randomInvoiceNumber = RandomUtils.randomInt();
    private String randomUnitDesc = RandomUtils.randomName("unit_Description");
    private Integer randomUnitPrice = RandomUtils.randomInt(100);

    @XmlElement
    private String invoiceDate;
    @XmlElement
    private String paymentDue;

    @XmlElement
    private String unitQuantity;
    @XmlElement
    private String units;

    @XmlElement
    private String unitNetAmount;

    @XmlElement
    private String repair;
    @XmlElement
    private String valuation;

    @XmlElement
    private String repairEstimate;
    @XmlElement
    private String matchService;

    @XmlElement
    private String sent;

    @XmlElement
    private String macroName;

    @XmlElement
    private String feedbackReceivedStatus;

    @XmlElement
    private String feedbackCompletedStatus;

    @XmlElement
    private String feedbackPartlyCompletedStatus;

    @XmlElement
    private String noAgrText;

    @XmlElement
    private String unpaidInvoiceStatusName;
    @XmlElement
    private String paidInvoiceStatusName;
    @XmlElement
    private String fikType;
    @XmlElement
    private String fikId;
    @XmlElement
    private String fikCreditCode;
    @XmlElement
    private String creditNoteType;
    @XmlElement
    private String creditNoteNumber;
    @XmlElement
    private String manuallyPaidByOwnSystem;

    public String getRandomAgreementName() {
        return agreementRandomName;
    }

    public String getTestAgreementName() {
        return testAgrName;
    }

    public String getTestAgreementForRnV() {
        return testAgrNameForRnV;
    }

    public String getTemplateName() {
        return templateName;
    }

    public String getAgreementEmail() {
        return agreementEmail;
    }

    public String getLocationEmail() {
        return locationEmail;
    }

    public String getAgreementTags() {
        return tags;
    }

    public String getCategoryName() {
        return category;
    }

    public String getCategory2Name() {
        return category2;
    }

    public String getDefaultPostalCode() {
        return defaultPostalCode;
    }

    public String getUpdPostalCode() {
        return updPostalCode;
    }

    public String getTestRnVShop() {
        return testRnVShop;
    }

    public String getTestSePaName() {
        return testSePa;
    }

    public String getPostalCode1() {
        return postalCode1;
    }

    public String getPostalCode2() {
        return postalCode2;
    }

    public String getPostalCode3() {
        return postalCode3;
    }

    public String getPostalCode4() {
        return postalCode4;
    }

    public String getAgreementRandomNotes() {
        return agreementRandomNotes;
    }

    public String getAgreementRandomWorkflow() {
        return agreementRandomWorkflow;
    }

    public String getAgreementRandomReminderTime() {
        return agreementRandomReminder.toString();
    }

    public String getAgreementRandomTimeoutTime() {
        return agreementRandomTimeout.toString();
    }

    public String getAgrIdColumnName() {
        return agrIdColumnName;
    }

    public String getAgrNameColumnName() {
        return agrNameColumnName;
    }

    public String getAgrSupplierColumnName() {
        return agrSupplierColumnName;
    }

    public String getAgrTypeColumnName() {
        return agrTypeColumnName;
    }

    public String getAgrStatusColumnName() {
        return agrStatusColumnName;
    }

    public String getTemplateFileLocation() {
        return defaultTemplateLoc;
    }

    public String getRandomTemplateName() {
        return randomTemplateName;
    }

    public String getClaimLineCat_PersonligPleje() {
        return lineCategory;
    }

    public String getClaimLineSubCat_Medicin() {
        return lineSubCategory;
    }

    public String getMailBoxAddress() {
        return mailBoxAddress;
    }

    public String getNewTaskMailSubject() {
        return newTaskMailSubj;
    }

    public String getCancelledTaskMailSubj() {
        return cancelledTaskMailSubj;
    }

    public String getCancelledMessageText() {
        return cancelledMessageText;
    }

    public String getClaimLineNameForRnV() {
        return claimLineName;
    }

    public String getWaitingStatus() {
        return waitingStatus;
    }

    public String getCancelledStatus() {
        return cancelledStatus;
    }

    public String getSaveTemplateTo() {
        return saveTemplateTo + "\\" + randomSavedTemplateName;
    }

    public String getUpdDesc() {
        return updDesc;
    }

    public String getUpdCat() {
        return updCat;
    }

    public String getUpdSubCategory() {
        return updSubcat;
    }

    public String getUpdQuantity() {
        return updQuantity;
    }

    public String getUpdYear() {
        return updYear;
    }

    public String getUpdMonth() {
        return updMonth;
    }

    public String getUpdNewPrice() {
        return updNewPrice;
    }

    public String getUpdRepairPrice() {
        return updRepairPrice;
    }

    public String getInvoiceSheetName() {
        return invoiceSheetName;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public String getInvoiceNumber() {
        return randomInvoiceNumber.toString();
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public String getPaymentDue() {
        return paymentDue;
    }

    public String getRandomUnitDesc() {
        return randomUnitDesc;
    }

    public String getUnitQuantity() {
        return unitQuantity;
    }

    public String getUnits() {
        return units;
    }

    public String getRandomUnitPrice() {
        return randomUnitPrice.toString();
    }

    public String getUnitNetAmount() {
        return unitNetAmount;
    }

    public String getRepairType() {
        return repair;
    }

    public String getValuaitonType() {
        return valuation;
    }

    public String getRepairEstimateType() {
        return repairEstimate;
    }

    public String getMatchServiceType() {
        return matchService;
    }

    public String getSentText() {
        return sent;
    }

    public String getExcelMacroName() {
        return randomSavedTemplateName + macroName;
    }

    public String getFeedbackReceivedStatusName() {
        return feedbackReceivedStatus;
    }

    public String getCompletedStatusName() {
        return feedbackCompletedStatus;
    }

    public String getPartlyCompletedStatusName() {
        return feedbackPartlyCompletedStatus;
    }

    public String getNoAgrSelectedText() {
        return noAgrText;
    }

    public String getUnpaidInvoiceStatusName() {
        return unpaidInvoiceStatusName;
    }

    public String getPaidInvoiceStatusName() {
        return paidInvoiceStatusName;
    }

    public String getFikType() {
        return fikType;
    }

    public String getFikId() {
        return fikId;
    }

    public String getFikCreditCode() {
        return fikCreditCode;
    }

    public String getCreditNoteType() {
        return creditNoteType;
    }

    public String getCreditNoteNumber() {
        return creditNoteNumber;
    }

    public String getManuallyPaidByOwnSystemText() {
        return manuallyPaidByOwnSystem;
    }
}
