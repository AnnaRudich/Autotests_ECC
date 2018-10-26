package com.scalepoint.automation.utils.annotations.ftoggle;

import com.scalepoint.automation.services.externalapi.ftoggle.FeatureIds;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface FeatureToggleSetting {

    FeatureIds type();

    boolean enabled() default true;
}
