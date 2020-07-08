package com.scalepoint.automation.utils.testng;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.xml.XmlSuite;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;

public class PerformanceSuiteListener {

    protected Logger log = LogManager.getLogger(PerformanceSuiteListener.class);

    protected static final int STEP = 5;
    protected static final String USERS = "users";
    protected static final String LIMIT = "limit";
    protected static  LocalDateTime END_TIME = null;

    protected static Integer breakPoint;
    protected static Integer maxLoad;
    protected static Integer maxLoad0_5;

//    @Override
//    public void onStart(ISuite iSuite) {
//        log.info("test");
//    }

//    @Override
//    public void onFinish(ISuite suite) {
//
//        switch (PerformanceSuite.findSuite(suite.getName())) {
//
//            case STRESS:
//
//                XmlSuite incrementalSuite = suite
//                        .getXmlSuite()
//                        .getChildSuites()
//                        .stream()
//                        .filter(xmlSuite -> PerformanceSuite.findSuite(xmlSuite.getName()).equals(PerformanceSuite.INCREMENTAL))
//                        .findFirst()
//                        .orElseThrow();
//
//                Integer users = Integer.valueOf(suite.getParameter(USERS));
//                breakPoint = users;
//                maxLoad = users - STEP;
//                maxLoad0_5 = maxLoad % 10 == 0 ? maxLoad / 2 : ((int) Math.floor((maxLoad / 10)) + 1) * 5;
//
//                XmlSuite performanceSuite = suite.getXmlSuite().getParentSuite();
//
//                XmlSuite loadSuite = getChildSuite(performanceSuite, PerformanceSuite.LOAD);
//                setUsers(loadSuite, maxLoad.toString());
//
//                XmlSuite enduranceSuite = getChildSuite(performanceSuite, PerformanceSuite.ENDURANCE);
//                setUsers(getChildSuite(enduranceSuite, PerformanceSuite.CONSTANT), maxLoad.toString());
//
//                XmlSuite spikeSuite = getChildSuite(performanceSuite, PerformanceSuite.SPIKE);
//                spikeSuite
//                        .getChildSuites()
//                        .stream()
//                        .filter(xmlSuite -> PerformanceSuite.findSuite(xmlSuite.getName()).equals(PerformanceSuite.CONSTANT))
//                        .forEach(xmlSuite -> setUsers(xmlSuite, maxLoad0_5.toString()));
//                spikeSuite
//                        .getChildSuites()
//                        .stream()
//                        .filter(xmlSuite -> PerformanceSuite.findSuite(xmlSuite.getName()).equals(PerformanceSuite.INCREMENTAL))
//                        .forEach(xmlSuite -> setLimit(xmlSuite, maxLoad0_5.toString()));
//                spikeSuite
//                        .getChildSuites()
//                        .stream()
//                        .filter(xmlSuite -> PerformanceSuite.findSuite(xmlSuite.getName()).equals(PerformanceSuite.INCREMENTAL))
//                        .forEach(xmlSuite -> setLimit(xmlSuite, maxLoad.toString()));
//
//
//                log.warn("Break point: {}", breakPoint);
//                break;
//        }
//    }

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

        if(now.isBefore(END_TIME))
        {

            log.info("Minutes left :{}", now.until(END_TIME, ChronoUnit.MINUTES));
            suite.run();
        }else {

            END_TIME = null;
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

    enum PerformanceSuite{

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
