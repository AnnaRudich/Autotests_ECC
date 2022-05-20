package com.scalepoint.automation.pageobjects.pages.testWidget;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.pageobjects.pages.selfService2.SelfService2Page;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.data.entity.input.PseudoCategory;

import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;

public class TestWidgetPage extends SelfService2Page {

    public TestWidgetPage saveItem() {

        saveItem.click();
        Wait.waitForSpinnerToDisappear();

        TestWidgetPage testWidgetPage;

        try {

            testWidgetPage = TestWidgetPage.class.getDeclaredConstructor().newInstance();
        } catch (Exception e) {

            throw new RuntimeException(e);
        }
        return testWidgetPage;
    }

    public TestWidgetPage addDescriptionWithOutSuggestions(String description){

        super.addDescriptionWithOutSuggestions(description);
        return this;
    }

    public TestWidgetPage selectPurchaseYear(String year){

        super.selectPurchaseYear(year);
        return this;
    }

    public TestWidgetPage selectPurchaseMonth(String month){

        super.selectPurchaseMonth(month);
        return this;
    }

    public TestWidgetPage addNewPrice(Double price){

        super.addNewPrice(price);
        return this;
    }

    public TestWidgetPage selectCategory(PseudoCategory pseudoCategory){

        super.selectCategory(pseudoCategory);
        return this;
    }

    public SelfService2Page sendResponseToEcc() {

        $(SEND_BUTTON_PATH).should(Condition.visible).click();
        return this;
    }

    public TestWidgetPage doAssert(Consumer<SelfService2Page.Asserts> assertFunc) {

        super.doAssert(assertFunc);
        return this;
    }
}
