package com.scalepoint.automation.services.externalapi.ftemplates;

import com.scalepoint.automation.services.externalapi.ftemplates.operations.FtCheckbox;
import com.scalepoint.automation.services.externalapi.ftemplates.operations.FtOperation;
import com.scalepoint.automation.services.externalapi.ftemplates.operations.FtSelect;
import com.scalepoint.automation.services.externalapi.ftemplates.operations.FtTextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FTSettings {

    private static Logger logger = LogManager.getLogger(FTSettings.class);

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

    public static ComparingResult differences(Document document, List<FtOperation> operations) {
        List<FtOperation> differedOperations = new ArrayList<>();
        List<FtOperation> initialStates = new ArrayList<>();

        for (FtOperation operation : operations) {
            boolean hasSameState = operation.hasSameState(document);
            if (!hasSameState) {
                logger.info("FTSettings should be updated because of: {}", operation.toString());
                differedOperations.add(operation.getRollbackOperation());
            }

            initialStates.add(operation.getRollbackOperation());
        }
        return new ComparingResult(differedOperations, initialStates);
    }

    public static class ComparingResult {
        List<FtOperation> differedOperations = Collections.emptyList();
        List<FtOperation> initialStates = Collections.emptyList();

        ComparingResult(List<FtOperation> differedOperations, List<FtOperation> initialStates) {
            this.differedOperations = differedOperations;
            this.initialStates = initialStates;
        }

        public List<FtOperation> getDifferedOperations() {
            return differedOperations;
        }

        public boolean hasSameStateAsRequested() {
            return differedOperations.isEmpty();
        }

        public List<FtOperation> getInitialStates() {
            return initialStates;
        }
    }
}