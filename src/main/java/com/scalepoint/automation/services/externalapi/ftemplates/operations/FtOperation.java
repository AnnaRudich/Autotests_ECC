package com.scalepoint.automation.services.externalapi.ftemplates.operations;

import com.scalepoint.automation.pageobjects.pages.admin.EditFunctionTemplatePage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class FtOperation {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    public abstract boolean hasSameState(Document document);

    public abstract FtOperation getRollbackOperation();

    public abstract void updateSetting(EditFunctionTemplatePage page);

    public abstract boolean isOperationApplied(EditFunctionTemplatePage page);

    public abstract FTSetting getSetting();

    public enum OperationType {
        CHECKBOX,
        INPUT,
        SELECT
    }
}
