package com.scalepoint.automation.pageobjects.pages.admin;

import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@EccPage
public class PseudoCategoryModelAddEditPage extends AdminBasePage {

    @FindBy(xpath = "//input[contains(@id, 'modelname')]")
    private WebElement modelNameField;

    @FindBy(id = "btnOk")
    private WebElement saveOption;

    private String byCategoryNameXpath = "//div/label[contains(.,'$1')]/input";

    @Override
    protected Page ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        Wait.waitForVisible(saveOption);
        return null;
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/Admin/pseudocategory_model_edit.jsp";
    }

    public PseudoCategoryModelPage updateNameAndSave(String model) {
        modelNameField.sendKeys(model);
        return save();
    }

    public PseudoCategoryModelPage save() {
        clickUsingJsIfSeleniumClickReturnError(saveOption);
        return at(PseudoCategoryModelPage.class);
    }

    public PseudoCategoryModelPage updateNewModelNameAndSave(String model) {
        modelNameField.clear();
        modelNameField.sendKeys(model);
        return save();
    }

    public PseudoCategoryModelAddEditPage selectCategory(String catName) {
        WebElement option = find(byCategoryNameXpath, catName);
        scrollTo(option);
        By xpath = By.xpath(byCategoryNameXpath.replace("$1", catName));
        Wait.waitForStaleElement(xpath);
        if (!option.isSelected()) {
            option.click();
        }
        return this;
    }
}
