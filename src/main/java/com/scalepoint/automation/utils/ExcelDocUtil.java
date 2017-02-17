package com.scalepoint.automation.utils;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.scalepoint.automation.utils.data.entity.ServiceAgreement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.scalepoint.automation.utils.ExcelDocUtil.FeedbackActionType.NO_CHANGES_KEEP_FILE;

public class ExcelDocUtil {

    private static Logger logger = LoggerFactory.getLogger(ExcelDocUtil.class);

    private ActiveXComponent activexComponent;
    private Dispatch workBooks;

    private String clId = "A25";
    private String description = "E25";
    private String categoryGroup = "F25";
    private String subCategory = "G25";
    private String quantity = "H25";
    private String ageMonth = "J25";
    private String ageYears = "K25";
    private String newPrice = "M25";
    private String repairPrice = "U25";

    private String mainTemplateSheet = "Template";
    private String invoiceSheet = "Invoice";

    private String invoiceType = "J3";
    private String invoiceNumber = "J4";
    private String invoiceDate = "J5";
    private String paymentDue = "J6";

    private String creditNote = "J7";

    private String unitDesc = "C26";
    private String unitQuantity = "E26";
    private String units = "F26";
    private String unitPrice = "G26";
    private String vat = "H26";
    private String fikType = "J11";
    private String fikId = "J12";
    private String fikCreditorCode = "J13";


    public ExcelDocUtil() {
        activexComponent = new ActiveXComponent("Excel.Application");
        activexComponent.setProperty("Visible", true);
        activexComponent.setProperty("DisplayAlerts", false);
    }

    public void openFile(String path) {
        logger.info("Excel file opening");
        workBooks = activexComponent.getProperty("Workbooks").toDispatch();
        Dispatch.call(workBooks, "Open", new Variant(path));
    }

    public void setCellValue(String sheetName, String cellCoordinate, Object value) {
        Dispatch sheet = Dispatch.call(activexComponent, "Sheets", sheetName).toDispatch();
        Dispatch.call(sheet, "Select");
        Dispatch cell = Dispatch.invoke(
                sheet,
                "Range",
                Dispatch.Get,
                new Object[]{cellCoordinate},
                new int[1]
        ).toDispatch();

        Dispatch.put(cell, "Value", value);
    }

    public void executeMacros(String macrosName) {
            logger.info("Run macro");
            activexComponent.setProperty("DisplayAlerts", false);
            Dispatch.call(activexComponent, "Run", macrosName);
    }

    public void closeAndDeleteFile(String fileLocation) {
        logger.info("Close file");
        activexComponent.setProperty("DisplayAlerts", false);
        activexComponent.invoke("Quit", new Variant[]{});
        ComThread.Release();
        deleteFile(fileLocation);
    }

    public static void deleteFile(String fileLocation) {
        Path path = Paths.get(fileLocation);
        try {
            Files.delete(path);
        } catch (Exception x) {
            x.printStackTrace();
        }
        logger.info("File was deleted - " + fileLocation);
    }

    public void closeFile() {
        logger.info("Close file");
        activexComponent.invoke("Quit", new Variant[]{});
        ComThread.Release();
    }

    public void updateFirstCLInExcel(ServiceAgreement serviceAgreement) {
        setCellValue(mainTemplateSheet, description, serviceAgreement.getUpdDesc());
        setCellValue(mainTemplateSheet, categoryGroup, serviceAgreement.getUpdCat());
        setCellValue(mainTemplateSheet, subCategory, serviceAgreement.getUpdSubCategory());
        setCellValue(mainTemplateSheet, quantity, serviceAgreement.getUpdQuantity());
        setCellValue(mainTemplateSheet, ageMonth, serviceAgreement.getUpdMonth());
        setCellValue(mainTemplateSheet, ageYears, serviceAgreement.getUpdYear());
        setCellValue(mainTemplateSheet, newPrice, serviceAgreement.getUpdNewPrice());
    }

    public void deleteCLIdForFirstLine() {
        setCellValue(mainTemplateSheet, clId, " ");
    }

    public void updateDescriptionForFirstLine(ServiceAgreement serviceAgreement) {
        setCellValue(mainTemplateSheet, description, serviceAgreement.getUpdDesc());
    }

    public void addRepairPriceForFirstLine(String repairPriceValue) {
        setCellValue(mainTemplateSheet, repairPrice, repairPriceValue);
    }

