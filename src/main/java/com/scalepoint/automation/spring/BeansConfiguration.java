package com.scalepoint.automation.spring;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.scalepoint.automation.services.externalapi.DatabaseApi;
import com.scalepoint.automation.services.externalapi.MongoDbApi;
import com.scalepoint.automation.services.usersmanagement.UsersManager;
import com.scalepoint.automation.utils.data.TestData;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@EnableAutoConfiguration
public class BeansConfiguration {

    protected Logger log = LogManager.getLogger(BeansConfiguration.class);

    @Value("${" + com.scalepoint.automation.utils.Configuration.KEY_PROTOCOL + "}")
    private String protocol;
    @Value("${" + com.scalepoint.automation.utils.Configuration.KEY_LOCALE + "}")
    private String locale;
    @Value("${" + com.scalepoint.automation.utils.Configuration.KEY_SERVER_URL + "}")
    private String serverUrl;
    @Value("${" + com.scalepoint.automation.utils.Configuration.KEY_ENVIRONMENT_URL + "}")
    private String environmentUrl;
    @Value("${" + com.scalepoint.automation.utils.Configuration.KEY_EVENT_API_URL + "}")
    private String eventApiUrl;
    @Value("${" + com.scalepoint.automation.utils.Configuration.KEY_CONTEXT_ECC + "}")
    private String eccContext;
    @Value("${" + com.scalepoint.automation.utils.Configuration.KEY_CONTEXT_ECC_ADMIN + "}")
    private String eccAdminContext;
    @Value("${" + com.scalepoint.automation.utils.Configuration.KEY_CONTEXT_ECC_RNV + "}")
    private String eccRnvContext;
    @Value("${" + com.scalepoint.automation.utils.Configuration.KEY_CONTEXT_ECC_SELFSERVICE + "}")
    private String eccSelfServiceContext;
    @Value("${" + com.scalepoint.automation.utils.Configuration.KEY_ECC_SOLR_URL + "}")
    private String solrBaseUrl;
    @Value("${" + com.scalepoint.automation.utils.Configuration.KEY_ECC_DB_URL + "}")
    private String dbUrl;
    @Value("${" + com.scalepoint.automation.utils.Configuration.KEY_EVENT_API_DB_URL + "}")
    private String eventApiDbUrl;
    @Value("${" + com.scalepoint.automation.utils.Configuration.KEY_MONGO_DB_CONNECTION_STRING + "}")
    private String mongoDbConnectionString;
    @Value("${" + com.scalepoint.automation.utils.Configuration.MONGO_VOUCHER_PREDICTION_COLLECTION_NAME + "}")
    private String mongoVoucherPredictionCollectionName;
    @Value("${" + com.scalepoint.automation.utils.Configuration.KEY_HUB_REMOTE + "}")
    private String hubRemoteUrl;
    @Value("${" + com.scalepoint.automation.utils.Configuration.KEY_HUB_REMOTE_ZALENIUM + "}")
    private String hubRemoteZaleniumUrl;
    @Value("${" + com.scalepoint.automation.utils.Configuration.KEY_HUB_LOCAL_ZALENIUM + "}")
    private String hubLocalZaleniumUrl;
    @Value("${" + com.scalepoint.automation.utils.Configuration.KEY_TEST_WIDGET_PROTOCOL + "}")
    private String testWidgetProtocol;
    @Value("${" + com.scalepoint.automation.utils.Configuration.KEY_URL_TEST_WIDGET + "}")
    private String urlTestWidget;



    @SuppressWarnings("Convert2Lambda")
    @Bean
    public ApplicationListener<ApplicationReadyEvent> listener() {
        return new ApplicationListener<ApplicationReadyEvent>() {
            @Override
            public void onApplicationEvent(ApplicationReadyEvent event) {
                com.scalepoint.automation.utils.Configuration.getInstance()
                        .setProtocol(protocol)
                        .setLocale(locale)
                        .setServerUrl(serverUrl)
                        .setEnvironmentUrl(environmentUrl)
                        .setEventApiUrl(eventApiUrl)
                        .setEccContext(eccContext)
                        .setEccAdminContext(eccAdminContext)
                        .setEccRnvContext(eccRnvContext)
                        .setSelfServiceContext(eccSelfServiceContext)
                        .setSolrBaseUrl(solrBaseUrl)
                        .setHubRemote(hubRemoteUrl)
                        .setHubLocalZalenium(hubLocalZaleniumUrl)
                        .setHubRemoteZalenium(hubRemoteZaleniumUrl)
                        .setUrlTestWidget(urlTestWidget)
                        .setTestWidgetProtocol(testWidgetProtocol);
                UsersManager.initManager(TestData.getSystemUsers());
            }
        };
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "eccDb")
    @Primary
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSource.setUrl(dbUrl);
        return dataSource;
    }

    @Bean
    public DatabaseApi databaseApi() {
        return new DatabaseApi(jdbcTemplate(dataSource()));
    }

    @Bean
    public MongoDbFactory mongoDbFactory(){
        MongoClientURI mongoClientURI = new MongoClientURI(mongoDbConnectionString);
        MongoClient mongoClient = new MongoClient(mongoClientURI);
        String databaseName = "voucherPrediction";

        return new SimpleMongoDbFactory(mongoClient, databaseName);
    }

    @Bean
    public MongoTemplate mongoTemplate(){
        return new MongoTemplate(mongoDbFactory());
    }

    @Bean
    public MongoDbApi mongoDbApi(){
        return new MongoDbApi(mongoTemplate());
    }
}
