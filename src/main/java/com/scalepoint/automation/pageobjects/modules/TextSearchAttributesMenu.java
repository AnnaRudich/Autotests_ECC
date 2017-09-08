package com.scalepoint.automation.pageobjects.modules;

import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.TextSearchPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.CheckBox;
import ru.yandex.qatools.htmlelements.element.Select;

import java.util.Arrays;

import static com.scalepoint.automation.utils.Wait.forCondition;

public class TextSearchAttributesMenu extends Module {

    @FindBy(id = "attSearchButton")
    private Button attributeSearchButton;

    private void yesNoSelect(By by, String[] options) {
        WebElement initialElement = null;
        try{
            initialElement = driver.findElement(by);
        } catch (Exception e){
            logger.error(e.getMessage());
        }

        assert initialElement != null;
        
        CheckBox checkBox = new CheckBox(initialElement.findElement(By.xpath("./parent::td/input")));
        if (!checkBox.isSelected()) {
            checkBox.select();
        }
        Select selectYesNo = new Select(initialElement.findElement(By.xpath("../parent::tr/td[@class='blueBorder']/select[1]")));
        Select comparision = new Select(initialElement.findElement(By.xpath("../parent::tr/td[@class='blueBorder']/select[2]")));
        Arrays.stream(options).forEach(option -> chooseOptionFromSelects(option, selectYesNo, comparision));
    }

    private void chooseOptionFromSelects(String option, Select... selects) {
        Arrays.stream(selects).forEach(select -> chooseOptionFromSelect(select, option));
    }

    private void chooseOptionFromSelect(Select select, String option) {
        try {
            select.selectByVisibleText(option);
            logger.info("Selecting option: " + option);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }

    private void selectAttribute(Attributes attributes) {
        switch (attributes.getType()) {
            case YES_NO_SELECT:
                yesNoSelect(attributes.getBy(), attributes.getOptions());
                break;
            default:
                throw new NotImplementedException("Can't find action for attribute type");
        }
    }

    public TextSearchAttributesMenu selectAttribute(Attributes... attributes) {
        Arrays.stream(attributes).forEach(this::selectAttribute);
        return this;
    }

    public TextSearchPage searchAttributes(){
        forCondition(ExpectedConditions.elementToBeClickable(attributeSearchButton)).click();
        return Page.at(TextSearchPage.class);
    }

    private enum Type {
        YES_NO_SELECT
    }

    public enum Attributes {
        SMARTPHONE_NEJ("Smartphone", Type.YES_NO_SELECT, "Nej", "og kun denne"),
        GPS_NEJ("GPS", Type.YES_NO_SELECT, "Nej", "og kun denne");

        private String name;
        private By by;
        private Type type;
        private String[] options;


        Attributes(String name, Type type, String... options) {
            this.name = name;
            this.by = By.xpath(String.format("//span[text()='%s']", name));
            this.type = type;
            this.options = options;
        }

        public String getName() {
            return name;
        }

        public By getBy() {
            return by;
        }

        public Type getType() {
            return type;
        }

        public String[] getOptions() {
            return options;
        }
    }

}
