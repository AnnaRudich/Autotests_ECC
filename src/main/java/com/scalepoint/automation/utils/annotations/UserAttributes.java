package com.scalepoint.automation.utils.annotations;

import com.scalepoint.automation.services.usersmanagement.CompanyCode;
import com.scalepoint.automation.utils.data.entity.credentials.User;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface UserAttributes {

    CompanyCode company();
    User.UserType type() default User.UserType.EXCEPTIONAL;
}
