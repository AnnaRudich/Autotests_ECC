package com.scalepoint.automation.utils.reports;

import com.aventstack.extentreports.ExtentTest;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.ITestContext;
import org.testng.ITestResult;

import java.time.Instant;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Consumer;
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

    public Node setTestResults(ITestContext iTestContext) {

        Set<ITestResult> passedTests = iTestContext.getPassedTests().getAllResults();
        Set<ITestResult> skippedTests = iTestContext.getSkippedTests().getAllResults();
        Set<ITestResult> failedTest = iTestContext.getFailedTests().getAllResults();

        createTest(failedTest, methodLog -> methodLog.setFail());
        createTest(skippedTests, methodLog -> methodLog.setSkip());
        createTest(passedTests, methodLog -> methodLog.setPass());

        limit = latest;
        return this;
    }

    public String getName() {

        return extentTest.getModel().getName();
    }

    public ExtentTest getExtentTest() {

        return extentTest;
    }

    private void createTest(Set<ITestResult> testResults, Consumer<MethodLog> func){

        if(testResults.size() > 0) {

            prepareResultsStream(testResults)
                    .forEach(iTestResult -> func.accept(new MethodLog(iTestResult)));
        }
    }

    private ExtentTest getTestMethodNode(String methodName){

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

    private Stream<ITestResult> prepareResultsStream(Set<ITestResult> testResults){

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

    private class MethodLog{

        ITestResult iTestResult;
        ExtentTest extentTest;

        MethodLog(ITestResult iTestResult){

            this.iTestResult = iTestResult;
            this.extentTest = getTestMethodNode(iTestResult.getMethod().getMethodName());
        }

        public void setPass(){

            extentTest
                    .pass(prepareUserLog());
        }

        public void setFail(){

            extentTest
                    .info(prepareUserLog())
                    .fail(iTestResult.getThrowable());
        }

        public void setSkip(){

            extentTest
                    .skip(prepareUserLog());
        }

        private String prepareUserLog(){

            return findUserParameter(iTestResult)
                    .getLogin()
                    .concat("_")
                    .concat(Instant
                            .ofEpochMilli(iTestResult.getEndMillis())
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime()
                            .toString());
        }

        private User findUserParameter(ITestResult iTestResult) {

            return (User) Arrays.stream(iTestResult.getParameters())
                    .filter(o -> o.getClass().equals(User.class))
                    .findFirst()
                    .orElseThrow(NoSuchElementException::new);
        }
    }
}
