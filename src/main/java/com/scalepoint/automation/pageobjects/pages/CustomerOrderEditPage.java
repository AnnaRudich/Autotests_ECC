package com.scalepoint.automation.pageobjects.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.dialogs.AddInternalNoteDialog;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.threadlocal.Window;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

@EccPage
public class CustomerOrderEditPage extends BaseClaimPage {

    @FindBy(name = "cancelButton")
    private SelenideElement cancelButton;

    @Override
    protected void ensureWeAreOnPage() {

        waitForUrl(getRelativeUrl());
        waitForAjaxCompletedAndJsRecalculation();
        cancelButton.shouldHave(Condition.attribute("disabled", "true"));
    }

    @Override
    protected String getRelativeUrl() {

        return "webshop/jsp/matching_engine/customer_order_edit.jsp";
    }

    private AddInternalNoteDialog cancelItem(Suborders.Suborder suborder){

        suborder.setCheckBox(true);
        return cancel();
    }

    public AddInternalNoteDialog cancelItemByDescription(String itemDescription){

        return cancelItem(new Suborders().getSuborderByDescription(itemDescription));
    }

    public AddInternalNoteDialog cancelItemByIndex(int index){

        return cancelItem(new Suborders().getSuborderByIndex(index));
    }

    public AddInternalNoteDialog cancelAllItems(){

        new Suborders()
                .subordersList
                .forEach(suborder -> suborder.setCheckBox(true));
        return cancel();
    }

    private AddInternalNoteDialog cancel(){

        cancelButton.click();
        getAlertTextAndAccept();
        Window.get().switchToLast();
        return BaseDialog.at(AddInternalNoteDialog.class);
    }

    public class Suborders{

        List<Suborder> subordersList;

        Suborders(){

            subordersList = $$(".suborder-table")
                    .stream()
                    .map(Suborder::new)
                    .collect(Collectors.toList());
        }

        Suborder getSuborderByDescription(String itemDescription){

            return subordersList
                    .stream()
                    .filter(suborder -> StringUtils.containsIgnoreCase(suborder.itemDescription, itemDescription))
                    .findFirst()
                    .orElseThrow(NoSuchElementException::new);
        }

        Suborder getSuborderByIndex(int index){

            return subordersList.get(0);
        }

        public class Suborder{

            String itemDescription;
            SelenideElement checkbox;

            Suborder(SelenideElement element){

                checkbox = element.find("input[type=\"checkbox\"]");
                itemDescription = element.find("#productLink").getText();
            }

            public Suborder setCheckBox(boolean status){

                checkbox.setSelected(status);
                return this;
            }

            public class Asserts {

            }
        }
    }
}
