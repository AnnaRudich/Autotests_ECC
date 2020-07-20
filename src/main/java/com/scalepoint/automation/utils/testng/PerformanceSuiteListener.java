package com.scalepoint.automation.utils.testng;

import com.scalepoint.automation.spring.PerformanceTestConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.*;
import org.testng.xml.XmlSuite;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class PerformanceSuiteListener implements ISuiteListener, IMethodInterceptor {

    protected Logger log = LogManager.getLogger(PerformanceSuiteListener.class);

    private static final int STEP = 5;
    private static final String USERS = "tests.performance.users";
    private static final String LIMIT = "tests.performance.limit";
    private static final String TEST_TIME = "tests.performance.testTime";
    private static final String ENABLED_TESTS = "tests.performance.enabledPerformanceTests";

    private static Integer testTime = null;
    private static LocalDateTime endTime = null;
    private static Integer breakPoint;
    private static Integer maxLoad;
    private static Integer maxLoad0_5;

    @Override
    public List<IMethodInstance> intercept(List<IMethodInstance> list, ITestContext iTestContext) {

        List<PerformanceTestConfig.PerformanceTestsNames> enabledGroups = Arrays
                .asList(iTestContext.getSuite().getParameter(ENABLED_TESTS)
                        .split(","))
                .stream()
                .map(PerformanceTestConfig.PerformanceTestsNames::findTest)
                .collect(Collectors.toList());


        List enabledMethods = list
                .stream()
                .filter(iMethodInstance ->
                        Arrays.stream(iMethodInstance.getMethod().getGroups())
                                .anyMatch(group ->
                                        enabledGroups.stream()
                                                .anyMatch(enabledTest ->
                                                        enabledTest.getTestName().equals(group))))
                .collect(Collectors.toList());

        log.info("Enabled list" + enabledMethods);

        return enabledMethods;
    }

    @Override
    public void onStart(ISuite iSuite) {

        switch (PerformanceSuite.findSuite(iSuite.getName())) {

            case CONSTANT:

                if(testTime == null){

                    testTime = Integer.valueOf(iSuite.getParameter(TEST_TIME));
                }

                if(endTime == null){

                    endTime = LocalDateTime.now().plusMinutes(testTime);
                }
                break;
        }
    }

    @Override
    public void onFinish(ISuite suite) {

        switch (PerformanceSuite.findSuite(suite.getName())) {

            case INCREMENTAL:

                Integer users = Integer.valueOf(suite.getParameter(USERS));
                Integer limit = Integer.valueOf(suite.getParameter(LIMIT));

                if (isSuitePassed(suite) && users < limit) {

                    incrementUsersLoad(suite.getXmlSuite());
                    suite.run();
                }
                break;

            case CONSTANT:

                repeatTestUntil(suite);
                break;

            case STRESS:

                users = Integer.valueOf(getChildSuite(suite.getXmlSuite(), PerformanceSuite.INCREMENTAL).getParameter(USERS));

                breakPoint = users;
                maxLoad = users - STEP;
                maxLoad0_5 = maxLoad % 10 == 0 ? maxLoad / 2 : ((int) Math.floor((maxLoad / 10)) + 1) * 5;

                XmlSuite performanceSuite = suite.getXmlSuite().getParentSuite();

                XmlSuite loadSuite = getChildSuite(performanceSuite, PerformanceSuite.LOAD);
                setUsers(loadSuite, maxLoad.toString());

                XmlSuite enduranceSuite = getChildSuite(performanceSuite, PerformanceSuite.ENDURANCE);
                setUsers(getChildSuite(enduranceSuite, PerformanceSuite.CONSTANT), maxLoad.toString());

                XmlSuite spikeSuite = getChildSuite(performanceSuite, PerformanceSuite.SPIKE);
                setLimit(getChildSuite(spikeSuite, PerformanceSuite.INCREMENTAL), maxLoad0_5.toString());

                spikeSuite
                        .getChildSuites()
                        .stream()
                        .filter(xmlSuite -> PerformanceSuite.findSuite(xmlSuite.getName()).equals(PerformanceSuite.CONSTANT))
                        .forEach(xmlSuite -> setUsers(xmlSuite, maxLoad0_5.toString()));

                spikeSuite
                        .getChildSuites()
                        .stream()
                        .filter(xmlSuite -> PerformanceSuite.findSuite(xmlSuite.getName()).equals(PerformanceSuite.LOAD))
                        .forEach(xmlSuite -> setUsers(xmlSuite, maxLoad.toString()));

                log.warn("Break point: {}", breakPoint);
                break;
        }
    }

    protected XmlSuite getChildSuite(XmlSuite xmlSuite, PerformanceSuite performanceSuite){

        return xmlSuite
                .getChildSuites()
                .stream()
                .filter(suite -> PerformanceSuite.findSuite(suite.getName()).equals(performanceSuite))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

    protected void repeatTestUntil(ISuite suite){

        LocalDateTime now = LocalDateTime.now();

        if(now.isBefore(endTime))
        {

            log.info("Minutes left :{}", now.until(endTime, ChronoUnit.MINUTES));
            suite.run();

        }else {

            endTime = null;
        }
    }

    protected boolean isSuitePassed(ISuite iSuite){

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

    protected void incrementUsersLoad(XmlSuite suite){

        Integer incrementedUsers = Integer.valueOf(suite.getParameter(USERS)) + STEP;
        setUsers(suite, incrementedUsers.toString());
    }

    protected Integer setUsers(XmlSuite suite, String users){

        Map parameters = suite.getParameters();
        parameters.put(USERS, users);

        return Integer.valueOf(users);
    }

    protected Integer setLimit(XmlSuite suite, String limit){

        Map parameters = suite.getParameters();
        parameters.put(LIMIT, limit);

        return Integer.valueOf(limit);
    }

    public enum PerformanceSuite{

        STRESS("Stress"),
        LOAD("Load"),
        ENDURANCE("Endurance"),
        SPIKE("Spike"),
        TRANSITION("Transition"),
        INCREMENTAL("Incremental"),
        CONSTANT("Constant"),
        PERFORMANCE("Performance");

        private final String suiteName;

        PerformanceSuite(String suiteName) {

            this.suiteName = suiteName;
        }

        public static PerformanceSuite findSuite(String suiteName){

            return Arrays.stream(PerformanceSuite.values())
                    .filter(performanceSuite ->  suiteName.contains(performanceSuite.suiteName))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException(suiteName));
        }
    }
}
