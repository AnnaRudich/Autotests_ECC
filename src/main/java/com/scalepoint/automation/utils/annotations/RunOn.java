package com.scalepoint.automation.utils.annotations;

import com.scalepoint.automation.utils.driver.DriverType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface RunOn {
    DriverType value();
}
