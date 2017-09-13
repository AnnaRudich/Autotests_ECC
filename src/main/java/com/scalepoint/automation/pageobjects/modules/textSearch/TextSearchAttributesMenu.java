package com.scalepoint.automation.pageobjects.modules.textSearch;

import com.scalepoint.automation.pageobjects.modules.Module;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.TextSearchPage;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import ru.yandex.qatools.htmlelements.element.Button;

import java.util.Arrays;

import static com.scalepoint.automation.utils.Wait.forCondition;

public class TextSearchAttributesMenu extends Module {

    @FindBy(id = "attSearchButton")
    private Button attributeSearchButton;

    public TextSearchAttributesMenu selectAttribute(Attributes... attributes) {
        Arrays.stream(attributes).forEach(Attributes::selectAttribute);
        return this;
    }

    public TextSearchPage searchAttributes() {
        forCondition(ExpectedConditions.elementToBeClickable(attributeSearchButton)).click();
        return Page.at(TextSearchPage.class);
    }

}
