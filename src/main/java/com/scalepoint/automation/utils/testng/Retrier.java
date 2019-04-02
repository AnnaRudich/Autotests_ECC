package com.scalepoint.automation.utils.testng;

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
     * Retries the convert if count is not 0.
     *
     * @param result The result of the convert.
     */
    @Override
    public boolean retry(ITestResult result) {
        boolean shouldRerun = count.getAndDecrement() > 0;
        logger.warn("Failed times " + (retryTimes - count.intValue()));
        return shouldRerun;
    }

    /**
     * The method implemented by the class that convert if the convert
     * must be retried or not.
     *
     * @param result The result of the convert.
     * @return true if the convert must be retried, false otherwise.
     */
    private boolean retryMethod(ITestResult result) {
        return true;
    }
}
