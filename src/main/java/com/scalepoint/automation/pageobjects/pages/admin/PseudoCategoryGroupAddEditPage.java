package com.scalepoint.automation.pageobjects.pages.admin;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.annotations.page.RequiredParameters;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Select;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

@EccPage
@RequiredParameters("pseudocatgroupid=%s")
public class PseudoCategoryGroupAddEditPage extends AdminBasePage {

    @FindBy(xpath = "//input[contains(@id, 'pcgroupname')]")
    private SelenideElement groupNameField;

    private Select getPseudoCategoryList(){

        return new Select($(By.name("pseudoCategoryList")));
    }

    private Button getEditButton(){

        return new Button($(By.id("btnEdit")));
    }

    private Button getSaveButton(){

        return new Button($(By.id("btnOk")));
    }

    private Button getMoveCategoryButton(){

        return new Button($(By.id("btnMove")));
    }

    @Override
    protected void ensureWeAreOnPage() {

        waitForUrl(getRelativeUrl());
        waitForAjaxCompletedAndJsRecalculation();
        groupNameField.should(Condition.visible);
    }

    @Override
    protected String getRelativeUrl() {

        return "webshop/jsp/Admin/pseudocategory_group_edit.jsp";
    }

    public List<String> getAllPseudoCategories() {

        List<String> stringList = new ArrayList<>();
        List<WebElement> allCategories = getPseudoCategoryList().getOptions();

        for (WebElement allCategory : allCategories) {

            String normalizedString = allCategory.getText()
                    .replaceAll("[\\s\\.:,%]", "")
                    .replaceAll("(\\[)?(.+?)(\\])?", "$2")
                    .trim();
            stringList.add(normalizedString);
        }
        return stringList;
    }

    public PseudoCategoryGroupPage addGroup(String groupName) {

        groupNameField.sendKeys(groupName);
        getSaveButton().click();
        return at(PseudoCategoryGroupPage.class);
    }

    public PseudoCategoryGroupPage updateNameAndSave(String groupName) {

        groupNameField.clear();
        groupNameField.sendKeys(groupName);
        getSaveButton().click();
        return at(PseudoCategoryGroupPage.class);
    }

    public PseudoCategoryGroupMovePage toMoveToGroupPage(String categoryName) {

        getPseudoCategoryList().selectByVisibleText(categoryName);
        getMoveCategoryButton().click();
        return at(PseudoCategoryGroupMovePage.class);
    }

    public boolean isCategoryDisplayed(String categoryName) {

        try {

            getPseudoCategoryList().selectByVisibleText(categoryName);
        } catch (NoSuchElementException e) {

            return false;
        }
        return true;
    }

    public PseudoCategoryGroupAddEditPage assertGroupDisplayed(String categoryName) {

        Assert.assertTrue(isCategoryDisplayed(categoryName));
        return this;
    }
}
