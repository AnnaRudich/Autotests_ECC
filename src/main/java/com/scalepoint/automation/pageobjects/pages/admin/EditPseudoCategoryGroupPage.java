package com.scalepoint.automation.pageobjects.pages.admin;

import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Select;

import java.util.ArrayList;
import java.util.List;

import static com.scalepoint.automation.utils.Wait.waitForVisible;

@EccPage
public class EditPseudoCategoryGroupPage extends Page {

    @FindBy(name = "pseudoCategoryList")
    private Select pseudoCategoryList;

    @FindBy(id = "btnEdit")
    private Button editButton;

    @Override
    protected Page ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForVisible(editButton);
        return this;
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/Admin/pseudocategory_group_edit.jsp?pseudocatgroupid=";
    }

    public List<String> getAllPseudoCategories() {
        List<String> stringList = new ArrayList<>();
        List<WebElement> allCategories = pseudoCategoryList.getOptions();
        for (WebElement allCategory : allCategories) {
            String normalizedString = allCategory.getText().replaceAll("[\\s\\.:,%]", "").replaceAll("(\\[)?(.+?)(\\])?", "$2").trim();
            stringList.add(normalizedString);
        }
        return stringList;
    }
}