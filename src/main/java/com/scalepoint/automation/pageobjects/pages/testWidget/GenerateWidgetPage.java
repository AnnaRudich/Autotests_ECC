package com.scalepoint.automation.pageobjects.pages.testWidget;

import com.scalepoint.automation.pageobjects.pages.Page;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

public class GenerateWidgetPage extends Page {

    private String SERVER_PATH = "#server";
    private String COUNTRY_PATH = "#country";
    private String CASE_TOKEN_PATH = "#caseToken";
    private String GENERATE_WIDGET_PATH = "#testWidget";

    @Override
    protected void ensureWeAreOnPage() {

        waitForUrl(getRelativeUrl());
        waitForAjaxCompletedAndJsRecalculation();
    }

    @Override
    protected String getRelativeUrl() {

        return "widget";
    }

    public GenerateWidgetPage setServer(String server){

        $(SERVER_PATH).setValue(server);
        return this;
    }

    public GenerateWidgetPage setCountry(String country){

        $(COUNTRY_PATH).setValue(country);
        return this;
    }

    public GenerateWidgetPage setCaseToken(String caseToken){

        $(CASE_TOKEN_PATH).setValue(caseToken);
        return this;
    }

    public TestWidgetPage generateWidget(){

        $(GENERATE_WIDGET_PATH).click();

        TestWidgetPage testWidgetPage;

        try {

            testWidgetPage = page(TestWidgetPage.class);
        } catch (Exception e) {

            throw new RuntimeException(e);
        }
        return testWidgetPage;
    }
}
