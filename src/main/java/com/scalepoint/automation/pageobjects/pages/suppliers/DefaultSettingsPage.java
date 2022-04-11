package com.scalepoint.automation.pageobjects.pages.suppliers;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.utils.annotations.page.EccAdminPage;
import lombok.Getter;
import org.openqa.selenium.By;

import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

@EccAdminPage
public class DefaultSettingsPage extends BaseSupplierAdminNavigation {

    @Override
    protected void ensureWeAreOnPage() {

        waitForUrl(getRelativeUrl());
        waitForAjaxCompletedAndJsRecalculation();
        $(".defaultSettings").should(Condition.visible);
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
            SelenideElement canServicePartnerUpdateSelfRisk;
            String daysBeforeReminder;
            String daysBeforeAutoApprove;

            DefaultSettingsRow(SelenideElement row){

                ElementsCollection columns = row.findAll(columnsPath);
                insuranceCompany = new InsuranceCompany(columns.get(0));
                selfRiskCollectedBy = new SelfRiskCollectedBy(columns.get(1), columns.get(2));
                paymentOfInvoice = new PaymentOfInvoice(columns.get(3), columns.get(4));
                overcollectedDeductible = new OvercollectedDeductible(columns.get(5) , columns.get(6));
                canServicePartnerUpdateSelfRisk  = columns.get(7).find("img");
                daysBeforeReminder = columns.get(8).getText();
                daysBeforeAutoApprove = columns.get(9).getText();
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

            public DefaultSettingsRow setOvercollectedDeductibleViaContent(){

                overcollectedDeductible.setViaContent();
                return this;
            }

            public DefaultSettingsRow setOvercollectedDeductibleViaInsuranceCompany(){

                overcollectedDeductible.setViaInsuranceCompany();
                return this;
            }

            public DefaultSettingsRow enableCanServicePartnerUpdateSelfRisk(){

                canServicePartnerUpdateSelfRisk.click();
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
}

