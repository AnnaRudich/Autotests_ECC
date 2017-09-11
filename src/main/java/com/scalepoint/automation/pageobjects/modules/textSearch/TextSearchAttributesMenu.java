package com.scalepoint.automation.pageobjects.modules.textSearch;

import com.scalepoint.automation.pageobjects.modules.Module;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.TextSearchPage;
import org.openqa.selenium.By;
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

    public enum Attributes {
        SMARTPHONE_NEJ("Smartphone", new YesNoAttributeAction(), "Nej", "og kun denne"),
        GPS_NEJ("GPS", new YesNoAttributeAction(), "Nej", "og kun denne");

        private String name;
        private By by;
        private SearchAttributesActions action;
        private String[] options;


        Attributes(String name, SearchAttributesActions action, String... options) {
            this.name = name;
            this.by = By.xpath(String.format("//span[text()='%s']", name));
            this.action = action;
            this.options = options;
        }

        public String getName() {
            return name;
        }

        public By getBy() {
            return by;
        }

        public SearchAttributesActions getAction() {
            return action;
        }

        public String[] getOptions() {
            return options;
        }

        public void selectAttribute() {
            action.selectAttribute(this);
        }

    }

}
