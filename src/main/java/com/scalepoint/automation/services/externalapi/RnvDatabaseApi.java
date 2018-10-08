package com.scalepoint.automation.services.externalapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by aru on 2018-10-05.
 */
public class RnvDatabaseApi {
    private static Logger logger = LoggerFactory.getLogger(DatabaseApi.class);

    private JdbcTemplate jdbcTemplate;

    public RnvDatabaseApi(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }


}
