package com.scalepoint.automation.utils.annotations.functemplate;

import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Repeatable(value = RequiredSettings.class)
public @interface RequiredSetting {

    String UNDEFINED = "undefined";

    FTSetting type();

    String value() default UNDEFINED;

    boolean enabled() default true;

    /**
     * this filed is used to mark FT settings which are expected to be set by default and not to be set by convert
     */
    boolean isDefault() default false;
}


