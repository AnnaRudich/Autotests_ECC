package com.scalepoint.automation.utils.testng;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ISuite;
import org.testng.ISuiteListener;

import java.time.LocalDateTime;

public class ConstantSuiteListener extends PerformanceSuiteListener implements ISuiteListener {

    private Logger log = LogManager.getLogger(ConstantSuiteListener.class);
    private static final String TEST_TIME = "performance.test.testTime";
    private static Integer testTime = null;


    @Override
    public void onStart(ISuite iSuite) {

        switch (PerformanceSuite.findSuite(iSuite.getName())) {

            case CONSTANT:

                if(testTime == null){

                    testTime = Integer.valueOf(iSuite.getParameter(TEST_TIME));
                }

                if(END_TIME == null){

                    END_TIME = LocalDateTime.now().plusMinutes(testTime);
                }
                break;
        }
    }

    @Override
    public void onFinish(ISuite suite) {

        switch (PerformanceSuite.findSuite(suite.getName())) {

            case CONSTANT:

                repeatTestUntil(suite);
                break;
        }
    }
}
