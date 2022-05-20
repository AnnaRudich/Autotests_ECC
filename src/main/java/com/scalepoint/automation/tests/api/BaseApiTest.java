package com.scalepoint.automation.tests.api;

import com.scalepoint.automation.services.restService.common.ServiceData;
import com.scalepoint.automation.spring.*;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.tests.widget.LoginFlow;
import com.scalepoint.automation.utils.threadlocal.CurrentUser;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest(classes = Application.class)
@TestExecutionListeners(inheritListeners = false, listeners = {
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class})
@Import({BeansConfiguration.class, EventApiDatabaseConfig.class, WireMockConfig.class, WireMockStubsConfig.class, LoginFlow.class})
public class BaseApiTest extends BaseTest {

    @BeforeMethod(alwaysRun = true)
    public void setUpData(Method method, Object[] objects) {

        Thread.currentThread().setName("Thread " + method.getName());
        ThreadContext.put("sessionid", method.getName());
        log.info("Starting {}, thread {}", method.getName(), Thread.currentThread().getId());
        ServiceData.init(databaseApi);
    }

    @AfterMethod(alwaysRun = true)
    public void cleanup(Method method) {

        log.info("Clean up after: {}", method.toString());
        CurrentUser.cleanUp();
        ThreadContext.clearMap();
    }

    protected static  <T> List<T> getObjectByClass(List objects, Class<T> clazz){

        return (List<T>) objects
                .stream()
                .filter(object -> object.getClass().equals(clazz))
                .map(object -> (T)object)
                .collect(Collectors.toList());
    }
}
