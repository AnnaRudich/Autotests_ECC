package com.scalepoint.automation.utils.testng;

import com.scalepoint.automation.spring.PerformanceTestConfig;
import com.scalepoint.automation.utils.reports.Report;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.*;
import org.testng.xml.XmlSuite;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class PerformanceSuiteListener implements ISuiteListener, IMethodInterceptor {

    protected Logger log = LogManager.getLogger(PerformanceSuiteListener.class);

    private static final int STEP = 5;
    public static final String USERS = "tests.performance.users";
    private static final String LIMIT = "tests.performance.limit";
    private static final String TEST_TIME = "tests.performance.testTime";
    private static final String ENABLED_TESTS = "tests.performance.enabledPerformanceTests";

    private Report report = Report.getInstance();

    private static Integer testTime = null;
    private static LocalDateTime endTime = null;
    private static Integer breakPoint;
    private static Integer maxLoad;
    private static Integer maxLoad0_5;

    private PerformanceSuite suiteType;

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
    public void onStart(ISuite suite) {

        suiteType = PerformanceSuite.findSuite(suite.getName());

        if(suiteType.equals(PerformanceSuite.CONSTANT)) {

            if (testTime == null) {

                testTime = Integer.valueOf(suite.getParameter(TEST_TIME));
            }

            if (endTime == null) {

                endTime = LocalDateTime.now().plusMinutes(testTime);
            }
        }
    }

    @Override
    public void onFinish(ISuite suite) {

        Integer users = Integer.valueOf(suite.getParameter(USERS));
        Integer limit = Integer.valueOf(suite.getParameter(LIMIT));
        ITestContext iTestContext = null;
        Date startDate = null;

        try {

            iTestContext = suite.getResults().values().stream()
                    .findFirst()
                    .orElseThrow(NoSuchElementException::new)
                    .getTestContext();

            startDate = iTestContext.getStartDate();
        }catch (NoSuchElementException e){

            log.info("Suite without ITestContext: {}", suite.getName());
        }

        suiteType = PerformanceSuite.findSuite(suite.getName());

        XmlSuite parentSuite = getParentSuite(suite);

        if(suiteType.equals(PerformanceSuite.LOAD)) {

            PerformanceSuite parentSuiteType = PerformanceSuite.findSuite(parentSuite.getName());

            if(parentSuiteType.equals(PerformanceSuite.PERFORMANCE)) {

                report.getTest(suite.getName())
                        .getNode(suite.getName().concat("_").concat(users.toString()), startDate)
                        .setTestResults(iTestContext);
                    report.getTest(suite.getName()).createChart();
            }
            if(parentSuiteType.equals(PerformanceSuite.SPIKE)){

                report.getTest(parentSuite.getName())
                        .getNode(suite.getName().concat("_").concat(users.toString()), startDate)
                        .setTestResults(iTestContext);
            }

            report.flush();
        }

        if(suiteType.equals(PerformanceSuite.ENDURANCE)) {

                report.getTest(suite.getName()).createChart();
            report.flush();
        }

        if(suiteType.equals(PerformanceSuite.SPIKE)) {

                report.getTest(suite.getName()).createChart();

            report.flush();
        }

        if(suiteType.equals(PerformanceSuite.INCREMENTAL)) {

            report.getTest(parentSuite.getName())
                    .getNode(suite.getName().concat("_").concat(users.toString()), startDate)
                    .setTestResults(iTestContext);
            report.flush();

            if (isSuitePassed(suite) && users < limit) {

                incrementUsersLoad(suite.getXmlSuite());
                suite.run();
            }
        }


        if(suiteType.equals(PerformanceSuite.CONSTANT)) {

            report.getTest(parentSuite.getName())
                    .getNode(suite.getName().concat("_").concat(users.toString()).concat(String.valueOf(iTestContext.getStartDate().toInstant().toEpochMilli())), startDate)
                    .setTestResults(iTestContext);
            report.flush();

            repeatTestUntil(suite);
        }

        if(suiteType.equals(PerformanceSuite.STRESS)) {

            users = Integer.valueOf(getChildSuite(suite.getXmlSuite(), PerformanceSuite.INCREMENTAL).getParameter(USERS));

            breakPoint = users;
            maxLoad = users - STEP;
            maxLoad0_5 = maxLoad % 10 == 0 ? maxLoad / 2 : ((int) Math.floor((maxLoad / 10)) + 1) * 5;

            XmlSuite performanceSuite = getParentSuite(suite);

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
            
                report.getTest(suite.getName()).createChart();
                report.flush();
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

    protected XmlSuite getParentSuite(ISuite iSuite){

        return iSuite.getXmlSuite().getParentSuite();
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
