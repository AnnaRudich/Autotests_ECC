package com.scalepoint.automation.tests;

import com.scalepoint.automation.services.externalapi.DatabaseApi;
import com.scalepoint.automation.services.externalapi.EventDatabaseApi;
import com.scalepoint.automation.services.restService.Common.ServiceData;
import com.scalepoint.automation.spring.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;

@SpringApplicationConfiguration(classes = Application.class)
@TestExecutionListeners(inheritListeners = false, listeners = {
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class})
@IntegrationTest
public abstract class AbstractBaseTest extends AbstractTestNGSpringContextTests {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected DatabaseApi databaseApi;

    @Autowired
    protected EventDatabaseApi eventApiDatabaseApi;

    @Value("${driver.type}")
    protected String browserMode;

    @BeforeMethod
    public void setUpData(){
        ServiceData.init(databaseApi);
    }


}
