package com.scalepoint.automation.utils.annotations;

import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;

public @interface FtSettingsByDefault {
    String UNDEFINED = "undefined";

    FTSetting type();

    String value() default UNDEFINED;

    boolean enabled() default true;
}