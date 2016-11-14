package com.scalepoint.automation.pageobjects.pages.admin;

import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Select;

import static com.scalepoint.automation.utils.Wait.waitForVisible;

/**
 * Created by asa on 11/14/2016.
 */
@EccPage
public class EditReasonsPage extends Page {
    @FindBy(id = "reason_type_dropdown")
    private Select reasonTypes;

    @FindBy(id = "company_list_dropdown")
    private Select companies;

    @FindBy(xpath = "//button[contains(text(), 'Refresh')]")
    private Button refresh;

    @FindBy(id = "editNotLowestChoiceReasonsForm")
    private WebElement editReasonsForm;

    @Override
    protected Page ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForVisible(reasonTypes);
        return this;
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/Admin/edit_reasons.jsp";
    }

    public EditReasonsPage selectCompany(String insuranceCompany){
        try {
            companies.selectByVisibleText(insuranceCompany);
        } catch (NoSuchElementException e) {

        }
        return this;
    }

    public EditReasonsPage selectReasonType (String visibleText) {
        try {
            reasonTypes.selectByVisibleText(visibleText);
        } catch (NoSuchElementException e) {
        }
        return this;
    }

    public EditReasonsPage refresh(){
        refresh.click();
        return this;
    }

    public boolean isEditReasonsFormVisible(){
        editReasonsForm.isDisplayed();
        return true;
    }

}
