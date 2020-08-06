package com.scalepoint.automation.utils.reports;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.model.Media;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.ITestContext;
import org.testng.ITestResult;

import java.time.Instant;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Stream;

public class Node {

    private Long limit;
    private Long latest;
    private ExtentTest extentTest;
    private List<ExtentTest> testMethodNodeList = new LinkedList<>();


    public Node(ExtentTest extentTest, Date startDate) {

        limit = startDate.toInstant().toEpochMilli();
        latest = startDate.toInstant().toEpochMilli();
        this.extentTest = extentTest;
    }

    public String getName() {

        return extentTest.getModel().getName();
    }

    public ExtentTest getTestMethodNode(String methodName){

        synchronized (this) {

            ExtentTest extentTest =  testMethodNodeList.stream()
                    .filter(testMethodNode -> testMethodNode.getModel().getName().equals(methodName))
                    .findFirst()
                    .orElseGet(() -> addNewTestMethodNode(methodName));

            return extentTest;
        }
    }

    private ExtentTest addNewTestMethodNode(String methodName){

        ExtentTest extentTest = this.extentTest.createNode(methodName);
        testMethodNodeList.add(extentTest);
        return extentTest;
    }

    public ExtentTest getExtentTest() {

        return extentTest;
    }


    public Node setTestResults(ITestContext iTestContext) {

        Set<ITestResult> passedTests = iTestContext.getPassedTests().getAllResults();
        Set<ITestResult> skippedTests = iTestContext.getSkippedTests().getAllResults();
        Set<ITestResult> failedTest = iTestContext.getFailedTests().getAllResults();


        createFailedTests(failedTest);
        createSkippedTests(skippedTests);
        createPassedTests(passedTests);

        limit = latest;
        return this;
    }

    private void createPassedTests(Set<ITestResult> testResults){

        if(testResults.size() > 0) {

            prepareResultsStream(testResults)
                    .forEach(iTestResult -> {

                        String methodName = iTestResult.getMethod().getMethodName();

                        getTestMethodNode(methodName).pass(findUserParameter(iTestResult).getLogin().concat("_") + Instant.ofEpochMilli(iTestResult.getEndMillis()).atZone(ZoneId.systemDefault()).toLocalDateTime());
                    });
        }
    }

    private void createSkippedTests(Set<ITestResult> testResults) {

        if(testResults.size() > 0) {

            prepareResultsStream(testResults)
                    .forEach(iTestResult -> {

                                String methodName = iTestResult.getMethod().getMethodName();

                                getTestMethodNode(methodName)
                                        .skip(findUserParameter(iTestResult).getLogin().concat("_") + Instant.ofEpochMilli(iTestResult.getEndMillis()).atZone(ZoneId.systemDefault()).toLocalDateTime());
                            }
                    );
        }
    }

    private void createFailedTests(Set<ITestResult> testResults) {

        if(testResults.size() > 0) {

            prepareResultsStream(testResults)
                    .forEach(iTestResult -> {

                                String methodName = iTestResult.getMethod().getMethodName();

                                getTestMethodNode(methodName)
                                        .info(findUserParameter(iTestResult).getLogin().concat("_") + Instant.ofEpochMilli(iTestResult.getEndMillis()).atZone(ZoneId.systemDefault()).toLocalDateTime())
                                        .fail(iTestResult.getThrowable());
                            }
                    );
        }
    }

    public Stream<ITestResult> prepareResultsStream(Set<ITestResult> testResults){

        Stream<ITestResult> stream = testResults.stream()
                .sorted(Comparator.comparingLong(ITestResult::getEndMillis).reversed())
                .filter(iTestResult -> iTestResult.getEndMillis() > limit);

        Long max = testResults.stream()
                .map(iTestResult -> iTestResult.getEndMillis())
                .max(Long::compareTo)
                .orElse(latest);

        latest = max > latest ? max : latest;

        return stream;
    }

    private User findUserParameter(ITestResult iTestResult) {

        return (User) Arrays.stream(iTestResult.getParameters())
                .filter(o -> o.getClass().equals(User.class))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }
}
