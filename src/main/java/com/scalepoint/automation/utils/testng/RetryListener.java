package com.scalepoint.automation.utils.testng;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Created by bza on 1/16/2018.
 */
public class RetryListener implements IAnnotationTransformer{

    @Override
    public void transform(ITestAnnotation iTestAnnotation, Class aClass, Constructor constructor, Method method) {

        iTestAnnotation.setRetryAnalyzer(Retrier.class);
    }
}
