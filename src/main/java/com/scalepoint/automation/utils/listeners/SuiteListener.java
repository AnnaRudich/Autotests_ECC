package com.scalepoint.automation.utils.listeners;

import com.scalepoint.automation.services.usersmanagement.UsersManager;
import com.scalepoint.automation.tests.TestCountdown;
import com.scalepoint.automation.utils.annotations.ScalepointIdTest;
import com.scalepoint.automation.utils.data.TestDataActions;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.*;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class SuiteListener implements ISuiteListener , IDataProviderInterceptor {

    protected static Logger log = LogManager.getLogger(SuiteListener.class);

    private Set<String> suitesStarted = new HashSet<>();

    @Override
    public void onStart(ISuite iSuite) {
        String name = iSuite.getName();
        log.info("Started suite {} with methods count {} ", name, iSuite.getAllMethods().size());

        boolean added = suitesStarted.add(name);
        if (added) {
            TestCountdown.init(iSuite.getAllMethods());
        }
    }

    @Override
    public void onFinish(ISuite iSuite) {
        log.info("Finished suite {}", iSuite.getName());
    }

    @Override
    public Iterator<Object[]> intercept(Iterator<Object[]> iterator, IDataProviderMethod iDataProviderMethod, ITestNGMethod iTestNGMethod, ITestContext iTestContext) {

        List<Object[]> testData = new LinkedList<>();

        Method method = iTestNGMethod.getConstructorOrMethod().getMethod();

        if(Arrays.stream(method.getAnnotations())
                .anyMatch(annotation -> annotation.annotationType().equals(ScalepointIdTest.class))){

            List<UsersManager.RequestedUserAttributes> scalepointIdUsersAttributes = TestDataActions
                    .extractAllUsersAttributesRequested(method)
                    .stream()
                    .map(requestedUserAttributes -> new UsersManager.RequestedUserAttributes(requestedUserAttributes.getCompanyCode(), User.UserType.SCALEPOINT_ID))
                    .collect(Collectors.toList());

            testData.add(TestDataActions.getTestDataParameters(method, UsersManager.fetchUsersWhenAvailable(scalepointIdUsersAttributes)).toArray());
        }

        iterator.forEachRemaining(testData::add);

        return testData.iterator();
    }
}
