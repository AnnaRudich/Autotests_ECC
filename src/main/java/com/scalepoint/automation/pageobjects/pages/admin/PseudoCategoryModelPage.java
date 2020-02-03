package com.scalepoint.automation.pageobjects.pages.admin;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import ru.yandex.qatools.htmlelements.element.Select;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForPageLoaded;

@EccPage
public class PseudoCategoryModelPage extends AdminBasePage {

    @FindBy(id = "btnAdd")
    private WebElement addButton;

    @FindBy(id = "btnRemove")
    private WebElement removeButton;

    @FindBy(id = "btnEdit")
    private WebElement editButton;

    @FindBy(name = "pseudoCategoryModelList")
    private Select modelsList;

    @Override
    protected PseudoCategoryModelPage ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForPageLoaded();
        $(addButton).waitUntil(Condition.visible, STANDARD_WAIT_UNTIL_TIMEOUT);
        return this;
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/Admin/pseudocategory_model.jsp";
    }

    public PseudoCategoryModelAddEditPage toAddModelPage() {
        ((JavascriptExecutor) driver).executeScript("addPseudoCategoryModel();");
        Wait.waitForDisplayed(By.xpath("//input[contains(@id, 'modelname')]"));
        return at(PseudoCategoryModelAddEditPage.class);
    }

    public boolean isModelDisplayed(String model) {
        try {
            modelsList.selectByVisibleText(model);
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;
    }

    public PseudoCategoryModelAddEditPage toEditPage(String model) {
        modelsList.selectByVisibleText(model);
        ((JavascriptExecutor) driver).executeScript("editPseudoCategoryModel();");
        return at(PseudoCategoryModelAddEditPage.class);
    }

    public PseudoCategoryModelPage selectModelToRemoveAndRemove(String model) {
        modelsList.selectByVisibleText(model);
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

