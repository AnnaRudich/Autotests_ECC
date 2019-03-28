package com.scalepoint.automation.utils.listeners;

import org.testng.ITestNGMethod;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

class TestCountdown {

    private static Set<String> leftMethods = new HashSet<>();

    static void init(List<ITestNGMethod> methods) {
        methods.forEach(m -> leftMethods.add(m.getRealClass().getSimpleName() + "." + m.getMethodName()));
    }

    static int countDown(String methodName) {
        leftMethods.remove(methodName);
        return leftMethods.size();
    }
}
