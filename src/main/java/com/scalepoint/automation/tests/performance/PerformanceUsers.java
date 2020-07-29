package com.scalepoint.automation.tests.performance;

import com.scalepoint.automation.utils.data.entity.credentials.User;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class PerformanceUsers {

    private static final String PASSWORD = "12341234";

    private static BlockingQueue<User> availableUsers;

    public static synchronized void init(int size) {

        availableUsers = new LinkedBlockingQueue<>();

        int testCompaniesLength = TestCompanies.values().length;

        for (int i = 0; i < size; i++){

            TestCompanies testCompany = TestCompanies.values()[i % testCompaniesLength];

            User user = User.builder()
                    .login("autotest-future" + testCompany.ftId + (Math.round(Math.floor(i / testCompaniesLength)) + 1))
                    .password(PASSWORD)
                    .companyCode(testCompany.companyCode)
                    .companyName(testCompany.companyName)
                    .companyId(testCompany.companyId)
                    .ftId(testCompany.ftId)
                    .build();

            availableUsers.add(user);
        }
    }

    public static synchronized User takeUser() throws InterruptedException {

        return availableUsers.take();
    }

    public static synchronized void releaseUser(User user){

        availableUsers.add(user);
    }

    enum TestCompanies{

        FUTURE50("Future50", "FUTURE50", 50, 50),
        FUTURE51("Future51", "FUTURE51", 51, 51),
        FUTURE52("Future52", "FUTURE52", 52, 52),
        FUTURE53("Future53", "FUTURE53", 53, 53),
        FUTURE54("Future54", "FUTURE54", 54, 54),
        FUTURE55("Future55", "FUTURE55", 55, 55),
        FUTURE56("Future56", "FUTURE56", 56, 56),
        FUTURE57("Future57", "FUTURE57", 57, 57);

        private final String companyName;
        private final String companyCode;
        private final Integer companyId;
        private final Integer ftId;

        TestCompanies(String companyName, String companyCode, Integer companyId, Integer ftId) {

            this.companyName = companyName;
            this.companyCode = companyCode;
            this.companyId = companyId;
            this.ftId = ftId;
        }
    }
}
