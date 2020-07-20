package com.scalepoint.automation.utils.testng;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ISuite;
import org.testng.ISuiteListener;

public class IncrementalSuiteListener extends PerformanceSuiteListener implements ISuiteListener {

    protected Logger log = LogManager.getLogger(IncrementalSuiteListener.class);

    @Override
    public void onStart(ISuite iSuite) {

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
        }
    }
}
