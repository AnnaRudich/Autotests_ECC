package com.scalepoint.automation.utils.driver;

import com.scalepoint.automation.utils.annotations.RunOn;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class DriverHelper {

    public DriverType getDriverType(Method method, String browserMode) {

        Class aClass = method.getDeclaringClass();
        DriverType driverType = null;

        if (method.getAnnotation(RunOn.class) != null) {
            driverType = method.getAnnotation(RunOn.class).value();

        } else {
            Annotation[] annotations = aClass.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof RunOn) {
                    driverType = ((RunOn) annotation).value();
                }
            }
            if (driverType == null) {
                driverType = DriverType.findByProfile(browserMode);
            }
        }
        return driverType;
    }
}
