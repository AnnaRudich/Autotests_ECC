package com.scalepoint.automation.utils.data.entity;

import com.scalepoint.automation.utils.RandomUtils;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
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
    private String error;
    @XmlElement
    private String sendTaskFailStatus;
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
    @XmlElement
    private String rnvSupplier;
    @XmlElement
    private String supplierSecurityToken;

    public String getInvoiceNumber() {
        return randomInvoiceNumber.toString();
    }

    public String getExcelMacroName() {
        return randomSavedTemplateName + macroName;
    }

}
