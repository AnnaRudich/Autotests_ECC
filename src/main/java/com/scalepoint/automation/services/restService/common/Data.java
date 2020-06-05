package com.scalepoint.automation.services.restService.common;

import com.scalepoint.automation.services.externalapi.DatabaseApi;

/**
 * Created by bza on 6/29/2017.
 */
public class Data {

    private String eccSessionId;
    private String rnvSessionId;
    private String claimToken;
    private Integer userId;
    private String selfServiceAccessToken;
    private static DatabaseApi databaseApi;

    public Data() {
    }

    public String getEccSessionId() {
        return eccSessionId;
    }

    public void setEccSessionId(String eccSessionId) {
        this.eccSessionId = eccSessionId;
    }

    public String getRnvSessionId() {
        return rnvSessionId;
    }

    public void setRnvSessionId(String rnvSessionId) {
        this.rnvSessionId = rnvSessionId;
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

    public void setSelfServiceAccessToken(String selfServiceAccessToken){

        this.selfServiceAccessToken = selfServiceAccessToken;
    }

    public String getSelfServiceAccessToken(){
        return selfServiceAccessToken;
    }
}
