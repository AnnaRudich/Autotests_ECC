package com.scalepoint.automation.utils.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.util.LinkedList;
import java.util.List;

public class Report {

    private static Report INSTANCE;

    private ExtentReports report;

    private List<Test> testsList = new LinkedList<>();

    private Report(){

        ExtentSparkReporter extentSparkReporter = new ExtentSparkReporter("");
        report = new ExtentReports();
        report.attachReporter(extentSparkReporter);
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
