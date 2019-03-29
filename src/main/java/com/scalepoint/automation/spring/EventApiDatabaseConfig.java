package com.scalepoint.automation.spring;

import com.scalepoint.automation.services.externalapi.EventDatabaseApi;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@EnableAutoConfiguration
public class EventApiDatabaseConfig {

    @Value("${" + com.scalepoint.automation.utils.Configuration.KEY_EVENT_API_DB_URL + "}")
    private String eventApiDbUrl;

    @Bean(name = "eventApiDb")
    public DataSource eventApiDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSource.setUrl(eventApiDbUrl);
        return dataSource;
    }

    @Bean
    public EventDatabaseApi eventDatabaseApi() {
        return new EventDatabaseApi(new JdbcTemplate(eventApiDataSource()));
    }
}
