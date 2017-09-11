package com.scalepoint.automation.pageobjects.modules.textSearch;

import com.scalepoint.automation.utils.threadlocal.Browser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.yandex.qatools.htmlelements.element.CheckBox;
import ru.yandex.qatools.htmlelements.element.Select;

import java.util.Arrays;

public class YesNoAttributeAction implements SearchAttributesActions {

    protected Logger logger = LogManager.getLogger(getClass());

    @Override
    public void selectAttribute(TextSearchAttributesMenu.Attributes attribute) {
        yesNoSelect(attribute.getBy(), attribute.getOptions());
    }

    private void yesNoSelect(By by, String[] options) {
        WebElement initialElement = null;
        try {
            initialElement = Browser.driver().findElement(by);
        } catch (Exception e) {
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
}
