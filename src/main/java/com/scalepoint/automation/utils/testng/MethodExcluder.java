package com.scalepoint.automation.utils.testng;

import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;

import java.util.List;
import java.util.stream.Collectors;

public class MethodExcluder implements IMethodInterceptor {

    @Override
    public List<IMethodInstance> intercept(List<IMethodInstance> list, ITestContext iTestContext) {

        String suite = iTestContext.getSuite().getParameter("tests.performance.enabledPerformanceTests");
        return list.stream().filter(iMethodInstance -> iMethodInstance.getMethod().getGroups()[0].equals(suite)).collect(Collectors.toList());
    }
}
