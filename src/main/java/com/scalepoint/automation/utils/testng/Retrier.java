package com.scalepoint.automation.utils.testng;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.util.RetryAnalyzerCount;

import java.util.concurrent.atomic.AtomicInteger;

public class Retrier extends RetryAnalyzerCount {

    protected Logger logger = LogManager.getLogger(Retrier.class);
    private static final int MAX_RETRY_ATTEMPTS = 3;
    private AtomicInteger counter = new AtomicInteger(1); //used only for logging purposes

    public Retrier() {
        setCount(MAX_RETRY_ATTEMPTS);
    }

    @Override
    public boolean retryMethod(ITestResult iTestResult) {
        return true;
    }

}
