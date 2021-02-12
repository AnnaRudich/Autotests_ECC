package com.scalepoint.automation.pageobjects.pages.rnv;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.annotations.page.RVPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;
import static com.scalepoint.automation.utils.Wait.waitForPageLoaded;

@RVPage
public class TaskWizardPage1 extends Page {

    @FindBy(css = "input[name='name']")
    private WebElement nameField;
    @FindBy(css = "#button-next")
    private WebElement nextBtn;


    @Override
    protected void ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForAjaxCompletedAndJsRecalculation();
        waitForPageLoaded();
        $(nameField).waitUntil(visible, TIME_OUT_IN_MILISECONDS);
    }

    @Override
    protected String getRelativeUrl() {
        return "/?orderToken";
    }

    public TaskWizardPage2 nextRnVstep() {
        hoverAndClick($(nextBtn));
        return at(TaskWizardPage2.class);
    }

    public TaskWizardPage1 selectRnvType(String lineDescription, String rnvType) {
        getServiceLineByDescription(lineDescription).clickTaskType();
        waitForAjaxCompletedAndJsRecalculation();
        selectValue(rnvType);
        return this;
    }

    public TaskWizardPage1 selectDamageType(String lineDescription, String damageType) {
        getServiceLineByDescription(lineDescription).clickDamageType();
        waitForAjaxCompletedAndJsRecalculation();
        selectValue(damageType);
        return this;
    }

    private void selectValue(String valueToSelect) {
        $$(".x-boundlist")
                .findBy(visible)
                .findAll("[role=option]")
                .findBy(text(valueToSelect))
                .click();
    }

    private List<ServiceLineHeader> getHeaders(){

        return $$("#serviceLineListId .x-grid-header-ct span")
                .stream()
                .map(element -> ServiceLineHeader.findByText(element.getText()))
                .collect(Collectors.toList());
    }

    public ServiceLine getServiceLineByDescription(String description){

        return $$("#serviceLineListId-body table [role=row]")
                .stream()
                .map(ServiceLine::new)
                .filter(serviceLine -> serviceLine.getDescription().equals(description))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

    class ServiceLine{

        private SelenideElement description;
        private SelenideElement damageType;
        private SelenideElement taskType;

        public ServiceLine(SelenideElement serviceLine){
            ElementsCollection serviceLines = serviceLine.findAll(By.cssSelector("td"));
            List<ServiceLineHeader> headers = getHeaders();
            description = serviceLines.get(headers.indexOf(ServiceLineHeader.DESCRIPTION));
            damageType = serviceLines.get(headers.indexOf(ServiceLineHeader.DAMAGE_TYPE));
            taskType = serviceLines.get(headers.indexOf(ServiceLineHeader.TASK_TYPE));
        }

        public String getDescription(){

            return description.getText();
        }

        public ServiceLine clickDamageType() {
            hoverAndClick(damageType);
            return this;
        }

        public ServiceLine clickTaskType() {
            hoverAndClick(taskType);
            return this;
        }
    }

    public enum ServiceLineHeader {

        DESCRIPTION("Beskrivelse"),
        CATEGORY("Kategori"),
        DAMAGE_TYPE("Skadetype"),
        TASK_TYPE("Opgavetype"),
        UNKNOWN("Unknown");

        private String text;

        ServiceLineHeader(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public static ServiceLineHeader findByText(String text){

            return Arrays.stream(ServiceLineHeader.values())
                    .filter(serviceLineHeader -> serviceLineHeader.getText().equals(text))
                    .findFirst()
                    .orElse(UNKNOWN);
        }
    }
}

