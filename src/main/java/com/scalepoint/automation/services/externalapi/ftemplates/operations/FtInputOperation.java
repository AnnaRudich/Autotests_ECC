package com.scalepoint.automation.services.externalapi.ftemplates.operations;

import com.scalepoint.automation.pageobjects.pages.EditFunctionTemplatePage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import org.jsoup.nodes.Document;

public class FtInputOperation extends FtOperation {

    private FTSetting setting;
    private String newValue;

    public FtInputOperation(FTSetting setting, String newValue) {
        this.setting = setting;
        this.newValue = newValue;
    }

    @Override
    public boolean hasSameState(Document document) {
        String currentValue = document.select(setting.getLocator()).val();
        logger.info("Setting: {} Current: {} Change to: {}", setting.name(), currentValue, newValue);
        return currentValue.equals(newValue);
    }

    @Override
    public void updateSetting(EditFunctionTemplatePage page) {
        page.updateValue(setting.getDescription(), newValue);
    }

    @Override
    public String toString() {
        return "FtInputOperation{" +
                "setting=" + setting.getDescription() +
                ", newValue='" + newValue + '\'' +
                '}';
    }
}
