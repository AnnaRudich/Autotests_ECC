package com.scalepoint.automation.pageobjects.pages.admin;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.annotations.page.RequiredParameters;
import com.scalepoint.automation.utils.data.entity.input.GenericItem;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.CheckBox;
import ru.yandex.qatools.htmlelements.element.Select;
import ru.yandex.qatools.htmlelements.element.TextInput;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

@EccPage
@RequiredParameters("shrfnbr=%s")
public class GenericItemsEditAdminPage extends AdminBasePage {

    @FindBy(id = "btnOk")
    private SelenideElement saveOption;

    private CheckBox getPublishedCheckBox(){

        return new CheckBox($(By.name("ispublished")));
    }

    private Select getCategoryGroup(){

        return new Select($(By.id("categorygroup")));
    }

    private Select getCategory(){

        return new Select($(By.id("category")));
    }

    private Select getIc(){

        return new Select($(By.name("icompanyid")));
    }

    private TextInput getDescriptionField(){

        return new TextInput($(By.xpath(".//*[contains(@id,'itemdescription_')]")));
    }

    private TextInput getPriceField(){

        return new TextInput($(By.name("defaultprice")));
    }

    @Override
    protected void ensureWeAreOnPage() {

        waitForUrl(getRelativeUrl());
        waitForAjaxCompletedAndJsRecalculation();
        $(getDescriptionField()).should(Condition.visible);
    }

    @Override
    protected String getRelativeUrl() {

        return "webshop/jsp/Admin/generic_items_edit.jsp";
    }

    public void save() {

        hoverAndClick(saveOption);
    }

    public GenericItemsEditAdminPage selectGroup(String groupName) {

        getCategoryGroup().selectByVisibleText(groupName);
        return this;
    }

    public GenericItemsEditAdminPage selectCategory(String categoryName) {

        getCategory().selectByVisibleText(categoryName);
        return this;
    }

    public GenericItemsEditAdminPage selectCompany(String companyName) {

        getIc().selectByVisibleText(companyName);
        return this;
    }

    public GenericItemsEditAdminPage setDescription(String description) {

        SelenideElement element = $(getDescriptionField());
        element.clear();
        element.sendKeys(description);
        return this;
    }

    public GenericItemsEditAdminPage addPrice(String price) {

        SelenideElement element = $(getPriceField());
        element.clear();
        element.sendKeys(price);
        return this;
    }

    public GenericItemsAdminPage addNewGenericItem(GenericItem genericItem, String company, boolean published) {

        selectGroup(genericItem.getGroup()).
                selectCategory(genericItem.getCategory()).
                selectCompany(company).
                setDescription(genericItem.getName()).
                addPrice(genericItem.getPrice()).
                publish(published).
                save();
        return at(GenericItemsAdminPage.class);
    }

    public GenericItemsEditAdminPage publish(boolean published) {

        if (published && !isSelected(getPublishedCheckBox()) ||
                !published && isSelected(getPublishedCheckBox())) {

            getPublishedCheckBox().click();
        }
        return this;
    }
}
