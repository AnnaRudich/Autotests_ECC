package com.scalepoint.automation.pageobjects.pages.admin;

import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.data.entity.InsuranceCompany;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

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
        companyCodeField.sendKeys(insuranceCompany.getIcCode().toUpperCase());
        unifiedCompanyCode.sendKeys(insuranceCompany.getIcCode().toUpperCase());

        companyNameField.sendKeys(insuranceCompany.getIcName());
        addressField.sendKeys(insuranceCompany.getAddress());
        zipCodeField.sendKeys(insuranceCompany.getZipCode());
        cityField.sendKeys(insuranceCompany.getIcCity());
        icCommonMailField.sendKeys(insuranceCompany.getCompanyCommonMail());

        if(StringUtils.isEmpty(sendTimeFromField.getText())){
            sendTimeFromField.sendKeys(insuranceCompany.getSendTimeFrom());
        }

        if(StringUtils.isEmpty(sendTimeToField.getText())){
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
}
