package com.scalepoint.automation.utils.testng;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import java.util.concurrent.atomic.AtomicInteger;

public class Retrier implements IRetryAnalyzer {

    // Default retry once.
    private AtomicInteger count = new AtomicInteger(2);

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
        return count.getAndDecrement() > 0;
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
