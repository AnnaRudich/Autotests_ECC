package com.scalepoint.automation.utils.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentKlovReporter;
import com.scalepoint.automation.utils.Configuration;

import java.util.LinkedList;
import java.util.List;

public class Report {

    private static final String KLOV_HOST = "https://report-server.scalepoint.dev/";
    private static final String KLOV_DB_HOST = "dev-ecc-tool03.spcph.local";
    private static final int KLOV_DB_PORT = 27017;
    private static Report INSTANCE;

    private ExtentReports report;

    private List<Test> testsList = new LinkedList<>();

    private Report(){

        ExtentKlovReporter extentKlovReporter = new ExtentKlovReporter("Autotest", "Report");
        extentKlovReporter.initKlovServerConnection(KLOV_HOST);
        extentKlovReporter.initMongoDbConnection(KLOV_DB_HOST, KLOV_DB_PORT);
        report = new ExtentReports();
        report.attachReporter(extentKlovReporter);
    }

    public static Report getInstance() {

        if (INSTANCE == null){

            return INSTANCE = new Report();
        }

        return INSTANCE;
    }

    public Test getTest(String testName){

        synchronized (this) {
            Test test = testsList.stream()
                    .filter(extentTest -> extentTest.getName().equals(testName))
                    .findFirst()
                    .orElseGet(() -> addTest(testName));

            return test;
        }
    }

    public void flush(){
        report.flush();
    }

    private Test addTest(String testName){

        Test test = new Test(report.createTest(testName));
        testsList.add(test);

        return test;
    }
}
