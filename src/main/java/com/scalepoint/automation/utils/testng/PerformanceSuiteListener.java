package com.scalepoint.automation.utils.testng;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ISuite;
import org.testng.xml.XmlSuite;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;

public class PerformanceSuiteListener {

    protected Logger log = LogManager.getLogger(PerformanceSuiteListener.class);

    protected static final int STEP = 5;
    protected static final String USERS = "tests.performance.users";
    protected static final String LIMIT = "tests.performance.limit";

    protected static  LocalDateTime END_TIME = null;
    protected static Integer breakPoint;
    protected static Integer maxLoad;
    protected static Integer maxLoad0_5;

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
