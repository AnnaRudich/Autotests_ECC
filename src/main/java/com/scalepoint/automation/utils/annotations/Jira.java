package com.scalepoint.automation.utils.annotations;

import java.lang.annotation.*;

@Repeatable(Jiras.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Jira {
    String value();
}
