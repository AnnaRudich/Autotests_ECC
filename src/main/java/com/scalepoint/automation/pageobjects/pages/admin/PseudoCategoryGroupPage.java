package com.scalepoint.automation.pageobjects.pages.admin;

import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Select;

import static com.scalepoint.automation.utils.Wait.waitForVisible;

@EccPage
public class PseudoCategoryGroupPage extends AdminBasePage {

    @FindBy(name = "pseudoCategoryGroupList")
    private Select pseudoCategoryGroupList;

    @FindBy(id = "btnEdit")
    private Button edit;

    @FindBy(id = "btnAdd")
    private Button add;

    @FindBy(name = "pseudoCategoryGroupList")
    private Select groups;

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/Admin/pseudocategory_group.jsp";
    }

    @Override
    protected Page ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForVisible(edit);
        return this;
    }

    public boolean isGroupDisplayed(String groupName) {
        try {
            groups.selectByVisibleText(groupName);
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;
    }

    public PseudoCategoryGroupAddEditPage editGroup(String pseudoCategory){
        pseudoCategoryGroupList.selectByVisibleText(pseudoCategory);
        edit.click();
        return at(PseudoCategoryGroupAddEditPage.class);
    }

    public PseudoCategoryGroupAddEditPage toAddGroupPage(){
        add.click();
        return at(PseudoCategoryGroupAddEditPage.class);
    }


}
