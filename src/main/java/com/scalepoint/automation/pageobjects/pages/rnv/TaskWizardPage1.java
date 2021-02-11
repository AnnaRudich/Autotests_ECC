package com.scalepoint.automation.pageobjects.pages.rnv;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.annotations.page.RVPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.NoSuchElementException;

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
            description = serviceLines.get(2);
            damageType = serviceLines.get(4);
            taskType = serviceLines.get(5);
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
}

