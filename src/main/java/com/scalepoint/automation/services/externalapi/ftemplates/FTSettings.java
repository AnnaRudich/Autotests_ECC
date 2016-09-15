package com.scalepoint.automation.services.externalapi.ftemplates;

import com.scalepoint.automation.services.externalapi.ftemplates.operations.FtCheckbox;
import com.scalepoint.automation.services.externalapi.ftemplates.operations.FtOperation;
import com.scalepoint.automation.services.externalapi.ftemplates.operations.FtSelect;
import com.scalepoint.automation.services.externalapi.ftemplates.operations.FtTextField;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class FTSettings {

    private static Logger logger = LoggerFactory.getLogger(FTSettings.class);

    public static FtOperation enable(FTSetting ftSetting) {
        return new FtCheckbox(ftSetting, FtCheckbox.OperationType.ENABLE);
    }

    public static FtOperation disable(FTSetting ftSetting) {
        return new FtCheckbox(ftSetting, FtCheckbox.OperationType.DISABLE);
    }

    public static FtOperation select(FTSetting ftSetting, String optionName) {
        return new FtSelect(ftSetting, optionName);
    }

    public static FtOperation setValue(FTSetting ftSetting, String value) {
        return new FtTextField(ftSetting, value);
    }

    public static List<FtOperation> differences(Document document, List<FtOperation> operations) {
        List<FtOperation> differedOperations = new ArrayList<>();

        for (FtOperation operation : operations) {
            boolean hasSameState = operation.hasSameState(document);
            if (!hasSameState) {
                differedOperations.add(operation.getRollbackOperation());
                logger.info("FTSettings should be updated because of: {}", operation.toString());
            }
        }
        return differedOperations;
    }
}

