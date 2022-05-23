package com.scalepoint.automation.utils.testng;

import com.scalepoint.automation.utils.reports.Node;
import com.scalepoint.automation.utils.reports.Report;
import com.scalepoint.automation.utils.reports.Test;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class TestListener implements ITestListener {

    private Report report;
    private Map<String, List<ITestNGMethod>> testsMap;

    @Override
    public void onStart(ITestContext context){

        testsMap = Arrays.stream(context.getAllTestMethods()).collect(Collectors.groupingBy(iTestNGMethod -> iTestNGMethod.getRealClass().getSimpleName()));
        report = Report.getInstance();
    }

    @Override
    public void onTestStart(ITestResult result) {

        String testClass = result.getMethod().getRealClass().getSimpleName();
        report.getTest(testClass).getExtentTest().info(String.format("Started : %s", result.getMethod().getMethodName()));
        report.flush();
    }

    @Override
    public void onTestSuccess(ITestResult result) {

        ITestNGMethod iTestNGMethod = result.getMethod();
        testsMap.get(iTestNGMethod.getRealClass().getSimpleName()).remove(iTestNGMethod);
        printLog(result, Node.MethodLog::setPass);
    }

    @Override
    public void onTestFailure(ITestResult result) {

        ITestNGMethod iTestNGMethod = result.getMethod();
        testsMap.get(iTestNGMethod.getRealClass().getSimpleName()).remove(iTestNGMethod);
        printLog(result, Node.MethodLog::setFail);
    }

    @Override
    public void onTestSkipped(ITestResult result) {

        printLog(result, Node.MethodLog::setSkip);
    }

    private void printLog(ITestResult result, Consumer<Node.MethodLog> func){

        ITestNGMethod method = result.getMethod();
        String testClass = method.getRealClass().getSimpleName();

        Test test = report.getTest(testClass);

        test.getExtentTest().info(String.format("%d : test(s) pending", testsMap.get(testClass).size()));

        test.setNode(method.getMethodName(), result.getTestContext())
                .createTest(Collections.singleton(result), func);

        report.flush();
    }
}
