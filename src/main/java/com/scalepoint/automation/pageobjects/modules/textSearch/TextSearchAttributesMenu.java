package com.scalepoint.automation.pageobjects.modules.textSearch;

import com.scalepoint.automation.pageobjects.modules.Module;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.TextSearchPage;
import org.openqa.selenium.By;
import ru.yandex.qatools.htmlelements.element.Button;

import java.util.Arrays;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForVisibleAndEnabled;

public class TextSearchAttributesMenu extends Module {

    private Button getAttributeSearchButton(){

        return new Button($(By.id("attSearchButton")));
    }

    public TextSearchAttributesMenu selectAttribute(Attributes... attributes) {

        Arrays.stream(attributes).forEach(Attributes::selectAttribute);
        return this;
    }

    public TextSearchPage searchAttributes() {

        waitForVisibleAndEnabled($(getAttributeSearchButton())).click();
        return Page.at(TextSearchPage.class);
    }

}
