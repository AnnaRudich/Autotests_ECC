package com.scalepoint.automation.spring;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;
import java.util.stream.Collectors;

@Configuration
@EnableAutoConfiguration
public class PerformanceTestConfig{

    @Bean
    public static List<PerformanceTestsNames> enabledPerformanceTest(@Value("${enabledPerformanceTest}") String enabledList){

        return Arrays.stream(enabledList.split(","))
                .map(name -> PerformanceTestConfig.PerformanceTestsNames.findTest(name))
                .collect(Collectors.toList());

    }

    public static final String TEST_LOGIN_USER = "loginUser";
    public static final String TEST_LOGIN_AND_OPEN_CLAIM = "loginAndOpenClaim";
    public static final String TEST_SELFSERVICE = "selfService";
    public static final String TEST_LOGIN_AND_OPEN_CLAIM_WITH_ITEMS = "loginAndOpenClaimWithItems";


    public enum PerformanceTestsNames{

        LOGIN_USER(TEST_LOGIN_USER),
        TLOGIN_AND_OPEN_CLAIM(TEST_LOGIN_AND_OPEN_CLAIM),
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
