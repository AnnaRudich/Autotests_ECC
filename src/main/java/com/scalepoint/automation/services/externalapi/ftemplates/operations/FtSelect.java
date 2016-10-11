package com.scalepoint.automation.services.externalapi.ftemplates.operations;

import com.scalepoint.automation.pageobjects.pages.admin.EditFunctionTemplatePage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSettings;
import org.jsoup.nodes.Document;

public class FtSelect extends FtOperation {

    private FTSetting setting;
    private String newOptionText;
    private FtOperation rollbackOperation;

    public FtSelect(FTSetting setting, String newOptionText) {
        this.setting = setting;
        this.newOptionText = newOptionText;
    }

    @Override
    public boolean hasSameState(Document document) {
        String text = document.select(setting.getLocator()).select("option[selected]").text();
        logger.info("Setting: {} Current: {} Change to: {}", setting.name(), text, newOptionText);

        rollbackOperation = FTSettings.select(setting, text);
        return newOptionText.equals(text);
    }

    @Override
    public boolean isOperationApplied(EditFunctionTemplatePage page) {
        return page.isSettingHasSameOptionSelected(setting, newOptionText);
    }

    @Override
    public FtOperation getRollbackOperation() {
        return rollbackOperation;
    }

    @Override
    public void updateSetting(EditFunctionTemplatePage page) {
        page.selectComboBoxValue(setting, newOptionText);
    }

    @Override
    public FTSetting getSetting() {
        return setting;
    }

    @Override
    public String toString() {
        return "FtSelect{" +
                "type=" + setting.getDescription() +
                ", newOptionText='" + newOptionText + '\'' +
                '}';
    }
}
