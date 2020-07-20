package com.scalepoint.automation.spring;

import java.util.Arrays;
import java.util.NoSuchElementException;

public class PerformanceTestConfig{


    public static final String TEST_LOGIN_USER = "loginUser";
    public static final String TEST_LOGIN_AND_OPEN_CLAIM = "loginAndOpenClaim";
    public static final String TEST_SELFSERVICE = "selfService";
    public static final String TEST_LOGIN_AND_OPEN_CLAIM_WITH_ITEMS = "loginAndOpenClaimWithItems";


    public enum PerformanceTestsNames{

        LOGIN_USER(TEST_LOGIN_USER),
        LOGIN_AND_OPEN_CLAIM(TEST_LOGIN_AND_OPEN_CLAIM),
        SELFSERVICE(TEST_SELFSERVICE),
        LOGIN_AND_OPEN_CLAIM_WITH_ITEMS(TEST_LOGIN_AND_OPEN_CLAIM_WITH_ITEMS);

        private String testName;


        PerformanceTestsNames(String name) {

            this.testName = name;
        }

        public static PerformanceTestsNames findTest(String name){

            return Arrays.stream(PerformanceTestsNames.values())
                    .filter(test ->  test.getTestName().equals(name))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException(name));
        }

        public String getTestName(){

            return testName;
        }
    }
}
