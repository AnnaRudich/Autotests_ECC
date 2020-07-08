package com.scalepoint.automation.utils.testng;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ISuite;
import org.testng.ISuiteListener;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;

public class PerformanceSuiteListener2 implements ISuiteListener {

    protected Logger log = LogManager.getLogger(PerformanceSuiteListener2.class);

    private static final int STEP = 5;
    private static final String USERS = "users";
    private static final String LIMIT = "limit";
    private static  LocalDateTime END_TIME = null;
    private static int ENDURANCE_TEST_MIN_TIME_IN_HOURS = 5;

    private static Integer breakPoint;
    private static Integer maxLoad;
    private static Integer maxLoad0_5;

    private static int spikeTestStepCounter = 3;

    @Override
    public void onStart(ISuite iSuite) {

        switch (PerformanceSuite.findSuite(iSuite.getName())){

            case LOAD:

                setUsers(iSuite, maxLoad.toString());
                break;

            case ENDURANCE:

                if(END_TIME == null){

                    END_TIME = LocalDateTime.now().plusMinutes(ENDURANCE_TEST_MIN_TIME_IN_HOURS);
                }

                setUsers(iSuite, maxLoad.toString());
                break;
        }
    }

    @Override
    public void onFinish(ISuite suite) {

        switch (PerformanceSuite.findSuite(suite.getName())) {

            case STRESS:

                int users = Integer.valueOf(suite.getParameter(USERS));
                int limit = Integer.valueOf(suite.getParameter(LIMIT));
                //TODO remove border
                if (isSuitePassed(suite) && users < limit) {

                    incrementUsersLoad(suite);
                    suite.run();
                } else {

                    breakPoint = users;
                    maxLoad = users - STEP;
                    maxLoad0_5 = maxLoad % 10 == 0 ? maxLoad / 2 : ((int) Math.floor((maxLoad / 10)) + 1) * 5;
                    log.warn("Break point: {}", breakPoint);
                }
                break;

            case ENDURANCE:

                repeatTestUntil(suite);
                break;
        }
    }

    private void repeatTestUntil(ISuite suite){

        LocalDateTime now = LocalDateTime.now();

        if(now.isBefore(END_TIME))
        {

            log.info("Minutes left :{}", now.until(END_TIME, ChronoUnit.MINUTES));
            suite.run();
        }else {

            END_TIME = null;
        }
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

    private void incrementUsersLoad(ISuite suite){

        Integer incrementedUsers = Integer.valueOf(suite.getParameter(USERS)) + STEP;
        setUsers(suite, incrementedUsers.toString());
    }

    private Integer setUsers(ISuite suite, String users){

        Map parameters = suite.getXmlSuite().getParameters();
        parameters.put(USERS, users);

        suite.getXmlSuite().setParameters(parameters);

        return Integer.valueOf(users);
    }

    enum PerformanceSuite{

        STRESS("Stress"),
        LOAD("Load"),
        ENDURANCE("Endurance"),
        SPIKE("Spike"),
        TRANSITION("Transition"),
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

    interface PerfromanceStep{

        void run();
    }

    class IncrementStep implements PerfromanceStep{


        @Override
        public void run() {

        }
    }

    class ConstantStep implements PerfromanceStep{

        @Override
        public void run() {

        }
    }
}