    public void updateInvoice(ServiceAgreement serviceAgreement) {
        setCellValue(invoiceSheet, invoiceType, serviceAgreement.getInvoiceType());
        setCellValue(invoiceSheet, invoiceNumber, serviceAgreement.getInvoiceNumber());
        setCellValue(invoiceSheet, invoiceDate, serviceAgreement.getInvoiceDate());
        setCellValue(invoiceSheet, paymentDue, serviceAgreement.getPaymentDue());
        setCellValue(invoiceSheet, unitDesc, serviceAgreement.getRandomUnitDesc());
        setCellValue(invoiceSheet, unitQuantity, serviceAgreement.getUnitQuantity());
        setCellValue(invoiceSheet, units, serviceAgreement.getUnits());
        setCellValue(invoiceSheet, unitPrice, serviceAgreement.getRandomUnitPrice());
        setCellValue(invoiceSheet, vat, serviceAgreement.getUnitNetAmount());
    }

    private void updateInvoiceAsCreditNote(ServiceAgreement serviceAgreement) {
        setCellValue(invoiceSheet, invoiceType, serviceAgreement.getCreditNoteType());
        setCellValue(invoiceSheet, invoiceNumber, serviceAgreement.getInvoiceNumber());
        setCellValue(invoiceSheet, creditNote, serviceAgreement.getCreditNoteNumber());
        setCellValue(invoiceSheet, invoiceDate, serviceAgreement.getInvoiceDate());
        setCellValue(invoiceSheet, paymentDue, serviceAgreement.getPaymentDue());
        setCellValue(invoiceSheet, unitDesc, serviceAgreement.getRandomUnitDesc());
        setCellValue(invoiceSheet, unitQuantity, serviceAgreement.getUnitQuantity());
        setCellValue(invoiceSheet, units, serviceAgreement.getUnits());
        setCellValue(invoiceSheet, unitPrice, serviceAgreement.getRandomUnitPrice());
        setCellValue(invoiceSheet, vat, serviceAgreement.getUnitNetAmount());
    }

    private void addFikInfo(ServiceAgreement serviceAgreement) {
        setCellValue(invoiceSheet, fikType, serviceAgreement.getFikType());
        setCellValue(invoiceSheet, fikId, serviceAgreement.getFikId());
        setCellValue(invoiceSheet, fikCreditorCode, serviceAgreement.getFikCreditCode());

    }

    public enum FeedbackActionType {
        NO_CHANGES,
        NO_CHANGES_KEEP_FILE,
        NO_INVOICE,
        DELETE_LINE,
        WITH_INVOICE,
        WITH_INVOICE_AND_FIK,
        DELETE_CLAIM_LINE_ID,
        DELETE_CLAIM_LINE_ID_ADD_REPAIR_PRICE,
        WITH_CREDIT_NOTE,
    }

    public void doFeedback(FeedbackActionType feedbackActionType, ServiceAgreement serviceAgreement) throws Exception {
        openFile(serviceAgreement.getSaveTemplateTo());
        switch (feedbackActionType) {
            case NO_CHANGES:
            case NO_CHANGES_KEEP_FILE:
                break;
            case NO_INVOICE:
                updateFirstCLInExcel(serviceAgreement);
                break;
            case DELETE_LINE:
                deleteCLIdForFirstLine();
                updateDescriptionForFirstLine(serviceAgreement);
                break;
            case WITH_INVOICE:
                updateFirstCLInExcel(serviceAgreement);
                updateInvoice(serviceAgreement);
                break;
            case WITH_INVOICE_AND_FIK:
                updateFirstCLInExcel(serviceAgreement);
                updateInvoice(serviceAgreement);
                addFikInfo(serviceAgreement);
                break;
            case DELETE_CLAIM_LINE_ID_ADD_REPAIR_PRICE:
                addRepairPriceForFirstLine("100");
            case DELETE_CLAIM_LINE_ID:
                deleteCLIdForFirstLine();
                updateDescriptionForFirstLine(serviceAgreement);
                break;
            case WITH_CREDIT_NOTE:
                updateFirstCLInExcel(serviceAgreement);
                updateInvoiceAsCreditNote(serviceAgreement);
                break;
        }

        executeMacros(serviceAgreement.getExcelMacroName());

        if (feedbackActionType == NO_CHANGES_KEEP_FILE) {
            closeFile();
        } else {
            closeAndDeleteFile(serviceAgreement.getSaveTemplateTo());
        }
    }
}
