package com.scalepoint.automation.services.externalapi.ftemplates.operations;

import com.scalepoint.automation.pageobjects.pages.EditFunctionTemplatePage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import org.jsoup.nodes.Document;

public class FtCheckBoxOperation extends FtOperation {

    private FTSetting setting;
    private OperationType operationType;

    public FtCheckBoxOperation(FTSetting setting, OperationType operationType) {
        this.setting = setting;
        this.operationType = operationType;
    }

    @Override
    public boolean hasSameState(Document document) {
        boolean checked = document.select(setting.getLocator()).hasAttr("checked");
        logger.info("Setting: {} Current: {} Change to: {}", setting.name(), checked, (operationType == OperationType.ENABLE));
        return (operationType == OperationType.ENABLE) == checked;
    }

    @Override
    public void updateSetting(EditFunctionTemplatePage page) {
        if (operationType == OperationType.ENABLE) {
            page.enableFeature(setting.getDescription());
        } else {
            page.disableFeature(setting.getDescription());
        }
    }

    public static enum OperationType {
        ENABLE,
        DISABLE,
    }

    @Override
    public String toString() {
        return "FtCheckBoxOperation {" +
                "setting=" + setting.getDescription() +
                ", operationType=" + operationType +
                '}';
    }
}
