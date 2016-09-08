package com.scalepoint.automation.services.externalapi.ftemplates;

import com.scalepoint.automation.services.externalapi.ftemplates.operations.FtCheckBoxOperation;
import com.scalepoint.automation.services.externalapi.ftemplates.operations.FtInputOperation;
import com.scalepoint.automation.services.externalapi.ftemplates.operations.FtOperation;
import com.scalepoint.automation.services.externalapi.ftemplates.operations.FtSelectOperation;

public class FT {

    public static FtOperation enable(FTSetting ftSetting) {
        return new FtCheckBoxOperation(ftSetting, FtCheckBoxOperation.OperationType.ENABLE);
    }

    public static FtOperation disable(FTSetting ftSetting) {
        return new FtCheckBoxOperation(ftSetting, FtCheckBoxOperation.OperationType.DISABLE);
    }

    public static FtOperation select(FTSetting ftSetting, String optionName) {
        return new FtSelectOperation(ftSetting, optionName);
    }

    public static FtOperation setValue(FTSetting ftSetting, String value) {
        return new FtInputOperation(ftSetting, value);
    }
}

