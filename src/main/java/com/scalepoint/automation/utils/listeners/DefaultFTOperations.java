package com.scalepoint.automation.utils.listeners;

import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSettings;
import com.scalepoint.automation.services.externalapi.ftemplates.operations.FtOperation;
import com.scalepoint.automation.utils.data.TestData;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DefaultFTOperations {

    public static List<FtOperation> getDefaultFTSettings(String companyCode){

        DefaultFTSettings defaultFTSettings = TestData.getDefaultFTSettingsForFuture();

        if(companyCode.equals("ALKA")){
            defaultFTSettings = TestData.getDefaultFTSettingsForAlka();
        }
        if(companyCode.equals("TOPDANMARK")){
            defaultFTSettings = TestData.getDefaultFTSettingsForTopdanmark();
        }
        if(companyCode.equals("SCALEPOINT")){
            defaultFTSettings = TestData.getDefaultFTSettingsForScalepoint();
        }
        if(companyCode.equals("TRYGFORSIKRING")){
            defaultFTSettings = TestData.getDefaultFTSettingsForTrygforsikring();
        }
        if(companyCode.equals("BAUTA")){
            defaultFTSettings = TestData.getDefaultFTSettingsForBauta();
        }
        if(companyCode.equals("TRYGHOLDING")){
            defaultFTSettings = TestData.getDefaultFTSettingsForTrygholding();
        }

        return defaultFTSettings
                .getDefaultSettings()
                .entrySet()
                .stream()
                .map(entry -> getFTOperation(entry))
                .collect(Collectors.toList());
    }

    private static FtOperation getFTOperation(Map.Entry<String, String> defaultSetting){

        String key = defaultSetting.getKey();
        String value = defaultSetting.getValue();
        FTSetting setting = FTSetting.valueOf(key);
        FtOperation.OperationType operationType = setting.getOperationType();

        switch (operationType) {
            case CHECKBOX:
                return value.equals("ENABLE") ? FTSettings.enable(setting) : FTSettings.disable(setting);
            case INPUT:
                return FTSettings.setValue(setting, value);
            case SELECT:
                return FTSettings.select(setting, value);
        }

        throw new RuntimeException(String.format("Operation type: %s is unknown", operationType));
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    @Data
    public static class DefaultFTSettings{
        @XmlElement
        private Map<String, String> defaultSettings;
    }
}
