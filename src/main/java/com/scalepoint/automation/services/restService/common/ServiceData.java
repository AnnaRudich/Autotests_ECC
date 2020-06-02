package com.scalepoint.automation.services.restService.common;

import com.scalepoint.automation.services.externalapi.DatabaseApi;

public class ServiceData {

    protected static ThreadLocal<Data> holder = new ThreadLocal<>();

    protected Data serviceData;

    public static void init(DatabaseApi databaseApi) {
        holder.set(new Data());
        holder.get().setDatabaseApi(databaseApi);
    }

    public static Data getData() {
        return holder.get();
    }
}
