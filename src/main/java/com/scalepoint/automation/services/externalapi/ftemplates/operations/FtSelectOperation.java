package com.scalepoint.automation.services.externalapi.ftemplates.operations;

import com.scalepoint.automation.pageobjects.pages.EditFunctionTemplatePage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import org.jsoup.nodes.Document;

public class FtSelectOperation extends FtOperation {

    private FTSetting setting;
    private String newOptionText;

    public FtSelectOperation(FTSetting setting, String newOptionText) {
        this.setting = setting;
        this.newOptionText = newOptionText;
    }

    @Override
    public boolean hasSameState(Document document) {
        String text = document.select(setting.getLocator()).text();
        logger.info("Setting: {} Current: {} Change to: {}", setting.name(), text, newOptionText);
        return newOptionText.equals(text);
    }

    @Override
    public void updateSetting(EditFunctionTemplatePage page) {
        page.selectComboboxValue(setting.getDescription(), newOptionText);
    }

    @Override
    public String toString() {
        return "FtSelectOperation{" +
                "setting=" + setting.getDescription() +
                ", newOptionText='" + newOptionText + '\'' +
                '}';
    }
}
