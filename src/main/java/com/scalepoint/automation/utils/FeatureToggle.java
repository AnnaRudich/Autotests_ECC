package com.scalepoint.automation.utils;

import com.scalepoint.automation.services.externalapi.ftoggle.FeatureId;
import com.scalepoint.automation.services.externalapi.ftoggle.FeatureValue;
import com.scalepoint.automation.services.restService.FeaturesToggleAdministrationService;
import com.scalepoint.automation.utils.annotations.ftoggle.FeatureToggleSetting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.testng.ITestContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;


@Component
public class FeatureToggle {

    protected Logger logger = LogManager.getLogger(FeatureToggle.class);

    private Map<FeatureId, Boolean> featureTogglesStateTestLevel = new HashMap<>();
    private static Map<FeatureId, Boolean> featureTogglesStateSuiteLevel = new HashMap<>();

    FeaturesToggleAdministrationService featureToggleService;

    public void updateFeatureToggles(ITestContext context) {

        featureToggleService = new FeaturesToggleAdministrationService();
        updateAndStoreDefaultState(getToggleSettingsFromSuite(context), featureTogglesStateSuiteLevel);

        logger.info("Feature Toggle State Suite level: {}", featureTogglesStateSuiteLevel);
    }

    public void updateFeatureToggles(Method method) {

        List<FeatureToggleSetting> featureToggleSettingsToUpdate = getToggleSettingsFromClass(method.getDeclaringClass());
        featureToggleSettingsToUpdate.addAll(getToggleSettingsFromMethod(method));

        updateAndStoreDefaultState(featureToggleSettingsToUpdate, featureTogglesStateTestLevel);

        logger.info("Feature Toggle State Method level: {}", featureTogglesStateTestLevel);
    }

    public void rollbackToggleSettingSuiteLevel() {

        rollbackToDefaultState(featureTogglesStateSuiteLevel);
    }

    public void rollbackToggleSettingTestLevel() {

        rollbackToDefaultState(featureTogglesStateTestLevel);
    }

    private void updateAndStoreDefaultState(List<FeatureToggleSetting> featureToggleSettings, Map<FeatureId, Boolean> defaultStates){

        featureToggleSettings.stream()
                .forEach(featureToggleSetting -> {

                    if(featureToggleSetting == null || defaultStates.containsKey(featureToggleSetting.type())){

                        return;
                    }

                    boolean toggleActualState = featureToggleService.getToggleStatus(featureToggleSetting.type().name());
                    boolean toggleExpectedState = featureToggleSetting.enabled();

                    defaultStates.put(featureToggleSetting.type(), toggleActualState);

                    if((toggleExpectedState != toggleActualState)){

                        featureToggleService.updateToggle(FeaturesToggleAdministrationService.ActionsOnToggle.of(toggleExpectedState), featureToggleSetting.type());
                    }
                });
    }

    private void rollbackToDefaultState(Map<FeatureId, Boolean> defaultStates){

        defaultStates.entrySet().stream().forEach(entry -> {

            featureToggleService.updateToggle(FeaturesToggleAdministrationService.ActionsOnToggle.of(entry.getValue()), entry.getKey());
        });

        logger.info("Rollback completed");
    }

    private List<FeatureToggleSetting> getToggleSettingsFromSuite(ITestContext context){

        return context.getSuite()
                .getXmlSuite()
                .getParameters()
                .entrySet()
                .stream()
                .filter(entry -> FeatureId.isValid(entry.getKey()))
                .map(entry ->
                        createFeatureToggleSetting(
                                FeatureId.getByName(entry.getKey()),
                                FeatureValue.getByStatus(entry.getValue()).isEnabled()))
                .collect(Collectors.toList());
    }

    private List<FeatureToggleSetting> getToggleSettingsFromClass(Class clazz) {

        return new ArrayList<>(Arrays.asList((FeatureToggleSetting[]) clazz.getDeclaredAnnotationsByType(FeatureToggleSetting.class)));
    }

    private List<FeatureToggleSetting> getToggleSettingsFromMethod(Method method) {

        return new ArrayList<>(Arrays.asList(method.getDeclaredAnnotationsByType(FeatureToggleSetting.class)));
    }

    public static FeatureToggleSetting createFeatureToggleSetting(FeatureId id, boolean enabled){

        return new FeatureToggleSetting(){

            @Override
            public Class<? extends Annotation> annotationType() {
                return FeatureToggleSetting.class;
            }

            @Override
            public FeatureId type() {
                return id;
            }

            @Override
            public boolean enabled() {
                return enabled;
            }
        };
    }
}
