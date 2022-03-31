package com.scalepoint.automation.pageobjects.pages.admin;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import ru.yandex.qatools.htmlelements.element.Select;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.verifyElementVisible;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

@EccPage
public class PseudoCategoryModelPage extends AdminBasePage {

    @FindBy(id = "btnAdd")
    private SelenideElement addButton;
    @FindBy(id = "btnRemove")
    private SelenideElement removeButton;
    @FindBy(id = "btnEdit")
    private SelenideElement editButton;

    private Select getModelsList(){

        return new Select($(By.name("pseudoCategoryModelList")));
    }

    @Override
    protected void ensureWeAreOnPage() {

        waitForUrl(getRelativeUrl());
        waitForAjaxCompletedAndJsRecalculation();
        addButton.should(Condition.visible);
    }

    @Override
    protected String getRelativeUrl() {

        return "webshop/jsp/Admin/pseudocategory_model.jsp";
    }

    public PseudoCategoryModelAddEditPage toAddModelPage() {

        ((JavascriptExecutor) driver).executeScript("addPseudoCategoryModel();");
        $(By.xpath("//input[contains(@id, 'modelname')]")).should(Condition.visible);
        return at(PseudoCategoryModelAddEditPage.class);
    }

    public boolean isModelDisplayed(String model) {

        try {

            getModelsList().selectByVisibleText(model);
        } catch (NoSuchElementException e) {

            return false;
        }
        return true;
    }

    public PseudoCategoryModelAddEditPage toEditPage(String model) {

        getModelsList().selectByVisibleText(model);
        ((JavascriptExecutor) driver).executeScript("editPseudoCategoryModel();");
        return at(PseudoCategoryModelAddEditPage.class);
    }

    public PseudoCategoryModelPage selectModelToRemoveAndRemove(String model) {

        getModelsList().selectByVisibleText(model);
        removeButton.click();
        return at(PseudoCategoryModelPage.class);
    }

    public PseudoCategoryModelPage assertModelDisplayed(String modelName) {

        Assert.assertTrue(isModelDisplayed(modelName));
        return this;
    }

    public PseudoCategoryModelPage assertModelNotDisplayed(String modelName) {

        Assert.assertFalse(isModelDisplayed(modelName));
        return this;
    }
}

