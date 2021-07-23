package com.scalepoint.automation.utils.listeners;

import com.scalepoint.automation.tests.TestCountdown;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ISuite;
import org.testng.ISuiteListener;

import java.util.HashSet;
import java.util.Set;

public class SuiteListener implements ISuiteListener {

    protected static Logger log = LogManager.getLogger(SuiteListener.class);

    private Set<String> suitesStarted = new HashSet<>();

    @Override
    public void onStart(ISuite iSuite) {
        String name = iSuite.getName();
        log.info("Started suite {} with methods count {} ", name, iSuite.getAllMethods().size());

        boolean added = suitesStarted.add(name);
        if (added) {
            TestCountdown.init(iSuite.getAllMethods());
        }
    }

    @Override
    public void onFinish(ISuite iSuite) {
        log.info("Finished suite {}", iSuite.getName());
    }
}
