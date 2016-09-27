package com.scalepoint.automation.pageobjects.pages;

import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.TextBlock;

@EccPage
public class BestFitPage extends Page {

    @FindBy(xpath = "//td[contains(@class,'bestfitleftheader_bottom') and contains(text(),'Markedspris')]")
    private TextBlock marketPrice;

    @FindBy(xpath = "//td[contains(@class,'bestfitleftheader_bottom') and contains(text(),'Markedspris')]/ following-sibling::td[1]")
    private TextBlock marketPriceValue;

    @Override
    protected Page ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        Wait.waitForElement(By.id("table1"));
        return null;
    }

    @Override
    protected String getRelativeUrl() {
        return "BestFit2";
    }

    public boolean isMarketPriceVisible() {
        return marketPrice.exists();
    }
}
