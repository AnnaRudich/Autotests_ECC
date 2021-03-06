package com.scalepoint.automation.utils.testng;

import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class HealthCheckListener implements ISuiteListener, IMethodInterceptor {

    private boolean healthCheckFailed = false;
    List<Suite> includedSuites = Arrays.asList(Suite.REGRESSION, Suite.QUNIT, Suite.SCALEPOINT_ID);

    @Override
    public void onStart(ISuite iSuite) {}

    @Override
    public void onFinish(ISuite iSuite) {

        if(Suite.findSuite(iSuite.getName()).equals(Suite.HEALTH_CHECK)){

            boolean test = isSuitePassed(iSuite);
            healthCheckFailed = !test;
        }
    }

    @Override
    public List<IMethodInstance> intercept(List<IMethodInstance> list, ITestContext iTestContext) {

        ISuite suite = iTestContext.getSuite();

        if (includedSuites.contains(Suite.findSuite(suite.getName())) && healthCheckFailed) {

            return new LinkedList<>();
        }
        return list;
    }

    private boolean isSuitePassed(ISuite iSuite){

        return iSuite
                .getResults()
                .values()
                .stream()
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException())
                .getTestContext()
                .getFailedTests()
                .size() == 0;
    }

    public enum Suite{

        HEALTH_CHECK("HealthCheck"),
        REGRESSION("allTestsExceptRnV"),
        QUNIT("qunit"),
        SCALEPOINT_ID ("scalepointId"),
        ALL("All");

        private final String suiteName;

        Suite(String suiteName) {

            this.suiteName = suiteName;
        }

        public static Suite findSuite(String suiteName){

            return Arrays.stream(Suite.values())
                    .filter(performanceSuite ->  suiteName.contains(performanceSuite.suiteName))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException(suiteName));
        }
    }

}
