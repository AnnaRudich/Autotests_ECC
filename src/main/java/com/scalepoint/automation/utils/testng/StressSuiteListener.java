package com.scalepoint.automation.utils.testng;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.xml.Parameters;
import org.testng.xml.XmlSuite;

import java.util.NoSuchElementException;

public class StressSuiteListener extends PerformanceSuiteListener implements ISuiteListener {

    protected Logger log = LogManager.getLogger(StressSuiteListener.class);

    @Override
    public void onStart(ISuite iSuite) {

    }

    @Override
    public void onFinish(ISuite suite) {

        switch (PerformanceSuite.findSuite(suite.getName())) {

            case STRESS:

                Integer users = Integer.valueOf(getChildSuite(suite.getXmlSuite(), PerformanceSuite.INCREMENTAL).getParameter(USERS));

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
}
