package com.scalepoint.automation.utils.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Bug {
    String bug();
    /**
     * The optional reason why the operations is ignored.
     */
    String value() default "";

    boolean alwaysRun() default false;
    boolean enabled() default false;
}
