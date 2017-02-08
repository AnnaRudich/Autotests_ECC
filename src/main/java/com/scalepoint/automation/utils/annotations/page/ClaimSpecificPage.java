package com.scalepoint.automation.utils.annotations.page;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  The page that must contain claim id in the url to use Page.to method for direct call
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ClaimSpecificPage {
}
