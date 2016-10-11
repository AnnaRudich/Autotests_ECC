package com.scalepoint.automation.services.externalapi.ftemplates.operations;

import com.scalepoint.automation.pageobjects.pages.admin.EditFunctionTemplatePage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSettings;
import org.jsoup.nodes.Document;

public class FtTextField extends FtOperation {

    private FTSetting setting;
    private String newValue;
    private FtOperation rollbackOperation;

    public FtTextField(FTSetting setting, String newValue) {
        this.setting = setting;
        this.newValue = newValue;
    }

    @Override
    public boolean hasSameState(Document document) {
        String currentValue = document.select(setting.getLocator()).val();
        logger.info("Setting: {} Current: {} Change to: {}", setting.name(), currentValue, newValue);

        rollbackOperation = FTSettings.setValue(setting, currentValue);
        return currentValue.equals(newValue);
    }

    @Override
    public boolean isOperationApplied(EditFunctionTemplatePage page) {
        return page.isSettingHasSameValue(setting, newValue);
    }

    @Override
    public FtOperation getRollbackOperation() {
        return rollbackOperation;
    }

    @Override
    public void updateSetting(EditFunctionTemplatePage page) {
        page.updateValue(setting, newValue);
    }

    @Override
    public FTSetting getSetting() {
        return setting;
    }

    @Override
    public String toString() {
        return "FtTextField{" +
                "type=" + setting.getDescription() +
                ", newValue='" + newValue + '\'' +
                '}';
    }
}
