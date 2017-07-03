package com.scalepoint.automation.services.restService.Common;

import com.scalepoint.automation.services.externalapi.DatabaseApi;

/**
 * Created by bza on 6/29/2017.
 */
public class Data {

    private String sessionId;
    private String claimToken;
    private Integer userId;
    private static DatabaseApi databaseApi;

    public Data(){
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getClaimToken() {
        return claimToken;
    }

    public void setClaimToken(String claimToken) {
        this.claimToken = claimToken;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public DatabaseApi getDatabaseApi() {
        return databaseApi;
    }

    public void setDatabaseApi(DatabaseApi databaseApi) {
        this.databaseApi = databaseApi;
    }
}
