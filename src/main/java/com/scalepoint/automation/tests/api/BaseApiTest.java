package com.scalepoint.automation.tests.api;

import com.scalepoint.automation.services.externalapi.DatabaseApi;
import com.scalepoint.automation.services.restService.Common.ServiceData;
import com.scalepoint.automation.spring.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;

@SpringApplicationConfiguration(classes = Application.class)
@IntegrationTest
public class BaseApiTest extends AbstractTestNGSpringContextTests {

    @Autowired
    protected DatabaseApi databaseApi;

    @BeforeMethod
    public void setUp(){
        ServiceData.init(databaseApi);
    }

}
