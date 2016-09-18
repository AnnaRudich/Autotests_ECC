package com.scalepoint.automation.services.externalapi.ftemplates.operations;

import com.scalepoint.automation.pageobjects.pages.EditFunctionTemplatePage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSettings;
import org.jsoup.nodes.Document;

public class FtCheckbox extends FtOperation {

    private FTSetting setting;
    private OperationType operationType;
    private FtOperation rollbackOperation;

    public FtCheckbox(FTSetting setting, OperationType operationType) {
        this.setting = setting;
        this.operationType = operationType;
    }

    @Override
    public boolean hasSameState(Document document) {
        boolean checked = document.select(setting.getLocator()).hasAttr("checked");
        logger.info("Setting: {} Current: {} Change to: {}", setting.name(), checked, (operationType == OperationType.ENABLE));

        rollbackOperation = checked ? FTSettings.enable(setting) : FTSettings.disable(setting);
        return (operationType == OperationType.ENABLE) == checked;
    }

    @Override
    public FtOperation getRollbackOperation() {
        return rollbackOperation;
    }

    @Override
    public void updateSetting(EditFunctionTemplatePage page) {
        if (operationType == OperationType.ENABLE) {
            page.enableFeature(setting.getDescription());
        } else {
            page.disableFeature(setting.getDescription());
        }
    }

    public enum OperationType {
        ENABLE,
        DISABLE,
    }

    @Override
    public String toString() {
        return "FtCheckbox {" +
                "type=" + setting.getDescription() +
                ", operationType=" + operationType +
                '}';
    }
}
