package com.scalepoint.automation.tests;

import org.testng.ITestNGMethod;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestCountdown {

    private static Set<String> leftMethods = new HashSet<>();

    public static void init(List<ITestNGMethod> methods) {
        methods.forEach(m -> leftMethods.add(m.getRealClass().getSimpleName() + "." + m.getMethodName()));
    }

    static int countDown(String methodName) {
        leftMethods.remove(methodName);
        return leftMethods.size();
    }
}
