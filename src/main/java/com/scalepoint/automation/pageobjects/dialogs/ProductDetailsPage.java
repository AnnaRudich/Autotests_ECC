package com.scalepoint.automation.pageobjects.dialogs;

import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.TextSearchPage;
import com.scalepoint.automation.utils.OperationalUtils;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.TextBlock;

@EccPage
public class ProductDetailsPage extends Page {

    @FindBy(id = "luk")
    private Button closeWindow;

    @FindBy(id = "market_price")
    private TextBlock marketPriceValue;

    @FindBy(id = "market_price_supplier")
    private TextBlock marketPriceSupplier;

    @Override
    protected Page ensureWeAreOnPage() {
        switchToLast();
        waitForUrl(getRelativeUrl());
        return this;
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/matching_engine/product_details_popup.jsp";
    }

    public TextSearchPage closeWindow(){
        closeDialog(closeWindow);
        return at(TextSearchPage.class);
    }

    public boolean isMarketPriceSupplierVisible(){
        return marketPriceSupplier.exists();
    }

    public boolean isMarketPriceVisible(){
        return marketPriceValue.exists();
    }

    public Double marketPriceValue(){
        return OperationalUtils.getDoubleValue(marketPriceValue.getText());
    }

    public String marketPriceSupplier(){
        return marketPriceSupplier.getText();
    }
}
