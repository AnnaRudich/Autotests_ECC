package com.scalepoint.automation.utils.testng;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.util.RetryAnalyzerCount;

import java.util.concurrent.atomic.AtomicInteger;

public class  Retrier2 extends RetryAnalyzerCount {

    protected Logger logger = LogManager.getLogger(Retrier2.class);
    private static final int MAX_RETRY_ATTEMPTS = 2;
    private AtomicInteger counter = new AtomicInteger(1); //used only for logging purposes

    public Retrier2() {
        setCount(MAX_RETRY_ATTEMPTS);
    }

    @Override
    public boolean retryMethod(ITestResult iTestResult) {
        return true;
    }

}
