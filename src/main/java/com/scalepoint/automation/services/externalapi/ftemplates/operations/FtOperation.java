package com.scalepoint.automation.services.externalapi.ftemplates.operations;

import com.scalepoint.automation.pageobjects.pages.EditFunctionTemplatePage;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class FtOperation {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    public abstract boolean hasSameState(Document document);

    public abstract void updateSetting(EditFunctionTemplatePage page);
}
