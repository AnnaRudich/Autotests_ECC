package com.scalepoint.automation.tests.widget;

import com.scalepoint.automation.services.externalapi.ftoggle.FeatureIds;
import com.scalepoint.automation.services.restService.FeaturesToggleAdministrationService;
import com.scalepoint.automation.utils.annotations.ftoggle.FeatureToggleSetting;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
public class FeatureToggle {

    protected Map<FeatureIds, Boolean> featureTogglesDefaultState = new HashMap<>();

    public void updateFeatureToggle(FeatureToggleSetting toggleSetting) {

        FeaturesToggleAdministrationService featureToggleService = new FeaturesToggleAdministrationService();

        if (toggleSetting == null) {

            return;
        }

        boolean toggleActualState = featureToggleService.getToggleStatus(toggleSetting.type().name());
        boolean toggleExpectedState = toggleSetting.enabled();

        if (toggleActualState != toggleExpectedState) {

            featureTogglesDefaultState.put(toggleSetting.type(), toggleActualState);
            featureToggleService.updateToggle(FeaturesToggleAdministrationService.ActionsOnToggle.of(toggleExpectedState), toggleSetting.type());
        }
    }

    public void rollbackToggleSetting(FeatureToggleSetting toggleSetting) {

        FeaturesToggleAdministrationService featuresToggleAdminApi = new FeaturesToggleAdministrationService();

        if (toggleSetting == null) {

            return;
        }

        FeatureIds toggleSettingType = toggleSetting.type();
        Boolean initialState = featureTogglesDefaultState.get(toggleSettingType);
        featuresToggleAdminApi.updateToggle(FeaturesToggleAdministrationService.ActionsOnToggle.of(initialState), toggleSettingType);
    }
}
