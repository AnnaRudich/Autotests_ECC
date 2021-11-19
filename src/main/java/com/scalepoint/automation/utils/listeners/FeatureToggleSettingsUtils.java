package com.scalepoint.automation.utils.listeners;

import com.scalepoint.automation.services.externalapi.ftoggle.FeatureIds;
import com.scalepoint.automation.utils.annotations.ftoggle.FeatureToggleSetting;
import org.testng.ITestContext;

import java.lang.annotation.Annotation;
import java.util.Optional;

import static com.scalepoint.automation.services.externalapi.ftoggle.FeatureIds.SCALEPOINTID_LOGIN_ENABLED;

public class FeatureToggleSettingsUtils {

    public static boolean isFeatureToggleSettingEnabled(ITestContext context, FeatureIds featureIds){

        return getFeatureToggleSetting(context)
                .equals(featureIds.name());
    }

    public static String getFeatureToggleSetting(ITestContext context){

        return Optional.ofNullable(context.getSuite().getParameter("FeatureToggleSetting"))
                .orElse("");
    }

    public static FeatureToggleSetting scalepointIdLoginEnabled(){

        return new FeatureToggleSetting(){

            @Override
            public Class<? extends Annotation> annotationType() {
                return FeatureToggleSetting.class;
            }

            @Override
            public FeatureIds type() {
                return SCALEPOINTID_LOGIN_ENABLED;
            }

            @Override
            public boolean enabled() {
                return true;
            }
        };
    }
}
