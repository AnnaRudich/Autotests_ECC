package com.scalepoint.automation.utils.listeners;

import org.testng.ISuite;
import org.testng.ISuiteListener;

import java.util.List;

public class SuiteListener implements ISuiteListener {
    @Override
    public void onStart(ISuite iSuite) {
        TestCountdown.init(iSuite.getAllMethods());
    }

    @Override
    public void onFinish(ISuite iSuite) {

    }
}
