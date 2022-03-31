package com.scalepoint.automation.pageobjects.pages.admin;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Select;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

@EccPage
public class PseudoCategoryGroupMovePage extends AdminBasePage {

    @FindBy(id = "btnOk")
    private SelenideElement okButton;

    private Select getGroups(){

        return new Select($(By.name("newpseudocatgroup")));
    }

    @Override
    protected void ensureWeAreOnPage() {

        waitForUrl(getRelativeUrl());
        waitForAjaxCompletedAndJsRecalculation();
        okButton.should(Condition.visible);
    }

    @Override
    protected String getRelativeUrl() {

        return "webshop/jsp/Admin/pseudocategory_group_move.jsp";
    }

    public PseudoCategoryGroupAddEditPage moveToGroup(String groupName) {

        getGroups().selectByVisibleText(groupName);
        okButton.click();
        return at(PseudoCategoryGroupAddEditPage.class);
    }
}
