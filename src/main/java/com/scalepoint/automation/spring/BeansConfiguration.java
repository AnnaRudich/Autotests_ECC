package com.scalepoint.automation.spring;

import com.scalepoint.automation.services.usersmanagement.UsersManager;
import com.scalepoint.automation.utils.data.TestData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
public class BeansConfiguration {

    @Value("${"+ com.scalepoint.automation.utils.Configuration.KEY_LOCALE+"}")
    private String locale;
    @Value("${"+ com.scalepoint.automation.utils.Configuration.KEY_SERVER_URL+"}")
    private String serverUrl;
    @Value("${"+ com.scalepoint.automation.utils.Configuration.KEY_ECC_CONTEXT+"}")
    private String eccContext;
    @Value("${"+ com.scalepoint.automation.utils.Configuration.KEY_ECC_ADMIN_CONTEXT+"}")
    private String eccAdminContext;

    @SuppressWarnings("Convert2Lambda")
    @Bean
    public ApplicationListener<ApplicationReadyEvent> listener() throws Exception {
        return new ApplicationListener<ApplicationReadyEvent>() {
            @Override
            public void onApplicationEvent(ApplicationReadyEvent event) {
                com.scalepoint.automation.utils.Configuration.init(locale, serverUrl, eccContext, eccAdminContext);
                UsersManager.initManager(TestData.getSystemUsers());
            }
        };
    }
}
