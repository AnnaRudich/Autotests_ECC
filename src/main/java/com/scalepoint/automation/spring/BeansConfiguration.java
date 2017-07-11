package com.scalepoint.automation.spring;

import com.scalepoint.automation.services.externalapi.DatabaseApi;
import com.scalepoint.automation.services.usersmanagement.UsersManager;
import com.scalepoint.automation.utils.data.TestData;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@EnableAutoConfiguration
public class BeansConfiguration {

    @Value("${" + com.scalepoint.automation.utils.Configuration.KEY_LOCALE + "}")
    private String locale;
    @Value("${" + com.scalepoint.automation.utils.Configuration.KEY_SERVER_URL + "}")
    private String serverUrl;
    @Value("${" + com.scalepoint.automation.utils.Configuration.KEY_CONTEXT_ECC + "}")
    private String eccContext;
    @Value("${" + com.scalepoint.automation.utils.Configuration.KEY_CONTEXT_ECC_ADMIN + "}")
    private String eccAdminContext;
    @Value("${" + com.scalepoint.automation.utils.Configuration.KEY_CONTEXT_ECC_RNV + "}")
    private String eccRnvContext;
    @Value("${" + com.scalepoint.automation.utils.Configuration.KEY_ECC_SOLR_PRODUCTS_URL + "}")
    private String solrProductsUrl;
    @Value("${" + com.scalepoint.automation.utils.Configuration.KEY_ECC_DB_URL+ "}")
    private String dbUrl;
    @Value("${" + com.scalepoint.automation.utils.Configuration.KEY_ECC_DB_USERNAME+ "}")
    private String dbUsername;
    @Value("${" + com.scalepoint.automation.utils.Configuration.KEY_ECC_DB_PASSWORD+ "}")
    private String dbPassword;


    @SuppressWarnings("Convert2Lambda")
    @Bean
    public ApplicationListener<ApplicationReadyEvent> listener() throws Exception {
        return new ApplicationListener<ApplicationReadyEvent>() {
            @Override
            public void onApplicationEvent(ApplicationReadyEvent event) {
                com.scalepoint.automation.utils.Configuration.init(locale, serverUrl, eccContext, eccAdminContext, eccRnvContext, solrProductsUrl);
                UsersManager.initManager(TestData.getSystemUsers());
            }
        };
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);
        return dataSource;
    }

    @Bean
    public DatabaseApi databaseApi() {
        return new DatabaseApi(jdbcTemplate(dataSource()));
    }
}
