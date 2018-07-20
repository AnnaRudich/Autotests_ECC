package com.scalepoint.automation.utils.testng;

import com.scalepoint.automation.tests.BaseTest;
import com.sun.org.apache.regexp.internal.RE;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import java.util.concurrent.atomic.AtomicInteger;

public class Retrier implements IRetryAnalyzer {

    protected Logger logger = LogManager.getLogger(Retrier.class);
    private final static int retryTimes = 3;

    // Default retry once.
    private AtomicInteger count = new AtomicInteger(retryTimes);

    /**
     * Set the max number of time the method needs to be retried.
     */
    protected void setCount(int count) {
        this.count.set(count);
    }

    /**
     * Return the current counter value
     */
    protected int getCount() {
        return this.count.get();
    }

    /**
     * Retries the test if count is not 0.
     *
     * @param result The result of the test.
     */
    @Override
    public boolean retry(ITestResult result) {
        Boolean shouldRerun = count.getAndDecrement() > 0;
        logger.warn("Failed times " + (retryTimes - count.intValue()));
        return shouldRerun;
    }

    /**
     * The method implemented by the class that test if the test
     * must be retried or not.
     *
     * @param result The result of the test.
     * @return true if the test must be retried, false otherwise.
     */
    private boolean retryMethod(ITestResult result) {
        return true;
    }
}
