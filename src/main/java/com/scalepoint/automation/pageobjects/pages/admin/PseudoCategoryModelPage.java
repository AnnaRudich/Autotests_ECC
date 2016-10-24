package com.scalepoint.automation.pageobjects.pages.admin;

import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Select;

@EccPage
public class PseudoCategoryModelPage extends Page {

    @FindBy(id = "btnAdd")
    private WebElement addButton;

    @FindBy(id = "btnRemove")
    private WebElement removeButton;

    @FindBy(id = "btnEdit")
    private WebElement editButton;

    @FindBy(name = "pseudoCategoryModelList")
    private Select modelsList;

    @Override
    protected Page ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        Wait.waitForVisible(addButton);
        return this;
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/Admin/pseudocategory_model.jsp";
    }

    public PseudoCategoryModelAddEditPage toAddPage() {
        clickAndWaitForDisplaying(addButton, By.xpath("//input[contains(@id, 'modelname')]"));
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
        editButton.click();
        return at(PseudoCategoryModelAddEditPage.class);
    }

    public PseudoCategoryModelPage selectModelToRemoveAndRemove(String model) {
        modelsList.selectByVisibleText(model);
        removeButton.click();
        return at(PseudoCategoryModelPage.class);
    }
}

