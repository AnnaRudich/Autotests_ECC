package com.scalepoint.automation.utils.testng;

import org.testng.IAnnotationTransformer;
import org.testng.IConfigurable;
import org.testng.IConfigureCallBack;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Created by bza on 1/16/2018.
 */
public class RetryListener implements IAnnotationTransformer, IConfigurable {

    @Override
    public void transform(ITestAnnotation iTestAnnotation, Class aClass, Constructor constructor, Method method) {

        iTestAnnotation.setRetryAnalyzer(Retrier.class);
    }

    @Override
    public void run(IConfigureCallBack iConfigureCallBack, ITestResult iTestResult) {

        iConfigureCallBack.runConfigurationMethod(iTestResult);

        if (iTestResult.getThrowable() != null) {

            for (int i = 0; i <= 3; i++) {

                iConfigureCallBack.runConfigurationMethod(iTestResult);
                if (iTestResult.getThrowable() == null) {

                    break;
                }
            }
        }
    }
}
