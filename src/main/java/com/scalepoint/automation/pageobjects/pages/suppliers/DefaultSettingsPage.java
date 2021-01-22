package com.scalepoint.automation.pageobjects.pages.suppliers;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.extjs.ExtRadioButton;
import com.scalepoint.automation.utils.annotations.page.EccAdminPage;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;
import static com.scalepoint.automation.utils.Wait.waitForPageLoaded;

@EccAdminPage
public class DefaultSettingsPage extends BaseSupplierAdminNavigation {

    @Override
    protected void ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForAjaxCompletedAndJsRecalculation();
        waitForPageLoaded();
        $(".defaultSettings").waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
    }

    @Override
    protected String getRelativeUrl() {
        return "#defaultSettings";
    }

    public DefaultSettingsGrid toDefaultSettingsGrid(){

        return new DefaultSettingsGrid();
    }

    public class DefaultSettingsGrid{

        private final By gridRowsPath = By.cssSelector("#contentAreaPanel table [role=row]");

        private List<DefaultSettingsRow> rows;

        DefaultSettingsGrid(){

            rows = $$(gridRowsPath).stream().map(DefaultSettingsRow::new).collect(Collectors.toList());
        }

        public DefaultSettingsRow getDefaultSettingsRow(int index){

            return rows.get(index);
        }

        public class DefaultSettingsRow{

            private final By columnsPath = By.cssSelector("[role=gridcell]");

            private InsuranceCompany insuranceCompany;
            private SelfRiskCollectedBy selfRiskCollectedBy;
            private PaymentOfInvoice paymentOfInvoice;
            private OvercollectedDeductible overcollectedDeductible;
            ExtRadioButton canServicePartnerUpdateSelfRisk;
            String daysBeforeReminder;
            String daysBeforeAutoApprove;

            DefaultSettingsRow(SelenideElement row){

                ElementsCollection columns = row.findAll(columnsPath);
                insuranceCompany = new InsuranceCompany(columns.get(0));
                selfRiskCollectedBy = new SelfRiskCollectedBy(columns.get(1), columns.get(2));
                paymentOfInvoice = new PaymentOfInvoice(columns.get(3), columns.get(4));
                overcollectedDeductible = new OvercollectedDeductible(columns.get(5) , columns.get(6));
                canServicePartnerUpdateSelfRisk  = new ExtRadioButton(columns.get(7));
                daysBeforeReminder = columns.get(8).getText();
                daysBeforeAutoApprove = columns.get(9).getText();
                System.out.println();
            }

            public DefaultSettingsRow setSelfRiskCollectedByInsuranceCompany(){
                selfRiskCollectedBy.setByInsuranceCompany();
                return this;
            }

            public DefaultSettingsRow setSelfRiskCollectedByServicePartner(){
                selfRiskCollectedBy.setByServicePartner();
                return this;
            }

            public DefaultSettingsRow setPaymentOfInvoiceByInsuranceCompany(){
                paymentOfInvoice.setByInsuranceCompany();
                return this;
            }

            public DefaultSettingsRow setPaymentOfInvoiceByScalepoint(){
                paymentOfInvoice.setByScalepoint();
                return this;
            }

            private class InsuranceCompany{

                private final By expanderPath = By.cssSelector("[class$=expander]");
                private final By namePath = By.cssSelector("span");

                private SelenideElement expander;
                @Getter
                String name;

                InsuranceCompany(SelenideElement insuranceCompany){

                    expander = insuranceCompany.find(expanderPath);
                    name =  insuranceCompany.find(namePath).getText();
                }
            }

            class SelfRiskCollectedBy{

                private CheckBox insuranceCompany;
                private CheckBox servicePartner;

                SelfRiskCollectedBy(SelenideElement insuranceCompany, SelenideElement servicePartner){
                    this.insuranceCompany = new CheckBox(insuranceCompany);
                    this.servicePartner = new CheckBox(servicePartner);
                }

                public SelfRiskCollectedBy setByInsuranceCompany(){

                    if(!insuranceCompany.isChecked()){
                        insuranceCompany.click();
                    }
                    return this;
                }
                public SelfRiskCollectedBy setByServicePartner(){

                    if(!servicePartner.isChecked()){
                        servicePartner.click();
                    }
                    return this;
                }
            }

            class PaymentOfInvoice{

                private CheckBox insuranceCompany;
                private CheckBox scalepoint;

                PaymentOfInvoice(SelenideElement insuranceCompany, SelenideElement scalepoint){
                    this.insuranceCompany = new CheckBox(insuranceCompany);
                    this.scalepoint = new CheckBox(scalepoint);
                }

                public PaymentOfInvoice setByInsuranceCompany(){

                    if(!insuranceCompany.isChecked()){
                        insuranceCompany.click();
                    }
                    return this;
                }
                public PaymentOfInvoice setByScalepoint(){

                    if(!scalepoint.isChecked()){
                        scalepoint.click();
                    }
                    return this;
                }
            }

            class OvercollectedDeductible{

                CheckBox viaContent;
                CheckBox viaInsuranceCompany;

                OvercollectedDeductible(SelenideElement viaContent, SelenideElement viaInsuranceCompany){
                    this.viaContent = new CheckBox(viaInsuranceCompany);
                    this.viaInsuranceCompany = new CheckBox(viaInsuranceCompany);
                }

                public OvercollectedDeductible setViaContent(){

                    if(!viaContent.isChecked()){
                        viaContent.click();
                    }
                    return this;
                }
                public OvercollectedDeductible setViaInsuranceCompany(){

                    if(!viaInsuranceCompany.isChecked()){
                        viaInsuranceCompany.click();
                    }
                    return this;
                }
            }

            class CheckBox{

                SelenideElement checkBox;

                CheckBox(SelenideElement checkBox){

                    this.checkBox = checkBox;
                }

                boolean isChecked(){

                    return checkBox.find("img").attr("class").contains("checked");
                }

                CheckBox click(){

                    checkBox.click();
                    return this;
                }
            }
        }
    }

//    public DefaultSettingsPage doAssert(Consumer<Asserts> assertsFunc) {
//        assertsFunc.accept(new Asserts());
//        return DefaultSettingsPage.this;
//    }

//    public class Asserts {
//        public Asserts assertVoucherPresent(String voucherName) {
//            assertTrue(isVoucherCreated(voucherName));
//            return this;
//        }
//
//        public Asserts assertVoucherAbsent(String voucherName) {
//            assertFalse(isVoucherCreated(voucherName));
//            return this;
//        }
//
//        public Asserts assertsIsExclusiveColumnDisplayed() {
//            assertTrue(isExclusiveColumnDisplayed());
//            return this;
//        }
//
//        public Asserts assertsIsExclusiveColumnNotDisplayed() {
//            assertFalse(isExclusiveColumnDisplayed());
//            return this;
//        }
//
//        public Asserts assertsIsExclusiveTickForVoucherDisplayed(String voucherName) {
//            assertTrue(isTickDisplayed(voucherName, byExclusiveXpath));
//            return this;
//        }
//
//        public Asserts assertsIsActiveTickForVoucherDisplayed(String voucherName) {
//            assertTrue(isTickDisplayed(voucherName, byExclusiveXpath));
//            return this;
//        }
//
//        public Asserts assertsIsNotActiveTickForVoucherDisplayed(String voucherName) {
//            assertFalse(isTickDisplayed(voucherName, byActiveXpath));
//            return this;
//        }
//    }
}

