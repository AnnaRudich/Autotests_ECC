package com.scalepoint.automation.pageobjects.dialogs;

import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.Window;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.TextBlock;

@EccPage
public class ProductDetailsPage extends Page {

    private static final String URL = "webshop/jsp/matching_engine/product_details_popup.jsp";

    @FindBy(id = "luk")
    private Button closeWindow;

    @FindBy(id = "")
    private TextBlock marketPriceValue;

    @FindBy(id = "market_price_supplier")
    private TextBlock marketPriceSupplier;

    @Override
    protected Page ensureWeAreOnPage() {
        Window.get().switchToLast();
        waitForUrl(URL);
        return this;
    }

    @Override
    protected String getRelativeUrl() {
        return URL;
    }

    public void closeWindow(){
        Window.get().closeDialog(closeWindow);
    }

    public boolean isMarketPriceSupplierVisible(){
        return marketPriceSupplier.exists();
    }

    public boolean isMarketPriceVisible(){
        return marketPriceValue.exists();
    }

    public Double marketPriceValue(){
        return getDoubleValue(marketPriceValue.getText());
    }

    public String marketPriceSupplier(){
        return marketPriceSupplier.getText();
    }

    public static double getDoubleValue(String input) {
        String[] array = input.split(" ");
        return Double.parseDouble((array[array.length-1]).replaceAll("\\.", "").replace(",", "."));
    }
}
