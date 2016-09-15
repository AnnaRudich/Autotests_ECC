package com.scalepoint.automation.utils.annotations.functemplate;

import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Repeatable(value = SettingsRequired.class)
public @interface SettingRequired {

    String UNDEFINED = "undefined";

    FTSetting type();
    String value() default UNDEFINED;
    boolean enabled() default true;
}


