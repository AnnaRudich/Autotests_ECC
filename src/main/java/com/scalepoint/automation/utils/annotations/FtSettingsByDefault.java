package com.scalepoint.automation.utils.annotations;

import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
/**
 * this annotation is used only as an info note
 * it has a list of FT settings which are expected to be set by default
 */
public @interface FtSettingsByDefault {
    String UNDEFINED = "undefined";

    FTSetting type();

    String value() default UNDEFINED;

    boolean enabled() default true;
}