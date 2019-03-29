package com.scalepoint.automation.utils.annotations;

import com.scalepoint.automation.services.usersmanagement.CompanyCode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface SupplierCompany {
    CompanyCode value() default CompanyCode.SCALEPOINT;

    boolean areWithVouchers() default true;
}
