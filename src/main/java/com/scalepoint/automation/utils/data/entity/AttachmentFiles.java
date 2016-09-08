package com.scalepoint.automation.utils.data.entity;

import com.scalepoint.automation.utils.RandomUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * Created by jts on 4/7/2015.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AttachmentFiles {

    @XmlElement
    private String jpgFile;
    @XmlElement
    private String jpgFile2;
    @XmlElement
    private String pngFile;
    @XmlElement
    private String txtFile;
    @XmlElement
    private String pdfFile;
    @XmlElement
    private String gifFile;
    @XmlElement
    private String bmpFile;
    @XmlElement
    private String jpegFile;
    @XmlElement
    private String xlsFile;
    @XmlElement
    private String bigFile;
    @XmlElement
    private String filesFolder;
    @XmlElement
    private String downloadAttachmentsFolder;
    @XmlElement
    private String downloadRnVFilesFolder;


    public String randomSavedTemplateName = RandomUtils.randomName("rnv_tempalate") + ".xlsm";
    public String randomSavedAttachmentName = RandomUtils.randomName("downloadedAttachment");

    public String getRandomSavedAttachmentName(String originalFileName) {
        String randomName = RandomUtils.randomInt(999999) + originalFileName;
        return randomName;
    }

    public String getJpgFileLoc() {
        return filesFolder + jpgFile;
    }

    public String getJpgFile2Loc() {
        return filesFolder + jpgFile2;
    }

    public String getPngFileLoc() {
        return filesFolder + pngFile;
    }

    public String getTxtFileLoc() {
        return filesFolder + txtFile;
    }

    public String getPdfFileLoc() {
        return filesFolder + pdfFile;
    }

    public String getGifFileLoc() {
        return filesFolder + gifFile;
    }

    public String getBmpFileLoc() {
        return filesFolder + bmpFile;
    }

    public String getJpegFileLoc() {
        return filesFolder + jpegFile;
    }

    public String getJpgFileName() {
        return jpgFile;
    }

    public String getJpgFile2Name() {
        return jpgFile2;
    }

    public String getPngFileName() {
        return pngFile;
    }

    public String getTxtFileName() {
        return txtFile;
    }

    public String getPdfFileName() {
        return pdfFile;
    }

    public String getGifFileName() {
        return gifFile;
    }

    public String getBmpFileName() {
        return bmpFile;
    }

    public String getJpegFileName() {
        return jpegFile;
    }


    public String getFileName(String fileLocation) {
        String[] splitedString = fileLocation.split("\\\\");
        String fileName = splitedString[splitedString.length - 1];
        return fileName;
    }

    public String getSaveRnVFileTo() {
        String fullPath = downloadRnVFilesFolder + "\\" + randomSavedTemplateName;
        return fullPath;
    }

    public String getSaveAttachmentFileTo(String fileName) {
        String fullPath = downloadAttachmentsFolder + "\\" + getRandomSavedAttachmentName(fileName);
        return fullPath;
    }

    public String getBigFileLoc() {
        return filesFolder + bigFile;
    }

    public String getBigFileName() {
        return bigFile;
    }

}
