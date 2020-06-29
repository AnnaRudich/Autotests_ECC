package com.scalepoint.automation.utils.testng;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.xml.XmlSuite;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SuiteListener implements ISuiteListener {

    protected Logger log = LogManager.getLogger(SuiteListener.class);

    @Override
    public void onStart(ISuite iSuite) {

    }

    @Override
    public void onFinish(ISuite suite) {

        String users = suite.getParameter("users");

        int test = suite.getResults().values().stream().findFirst().get().getTestContext().getFailedTests().size();

        if(test == 0){
            Integer newUsers = Integer.valueOf(users) + 10;
            Map parameters = new HashMap();
            parameters.put("users", newUsers.toString());
            XmlSuite updatedSuite = suite.getXmlSuite();
            updatedSuite.setParameters(parameters);

            XmlSuite xmlSuite = suite.getXmlSuite();
            xmlSuite = updatedSuite;

            suite.run();

        } else {

            log.warn("Failing users: {}", users);
        }
    }

}
