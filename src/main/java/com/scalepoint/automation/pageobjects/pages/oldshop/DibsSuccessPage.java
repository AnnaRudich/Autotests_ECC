package com.scalepoint.automation.pageobjects.pages.oldshop;

import com.scalepoint.automation.pageobjects.pages.CustomerDetailsPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Link;

@EccPage
public class DibsSuccessPage extends Page {

    @FindBy(xpath = "//a[contains(@onclick, 'logout')]")
    private Link logout;

    @Override
    protected Page ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        return this;
    }

    @Override
    protected String getRelativeUrl() {
        return "shop/OrderProcessSuccess";
    }

    public CustomerDetailsPage toCustomerDetails() {
        logout.click();
        return at(CustomerDetailsPage.class);
    }
}
